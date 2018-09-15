package gtu._work.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import gtu.swing.util.JCommonUtil;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class SingletonMakerUI extends JFrame {

    private JPanel contentPane;
    private JTextField classText;
    private JComboBox typeComboBox;
    private JTextArea resultArea;
    private JTextArea parameterArea;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SingletonMakerUI frame = new SingletonMakerUI();
                     gtu.swing.util.JFrameUtil.setVisible(true,frame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public SingletonMakerUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 499, 377);
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
                RowSpec.decode("default:grow"),
                FormFactory.RELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"),}));

        JLabel label = new JLabel("類型");
        panel.add(label, "2, 2, right, default");

        typeComboBox = new JComboBox();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for(TypeComboEnum e : TypeComboEnum.values()) {
            model.addElement(e);
        }
        typeComboBox.setModel(model);
        panel.add(typeComboBox, "4, 2, fill, default");

        JLabel lblNewLabel = new JLabel("類別名");
        panel.add(lblNewLabel, "2, 4, right, default");

        classText = new JTextField();
        panel.add(classText, "4, 4, fill, default");
        classText.setColumns(10);
        
        JLabel label_1 = new JLabel("參數");
        panel.add(label_1, "2, 6");
        
        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane, "4, 6, fill, fill");
        
        parameterArea = new JTextArea();
        scrollPane.setViewportView(parameterArea);

        JPanel panel_2 = new JPanel();
        panel.add(panel_2, "4, 8, fill, fill");

        JButton btnNewButton = new JButton("產生");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeBtnAction();
            }
        });
        panel_2.add(btnNewButton);
        
        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("New tab", null, panel_1, null);
        panel_1.setLayout(new BorderLayout(0, 0));
        
        JLabel lblNewLabel_1 = new JLabel("New label");
        panel_1.add(lblNewLabel_1, BorderLayout.NORTH);
        
        JLabel lblNewLabel_2 = new JLabel("New label");
        panel_1.add(lblNewLabel_2, BorderLayout.WEST);
        
        JLabel lblNewLabel_3 = new JLabel("New label");
        panel_1.add(lblNewLabel_3, BorderLayout.EAST);
        
        JLabel lblNewLabel_4 = new JLabel("New label");
        panel_1.add(lblNewLabel_4, BorderLayout.SOUTH);
        
        resultArea = new JTextArea();
        panel_1.add(JCommonUtil.createScrollComponent(resultArea), BorderLayout.CENTER);
    }

    private enum TypeComboEnum {
        Singleton() {
            @Override
            String makeClass(SingletonMakerUI ui) {
                StringBuilder sb = new StringBuilder();
                sb.append("public class {0} '{'                                     \n");
                sb.append("    private static final {0} _INST = new {0}();  \n");
                sb.append("    private {0} () '{'}                                  \n");
                sb.append("    public static {0} getInstance() '{'                  \n");
                sb.append("        return _INST;                                        \n");
                sb.append("    }                                                        \n");
                sb.append("}                                                            \n");
                return MessageFormat.format(sb.toString(), ui.classText.getText());
            }
        }, //
        NewInstance() {
            @Override
            String makeClass(SingletonMakerUI ui) {
                StringBuilder sb = new StringBuilder();
                sb.append("public class {0} '{'                                     \n");
                sb.append("    private {0} () '{'}                                  \n");
                sb.append("    public static {0} newInstance() '{'                  \n");
                sb.append("        return new {0}();                                        \n");
                sb.append("    }                                                        \n");
                sb.append("}                                                            \n");
                return MessageFormat.format(sb.toString(), ui.classText.getText());
            }
        }, //
        Builder() {
            private String getParameterDeclare(String[] parameters) {
                StringBuilder sb = new StringBuilder();
                sb.append("        private String {0};                                                                    \n");
                StringBuilder sb2 = new StringBuilder();
                for(String param : parameters) {
                    sb2.append(MessageFormat.format(sb.toString(), param));
                }
                return sb2.toString();
            }
            
            private String getParameterMethod(String className, String[] parameters) {
                StringBuilder sb = new StringBuilder();
                sb.append("        public {0} {1}(String {1}) '{'                                                  \n");
                sb.append("            this.{1} = {1};                                                             \n");
                sb.append("            return this;                                                                              \n");
                sb.append("        }                                                                                             \n");
                StringBuilder sb2 = new StringBuilder();
                for(String param : parameters) {
                    sb2.append(MessageFormat.format(sb.toString(), className, param));
                }
                return sb2.toString();
            }
            
            @Override
            String makeClass(SingletonMakerUI ui) {
                StringBuilder sb = new StringBuilder();
                String[] parameters = StringUtils.trimToEmpty(ui.parameterArea.getText()).split(",", -1);
                
                sb.append("    public class {0} '{'                                                                              \n");
                sb.append("        private {0}() '{'}                                                                            \n");
                sb.append("        public static {0} newInstance() '{'                                                           \n");
                sb.append("            return new {0}();                                                                       \n");
                sb.append("        }                                                                                             \n");
                sb.append("                                                                                                      \n");
                sb.append("{1}");
                sb.append("{2}");
                sb.append("        public void build() '{'                                                                         \n");
                sb.append("            try '{'                                                                                     \n");
                sb.append("                //TODO                                                                                \n");
                sb.append("            }catch(Exception ex) '{'                                                                    \n");
                sb.append("                throw new RuntimeException(\"{0}.build ERR : \" + ex.getMessage() ,ex);               \n");
                sb.append("            }                                                                                         \n");
                sb.append("        }                                                                                             \n");
                sb.append("    }                                                                                                 \n");
                
                String declare = getParameterDeclare(parameters);
                String method = getParameterMethod(ui.classText.getText(), parameters);
                
                return MessageFormat.format(sb.toString(), ui.classText.getText(), declare, method);
            }
        }, //
        ;

        TypeComboEnum() {
        }

        abstract String makeClass(SingletonMakerUI ui);
    }
    


    private void makeBtnAction() {
        try {
            String className = classText.getText();
            Validate.notEmpty(className, "類別名未填");
            Validate.notNull(typeComboBox.getSelectedItem(), "類型未選");
            
            TypeComboEnum type = (TypeComboEnum)typeComboBox.getSelectedItem();
            String resultStr = type.makeClass(this);
            resultArea.setText(resultStr);

            JCommonUtil._jOptionPane_showMessageDialog_info("成功!");
        } catch (Exception ex) {
            JCommonUtil.handleException(ex);
        }
    }
}
