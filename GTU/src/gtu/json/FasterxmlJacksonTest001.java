
package gtu.json;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FasterxmlJacksonTest001 {

    public static void main(String[] args) {
        TTTT t = new TTTT();
        t.xxxx = BigDecimal.TEN;
        ObjectMapper mapper = new ObjectMapper();
        try {
            String thingString = mapper.writeValueAsString(t);
            System.out.println(thingString);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class TTTT {
        @JsonFormat(shape = Shape.NUMBER)
        private BigDecimal xxxx;

        @JsonIgnore
        private boolean testBoolean;
    }
}
