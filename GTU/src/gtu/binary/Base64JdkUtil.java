package gtu.binary;

import java.util.Base64;

import org.apache.commons.lang.StringUtils;

public class Base64JdkUtil {

    public static void main(String[] args) {
        String result = decodeToString(
                "YXt+Q1czD1kLERhVLwcYfyJVXAMwHi1cBT0PTRxvPhssSgMhDgIFGAVlAD8hN1g5BAFhNiYcehV5WDh0dzJhEVFyBURMaTRILDckOQ47HDMVPytRPTB+L2UUHSA5NCQ9KVpfBi86aj0RZB9BCi4jYD8uIlgnLFsgYjhkRhoDPFQwWyolPg4+OB1ZeTp/cFMe\",\"13053kE3QBZ/LjUFhg43TVljID75S+kna0dTCVHmH1MHmNaMgE0Ljf7o72iF9qTe26Tk/ZeqaaaNYPhkwQObpZ4W7QOFmqTjqj+Szi0QA0ZpDDHQqzcliF0N+p6syDimzhpUdIhNQkHM\",\"YXt+Q1czD1kLERhVLwcYfyJVXAMwHi1cBT0PTRxvPhssSgMhDgIFGAVlAD8hN1g5BAFhNiYcehV5WDh0dzJhEVFyBURMaTRILDckOQ47HDMVPytRPTB+L2UUHSA5NCQ9KVpfBi86aj0RZB9BCi4jYD8uIlgnLFsgYjhkRhoDPFQwWyolPg4+OB1ZeTp/cFMe");
        System.out.println(result);
        
        String str2 = encode("3y3nIS0p6KoAAAAAAAA4oZJtMc46F2hKE-yROCv9gnt3ZsZhSPjdbZC3zni6qAIU");
        System.out.println(str2);
        
        System.out.println("done...");
    }

    public static String encode(String str) {
        try {
            byte[] arry = StringUtils.trimToEmpty(str).getBytes("UTF8");
            return Base64.getEncoder().encodeToString(arry);
        } catch (Exception e) {
            throw new RuntimeException("Base64Jdk.encode ERR : " + e.getMessage(), e);
        }
    }

    public static String decodeToString(String str) {
        try {
            return new String(Base64.getDecoder().decode(str), "UTF8");
        } catch (Exception e) {
            try {
                return CipherBase64.decode(str, "UTF8");
            } catch (Exception ex) {
                throw new RuntimeException("Base64Jdk.encode ERR : " + e.getMessage(), e);
            }
        }
    }

    public static String encode(byte[] arry) {
        try {
            return Base64.getEncoder().encodeToString(arry);
        } catch (Exception e) {
            throw new RuntimeException("Base64Jdk.encode ERR : " + e.getMessage(), e);
        }
    }

    public static byte[] decodeToByteArry(String str) {
        try {
            return Base64.getDecoder().decode(str);
        } catch (Exception e) {
            try {
                return CipherBase64.decodeToByteArry(str);
            } catch (Exception ex) {
                throw new RuntimeException("Base64Jdk.encode ERR : " + e.getMessage(), e);
            }
        }
    }
}