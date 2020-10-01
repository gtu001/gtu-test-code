package gtu.json.serialTest001;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.reflect.FieldUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;

import lombok.Data;

/**
 * https://www.itdaan.com/tw/3f59b33ce5ced03fac35864fa4e40f3
 */
public class JSonSerializerTest001 {

    @Data
    public static class ZsDoCalLimitResDTO {

        @JsonProperty("returnCode")
        private String returnCode;

        @JsonProperty("returnMsg")
        private String returnMsg;

        @JsonProperty("detail")
        private ZsDoCalLimitDetailResDTO detail;
    }

    @Data
    public static class ZsDoCalLimitDetailResDTO {

        @JsonProperty("RETURN_MSG")
        private String returnMsg;

        @JsonProperty("RETURN_CODE")
        private String returnCode;

        @JsonProperty("DATAS")
        private ZsDoCalLimitDatasResDTO datas;
    }

    @Data
    @JsonSerialize(using = CustomJsonSerializer.class) // 自己加的
    @JsonDeserialize(using = CustomJsonDeserializer.class) // 自己加的
    public static class ZsDoCalLimitDatasResDTO {
        // @JsonProperty("TTTTTT")
        @MapAsField
        private Map<String, ZsDoCalLimitS122531322ResDTO> dynamicMap = null;
    }

    @Data
    public static class ZsDoCalLimitS122531322ResDTO {
        @JsonProperty("insuredTravelConvnte")
        private String insuredTravelConvnte;

        @JsonProperty("insuredFlag")
        private String insuredFlag;

        @JsonProperty("isByPass")
        private String isByPass;

        @JsonProperty("calAmountLimit")
        private String calAmountLimit;

        @JsonProperty("referenceId")
        private String referenceId;

        @JsonProperty("errorMsg")
        private String errorMsg;
    }

    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MapAsField {
    }

    public static class CustomJsonDeserializer extends JsonDeserializer<Object> {

        @Override
        public Object deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode node = oc.readTree(jsonParser);

            Class createClz = null;

            Class currentClz = jsonParser.getCurrentValue().getClass();
            A: for (Field field : currentClz.getDeclaredFields()) {
                if (field.getType().getAnnotation(JsonDeserialize.class) != null) {

                    System.out.println(field.getName() + " ----- " + field.getType().getAnnotation(JsonDeserialize.class));
                    JsonDeserialize anno = field.getType().getAnnotation(JsonDeserialize.class);
                    if (CustomJsonDeserializer.class == anno.using()) {
                        createClz = field.getType();
                        break A;
                    }
                }
            }

            if (createClz != null) {
                try {
                    ObjectMapper mapper = new ObjectMapper();

                    Object mapObj = createClz.newInstance();

                    Map<String, Object> map = new HashMap<String, Object>();
                    Iterator<String> it = node.fieldNames();
                    while (it.hasNext()) {
                        String name = it.next();
                        JsonNode node1 = node.get(name);
                        if (JsonNodeType.ARRAY == node1.getNodeType()) {
                        } else if (JsonNodeType.BINARY == node1.getNodeType()) {
                        } else if (JsonNodeType.BOOLEAN == node1.getNodeType()) {
                        } else if (JsonNodeType.MISSING == node1.getNodeType()) {
                        } else if (JsonNodeType.NULL == node1.getNodeType()) {
                        } else if (JsonNodeType.NUMBER == node1.getNodeType()) {
                        } else if (JsonNodeType.OBJECT == node1.getNodeType()) {

                            ZsDoCalLimitS122531322ResDTO realObj = mapper.convertValue(node1, ZsDoCalLimitS122531322ResDTO.class);
                            map.put(name, realObj);
                        } else if (JsonNodeType.POJO == node1.getNodeType()) {
                        } else if (JsonNodeType.STRING == node1.getNodeType()) {
                            map.put(name, node1.asText());
                        }
                    }

                    Field mapField = FieldUtils.getDeclaredField(createClz, "dynamicMap", true);
                    mapField.set(mapObj, map);

                    return mapObj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public static class CustomJsonSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();

            JavaType javaType = provider.constructType(value.getClass());
            BeanDescription beanDesc = provider.getConfig().introspect(javaType);

            ListIterator<BeanPropertyDefinition> itor = beanDesc.findProperties().listIterator();

            // Remove map field
            ArrayList<BeanPropertyDefinition> list = new ArrayList<>();
            while (itor.hasNext()) {
                BeanPropertyDefinition n = itor.next();

                System.out.println("fullName = " + n.getFullName());

                if (n.getField().getAnnotated().getAnnotation(MapAsField.class) != null && // Only
                                                                                           // handle
                                                                                           // this
                        Map.class.isAssignableFrom(n.getField().getRawType())) {
                    itor.remove();
                    list.add(n);
                }
            }

            JsonSerializer<Object> serializer = BeanSerializerFactory.instance.findBeanSerializer(provider, javaType, beanDesc);
            serializer.unwrappingSerializer(null).serialize(value, gen, provider);

            // Handle all map field
            for (BeanPropertyDefinition d : list) {
                try {
                    Field field = d.getField().getAnnotated();
                    field.setAccessible(true);
                    Map<?, ?> v = (Map<?, ?>) field.get(value);
                    if (v != null && !v.isEmpty()) {
                        for (Map.Entry o : v.entrySet()) {
                            gen.writeObjectField(o.getKey().toString(), o.getValue());
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            gen.writeEndObject();
        }
    }

    public static void main(String[] args) throws IOException {
        String insuredId = "A123456789";
        ObjectMapper objectMapper = new ObjectMapper();

        ZsDoCalLimitResDTO zsDoCalLimitResDTO = new ZsDoCalLimitResDTO();
        zsDoCalLimitResDTO.setReturnCode("0");
        zsDoCalLimitResDTO.setReturnMsg("success");

        ZsDoCalLimitDetailResDTO ZsDoCalLimitDetailResDTO = new ZsDoCalLimitDetailResDTO();
        ZsDoCalLimitDetailResDTO.setReturnCode("0");
        ZsDoCalLimitDetailResDTO.setReturnMsg("success");

        ZsDoCalLimitDatasResDTO ZsDoCalLimitDatasResDTO = new ZsDoCalLimitDatasResDTO();

        ZsDoCalLimitS122531322ResDTO ZsDoCalLimitS122531322ResDTO = new ZsDoCalLimitS122531322ResDTO();
        ZsDoCalLimitS122531322ResDTO.setCalAmountLimit("a");
        ZsDoCalLimitS122531322ResDTO.setInsuredFlag("Y");
        ZsDoCalLimitS122531322ResDTO.setIsByPass("X");
        ZsDoCalLimitS122531322ResDTO.setReferenceId("Z");

        Map<String, ZsDoCalLimitS122531322ResDTO> map11 = new HashMap<String, ZsDoCalLimitS122531322ResDTO>();
        map11.put("S122531322", ZsDoCalLimitS122531322ResDTO);
        ZsDoCalLimitDatasResDTO.setDynamicMap(map11);

        ZsDoCalLimitDetailResDTO.setDatas(ZsDoCalLimitDatasResDTO);
        zsDoCalLimitResDTO.setDetail(ZsDoCalLimitDetailResDTO);

        String resultStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(zsDoCalLimitResDTO);
        System.out.println(resultStr);

        // ======================================================================
        ZsDoCalLimitResDTO revertObj = objectMapper.readValue(resultStr, ZsDoCalLimitResDTO.class);

        System.out.println(ReflectionToStringBuilder.toString(revertObj, org.apache.commons.lang.builder.ToStringStyle.MULTI_LINE_STYLE));
    }
}
