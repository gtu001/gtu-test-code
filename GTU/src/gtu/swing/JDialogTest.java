package gtu.swing;

import javax.swing.JDialog;

public class JDialogTest {

	public JDialogTest() {

		new JDialog() {
			{
				setSize(200, 100);
				setVisible(true);
			}
		};
	}

	public static void main(String[] args) {
		new JDialogTest();
	}
}