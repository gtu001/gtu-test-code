package gtu._work.startup;

import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class ProxySwaperUI extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JToggleButton noIeProxy;
    private JToggleButton useIeProxy;

    /**
     * Auto-generated main method to display this JFrame
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ProxySwaperUI inst = new ProxySwaperUI();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public ProxySwaperUI() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            {
                useIeProxy = new JToggleButton();
                getContentPane().add(useIeProxy, BorderLayout.NORTH);
                useIeProxy.setText("USE IE PROXY");
                useIeProxy.setPreferredSize(new java.awt.Dimension(355, 88));
                useIeProxy.addActionListener(new ActionListener() {
                    StringBuilder sb = new StringBuilder();
                    {
                        sb.append(" @echo 啟用IE Proxy代理                                                                                                      \n");
                        sb.append(" @reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyEnable /t REG_DWORD /d 1 /f           \n");
                        sb.append(" @reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyServer /d \"192.168.2.14:3128\" /f    \n");
                    }

                    public void actionPerformed(ActionEvent evt) {
                        executeCommand(sb);
                        JCommonUtil._jOptionPane_showMessageDialog_info("啟用IE Proxy代理\n192.168.2.14:3128");
                    }
                });
            }
            {
                noIeProxy = new JToggleButton();
                getContentPane().add(noIeProxy, BorderLayout.CENTER);
                noIeProxy.setText("NO IE PROXY");
                noIeProxy.addActionListener(new ActionListener() {
                    StringBuilder sb = new StringBuilder();
                    {
                        sb.append(" @echo 關閉IE Proxy代理                                                                                              \n");
                        sb.append(" @reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" /v ProxyEnable /t REG_DWORD /d 0 /f     \n");
                    }

                    public void actionPerformed(ActionEvent evt) {
                        executeCommand(sb);
                        JCommonUtil._jOptionPane_showMessageDialog_info("關閉IE Proxy代理");
                    }
                });
            }
            pack();
            this.setSize(363, 212);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void executeCommand(StringBuilder sb) {
        try {
            BufferedReader reader = new BufferedReader(new StringReader(sb.toString()));
            for (String line = null; (line = reader.readLine()) != null;) {
                System.out.println("cmd /c " + line);
                Runtime.getRuntime().exec("cmd /c " + line);
            }
            reader.close();
        } catch (IOException e) {
            JCommonUtil.handleException(e);
        }
    }
}
