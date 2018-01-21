package gtu.binary;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5_forHTC {

	private static final String MESSAGE_DIGEST_MD5 = "MD5";
	
	public static void main(String args[]){
		
		String a = "foo";
		String b = "bar";
		String c = "quz";
		String csum = getMD5(a, b, c);
		
		System.out.println(csum);
		
	}
	
    private static String getMD5(String a, String b, String c) {
    	MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(MESSAGE_DIGEST_MD5);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(a);
        sb.append(b);
        sb.append(c);
        md.update(sb.toString().getBytes());
        BigInteger number = new BigInteger(1, md.digest());
        sb.delete(0, sb.length());
        sb.append(number.toString(16));
        while (sb.length() < 32) {
            sb.insert(0, "0");
        }
        return sb.toString().toUpperCase();
    }
}
