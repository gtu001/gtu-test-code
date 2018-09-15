package gtu._work.ui;

import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;

public class PLSqlCallerGenerateUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextArea textArea_1;
    private JTextArea textArea;
    private JTabbedPane tabbedPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PLSqlCallerGenerateUI frame = new PLSqlCallerGenerateUI();
                     gtu.swing.util.JFrameUtil.setVisible(true,frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void tabbedPaneMouseClicked() {
        String str = textArea_1.getText();
        if (StringUtils.isBlank(str)) {
            return;
        }
        str = str.replaceAll("\n", "");
        System.out.println(str);
        Pattern ptn = Pattern.compile("([\\w\\.]+)\\((.*)\\)", Pattern.MULTILINE);
        Matcher mth = ptn.matcher(str);
        if (!mth.find()) {
            JCommonUtil._jOptionPane_showMessageDialog_error("格式不符");
            return;
        }
        String methodName = mth.group(1);
        String methodParameters = mth.group(2);
        System.out.println("methodName = " + methodName);
        System.out.println("methodParameters = " + methodParameters);

        List<ParamType> pList = new ArrayList<ParamType>();

        Pattern ptn2 = Pattern.compile("(\\w+)", Pattern.MULTILINE);
        String[] parameters = methodParameters.split(",", -1);
        for (String paramStr : parameters) {
            paramStr = paramStr.replaceAll("\n", "");
            Matcher mth2 = ptn2.matcher(paramStr);

            int ii = -1;
            ParamType p = new ParamType();
            while (mth2.find()) {
                ii++;
                String str2 = mth2.group();
                if (ii == 0) {
                    p.name = str2;
                } else {
                    if (str2.equalsIgnoreCase("in")) {
                        p.in = true;
                    } else if (str2.equalsIgnoreCase("out")) {
                        p.out = true;
                    } else {
                        p.type = str2;
                    }
                }
            }

            pList.add(p);
            System.out.println(p);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(" declare                                     \n");
        sb.append("                                             \n");
        for (ParamType p : pList) {
            String lengthStr = "";
            if (p.type.equalsIgnoreCase("varchar2")) {
                lengthStr = "(100)";
            }
            sb.append("    " + p.name + " " + p.type + "" + lengthStr + ";\n");
        }
        sb.append("                                             \n");
        sb.append(" begin                                       \n");
        sb.append("                                             \n");

        for (ParamType p : pList) {
            sb.append("    " + p.name + " := ''; \n");
        }

        sb.append("    "+methodName+"(\n");
        for (int ii = 0; ii < pList.size(); ii++) {
            ParamType p = pList.get(ii);
            String comma = ",";
            if (ii == pList.size() - 1) {
                comma = "";
            }
            sb.append("        " + p.name + "" + comma + "\n");
        }
        sb.append("    );\n");

        for (ParamType p : pList) {
            String prefix = "--";
            if (p.out) {
                prefix = "";
            }
            sb.append("    " + prefix + " dbms_output.put_line('" + p.name + " = ' || " + p.name + "); \n");
        }

        sb.append("                                             \n");
        sb.append(" end;                                        \n");

        textArea.setText(sb.toString());
    }

    private static class ParamType {
        String name;
        String type;
        boolean in;
        boolean out;

        @Override
        public String toString() {
            return "ParamType [name=" + name + ", type=" + type + ", in=" + in + ", out=" + out + "]";
        }
    }

    /**
     * Create the frame.
     */
    public PLSqlCallerGenerateUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 587, 423);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tabbedPaneMouseClicked();
            }
        });
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        tabbedPane.addTab("New tab", null, panel, null);
        panel.setLayout(new BorderLayout(0, 0));

        textArea_1 = new JTextArea();
        createScrollComponent(panel, textArea_1);
        // panel.add(textArea_1, BorderLayout.CENTER);

        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));

        textArea = new JTextArea();
        createScrollComponent(panel_1, textArea);
        // panel_1.add(textArea, BorderLayout.CENTER);
    }

    private void createScrollComponent(JPanel jPanel2, JComponent jcommponent) {
        JScrollPane jScrollPane1 = new JScrollPane();
        jPanel2.add(jScrollPane1, BorderLayout.CENTER);
//        jScrollPane1.setPreferredSize(new java.awt.Dimension(411, 262));
        jScrollPane1.setViewportView(jcommponent);
    }
}
