package _temp;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class Test44 {

    public static void main(String[] args) throws InterruptedException, IOException {
        Test44 t = new Test44();

        StringBuilder sb = new StringBuilder();
        sb.append(" a 1969 interview with the newspaper the <i>Baltimore                                            \n");
        sb.append(" Afro-American, </i>Benning defined the two principles of the New Breed as                       \n");
        sb.append(" “freedom and economic independence for the black man.” In its early days, the                   \n");
        sb.append(" company received a $20,000 loan from the Negro Industrial and Economic Union                    \n");
        sb.append(" (now the Black Economic Union), which helped it get the early prototypes of the                 \n");
        sb.append(" dashiki produced. According to Davis, Burlington Mills Fabric donated unlimited                 \n");
        sb.append(" materials to the company, allowing it to incorporate different colors and                       \n");
        sb.append(" fabric in the dashiki. “I always say one of the best advertising tools in                       \n");
        sb.append(" fashion is word of mouth,” says Davis, who’s designed footwear for Michael                      \n");
        sb.append(" Jackson and made Muhammad Ali’s costume for Oscar Brown Jr.’s Broadway musical<i> Buck          \n");
        sb.append(" White.</i> “Eventually people were asking, ‘Who are these New Breed guys?’                      \n");
        sb.append(" and we opened our factory in Brooklyn [in 1969].”<o:p></o:p>                                    \n");

        System.out.println(t.appendReplacementEscape(sb.toString()));

        System.out.println("done...");
    }

    private class AppendReplacementEscaper {
        String content;
        String result;

        AppendReplacementEscaper(String content) {
            this.content = content;
            result = StringUtils.trimToEmpty(content).toString();
            result = replaceChar(result, '$');
            result = replaceChar(result, '/');
        }

        private String replaceChar(String content, char from) {
            StringBuffer sb = new StringBuffer();
            char[] arry = StringUtils.trimToEmpty(content).toCharArray();
            for (char a : arry) {
                if (a == from) {
                    sb.append("\\" + a);
                } else {
                    sb.append(a);
                }
            }
            return sb.toString();
        }

        private String getResult() {
            return result;
        }
    }

    private String appendReplacementEscape(String content) {
        String errorTag = "";
        try {
            return new AppendReplacementEscaper(content).getResult();
        } catch (Exception ex) {
            throw new RuntimeException(String.format("[tag : %s][%s]\n", errorTag, content) + ex.getMessage(), ex);
        }
    }
}
