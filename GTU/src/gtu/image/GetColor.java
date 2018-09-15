package gtu.image;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class GetColor extends JFrame {
    public static void main(String args[]) {
        new GetColor();
    }

    public GetColor() {
        Colors clr = new Colors("file:///D:/workstuff/workspace/gtu-test-code/GTU/nicePic.JPG");
        this.add(clr);
        this.setSize(220, 120);
         gtu.swing.util.JFrameUtil.setVisible(true,this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class Colors extends JComponent {

    String strUrl;
    BufferedImage bi;

    public Colors(String u) {
        this.strUrl = u;
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                displayColor(e);
            }
        });
    }

    public void displayColor(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        if (bi == null || x > bi.getWidth() || y > bi.getHeight())
            return;
        int rgb = bi.getRGB(x, y);
        System.out.println("rgb " + rgb);
    }

    public BufferedImage getImage() {
        return bi;
    }

    public void paint(Graphics g) {
        try {
            URL url = new URL(strUrl);
            bi = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bi != null) {
            g.drawImage(bi, 0, 0, bi.getWidth(), bi.getHeight(), null);
        }
    }
}
