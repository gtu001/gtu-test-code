package gtu.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EasyTextButtonSwing extends JFrame {
	private static final long serialVersionUID = 1L;

	private JButton buttonOK = new JButton("OK");

	private JTextField textName = new JTextField(15);

	public static void main(String[] args) {
		new EasyTextButtonSwing();
	}

	public EasyTextButtonSwing() {
		this.setSize(325, 100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		ButtonListener bl = new ButtonListener();

		JPanel panel1 = new JPanel();

		panel1.add(new JLabel("Enter your name: "));
		panel1.add(textName);

		buttonOK.addActionListener(bl);
		panel1.add(buttonOK);

		this.add(panel1);

 		gtu.swing.util.JFrameUtil.setVisible(true,this);
	}

	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonOK) {
				String name = textName.getText();

				if (name.length() == 0) {
					JOptionPane.showMessageDialog(EasyTextButtonSwing.this, "=0", "",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(EasyTextButtonSwing.this,
							"Good morning " + name, "Salutations",
							JOptionPane.INFORMATION_MESSAGE);
				}
				textName.requestFocus();
			}
		}
	}
}
