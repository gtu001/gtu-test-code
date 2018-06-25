package gtu._work.ui;
import gtu._work.BTMovieMove;
import gtu.properties.PropertiesGroupUtils;
import gtu.properties.PropertiesUtil;
import gtu.swing.util.JCommonUtil;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.lang.StringUtils;
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
public class DownloadMoveUI extends javax.swing.JFrame {
	private JTabbedPane jTabbedPane1;
	private JPanel jPanel1;
	private JLabel jLabel4;
	private JScrollPane jScrollPane1;
	private JTextArea logArea;
	private JButton executeBtn;
	private JLabel jLabel1;
	private JTextField downloadDirText;
	private JLabel jLabel2;
	private JTextField unCompleteFileSubNameText;
	private JLabel jLabel3;
	private JTextField fileSizeText;
	private JTextField subFileNameText;
	private JCheckBox isDeleteBox;
	private JPanel jPanel2;
	
	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				DownloadMoveUI inst = new DownloadMoveUI();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public DownloadMoveUI() {
		super();
		initGUI();
	}
	
	private File configFile = new File(PropertiesUtil.getJarCurrentPath(getClass()), getClass().getSimpleName() + "_config.properties");//設定黨
//	private File configFile = new File("D:/gtu-test-code/GTU/target/classes/gtu/_work/ui/DownloadMoveUI_config.properties");
	PropertiesGroupUtils configUtil = new PropertiesGroupUtils(configFile);
	Map<String,String> configProp;
	
	private static final String SUBFILENAME = "subFileName";
	private static final String FILESIZE = "fileSize";
	private static final String UNCOMPLETEFILESUBNAME = "unCompleteFileSubName";
	private static final String DOWNLOADDIR = "downloadDir";
	private JButton newConfigBtn;
	
	private void initGUI() {
		try {
		    configProp = configUtil.loadConfig();
		    
			BorderLayout thisLayout = new BorderLayout();
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				jTabbedPane1 = new JTabbedPane();
				getContentPane().add(jTabbedPane1, BorderLayout.CENTER);
				{
					jPanel1 = new JPanel();
					jTabbedPane1.addTab("jPanel1", null, jPanel1, null);
					{
						isDeleteBox = new JCheckBox();
						jPanel1.add(isDeleteBox);
						isDeleteBox.setText("\u662f\u5426\u522a\u9664\u642c\u51fa\u7684\u76ee\u9304");
						isDeleteBox.setPreferredSize(new java.awt.Dimension(383,21));
					}
					{
						jLabel4 = new JLabel();
						jPanel1.add(jLabel4);
						jLabel4.setText("\u4fdd\u7559\u7684\u526f\u6a94\u540d");
					}
					{
						subFileNameText = new JTextField();
						jPanel1.add(subFileNameText);
						subFileNameText.setText(".avi,.wmv,.mp4,.srt,.sub,.mkv,.rar,.rmvb,.idx,.zip,.7z,.flv,.asf,.ass");
						subFileNameText.setPreferredSize(new java.awt.Dimension(304,24));
						
						if(configProp.containsKey(SUBFILENAME)){
						    subFileNameText.setText(configProp.get(SUBFILENAME));
                        }
					}
					{
						jLabel3 = new JLabel();
						jPanel1.add(jLabel3);
						jLabel3.setText("\u7b26\u5408\u7684\u6a94\u6848\u5927\u5c0f(KB)");
					}
					{
						fileSizeText = new JTextField();
						jPanel1.add(fileSizeText);
						fileSizeText.setText("5000");
						fileSizeText.setPreferredSize(new java.awt.Dimension(276,24));
						
						if(configProp.containsKey(FILESIZE)){
						    fileSizeText.setText(configProp.get(FILESIZE));
                        }
					}
					{
						jLabel2 = new JLabel();
						jPanel1.add(jLabel2);
						jLabel2.setText("\u672a\u5b8c\u6210\u526f\u6a94\u540d");
					}
					{
						unCompleteFileSubNameText = new JTextField();
						jPanel1.add(unCompleteFileSubNameText);
						unCompleteFileSubNameText.setText(".td");
						unCompleteFileSubNameText.setPreferredSize(new java.awt.Dimension(315,24));
						
						if(configProp.containsKey(UNCOMPLETEFILESUBNAME)){
						    unCompleteFileSubNameText.setText(configProp.get(UNCOMPLETEFILESUBNAME));
                        }
					}
					{
						jLabel1 = new JLabel();
						jPanel1.add(jLabel1);
						jLabel1.setText("\u4e0b\u8f09\u76ee\u9304");
					}
					{
						downloadDirText = new JTextField();
						JCommonUtil.jTextFieldSetFilePathMouseEvent(downloadDirText, true);
						jPanel1.add(downloadDirText);
						downloadDirText.setText("D:/TDDOWNLOAD");
						downloadDirText.setPreferredSize(new java.awt.Dimension(328,24));
						
						if(configProp.containsKey(DOWNLOADDIR)){
						    downloadDirText.setText(configProp.get(DOWNLOADDIR));
                        }
					}
					{
						executeBtn = new JButton();
						jPanel1.add(executeBtn);
						executeBtn.setText("\u57f7\u884c");
						{
						    newConfigBtn = new JButton("下一組");
						    newConfigBtn.addActionListener(new ActionListener() {
						        public void actionPerformed(ActionEvent arg0) {
						            configUtil.next();
						            configProp = configUtil.loadConfig();
						            if(configProp.containsKey(SUBFILENAME)){
			                            subFileNameText.setText(configProp.get(SUBFILENAME));
			                        }
			                        if(configProp.containsKey(FILESIZE)){
			                            fileSizeText.setText(configProp.get(FILESIZE));
			                        }
			                        if(configProp.containsKey(UNCOMPLETEFILESUBNAME)){
			                            unCompleteFileSubNameText.setText(configProp.get(UNCOMPLETEFILESUBNAME));
			                        }
			                        if(configProp.containsKey(DOWNLOADDIR)){
			                            downloadDirText.setText(configProp.get(DOWNLOADDIR));
			                        }
						        }
						    });
						    jPanel1.add(newConfigBtn);
						}
						executeBtn.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								executeBtnAction();
								saveConfig();
							}
						});
					}
				}
				{
					jPanel2 = new JPanel();
					BorderLayout jPanel2Layout = new BorderLayout();
					jPanel2.setLayout(jPanel2Layout);
					jTabbedPane1.addTab("jPanel2", null, jPanel2, null);
					{
						jScrollPane1 = new JScrollPane();
						jPanel2.add(jScrollPane1, BorderLayout.CENTER);
						jScrollPane1.setPreferredSize(new java.awt.Dimension(411, 262));
						{
							logArea = new JTextArea();
							jScrollPane1.setViewportView(logArea);
						}
					}
				}
			}
			pack();
			this.setSize(432, 329);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}
	
	private void saveConfig(){
	    configProp.put(SUBFILENAME, subFileNameText.getText());
        configProp.put(FILESIZE, fileSizeText.getText());
        configProp.put(UNCOMPLETEFILESUBNAME, unCompleteFileSubNameText.getText());
        configProp.put(DOWNLOADDIR, downloadDirText.getText());
        configUtil.saveConfig(configProp);
	}

	private void executeBtnAction() {
		try {
			logArea.setText("");
			Validate.notBlank(downloadDirText.getText(), "輸入下載目錄");
			Validate.notBlank(unCompleteFileSubNameText.getText(), "輸入未完成副檔名");
			Validate.notBlank(fileSizeText.getText(), "輸入檔案大小");
			Validate.isTrue(StringUtils.isNumeric(fileSizeText.getText()), "檔案大小必須為數直");
			Validate.notBlank(subFileNameText.getText(), "輸入要保留的副檔名格式");
			File downloadDir = new File(downloadDirText.getText());
			Validate.isTrue(downloadDir.exists() && downloadDir.isDirectory(), "下載目錄路徑不存在");

			BTMovieMove move = new BTMovieMove();
			String unCompleteFileSubName = unCompleteFileSubNameText.getText();
			if (!unCompleteFileSubName.startsWith(".")) {
				unCompleteFileSubName = "." + unCompleteFileSubName;
			}
			
			Set<String> set = new HashSet<String>();
			for(String subName : subFileNameText.getText().split(",")){
				if (!subName.startsWith(".")) {
					subName = "." + subName;
				}
				set.add(subName);
			}

			move.setBtUnCompleteFileEnd(unCompleteFileSubName);
			move.setBigFileSize(Integer.parseInt(fileSizeText.getText()) * 1000);// 5mb
			move.setMovieDir(downloadDir);
			move.setDeleteNonWorkingDir(isDeleteBox.isSelected());
			move.setMovieSubSet(set);
			System.out.println(move.getMovieSubSet());
			
			move.execute();
			
			List<String> notContainSubNameList = new ArrayList<String>();
			for(String subName : move.getCurrentSubFileNameSet()){
				if(!set.contains(subName)){
					notContainSubNameList.add(subName);
				}
			}
			
			move.getLogSb().append("有出現於未定義的檔案格式:"+notContainSubNameList);
			
			logArea.setText(move.getLogSb().toString());
		} catch (Exception ex) {
			JCommonUtil.handleException(ex);
		}
	}
}
