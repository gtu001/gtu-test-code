package _temp.swing;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// w ww. j a va 2s. c om
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Main {
    JFrame frame = new JFrame();
    JScrollPane scrollPane = new JScrollPane();
    JTextArea textArea = new JTextArea(10, 15);
    JButton button = new JButton("change");
    Font newFont = new Font("Courier", Font.PLAIN, 10);

    public Main() {
        textArea.setText("This is just a line of text");
        textArea.setFont(newFont);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setFont(textArea.getFont().deriveFont(20f));
                frame.pack();
            }
        });
        scrollPane.setViewportView(textArea);
        frame.add(scrollPane);
        frame.add(button, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(100, 100);
        frame.pack();
         gtu.swing.util.JFrameUtil.setVisible(true,frame);
    }

    public static void main(String[] args) {
        Main fs = new Main();
    }
}
