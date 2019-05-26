package gtu.log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

public class LogBlockCreater {

    private static final String START_BLOCK;
    private static final String END_BLOCK;
    private static final String MIDDLE_LINE_BLOCK;
    private static final String MESSAGE_BLOCK;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("    ╔════════════════════════════════════════════════════════════════════════════════════════\r\n");
        START_BLOCK = sb.toString();
        sb.setLength(0);
        sb.append("    ║ %s                                                                                     \r\n");
        MESSAGE_BLOCK = sb.toString();
        sb.setLength(0);
        sb.append("    ╟────────────────────────────────────────────────────────────────────────────────────────\r\n");
        MIDDLE_LINE_BLOCK = sb.toString();
        sb.setLength(0);
        sb.append("    ╚════════════════════════════════════════════════════════════════════════════════════════\r\n");
        END_BLOCK = sb.toString();
        sb.setLength(0);
    }

    private LogBlockCreater() {
    }

    private StringBuilder sb = new StringBuilder();

    public LogBlockCreater appendStart() {
        sb.append(START_BLOCK);
        return this;
    }

    public LogBlockCreater appendEnd() {
        sb.append(END_BLOCK);
        return this;
    }

    public LogBlockCreater appendMiddleLine() {
        sb.append(MIDDLE_LINE_BLOCK);
        return this;
    }

    public LogBlockCreater appendMessage(String line) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new StringReader(line));
            for (String l = null; (l = reader.readLine()) != null;) {
                sb.append(String.format(MESSAGE_BLOCK, l));
            }
        } catch (Exception ex) {
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
            }
        }
        return this;
    }

    public LogBlockCreater appendMessage(List<String> lines) {
        for (String l : lines) {
            sb.append(String.format(MESSAGE_BLOCK, l));
        }
        return this;
    }

    public String getResult(boolean addNewLine) {
        String prefix = addNewLine ? "\r\n" : "";
        return prefix + sb.toString();
    }
}
