package gtu.crystal;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CrystalTrail {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String rtnval = new CrystalTrail().getTrialSerialNumber();
        System.out.println(rtnval);
    }

    private String getTrialSerialNumber() {
        String urlFormat = "http://www.inetsoftware.de/English/scripts/LicenseKeyRemoteRequest_Trial.asp?ip={0}&version={1}&host={2}";
        String str = null;
        try {
            InetAddress localInetAddress = InetAddress.getLocalHost();
            String ip = localInetAddress.getHostAddress();
            if (ip.equals("127.0.0.1")) {
                System.out.println("You can not get a license for " + ip + " (localhost).Please set up a real IP address and host name.");
            }
            ip = "192.168.200.92";
            String url = MessageFormat.format(urlFormat, new Object[] { ip, "5", localInetAddress.getHostName() });
            URL localURL = new URL(url);
            URLConnection localURLConnection = localURL.openConnection();
            localURLConnection.setDoInput(true);
            localURLConnection.connect();
            InputStream localInputStream = localURLConnection.getInputStream();
            byte[] arrayOfByte = new byte[512];
            int i1 = localInputStream.read(arrayOfByte);
            localInputStream.close();
            str = new String(arrayOfByte, 0, i1);
        } catch (Exception localException) {
            StringWriter localObject = new StringWriter();
            localException.printStackTrace(new PrintWriter((Writer) localObject));
            localObject.flush();
            System.out.println("Connection refused!");
            System.out.println(localException.getMessage());
            localException.printStackTrace();
        }
        return str;
    }
    
    
    private HSSFWorkbook getExcelTemplate() {
        String url = "http://10.36.2.47:8080/scorecard/template/Scorcard_Analysis_by_Excel_Template3.xls";
        String str = null;
        try {
            InetAddress localInetAddress = InetAddress.getLocalHost();
            String ip = localInetAddress.getHostAddress();
            URL localURL = new URL(url);
            URLConnection localUrlConn = localURL.openConnection();
            localUrlConn.setDoInput(true);
            localUrlConn.connect();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream localInput = localUrlConn.getInputStream();
            byte[] aOfByte = new byte[1024*8];
            byte[] content = null;
            int pos = -1;
            while((pos = localInput.read(aOfByte))!=-1) {
                content = new byte[pos];
                System.arraycopy(aOfByte, 0, content, 0, content.length);
                baos.write(content);
            }
            localInput.close();
            ByteArrayInputStream byteIS = new ByteArrayInputStream(baos.toByteArray());
            byteIS.close();
            HSSFWorkbook wb = new HSSFWorkbook(byteIS);
            return wb;
        } catch (Exception localException) {
            StringWriter localObject = new StringWriter();
            localException.printStackTrace(new PrintWriter((Writer) localObject));
            localObject.flush();
            System.out.println("Connection refused!");
            System.out.println(localException.getMessage());
            localException.printStackTrace();
            return null;
        }
    }
}
