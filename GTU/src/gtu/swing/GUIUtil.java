package gtu.swing;

import java.awt.Frame;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;

public class GUIUtil {

	/**
	 * 取得最上層的視窗
	 * 
	 * @return
	 */
	public static Frame getTopFrame() {
		Frame[] frames = Frame.getFrames();
		for (int i = 0; i < frames.length; i++) {
			if (frames[i].getFocusOwner() != null) {
				return frames[i];
			}
		}
		if (frames.length > 0) {
			return frames[0];
		}
		return null;
	}

	/**
	 * Return <TT>true</TT> if <TT>frame</TT> is a tool window. I.E. is the
	 * <TT>JInternalFrame.isPalette</TT> set to <TT>Boolean.TRUE</TT>?
	 * 
	 * @param frame
	 *            The <TT>JInternalFrame</TT> to be checked.
	 * 
	 * @throws IllegalArgumentException
	 *             If <TT>frame</TT> is <TT>null</TT>.
	 */
	public static boolean isToolWindow(JInternalFrame frame) {
		if (frame == null) {
			throw new IllegalArgumentException("null JInternalFrame passed");
		}

		final Object obj = frame.getClientProperty("JInternalFrame.isPalette");
		return obj != null && obj == Boolean.TRUE;
	}

	/**
	 * Make the passed internal frame a Tool Window.
	 */
	public static void makeToolWindow(JInternalFrame frame, boolean isToolWindow) {
		if (frame == null) {
			throw new IllegalArgumentException("null JInternalFrame passed");
		}
		frame.putClientProperty("JInternalFrame.isPalette",
				isToolWindow ? Boolean.TRUE : Boolean.FALSE);
	}

	public static void moveToFront(final JInternalFrame fr) {
		if (fr != null) {
			processOnSwingEventThread(new Runnable() {
				public void run() {
					fr.moveToFront();
 					gtu.swing.util.JFrameUtil.setVisible(true,fr);
					try {
						fr.setSelected(true);
						if (fr.isIcon()) {
							fr.setIcon(false);
						}
						fr.setSelected(true);
					} catch (PropertyVetoException ex) {

					}
					fr.requestFocus();
				}
			});
		}

	}

	public static void processOnSwingEventThread(Runnable todo) {
		processOnSwingEventThread(todo, false);
	}

	public static void processOnSwingEventThread(Runnable todo, boolean wait) {
		if (todo == null) {
			throw new IllegalArgumentException("Runnable == null");
		}

		if (wait) {
			if (SwingUtilities.isEventDispatchThread()) {
				todo.run();
			} else {
				try {
					SwingUtilities.invokeAndWait(todo);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
		} else {
			if (SwingUtilities.isEventDispatchThread()) {
				todo.run();
			} else {
				SwingUtilities.invokeLater(todo);
			}
		}
	}
}
