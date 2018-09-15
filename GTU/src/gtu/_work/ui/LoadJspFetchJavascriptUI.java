package gtu._work.ui;
import gtu.date.DateUtil;
import gtu.file.FileUtil;
import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;


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
public class LoadJspFetchJavascriptUI extends javax.swing.JFrame {
	private static final long serialVersionUID = 1L;
	private JTabbedPane jTabbedPane1;
	private JPanel jPanel1;
	private JTextField filePathText;
	private JLabel jLabel3;
	private JTextField tagPatternText;
	private JTextField subNameText;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JButton executeBtn;

	/**
	* Auto-generated main method to display this JFrame
	 * @throws IOException 
	*/
	public static void main(String[] args) throws IOException {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LoadJspFetchJavascriptUI inst = new LoadJspFetchJavascriptUI();
				inst.setLocationRelativeTo(null);
 				gtu.swing.util.JFrameUtil.setVisible(true,inst);
			}
		});
	}
	
	public LoadJspFetchJavascriptUI() {
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
				jTabbedPane1.setPreferredSize(new java.awt.Dimension(436, 153));
				{
					jPanel1 = new JPanel();
					jTabbedPane1.addTab("jPanel1", null, jPanel1, null);
					jPanel1.setPreferredSize(new java.awt.Dimension(431, 125));
					{
						jLabel1 = new JLabel();
						jPanel1.add(jLabel1);
						jLabel1.setText("\u6a94\u6848\u8def\u5f91");
					}
					{
						filePathText = new JTextField();
						JCommonUtil.jTextFieldSetFilePathMouseEvent(filePathText, true);
						jPanel1.add(filePathText);
						filePathText.setPreferredSize(new java.awt.Dimension(354, 24));
					}
					{
						jLabel2 = new JLabel();
						jPanel1.add(jLabel2);
						jLabel2.setText("\u7db2\u9801\u526f\u6a94\u540d");
						jLabel2.setPreferredSize(new java.awt.Dimension(81, 17));
					}
					{
						subNameText = new JTextField();
						jPanel1.add(subNameText);
						subNameText.setPreferredSize(new java.awt.Dimension(282, 24));
					}
					{
						jLabel3 = new JLabel();
						jPanel1.add(jLabel3);
						jLabel3.setText("\u8981\u6293\u7684TagPattern");
						jLabel3.setPreferredSize(new java.awt.Dimension(115, 17));
					}
					{
						tagPatternText = new JTextField();
						jPanel1.add(tagPatternText);
						tagPatternText.setPreferredSize(new java.awt.Dimension(259, 24));
					}
					{
						executeBtn = new JButton();
						jPanel1.add(executeBtn);
						executeBtn.setText("執行");
						executeBtn.setPreferredSize(new java.awt.Dimension(180, 45));
						executeBtn.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								executeBtnActionPerformed(evt);
							}
						});
					}
				}
			}
			pack();
			this.setSize(452, 218);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	private Pattern javascriptStart = Pattern.compile("\\<z?script", Pattern.CASE_INSENSITIVE);
	private Pattern javascriptEnd = Pattern.compile("\\<\\/z?script\\>", Pattern.CASE_INSENSITIVE);
	
	private void executeBtnActionPerformed(ActionEvent evt) {
		try{
			String subName = Validate.notBlank(subNameText.getText());
			if(StringUtils.isNotBlank(tagPatternText.getText())){
				String startP = "\\<" + tagPatternText.getText();
				String endP = "\\<\\/"+ tagPatternText.getText() +"\\>";
				javascriptStart = Pattern.compile(startP, Pattern.CASE_INSENSITIVE);
				javascriptEnd = Pattern.compile(endP, Pattern.CASE_INSENSITIVE);
				JCommonUtil._jOptionPane_showMessageDialog_info(String.format("自訂tagPattern:\nstart:\n%send:\n%s", startP, endP));
			}
			
			long startTime = System.currentTimeMillis();
			
			File file = JCommonUtil.filePathCheck(filePathText.getText(), "設定網頁目錄", false);
			List<File> fileList = new ArrayList<File>();
			if(file.isFile()){
				fileList.add(file);
			}else{
				if(subName.startsWith(".")){
					subName = subName.substring(1);
				}
				FileUtil.searchFilefind(file, ".*\\." + subName + "$", fileList);
			}
			File outputFile = new File(FileUtil.DESKTOP_PATH, "javascript_" + DateUtil.getCurrentDateTime(false) + ".txt");
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "utf8"));
			for(File f : fileList){
				String content = writeScript(f);
				if(content.length() > 0){
					writer.write("#start#" + f.getAbsolutePath() + "#==============================================");
					writer.newLine();
					writer.write(content);
					writer.write("#end  #" + f.getAbsolutePath() + "#==============================================");
					writer.newLine();
				}
			}
			writer.flush();
			writer.close();
			
			long duringTime = System.currentTimeMillis() - startTime;
			
			JCommonUtil._jOptionPane_showMessageDialog_info("全部完成,檔案數:"+fileList.size() + "\n耗時:"+duringTime);
		}catch(Exception ex){
			JCommonUtil.handleException(ex);
		}
	}
	
	private String writeScript(File file) throws IOException{
		StringBuffer sb = new StringBuffer(FileUtils.readFileToString(file, "utf8"));
		StringBuffer sb2 = new StringBuffer();
		for(;;){
			Matcher m1 = javascriptStart.matcher(sb.toString());
			Matcher m2 = javascriptEnd.matcher(sb.toString());
			if(m1.find() && m2.find()){
				sb2.append(sb.substring(m1.start(), m2.end())+"\n\n");
				sb.delete(m1.start(), m2.end());
			}else{
				break;
			}
		}
		return sb2.toString();
	}
}
