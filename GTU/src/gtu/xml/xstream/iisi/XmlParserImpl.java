/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.xml.xstream.iisi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterRegistry;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.core.DefaultConverterLookup;
import com.thoughtworks.xstream.core.util.ClassLoaderReference;
import com.thoughtworks.xstream.core.util.CompositeClassLoader;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * XML轉換實作.
 * 
 * @author Chifeng Tsai (Refiner)
 * @author Sandy Chiu
 * @author Jiayun Zhou (Refiner)
 */
public class XmlParserImpl {

    final private static String KEY_InvertNULL = "<!--risIN-->";

    /**
     * @author sandy <br>
     *         OLDSVN#43497 @ 2013/02/04 <br>
     *         SP訊息通報: debug:IllegalArgumentException: XPP3 pull parser library not present. Specify another driver. For
     *         example: new XStream(new DomDriver())
     */
    final private XStream xstream;

    /***/
    final private XStream xstreamInvertNULL;

    /** 去除空白、換行字元. */
    final private XStream xstreamNoblank;

    /**
     * 
     */
    public XmlParserImpl() {
        final XStream xstreamA = new XStream(new DomDriver());
        this.xstream = xstreamA;
        this.xstreamNoblank = new XStream(new RisDomDriver());
        {
            final ReflectionProvider reflectionProvider = xstreamA.getReflectionProvider();
            final Mapper mapper = xstreamA.getMapper();
            final DefaultConverterLookup converterLookup = new DefaultConverterLookup();
            converterLookup.registerConverter(new RisReflectionConverter(mapper, reflectionProvider), -19);
            converterLookup.registerConverter(new RisStringConverter(), 20);
            final ConverterRegistry converterRegistry = null;
            final XStream xstream = new XStream(//
                    reflectionProvider //
                    , new RisDomDriver() //, new XppDriver() //RisXppDriver
                    , new ClassLoaderReference(new CompositeClassLoader()) //
                    , mapper   //
                    , converterLookup //
                    , converterRegistry //
                );
            this.xstreamInvertNULL = xstream;
        }
    }

    /**
     * @see tw.gov.moi.ae.parser.XmlParser#genXmlString(java.lang.Object, tw.gov.moi.ae.parser.XmlParser.Mode)
     */
    public String genXmlString(Object obj, Mode mode) {
        try {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            final Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
            if (mode == Mode.invertNull) {
                writer.write(KEY_InvertNULL);
                this.xstreamInvertNULL.toXML(obj, writer);
            } else if (mode == Mode.noblank) {
                this.xstreamNoblank.toXML(obj, writer);
            } else {
                this.xstream.toXML(obj, writer);
            }
            writer.flush();
            String xml = outputStream.toString("UTF-8");
            return xml;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see tw.gov.moi.ae.parser.XmlParser#genXmlString(java.lang.Object)
     */
    public String genXmlString(final Object obj) {
        return this.genXmlString(obj, Mode.normal);
    }

    /**
     * @see tw.gov.moi.ae.parser.XmlParser#parseToObj(java.lang.String)
     */
    public Object parseToObj(final String str) {
        try {
            if (StringUtils.startsWith(str, KEY_InvertNULL)) {
                return this.xstreamInvertNULL.fromXML(str);
            } else {
                return this.xstream.fromXML(str);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @see tw.gov.moi.ae.parser.XmlParser#parseToObj(java.lang.Class, java.lang.String)
     */
    @SuppressWarnings("unchecked")
    public <T> T parseToObj(Class<T> clz, String str) {
        return (T) parseToObj(str);
    }

    //    /**
    //     * @see tw.gov.moi.ae.parser.XmlParser#fromJAXBStyle(java.lang.String)
    //     */
    //    @Override
    //    public String fromJAXBStyle(String xml) {
    //        try {
    //            SoapXMLTransformer transformer = new SoapXMLTransformer(this.wsXmlParserImpl, this, xml);
    //            logger.debug("transformer : {}", transformer);
    //            return transformer.getXstreamResult();
    //        } catch (Exception e) {
    //            throw new RuntimeException(AeCDMesg.AE9003E, "from webservice format", e);
    //        }
    //    }

    /**
     * @throws IOException
     * @see tw.gov.moi.ae.parser.XmlParser#saveAsXml(java.lang.Object, tw.gov.moi.ae.filesystem.RisFile)
     */
    public void saveAsXml(Object obj, File file) throws IOException {
        final String xml = this.genXmlString(obj);
        FileUtils.writeStringToFile(file, xml, "UTF-8");
    }

    /**
     * @throws IOException
     * @see tw.gov.moi.ae.parser.XmlParser#loadFromXml(java.lang.Class, tw.gov.moi.ae.filesystem.RisFile)
     */
    public <T> T loadFromXml(Class<T> clz, File file) throws IOException {
        String xml = FileUtils.readFileToString(file, "UTF-8");
        return this.parseToObj(clz, xml);
    }

    static class RisDomDriver extends DomDriver {
        public HierarchicalStreamWriter createWriter(Writer out) {
            RisPrettyPrintWriter printWriter = new RisPrettyPrintWriter(out, RisPrettyPrintWriter.XML_QUIRKS,
                    new char[0]);
            return printWriter;
        }
    }
    
    static class RisStringConverter implements Converter {
        public boolean canConvert(Class type) {
            return String.class.equals(type);
        }

        public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
            writer.setValue(source.toString());
        }

        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            String value = reader.getValue();
            if (value == null)
                return "";
            if ("".equals(value)) {
                return null;
            }
            return value;
        }
    }
    
    static enum Mode {
        normal, invertNull, noblank;
    }
}
