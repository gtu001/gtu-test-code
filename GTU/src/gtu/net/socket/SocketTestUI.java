package gtu.net.socket;
import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


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
public class SocketTestUI extends javax.swing.JFrame {
	private JTabbedPane jTabbedPane1;
	private JPanel jPanel1;
	private JRadioButton clientRadio;
	private JPanel jPanel2;
	private JTextField strText;
	private JTextArea logArea;
	private JButton connectionBtn;
	private JRadioButton serverRadio;
	private JTextArea serverLogArea;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane1;
	private JTextField portText;
	private JTextField ipText;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JPanel jPanel3;
	
	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SocketTestUI inst = new SocketTestUI();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public SocketTestUI() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			BorderLayout thisLayout = new BorderLayout();
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(thisLayout);
			{
				jTabbedPane1 = new JTabbedPane();
				getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
				{
					jPanel1 = new JPanel();
					BorderLayout jPanel1Layout = new BorderLayout();
					jPanel1.setLayout(jPanel1Layout);
					jTabbedPane1.addTab("測試", null, jPanel1, null);
					{
						jPanel3 = new JPanel();
						jPanel1.add(jPanel3, BorderLayout.NORTH);
						jPanel3.setPreferredSize(new java.awt.Dimension(515, 39));
						{
							jLabel1 = new JLabel();
							jPanel3.add(jLabel1);
							jLabel1.setText("IP");
						}
						{
							ipText = new JTextField();
							ipText.setText("127.0.0.1");
							jPanel3.add(ipText);
							ipText.setPreferredSize(new java.awt.Dimension(163, 24));
						}
						{
							jLabel2 = new JLabel();
							jPanel3.add(jLabel2);
							jLabel2.setText("Port");
						}
						{
							portText = new JTextField();
							portText.setText("8189");
							jPanel3.add(portText);
							portText.setPreferredSize(new java.awt.Dimension(80, 24));
						}
						{
							serverRadio = new JRadioButton();
							jPanel3.add(serverRadio);
							serverRadio.setText("server");
						}
						{
							clientRadio = new JRadioButton();
							jPanel3.add(clientRadio);
							clientRadio.setText("client");
							JCommonUtil.createRadioButtonGroup(serverRadio,clientRadio);
						}
						{
							connectionBtn = new JButton();
							jPanel3.add(connectionBtn);
							connectionBtn.setText("\u9023\u7dda");
							connectionBtn.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									connectionBtnAction();
								}
							});
						}
					}
					{
						jScrollPane2 = new JScrollPane();
						jPanel1.add(jScrollPane2, BorderLayout.CENTER);
						jScrollPane2.setPreferredSize(new java.awt.Dimension(515, 244));
						{
							logArea = new JTextArea();
							jScrollPane2.setViewportView(logArea);
						}
					}
					{
						strText = new JTextField();
						jPanel1.add(strText, BorderLayout.SOUTH);
						strText.addKeyListener(new KeyAdapter() {
							public void keyPressed(KeyEvent evt) {
								strTextkeyPressed(evt);
							}
						});
					}
				}
				{
					jPanel2 = new JPanel();
					BorderLayout jPanel2Layout = new BorderLayout();
					jPanel2.setLayout(jPanel2Layout);
					jTabbedPane1.addTab("伺服器Log", null, jPanel2, null);
					{
						jScrollPane1 = new JScrollPane();
						jPanel2.add(jScrollPane1, BorderLayout.CENTER);
						jScrollPane1.setPreferredSize(new java.awt.Dimension(515, 307));
						{
							serverLogArea = new JTextArea();
							jScrollPane1.setViewportView(serverLogArea);
						}
					}
				}
			}
			pack();
			this.setSize(536, 374);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	PrintWriter serverOut;//server feeback
	PrintStream serverLogOut = new PrintStream(new OutputStream() {
		@Override
		public void write(int arg0) throws IOException {
			serverLogArea.append(Character.toString((char)arg0));
		}
	});// client to server
	private void strTextkeyPressed(KeyEvent evt) {
		if(evt.getKeyCode() == 10){
			String value = strText.getText();
			serverOut.println(value);
			strText.setText("");
		}
	}
	private void connectionBtnAction(){
		final int port = Integer.parseInt(portText.getText());
		final String ip = ipText.getText();
		if(serverRadio.isSelected()){
			new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
				@Override
				public void run() {
					SocketServer server = new SocketServer();
					server.execute(port, serverLogOut);
				}
			}, "xxxxxxxxxxxxxxxxx1").start();
		}
		if(clientRadio.isSelected()){
			new Thread(Thread.currentThread().getThreadGroup(), new Runnable() {
				@Override
				public void run() {
					try {
						Socket s = new Socket(ip, port);
						BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
						serverOut = new PrintWriter(s.getOutputStream(), true);
						boolean more = true;
						while (more) {
							String inStr = in.readLine();
							if (inStr == null) {
								more = false;
							} else {
								logArea.append("" + inStr + "\n");
							}
						}
					} catch (IOException e) {
						logArea.append("Exception in line 52 : " + e);
					}
				}
			}, "xxxxxxxxxxxxxxxxx2").start();
		}
	}
}
