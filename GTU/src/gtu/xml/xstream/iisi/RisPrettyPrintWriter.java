/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.xml.xstream.iisi;

import java.io.Writer;

import com.thoughtworks.xstream.core.util.FastStack;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.xml.AbstractXmlWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;

/**
 * 
 * @author tsaicf
 */
public class RisPrettyPrintWriter extends AbstractXmlWriter {
    
    public static String NULL_STRING = "";

    public static int XML_QUIRKS = -1;

    public static int XML_1_0 = 0;

    public static int XML_1_1 = 1;

    private final QuickWriter writer;

    private final FastStack elementStack = new FastStack(16);

    @SuppressWarnings("unused")
    private final char[] lineIndenter;

    private final int mode;

    private boolean tagInProgress;

    protected int depth;

    private boolean readyForNewLine;

    private boolean tagIsEmpty;

    private String newLine;

    private static final char[] NULL = "&#x0;".toCharArray();

    private static final char[] AMP = "&amp;".toCharArray();

    private static final char[] LT = "&lt;".toCharArray();

    private static final char[] GT = "&gt;".toCharArray();

    private static final char[] CR = "&#xd;".toCharArray();

    private static final char[] QUOT = "&quot;".toCharArray();

    private static final char[] APOS = "&apos;".toCharArray();

    private static final char[] CLOSE = "</".toCharArray();

    private RisPrettyPrintWriter(Writer writer, int mode, char[] lineIndenter, XmlFriendlyReplacer replacer, String newLine) {
        super(replacer);
        this.writer = new QuickWriter(writer);
        this.lineIndenter = lineIndenter;
        this.newLine = "";
        this.mode = mode;
        if (mode < XML_QUIRKS || mode > XML_1_1) {
            throw new IllegalArgumentException("Not a valid XML mode");
        }
    }

    /**
     * @since 1.3
     */
    public RisPrettyPrintWriter(Writer writer, int mode, char[] lineIndenter, XmlFriendlyReplacer replacer) {
        this(writer, mode, lineIndenter, replacer, "\n");
    }

    /**
     * @since 1.3
     */
    public RisPrettyPrintWriter(Writer writer, int mode, char[] lineIndenter) {
        this(writer, mode, lineIndenter, new XmlFriendlyReplacer());
    }

    /**
     * @since 1.3
     */
    public RisPrettyPrintWriter(Writer writer, int mode, String lineIndenter) {
        this(writer, mode, lineIndenter.toCharArray());
    }

    /**
     * @since 1.3
     */
    public RisPrettyPrintWriter(Writer writer, int mode, XmlFriendlyReplacer replacer) {
        this(writer, mode, new char[] { ' ', ' ' }, replacer);
    }

    /**
     * @since 1.3
     */
    public RisPrettyPrintWriter(Writer writer, int mode) {
        this(writer, mode, new char[] { ' ', ' ' });
    }

    @Override
    public void startNode(String name) {
        String escapedName = escapeXmlName(name);
        this.tagIsEmpty = false;
        finishTag();
        this.writer.write('<');
        this.writer.write(escapedName);
        this.elementStack.push(escapedName);
        this.tagInProgress = true;
        this.depth++;
        this.readyForNewLine = true;
        this.tagIsEmpty = true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void startNode(String name, Class clazz) {
        startNode(name);
    }

    @Override
    public void setValue(String text) {
        this.readyForNewLine = false;
        this.tagIsEmpty = false;
        finishTag();

        writeText(this.writer, text);
    }

    @Override
    public void addAttribute(String key, String value) {
        this.writer.write(' ');
        this.writer.write(escapeXmlName(key));
        this.writer.write('=');
        this.writer.write('\"');
        writeAttributeValue(this.writer, value);
        this.writer.write('\"');
    }

    protected void writeAttributeValue(QuickWriter writer, String text) {
        writeText(text);
    }

    protected void writeText(QuickWriter writer, String text) {
        writeText(text);
    }

    private void writeText(String text) {

        if (NULL_STRING.equals(text)) {
            this.writer.write(text);
            return;
        }

        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            switch (c) {
                case '\0':
                    if (this.mode == XML_QUIRKS) {
                        this.writer.write(NULL);
                    } else {
                        throw new StreamException("Invalid character 0x0 in XML stream");
                    }
                    break;
                case '&':
                    this.writer.write(AMP);
                    break;
                case '<':
                    this.writer.write(LT);
                    break;
                case '>':
                    this.writer.write(GT);
                    break;
                case '"':
                    this.writer.write(QUOT);
                    break;
                case '\'':
                    this.writer.write(APOS);
                    break;
                case '\r':
                    this.writer.write(CR);
                    break;
                case '\t':
                case '\n':
                    this.writer.write(c);
                    break;
                default:
                    if (Character.isDefined(c) && !Character.isISOControl(c)) {
                        if (this.mode != XML_QUIRKS) {
                            if (c > '\ud7ff' && c < '\ue000') {
                                throw new StreamException("Invalid character 0x" + Integer.toHexString(c) + " in XML stream");
                            }
                        }
                        this.writer.write(c);
                    } else {
                        if (this.mode == XML_1_0) {
                            if (c < 9 || c == '\u000b' || c == '\u000c' || c == '\u000e' || c == '\u000f') {
                                throw new StreamException("Invalid character 0x" + Integer.toHexString(c) + " in XML 1.0 stream");
                            }
                        }
                        if (this.mode != XML_QUIRKS) {
                            if (c == '\ufffe' || c == '\uffff') {
                                throw new StreamException("Invalid character 0x" + Integer.toHexString(c) + " in XML stream");
                            }
                        }
                        this.writer.write("&#x");
                        this.writer.write(Integer.toHexString(c));
                        this.writer.write(';');
                    }
            }
        }
    }

    @Override
    public void endNode() {
        this.depth--;
        if (this.tagIsEmpty) {
            this.writer.write('/');
            this.readyForNewLine = false;
            finishTag();
            this.elementStack.popSilently();
        } else {
            finishTag();
            this.writer.write(CLOSE);
            this.writer.write((String) this.elementStack.pop());
            this.writer.write('>');
        }
        this.readyForNewLine = true;
        if (this.depth == 0) {
            this.writer.flush();
        }
    }

    private void finishTag() {
        if (this.tagInProgress) {
            this.writer.write('>');
        }
        this.tagInProgress = false;
        if (this.readyForNewLine) {
            endOfLine();
        }
        this.readyForNewLine = false;
        this.tagIsEmpty = false;
    }

    protected void endOfLine() {
        //this.writer.write(getNewLine());
        //for (int i = 0; i < this.depth; i++) {
        //    this.writer.write(this.lineIndenter);
        //}
    }

    @Override
    public void flush() {
        this.writer.flush();
    }

    @Override
    public void close() {
        this.writer.close();
    }

    protected String getNewLine() {
        return this.newLine;
    }
}
