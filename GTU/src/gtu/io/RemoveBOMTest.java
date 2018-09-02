package gtu.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class RemoveBOMTest {
	
	public static void main(String[] args) throws IOException {
		String UTF8_BOM = "\uFEFF";
		File file = new File("c:/someFile.txt");
		String content = FileUtils.readFileToString(file, "utf-8");
		content = content.trim();
		if (content.startsWith(UTF8_BOM)){
			content = content.replace(UTF8_BOM, "");
		}
	}
}
