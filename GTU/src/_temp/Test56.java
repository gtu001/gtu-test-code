package _temp;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test56 {

    public static void main(String[] args) {
		String msg = "^.jar^.class";
        String[] arry = StringUtils.trimToEmpty(msg).split("\\^", -1);
        // System.out.println(Arrays.toString(arry));
        Pattern ptn = Pattern.compile("[\\w\\-\\:\\/]+\\s\\d{2}\\:\\d{2}\\:\\d{2}|[\\w\\-\\:\\/]+|\\w+");
        Matcher mth = ptn.matcher("2018-33-22");
        while (mth.find()) {
            System.out.println(mth.group());
        }
        System.out.println("done...");


        TTTT t = new TTTT();
        t.xxxx = BigDecimal.TEN;
        ObjectMapper mapper =new ObjectMapper();
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
        @JsonFormat(shape=Shape.NUMBER)
        private BigDecimal xxxx;
    }
}