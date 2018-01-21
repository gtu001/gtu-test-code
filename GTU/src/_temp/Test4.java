package _temp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Test4 {

    public static void main(String[] a) throws Exception {
        Test4 t = new Test4();
        t.execute();
        System.out.println("done...");
    }
    
    public void execute() throws IOException, InterruptedException{
        try {           
            Runtime rt = Runtime.getRuntime ();
            Process proc = rt.exec (new String[]{"cmd", "/c", "telnet", "ptt.cc"
                    + ""});
 
            StreamConsumer sc1 = new StreamConsumer(proc.getInputStream(), "input");
            StreamConsumer sc2 = new StreamConsumer(proc.getErrorStream(), "error");
            
            sc1.start();
            sc2.start();
            
 
            int exitVal = proc.waitFor ();
            System.out.println ("Process exitValue: " + exitVal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static class StreamConsumer extends Thread {
        InputStream is;
        String type;
         
        StreamConsumer (InputStream is, String type) {
            this.is = is;
            this.type = type;
        }
         
        public void run () {
            try {
                InputStreamReader isr = new InputStreamReader (is, "big5");
                BufferedReader br = new BufferedReader (isr);
                String line = null;
                while ((line = br.readLine()) != null)
                    System.out.println (type + ">" + line);   
            } catch (IOException ioe) {
                ioe.printStackTrace(); 
            }
        }
    }
}
