package gtu.ant;

import java.awt.TrayIcon.MessageType;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import gtu.swing.util.HideInSystemTrayHelper;

/**
 * 做系統通知用
 */
public class AntNotify extends Task {

    private AntConfigHelper config;

    private String type;
    private String title;
    private String message;

    private static boolean isWindows = false;
    static {
        if (System.getProperty("os.name").startsWith("Windows")) {
            isWindows = true;
        } else if ("Linux".equals(System.getProperty("os.name"))) {
            isWindows = false;
        }
    }

    public static void main(String[] args) {
        AntNotify t = new AntNotify();
        t.message = args[0];
        t.execute();
    }

    @Override
    public void execute() throws BuildException {
        try {
            config = AntConfigHelper.of(this.getProject());

            this.log("[notify]" + title + " : " + message);

            type = StringUtils.trimToEmpty(type);
            title = StringUtils.trimToEmpty(title);
            message = StringUtils.trimToEmpty(message);

            type = config.getParseAfterValue(type);
            title = config.getParseAfterValue(title);
            message = config.getParseAfterValue(message);

            if (StringUtils.isBlank(message)) {
                throw new Exception("訊息不可為空");
            }

            if (true) {
                MessageType messageType = MessageType.INFO;
                try {
                    MessageType.valueOf(type.toUpperCase());
                } catch (Exception ex) {
                }
                try {
                    HideInSystemTrayHelper inst = HideInSystemTrayHelper.newInstance();
                    inst.apply();
                    inst.displayMessage(title, message, messageType);
                    inst.removeTray(10000L);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (isWindows) {
                Runtime.getRuntime().exec(String.format("cmd /c start echo %s", message));
                this.log("# window mode");

                this.getProject().setNewProperty("notify_cmd", "cmd");
                this.getProject().setNewProperty("notify_message", String.format(" /c start echo \"%s\"", message));
            } else {
                String ubuntuExec = String.format("gnome-terminal -- sh -c \"echo %s; sleep 0; exec bash\"", message);
                String mostLinuxExec = String.format("xterm -e \"echo \"%s\"; bash\" ", message);
                runExeCommand(ubuntuExec);
                runExeCommand(mostLinuxExec);
                this.log("# linux mode");

                this.getProject().setNewProperty("notify_cmd", "gnome-terminal");
                this.getProject().setNewProperty("notify_message", String.format(" -- sh -c \"echo %s; sleep 0; exec bash\"", message));
            }
        } catch (Exception e) {
            throw new BuildException(e);
        }
    }

    private void runExeCommand(String command) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
