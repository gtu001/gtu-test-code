package gtu.swing.util;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
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
public class LookAndFeelUtil extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private JComboBox jComboBox1;
    private JButton jButton1;
    private JTextArea jTextArea1;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton1;
    private JTextField jTextField1;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LookAndFeelUtil inst = new LookAndFeelUtil();
                inst.setLocationRelativeTo(null);
                 gtu.swing.util.JFrameUtil.setVisible(true,inst);
            }
        });
    }

    public LookAndFeelUtil() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            final JFrame frame = this;
            FlowLayout thisLayout = new FlowLayout();
            getContentPane().setLayout(thisLayout);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            {
                DefaultComboBoxModel jComboBox1Model = new DefaultComboBoxModel();
                for (LookAndFeel look : LookAndFeel.values()) {
                    jComboBox1Model.addElement(look);
                }
                jComboBox1 = new JComboBox();
                getContentPane().add(jComboBox1);
                jComboBox1.setModel(jComboBox1Model);
                jComboBox1.setPreferredSize(new java.awt.Dimension(176, 31));
                jComboBox1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        ((LookAndFeel) jComboBox1.getSelectedItem()).apply(frame);
                    }
                });
            }
            {
                jButton1 = new JButton();
                getContentPane().add(jButton1);
                jButton1.setText("jButton1");
                jButton1.setPreferredSize(new java.awt.Dimension(71, 30));
            }
            {
                jTextField1 = new JTextField();
                getContentPane().add(jTextField1);
                jTextField1.setText("jTextField1");
                jTextField1.setPreferredSize(new java.awt.Dimension(76, 29));
            }
            {
                jRadioButton1 = new JRadioButton();
                getContentPane().add(jRadioButton1);
                jRadioButton1.setText("jRadioButton1");
            }
            {
                jRadioButton2 = new JRadioButton();
                getContentPane().add(jRadioButton2);
                jRadioButton2.setText("jRadioButton2");
            }
            {
                jTextArea1 = new JTextArea();
                getContentPane().add(jTextArea1);
                jTextArea1.setText("jTextArea1");
                jTextArea1.setPreferredSize(new java.awt.Dimension(104, 71));
            }
            pack();
            this.setSize(370, 164);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum LookAndFeel {
        METAL("javax.swing.plaf.metal.MetalLookAndFeel"), //
        WINDOW("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"), //
        MOTIF("com.sun.java.swing.plaf.motif.MotifLookAndFeel"), //
        NIMBUS("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"), //
        SYNTH("javax.swing.plaf.synth.SynthLookAndFeel"), //
        WIN_CLASSIC("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"), //
        ;
        final String type;

        LookAndFeel(String type) {
            this.type = type;
        }

        public void apply(JFrame frame) {
            try {
                UIManager.setLookAndFeel(type);
                SwingUtilities.updateComponentTreeUI(frame);
                frame.pack();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
