/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi;

import java.util.Collection;
import java.util.List;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.FontSelector;
import com.lowagie.text.pdf.HyphenationEvent;

/**
 * 
 * @author tsaicf
 */
public class ParagraphBuilder {

    //================================================
    //== [Enumeration types] Block Start
    //====
    //====
    //== [Enumeration types] Block End 
    //================================================
    //== [static variables] Block Start
    //====
    //====
    //== [static variables] Block Stop 
    //================================================
    //== [instance variables] Block Start
    //====
    private PDFDocument docBuilder;

    private Paragraph paragraph;

    //====
    //== [instance variables] Block Stop 
    //================================================
    //== [static Constructor] Block Start
    //====
    //====
    //== [static Constructor] Block Stop 
    //================================================
    //== [Constructors] Block Start (含init method)
    //====

    ParagraphBuilder(PDFDocument docBuilder) throws DocumentException {
        this.docBuilder = docBuilder;
        this.paragraph = new Paragraph();
    }

    //====
    //== [Constructors] Block Stop 
    //================================================
    //== [Static Method] Block Start
    //====
    //====
    //== [Static Method] Block Stop 
    //================================================
    //== [Accessor] Block Start
    //====
    //====
    //== [Accessor] Block Stop 
    //================================================
    //== [Overrided Method] Block Start (Ex. toString/equals+hashCode)
    //====
    //====
    //== [Overrided Method] Block Stop 
    //================================================
    //== [Method] Block Start
    //====
    //####################################################################
    //## [Method] sub-block : Setter
    //####################################################################

    public ParagraphBuilder setAlignment(int alignment) {
        this.paragraph.setAlignment(alignment);
        return this;
    }

    public ParagraphBuilder setAlignment(String alignment) {
        this.paragraph.setAlignment(alignment);
        return this;
    }

    public ParagraphBuilder setLeading(float fixedLeading) {
        this.paragraph.setLeading(fixedLeading);
        return this;
    }

    public ParagraphBuilder setMultipliedLeading(float multipliedLeading) {
        this.paragraph.setMultipliedLeading(multipliedLeading);
        return this;
    }

    public ParagraphBuilder setLeading(float fixedLeading, float multipliedLeading) {
        this.paragraph.setLeading(fixedLeading, multipliedLeading);
        return this;
    }

    public ParagraphBuilder setIndentationLeft(float indentation) {
        this.paragraph.setIndentationLeft(indentation);
        return this;
    }

    public ParagraphBuilder setIndentationRight(float indentation) {
        this.paragraph.setIndentationRight(indentation);
        return this;
    }

    public ParagraphBuilder setFirstLineIndent(float firstLineIndent) {
        this.paragraph.setFirstLineIndent(firstLineIndent);
        return this;
    }

    public ParagraphBuilder setSpacingBefore(float spacing) {
        this.paragraph.setSpacingBefore(spacing);
        return this;
    }

    public ParagraphBuilder setSpacingAfter(float spacing) {
        this.paragraph.setSpacingAfter(spacing);
        return this;
    }

    public ParagraphBuilder setKeepTogether(boolean keeptogether) {
        this.paragraph.setKeepTogether(keeptogether);
        return this;
    }

    public ParagraphBuilder setExtraParagraphSpace(float extraParagraphSpace) {
        this.paragraph.setExtraParagraphSpace(extraParagraphSpace);
        return this;
    }

    public ParagraphBuilder setFont(Font font) {
        this.paragraph.setFont(font);
        return this;
    }

    public ParagraphBuilder setHyphenation(HyphenationEvent hyphenation) {
        this.paragraph.setHyphenation(hyphenation);
        return this;
    }

    //####################################################################
    //## [Method] sub-block : Add
    //####################################################################

    public ParagraphBuilder add(int index, Object o) {
        this.paragraph.add(index, o);
        return this;
    }

    @SuppressWarnings("unchecked")
    public ParagraphBuilder addAll(int index, Collection<?> c) {
        this.paragraph.addAll(index, c);
        return this;
    }

    @SuppressWarnings("rawtypes")
    public ParagraphBuilder addAll(Collection collection) {
        this.paragraph.addAll(collection);
        return this;
    }

    public ParagraphBuilder add(Object o) {
        this.paragraph.add(o);
        return this;
    }

    //####################################################################
    //## [Method] sub-block : Add Chinese Text 
    //####################################################################

    public ParagraphBuilder addText(String text) throws DocumentException {
        return addText(text, this.docBuilder.getFontInfo());
    }

    public ParagraphBuilder addText(String text, FontInfo fontInfo) throws DocumentException {
        FontSelector fontSelector = fontInfo.fontFactory.getFontSelector(fontInfo);
        final Phrase phrase = fontSelector.process(text);

        if (fontInfo.backgroundColor != null) {
            @SuppressWarnings("unchecked")
            List<Chunk> chunks = phrase.getChunks();
            for (Chunk chunk : chunks) {
                chunk.setBackground(fontInfo.backgroundColor);
            }
        }

        this.paragraph.add(phrase);

        return this;
    }

    //####################################################################
    //## [Method] sub-block : Add Chinese Text 
    //####################################################################

    /**
     * @return the paragraph
     * @throws DocumentException
     */
    public void appendMe() throws DocumentException {
        this.docBuilder.add(this.paragraph);
    }

    /**
     * @return the paragraph
     */
    public Paragraph getParagraph() {
        return this.paragraph;
    }

    //====
    //== [Method] Block Stop 
    //================================================

}
