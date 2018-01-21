package gtu.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class TTTTTT {
	public void test() {
		try {
			BufferedReader bufReader = new BufferedReader(
					new InputStreamReader(System.in));
			BufferedWriter bufWriter = new BufferedWriter(new FileWriter(
					"fileName"));

			String keyin = null;
			while ((keyin = bufReader.readLine()) != null) {
				bufWriter.write(keyin);
				bufWriter.newLine();
			}

			bufReader.close();
			bufWriter.close();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void test2() {
		FileReader reader = null;
		FileWriter writer = null;
		BufferedReader br = null;
		BufferedWriter bw = null;
		String data;

		try {
			// 建立物件，並且裝上Buffer
			reader = new FileReader("A.txt");
			writer = new FileWriter("B.txt", true);
			br = new BufferedReader(reader);
			bw = new BufferedWriter(writer);

			// 使用while迴圈逐行讀取資料
			// 再利用write將data儲存資料
			while ((data = br.readLine()) != null) {
				bw.write(data);
				// 每次會讀取一行，所以幫它換行
				bw.newLine();
			}

		} catch (FileNotFoundException e) {
			System.out.print("檔案找不到");

		} catch (IOException e) {

		} finally {
			try {
				// 記得兩個都要關閉
				br.close();
				bw.close();
			} catch (IOException e) {

			}

		}
	}

	public void test3() {
		try {
			File file = new File("\\\\168.0.3.18/d$/a.svg");
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);
			// bw.write(singleLine.toString());
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void test4() throws IOException {
		// 创建一个字符写入流对象。
		FileWriter fw = new FileWriter("F:\\02.txt");

		// 为了提高字符写入流效率，加入了缓冲技术。
		// 只要将需要被提高效率的流对象作为参数传递给缓冲区的构造函数即可。
		BufferedWriter bufw = new BufferedWriter(fw);
		for (int x = 1; x < 5; x++) {
			bufw.write("abcdes" + x);
			bufw.newLine();// 写入一个行分隔符

			bufw.flush();// 记住，只要用到缓冲区，就要记得刷新。
		}

		// 其实关闭缓冲区，就是在关闭缓冲中的流对象。
		bufw.close();

	}

	public void test5() {
		File f = null;
		FileOutputStream out = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		String charSet = null;
		try {
			f = new File("OutFile.xml");
			out = new FileOutputStream(f);
			osw = new OutputStreamWriter(out, charSet);
			bw = new BufferedWriter(osw);

			bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			bw.write("<root>\n");
			bw.write("\t<a>\n");
			bw.write("\t\t<b>中文</b>\n");
			bw.write("\t\t<b>55555</b>\n");
			bw.write("\t</a>\n");
			bw.write("</root>\n");

			bw.close();
			osw.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			bw = null;
			osw = null;
			out = null;
			f = null;
		}

	}

	public static void main(String[] args) {
		File file1 = new File("f:\\01.txt");// 指定源文件
		File file2 = new File("f:\\02.txt");// 指定目的文件

		FileOutputStream fos = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file1);// 把file1写入文件输入流
			fos = new FileOutputStream(file2);// 把文件输出流写入到file2文件中

			// byte[] buf = new byte[1024];//定义一个字节缓冲区（就是一个字节数组）

			// 创建一个字节数组，数组长度与file中获得的字节数相等，其中avaiable()方法返回可读的字节数
			byte[] buf = new byte[fis.available()];// 创建字节数组时也可用上面那种方法。

			int len = 0;
			while ((len = fis.read(buf)) != -1) {// 按一个个字节写入到字节缓冲区中
				fos.write(buf, 0, len);// 将字节缓冲区中的从0开始的len长度的字节写入到字节输出流中
			}
		} catch (IOException e) {
			// e.printStackTrace();
			throw new RuntimeException("复制文件失败");
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				// e.printStackTrace();
				throw new RuntimeException("读取关闭失败");
			}
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("写入关闭失败");
			}
		}
	}
}
