package gtu.console;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        return readContent(System.in);
    }

    public static String readContent(InputStream in) {
        System.out.println("請輸入(exit,quit為結束) : ");
        InputStreamReader reader = null;
        BufferedReader bufreader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new InputStreamReader(in);
            bufreader = new BufferedReader(reader);
            while (true) {
                String line = bufreader.readLine();
                if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                    break;
                } else {
                    sb.append(line + "\r\n");
                    System.out.println(line);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                bufreader.close();
            } catch (Exception e) {
            }
            try {
                reader.close();
            } catch (Exception e) {
            }
        }
        return sb.toString();
    }

    /**
     * 讀入console資料
     * 
     * @param trim
     *            是否要trim
     * @param emptyIgnore
     *            是否忽略空白行
     * @param distinct
     *            是否不重複加入
     * @return
     */
    public static List<String> readContentToList(boolean trim, boolean emptyIgnore, boolean distinct) {
        System.out.println("請輸入 : ");
        List<String> lst = new ArrayList<String>();
        InputStreamReader reader = new InputStreamReader(System.in);
        BufferedReader bufreader = new BufferedReader(reader);
        try {
            while (true) {
                String line = bufreader.readLine();
                if (line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit")) {
                    break;
                } else {
                    if (trim) {
                        line = StringUtils.trimToEmpty(line);
                    }
                    if (emptyIgnore && StringUtils.isBlank(line)) {
                        continue;
                    }
                    if (distinct) {
                        if (!lst.contains(line)) {
                            lst.add(line);
                            continue;
                        }
                    }
                    lst.add(line);
                    System.out.println(line);
                }
            }
            bufreader.close();
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lst;
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
