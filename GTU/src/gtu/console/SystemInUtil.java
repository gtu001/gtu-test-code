package gtu.console;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Troy 2012/1/2
 * 
 */
public class SystemInUtil {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Console console = System.console();
        if (console == null) {
            System.err.println("sales: unable to obtain console");
            return;
        }

        String username = console.readLine("Enter username: ");
        System.out.println(username);
        char[] password = console.readPassword("Enter password: ");
        System.out.println(Arrays.toString(password));

        console.printf("done... %s", username);
    }

    /**
     * 讀取System.in輸入訊息 當輸入quit或exit則回傳輸入結果
     * 
     * @return
     */
    public static String readContent() {
        System.out.println("請輸入 : ");
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader bufreader = new BufferedReader(reader);
        StringBuffer sb = new StringBuffer();
        try {
            while (true) {
                String line = bufreader.readLine();
                if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                    break;
                } else {
                    sb.append(line + "\r\n");
                    System.out.println(line);
                }
            }
            bufreader.close();
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 只讀一行
     * 
     * @return
     */
    public static String readLine(String questionLine) {
        if (StringUtils.isNotBlank(questionLine)) {
            System.out.print(questionLine);
        }
        return readLine();
    }

    /**
     * 只讀一行
     * 
     * @return
     */
    public static String readLine() {
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader bufreader = new BufferedReader(reader);
        String line = null;
        try {
            while (line == null) {
                line = bufreader.readLine();
                Thread.sleep(100);
            }
            // bufreader.close();
            // reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return line;
    }
}
