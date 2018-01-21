/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.xml.xstream.iisi;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

/**
 * 
 * @author tsaicf
 */
public class RisReflectionConverter extends ReflectionConverter {

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
    /**
     * @param mapper
     * @param reflectionProvider
     */
    public RisReflectionConverter(Mapper mapper, ReflectionProvider reflectionProvider) {
        super(mapper, reflectionProvider);
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

    @SuppressWarnings("rawtypes")
    @Override
    protected void doMarshal(final Object source, final HierarchicalStreamWriter writer, final MarshallingContext context) {
        final Set seenFields = new HashSet();
        final Map defaultFieldDefinition = new HashMap();

        // Attributes might be preferred to child elements ...
        this.reflectionProvider.visitSerializableFields(source, new ReflectionProvider.Visitor() {
            @SuppressWarnings("unchecked")
            @Override
            public void visit(String fieldName, Class type, Class definedIn, Object value) {
                if (!RisReflectionConverter.this.mapper.shouldSerializeMember(definedIn, fieldName)) {
                    return;
                }
                if (!defaultFieldDefinition.containsKey(fieldName)) {
                    Class lookupType = source.getClass();
                    // See XSTR-457 and OmitFieldsTest
                    // if (definedIn != source.getClass() && !mapper.shouldSerializeMember(lookupType, fieldName)) {
                    //    lookupType = definedIn;
                    // }
                    defaultFieldDefinition.put(fieldName, RisReflectionConverter.this.reflectionProvider.getField(lookupType, fieldName));
                }

                SingleValueConverter converter = RisReflectionConverter.this.mapper.getConverterFromItemType(fieldName, type, definedIn);
                if (type == String.class) {
                    if (value == null) {
                        value = RisPrettyPrintWriter.NULL_STRING; // "<![CDATA[]]>";
                    } else if ("".equals(value)) {
                        value = null;
                    }
                }

                if (converter != null) {
                    if (value != null) {
                        if (seenFields.contains(fieldName)) {
                            throw new ConversionException("Cannot write field with name '" + fieldName + "' twice as attribute for object of type "
                                    + source.getClass().getName());
                        }
                        final String str = converter.toString(value);
                        if (str != null) {
                            writer.addAttribute(RisReflectionConverter.this.mapper.aliasForAttribute(RisReflectionConverter.this.mapper
                                    .serializedMember(definedIn, fieldName)), str);
                        }
                    }
                    // TODO: name is not enough, need also "definedIn"! 
                    seenFields.add(fieldName);
                }
            }
        });

        // Child elements not covered already processed as attributes ...
        this.reflectionProvider.visitSerializableFields(source, new ReflectionProvider.Visitor() {
            @Override
            public void visit(String fieldName, Class fieldType, Class definedIn, Object newObj) {
                if (!RisReflectionConverter.this.mapper.shouldSerializeMember(definedIn, fieldName)) {
                    return;
                }

                if (fieldType == String.class) {
                    if (newObj == null) {
                        newObj = RisPrettyPrintWriter.NULL_STRING;
                    } else if ("".equals(newObj)) {
                        newObj = null;
                    }
                }

                if (!seenFields.contains(fieldName) && newObj != null) {
                    Mapper.ImplicitCollectionMapping mapping =
                        RisReflectionConverter.this.mapper.getImplicitCollectionDefForFieldName(source.getClass(), fieldName);
                    if (mapping != null) {
                        if (mapping.getItemFieldName() != null) {
                            Collection list = (Collection) newObj;
                            for (Iterator iter = list.iterator(); iter.hasNext();) {
                                Object obj = iter.next();
                                writeField(fieldName, mapping.getItemFieldName(), mapping.getItemType(), definedIn, obj);
                            }
                        } else {
                            context.convertAnother(newObj);
                        }
                    } else {
                        writeField(fieldName, fieldName, fieldType, definedIn, newObj);
                    }
                }
            }

            private void writeField(String fieldName, String aliasName, Class fieldType, Class definedIn, Object newObj) {
                ExtendedHierarchicalStreamWriterHelper.startNode(writer,
                        RisReflectionConverter.this.mapper.serializedMember(source.getClass(), aliasName), fieldType);

                Class actualType = newObj.getClass();

                Class defaultType = RisReflectionConverter.this.mapper.defaultImplementationOf(fieldType);
                if (!actualType.equals(defaultType)) {
                    String serializedClassName = RisReflectionConverter.this.mapper.serializedClass(actualType);
                    if (!serializedClassName.equals(RisReflectionConverter.this.mapper.serializedClass(defaultType))) {
                        writer.addAttribute(RisReflectionConverter.this.mapper.aliasForAttribute("class"), serializedClassName);
                    }
                }

                final Field defaultField = (Field) defaultFieldDefinition.get(fieldName);
                if (defaultField.getDeclaringClass() != definedIn) {
                    writer.addAttribute(RisReflectionConverter.this.mapper.aliasForAttribute("defined-in"),
                            RisReflectionConverter.this.mapper.serializedClass(definedIn));
                }

                Field field = RisReflectionConverter.this.reflectionProvider.getField(definedIn, fieldName);
                marshallField(context, newObj, field);
                writer.endNode();
            }

        });
    }

    final private ReflectionProvider pureJavaReflectionProvider = new PureJavaReflectionProvider();

    private Object instantiatePureJavaInstance(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String readResolveValue = reader.getAttribute(this.mapper.aliasForAttribute("resolves-to"));
        Object currentObject = context.currentObject();
        if (currentObject != null) {
            return currentObject;
        } else if (readResolveValue != null) {
            return this.pureJavaReflectionProvider.newInstance(this.mapper.realClass(readResolveValue));
        } else {
            return this.pureJavaReflectionProvider.newInstance(context.getRequiredType());
        }
    }

    private static void fillAllStringField(final Object dto, final Object prototype) {
        if (null == dto) {
            return;
        }
        final Field[] fields = dto.getClass().getDeclaredFields();
        for (Field aField : fields) {
            Class<?> types = aField.getType();
            if (String.class.equals(types)) {
                try {
                    Object defaultValue = FieldUtils.readField(aField, prototype, true);
                    FieldUtils.writeField(aField, dto, defaultValue, true);
                } catch (IllegalArgumentException e) { // e.printStackTrace();
                } catch (IllegalAccessException e) { // e.printStackTrace();
                }
            }
        }
    }

    /**
     * 反解
     * 
     * @see com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader,
     *      com.thoughtworks.xstream.converters.UnmarshallingContext)
     */
    @Override
    public Object unmarshal(final HierarchicalStreamReader reader, final UnmarshallingContext context) {
        Object result = instantiateNewInstance(reader, context);
        Object prototype = instantiatePureJavaInstance(reader, context);
        if (result != prototype && prototype != null) {
            fillAllStringField(result, prototype);
        }
        // process default string = ""

        result = doUnmarshal(result, reader, context);
        return this.serializationMethodInvoker.callReadResolve(result);
    }

    //====
    //== [Overrided Method] Block Stop 
    //================================================
    //== [Method] Block Start
    //====
    //####################################################################
    //## [Method] sub-block : 
    //####################################################################    
    //====
    //== [Method] Block Stop 
    //================================================

}
