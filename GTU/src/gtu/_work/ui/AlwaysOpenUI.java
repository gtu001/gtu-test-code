package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang3.StringUtils;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.properties.PropertiesUtilBean;
import gtu.swing.util.HideInSystemTrayHelper;
import gtu.swing.util.JCommonUtil;

public class AlwaysOpenUI extends JFrame {

    private JPanel contentPane;
    private JTextField textField;
    private JToggleButton tglbtnStartend;
    private Timer timer = new Timer();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AlwaysOpenUI frame = new AlwaysOpenUI();
                     gtu.swing.util.JFrameUtil.setVisible(true,frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private PropertiesUtilBean config = new PropertiesUtilBean(AlwaysOpenUI.class);

    /**
     * Create the frame.
     */
    public AlwaysOpenUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        
        JPanel panel = new JPanel();
        tabbedPane.addTab("New tab", null, panel, null);
        panel.setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.RELATED_GAP_COLSPEC,
                FormFactory.DEFAULT_COLSPEC,
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),},
            new RowSpec[] {
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,
                FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC,}));
        
        JLabel lbln = new JLabel("每N秒觸發一次");
        panel.add(lbln, "2, 2, right, default");
        
        textField = new JTextField();
        panel.add(textField, "4, 2, fill, default");
        textField.setColumns(10);
        
        if(config.getConfigProp().containsKey("sec")) {
            String sec = config.getConfigProp().getProperty("sec");
            textField.setText(sec);
        }
        
        tglbtnStartend = new JToggleButton("啟動/結束");
        tglbtnStartend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String sec = textField.getText();
                    if(tglbtnStartend.isSelected()) {
                        timer = new Timer();
                        timer.schedule(createTask(), 0, Integer.parseInt(sec) * 1000);
                        
                        if(StringUtils.isNumeric(sec)) {
                            config.getConfigProp().setProperty("sec", sec);
                            config.store();
                        }
                    }else {
                        timer.cancel();
                    }
                }catch(Exception ex) {
                    JCommonUtil.handleException(ex);
                }
            }
        });
        
        this.addWindowListener(new WindowListener() {
            
            @Override
            public void windowOpened(WindowEvent e) {
            }
            
            @Override
            public void windowIconified(WindowEvent e) {
            }
            
            @Override
            public void windowDeiconified(WindowEvent e) {
            }
            
            @Override
            public void windowDeactivated(WindowEvent e) {
            }
            
            @Override
            public void windowClosing(WindowEvent e) {
                String sec = textField.getText();
                if(StringUtils.isNumeric(sec)) {
                    config.getConfigProp().setProperty("sec", sec);
                    config.store();
                }
            }
            
            @Override
            public void windowClosed(WindowEvent e) {
            }
            
            @Override
            public void windowActivated(WindowEvent e) {
            }
        });
        panel.add(tglbtnStartend, "4, 16");
        
        JCommonUtil.setJFrameCenter(this);
        HideInSystemTrayHelper.newInstance().apply(this, "保持登入工具", "resource/images/ico/whtsuvifzdlhiqcxubpu.ico");
    }
    
    private TimerTask createTask() {
        return new TimerTask() {
            private int getAdd() {
                int[] arry = new int[] { -1, 1 };
                int pos = new Random().nextInt(arry.length);
                return arry[pos];
            }

            @Override
            public void run() {
                try {
                    Point p = MouseInfo.getPointerInfo().getLocation();
                    Robot robot = new Robot();
                    robot.mouseMove(p.x + getAdd(), p.y + getAdd());
                    System.out.println("move pos!!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }
}
