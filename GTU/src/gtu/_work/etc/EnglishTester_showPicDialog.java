package gtu._work.etc;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class EnglishTester_showPicDialog extends JFrame {
    private MyPanel jPanel1;

    /**
    * Auto-generated main method to display this JDialog
    */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                EnglishTester_showPicDialog inst = new EnglishTester_showPicDialog();
                inst.setVisible(true);
            }
        });
    }
    
    public EnglishTester_showPicDialog() {
        super();
        initGUI();
    }
    
    private void initGUI() {
        try {
            {
            }
            BorderLayout thisLayout = new BorderLayout();
            getContentPane().setLayout(thisLayout);
            {
                jPanel1 = new MyPanel();
                getContentPane().add(jPanel1, BorderLayout.CENTER);
            }
            this.setSize(251, 207);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class MyPanel extends JPanel {
        private Image image = null;
        protected void paintComponent(Graphics g) {
            this.setOpaque(false); //JPanel 透明模式
            if(image != null){
                g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        }
        public Image getImage() {
            return image;
        }
        public void setImage(Image image) {
            this.image = image;
        }
    }

    public MyPanel getjPanel1() {
        return jPanel1;
    }

    public void setjPanel1(MyPanel jPanel1) {
        this.jPanel1 = jPanel1;
    }
}
