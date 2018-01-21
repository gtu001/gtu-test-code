package gtu.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class JFrameTest {

	public static void main(String args[]) {
		JFrameTest test = new JFrameTest();

		test.showScreenSize();

		// 是否使用古典風
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame f = new JFrame("JFrame標題");

		f.setLayout(new FlowLayout());
		// f.setLayout(new GridLayout("列", "欄"));

		test.setFrameLocationAndSize(500, 300, f);

		test.frameCloseMode(f);
		test.frameCloseConfirm(f);

		test.frameEscEvents(f);

		test.setIconImages(f);

		test.frameCantMoveOutScreen(f);

		test.setting(f);

		f.setVisible(true);

		test.showExistsFrameInfo();

		// UIManager.setLookAndFeel(className);
		SwingUtilities.updateComponentTreeUI(f);
	}

	/**
	 * 視窗最大化
	 * 
	 * @param frame
	 */
	private void frameMaximize(JFrame frame) {
		GraphicsEnvironment env = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}

	/**
	 * 視窗無法被移出螢幕
	 * 
	 * @param frame
	 */
	private void frameCantMoveOutScreen(JFrame frame) {
		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent evt) {
				Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
				int x = evt.getComponent().getX();
				int y = evt.getComponent().getY();
				if (y < 0) {
					y = 0;
				}
				if (x < 0) {
					x = 0;
				}
				if (x > size.getWidth() - evt.getComponent().getWidth()) {
					x = (int) size.getWidth() - evt.getComponent().getWidth();
				}
				if (y > size.getHeight() - evt.getComponent().getHeight()) {
					y = (int) size.getHeight() - evt.getComponent().getHeight();
				}
				evt.getComponent().setLocation(x, y);
			}
		});
	}

	/**
	 * 雜項設定
	 * 
	 * @param frame
	 */
	private void setting(JFrame frame) {
		frame.setResizable(true);

		// 設為true則無邊框也沒有最上面(縮小,關閉)那一列
		frame.setUndecorated(false);

		// 讓視窗按照所含的原建範圍顯示
		// frame.pack();

		frame.setAlwaysOnTop(true);
		frame.setLocationByPlatform(true);

		// 設為false拖曳效果會不見
		frame.setFocusableWindowState(true);
	}

	/**
	 * 取得所有存在視窗資訊
	 */
	private void showExistsFrameInfo() {
		Frame[] frames = Frame.getFrames();
		System.out.println("frame count = " + frames.length);
		for (int i = 0; i < frames.length; i++) {
			String title = frames[i].getTitle();
			boolean isVisible = frames[i].isVisible();
			System.out.println("視窗 : [" + title + "] 是否顯示  : [" + isVisible
					+ "]");
		}
	}

	/**
	 * 設定關閉視窗須確認
	 * 
	 * @param frame
	 */
	private void frameCloseConfirm(final JFrame frame) {
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
			}

			public void windowClosing(WindowEvent e) {
				if (JOptionPane.showConfirmDialog(null, "確定關閉?") == JOptionPane.YES_OPTION) {
					frame.setVisible(false);
					frame.dispose();
				}
			}
		});
	}

	/**
	 * 設定ICON
	 * 
	 * @param frame
	 */
	private void setIconImages(JFrame frame) {
		final int SMALL_ICON_WIDTH = 16;
		final int SMALL_ICON_HEIGHT = 16;
		final int SMALL_ICON_RENDER_WIDTH = 10;

		List<BufferedImage> images = new ArrayList<BufferedImage>();

		BufferedImage bi = new BufferedImage(SMALL_ICON_WIDTH,
				SMALL_ICON_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, SMALL_ICON_RENDER_WIDTH, SMALL_ICON_HEIGHT);
		g.dispose();
		images.add(bi);

		frame.setIconImages(images);

		// 下面效果不明
		// frame.setIconImage(Toolkit.getDefaultToolkit().getImage("icon.gif"));
	}

	/**
	 * 當按下Esc時觸發的事件
	 * 
	 * @param frame
	 */
	private void frameEscEvents(JFrame frame) {
		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent paramActionEvent) {
				System.out.println("Esc action performd...");
			}
		};
		JPanel content = (JPanel) frame.getContentPane();
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		content.registerKeyboardAction(actionListener, stroke,
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	/**
	 * 關閉視窗的模式
	 * 
	 * @param jframe
	 */
	private void frameCloseMode(JFrame jframe) {
		// JFrame.EXIT_ON_CLOSE; //關閉就中斷程式
		// JFrame.HIDE_ON_CLOSE; //關閉就隱藏不中斷程式(預設值)
		// JFrame.DISPOSE_ON_CLOSE;
		// JFrame.DO_NOTHING_ON_CLOSE;
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * 顯示螢幕尺寸
	 */
	private void showScreenSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int) screenSize.getHeight();
		int width = (int) screenSize.getWidth();
		System.out.println(String.format("螢幕  : 寬 [%d] 高[%d]", width, height));
	}

	/**
	 * 設定視窗大小位置
	 * 
	 * @param width
	 * @param height
	 * @param jframe
	 */
	private void setFrameLocationAndSize(int width, int height, JFrame jframe) {
		// 取得中心點
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getCenterPoint();
		// 取得置中位置
		int left = (int) center.getX() - width / 2;
		int top = (int) center.getY() - height / 2;

		jframe.setSize(width, height);// 寬 高
		jframe.setLocation(left, top);// 寬 高

		// 或是一次設兩種
		jframe.setBounds(left, top, width, height);
	}
}