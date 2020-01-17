package gtu.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import gtu.file.FileUtil;

public class RemoveBOMTest {
	
	public static void main(String[] args) throws IOException {
		String UTF8_BOM = "\uFEFF";//BOM
//		String UTF8_BOM = "\ufeff";//TEST
		File file = new File("D:\\work_tool\\20200114_sister_workspace\\cashweb\\src\\conf\\com\\wistron\\cashweb\\ibatis\\sql-map-config.xml");
		String content = FileUtils.readFileToString(file, "utf-8");
		content = content.trim();
		if (content.startsWith(UTF8_BOM)){
			content = content.replace(UTF8_BOM, "");
		}
		FileUtil.saveToFile(file, content, "UTF-8");
		System.out.println("done...");
	}
}
