package gtu.apache;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class ArgsTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Options options = new Options();

        // options.addOption(選項名稱, 選項別名, 是否帶參數, 選項描述);
        options.addOption("u", "username", true, "輸入姓名");
        options.addOption("p", "password", true, "輸入密碼");
        options.addOption("b", "background", false, "是否背景執行");

        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;
        String username = null;
        String password = null;
        String background = null;
        try {

            args = new String[] { "-u", "UUUUUU", "-p", "PPPPPPP", "-b", "BBBBBB" };

            cmd = parser.parse(options, args);

            if (cmd.hasOption("username")) {
                username = cmd.getOptionValue("u", "");
                System.out.println("輸入 username : " + username);
            }
            if (cmd.hasOption("password")) {
                password = cmd.getOptionValue("p", "");
                System.out.println("輸入 password : " + password);
            }
            if (cmd.hasOption("background")) {
                background = cmd.getOptionValue("b");
                System.out.println("輸入  background : " + background);
            }
        } catch (ParseException e) {
            e.printStackTrace();

            // formatter.printHelp( 程式名稱, options );//純粹印help資訊
            formatter.printHelp("Test.jar", options);
            return;
        }
    }
}
