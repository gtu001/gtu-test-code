package gtu.io;

import java.io.IOException;
import java.io.PushbackInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import sun.nio.cs.StreamDecoder;

/**
 *  去除UTF-8 BOM表頭的InputStreamReader
如果你曾經使用Java遇過讀一些文字檔前面出現??等亂碼的話, 
表示該份文件有加入bom(byte order mark), 
基本上bom的用途原本是用來讓程式辨別該份文檔的編碼格式, 
Microsoft在Windows 2000以後的Notepad存UTF-8的檔案會加上 BOM(Byte Order Mark, U+FEFF), 
主要是因為UTF-8和ASCII是相容的, 為了避免使用者自己忘記用甚麼存, 
造成UTF-8檔案用 ASCII 模式開看到是亂碼, 所以在檔案最前面加上BOM.
看到這裡可能有很多人會開始%#~!微軟...
但是碰到問題總是得解決的, 下面這個改寫InputStreamReader的UnicodeInputStreamReader
就能夠讓你免去上述煩惱, 所有功能仍然和InputStreamReader相同, 
只是在constructor中加入判斷bom表頭的機制, 使用PushbackInputStream當讀取到相關格式時能夠移動檔案開始位置.
使用方式: BufferedReader in = new BufferedReader(new UnicodeInputStreamReader(new FileInputStream(file), encode));
 */
public class UnicodeInputStreamReader extends InputStreamReader {
	private static final int BOM_SIZE = 4;
	private final StreamDecoder decoder;
	private PushbackInputStream pushBack;
	private String encode;
	private String defaultEnc;

	public UnicodeInputStreamReader(InputStream input, String defaultEnc)
			throws UnsupportedEncodingException {
		super(input);
		try {
			this.defaultEnc = defaultEnc;
			pushBack = new PushbackInputStream(input, BOM_SIZE);
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		decoder = StreamDecoder.forInputStreamReader(pushBack, this, encode);
	}

	private void init() throws IOException {
		byte[] bom = new byte[BOM_SIZE];
		int n, unread;

		// 初始讀取一次
		n = pushBack.read(bom, 0, bom.length);

		// 比對表頭
		if ((bom[0] == (byte) 0x00) && (bom[1] == (byte) 0x00)
				&& (bom[2] == (byte) 0xFE) && (bom[3] == (byte) 0xFF)) {
			encode = "UTF-32BE";
			unread = n - 4;
		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE)
				&& (bom[2] == (byte) 0x00) && (bom[3] == (byte) 0x00)) {
			encode = "UTF-32LE";
			unread = n - 4;
		} else if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB)
				&& (bom[2] == (byte) 0xBF)) {
			encode = "UTF-8";
			unread = n - 3;
		} else if ((bom[0] == (byte) 0xFE) && (bom[1] == (byte) 0xFF)) {
			encode = "UTF-16BE";
			unread = n - 2;
		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE)) {
			encode = "UTF-16LE";
			unread = n - 2;
		} else {
			// 如果沒有找到任何表頭, 則退回長度等於原先總長
			encode = defaultEnc;
			unread = n;
		}
		System.out.println("has BOM=" + ((unread == n) ? false : true)
				+ ", encode=" + encode + ", read=" + n + ", unread=" + unread);
		// 計算應該退回多少byte
		if (unread > 0)
			pushBack.unread(bom, (n - unread), unread);
	}

	public String getEncoding() {
		return encode;
	}

	public int read() throws IOException {
		return decoder.read();
	}

	public int read(char cbuf[], int offset, int length) throws IOException {
		return decoder.read(cbuf, offset, length);
	}

	public boolean ready() throws IOException {
		return decoder.ready();
	}

	public void close() throws IOException {
		decoder.close();
	}
}