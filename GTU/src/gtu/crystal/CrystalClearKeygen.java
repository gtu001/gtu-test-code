package gtu.crystal;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class CrystalClearKeygen {
    
    String o10 = null;
    String o11 = null;
    String o12 = null;

    public CrystalClearKeygen() throws Exception {
        
        String defaultIP = InetAddress.getLocalHost().getHostAddress();
        System.out.println("***** IP Address (default="+defaultIP+")*****");
        System.out.print("IP : ");
        String IP = input();
        if(IP == null || IP.length() == 0){
            IP = defaultIP;
        }

        System.out.println("***** CPU # (default=1) *****");
        System.out.print("CPU # : ");
        String cpu = input();

        System.out.println("***** Version # (default=6) *****");
        System.out.print("Version # : ");
        String version = input();

        System.out.println("***** Date # (default=sysdate) *****");
        System.out.print("Date [yyyy.mm.dd] : ");
        String date = input();

        if ((date.length() > 0) && (date.length() != 10)) {
            System.out.println("Date Format Error!! [yyyy.mm.dd]");
            System.exit(0);
        }

        System.out.println("***** LicenseType (default=1) *****");
        System.out.println("1 ==> Site License");
        System.out.println("2 ==> Single Server License");
        System.out.println("3 ==> Standalone License");
        System.out.println("4 ==> Trial License for 90 days with 2 clients");
        System.out.println("5 ==> Trial License for 30 days with unlimited clients");
        System.out.print("LicenseType : ");
        String LicenseType = input();

        String key = "";
        
        StringTokenizer tmpIP = new StringTokenizer(IP, ".");
        if (tmpIP.countTokens() != 4) {
            System.out.println("IP Error!! count " + tmpIP.countTokens());
            System.exit(0);
        }
        
        while (tmpIP.hasMoreTokens()) {
            int chk = -1;
            try {
                chk = Integer.parseInt(tmpIP.nextToken());
            } catch (Exception ex) {
                System.out.println("IP Error!! parse");
                System.exit(0);
            }
            if ((chk < 0) || (chk > 255)) {
                System.out.println("IP Error!! range");
                System.exit(0);
                break;
            }

            key = key + ((Integer.toHexString(chk).length() < 2) ? "0" : "") + Integer.toHexString(chk).toUpperCase();
//            System.out.println("IP key : " + key);
        }

        Calendar ca = Calendar.getInstance();
        try {
            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(5, 7));
            int day = Integer.parseInt(date.substring(8, 10));
            ca.set(year, month - 1, day);
        } catch (Exception year) {
        }
        Date dateTMP = ca.getTime();

        String expire_day = getDateString(dateTMP);
        key = key + expire_day;
        
//        System.out.println("Date key : " + key);

        String caseType = null;

        int licenseTMP = 1;
        try {
            licenseTMP = Integer.parseInt(LicenseType);
        } catch (Exception ex) {
        }
        int versionTMP = 6;
        try {
            versionTMP = Integer.parseInt(version);
        } catch (Exception ex) {
        }
        switch (licenseTMP) {
        case 2:
            caseType = versionTMP + "4";
            break;
        case 3:
            caseType = versionTMP + "5";
            break;
        case 4:
            caseType = versionTMP + "2";
            break;
        case 5:
            caseType = versionTMP + "3";
            break;
        case 1:
        default:
            caseType = versionTMP + "6";
        }

        key = key + caseType;
//        System.out.println("Case key : " + key);

        if (cpu.equals("")) {
            cpu = "01";
        } else {
            int cpuTMP = 1;
            try {
                cpuTMP = Integer.parseInt(cpu);
            } catch (Exception ex) {
            }
            cpu = (cpuTMP < 16) ? "0" + Integer.toString(cpuTMP, 16).toUpperCase() : Integer.toString(cpuTMP, 16).toUpperCase();
        }
        key = key + cpu;
//        System.out.println("CPU key : " + key);

        this.o10 = "00";
        this.o11 = "03";
        this.o12 = "00";

        String checkStr = key + this.o10 + this.o11 + this.o12;

        while (!(checkKey(checkStr))) {
            checkStr = key + this.o10 + this.o11 + this.o12;
            checkKey(checkStr);
        }
        key = key.substring(0, 6) + "-" + key.substring(6, 10) + "-" + key.substring(10, 16) + "-" + key.substring(16, 20) + this.o10 + "-"
                + this.o11 + this.o12;
        
        System.out.println("final Key : " + key);
    }

    private boolean checkKey(String checkStr) throws NumberFormatException {
        boolean pass = false;
        int i1 = 0;
        byte[] abyte0 = new byte[13];
        int j1 = checkStr.length();

        for (int k1 = 0; i1 < j1 - 1; ++k1) {
            if (checkStr.charAt(i1) == '-') {
                ++i1;
            }
            if ((i1 + 2 <= j1) && (k1 < abyte0.length)) {
                abyte0[k1] = (byte) Integer.parseInt(checkStr.substring(i1, i1 + 2), 16);
                i1 += 2;
            } else {
                k1 = abyte0.length;
                abyte0 = new byte[abyte0.length];
                break;
            }
        }
        int c1 = 0;
        for (int i = 0; i < 10; ++i) {
            c1 += (abyte0[i] & 0xFF);
        }
        int v1 = (abyte0[10] & 0xFF) + ((abyte0[11] & 0xFF) << 8);

        pass = c1 == v1;

        if (!(pass)) {
            this.o10 = ((Integer.toHexString(c1 - v1).length() < 2) ? "0" : "") + Integer.toHexString(Math.abs(c1 - v1)).toUpperCase();
        }

        int c2 = 0;
        for (int i = 0; i < 12; ++i) {
            c2 ^= abyte0[i] & 0xFF;
        }
        int v2 = abyte0[12] & 0xFF;

        pass = (pass) && (c2 == v2);

        if (!(pass)) {
            this.o12 = ((Integer.toHexString(c2).length() < 2) ? "0" : "") + Integer.toHexString(c2).toUpperCase();
        }

        return pass;
    }

    public static void main(String[] args) {
        CrystalClearKeygen crystalClear_keygen1;
        try {
            crystalClear_keygen1 = new CrystalClearKeygen();
        } catch (Exception ex) {
        }
    }

    private String input() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String keyin = br.readLine();
        return keyin;
    }

    private String getDateString(Date day) {
        String str = "";
        long t = day.getTime();
        t /= 1000L;
        String tmp = Long.toHexString(t).toUpperCase();

        str = "00" + tmp.substring(4, 6) + tmp.substring(2, 4) + tmp.substring(0, 2);

        return str;
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
}