package gtu.image;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import com.google.common.io.Files;

import gtu.file.FileUtil;

public class DataUriUtil {

    private static final DataUriUtil _INST = new DataUriUtil();

    private DataUriUtil() {
    }

    public static void main(String[] args) throws IOException {
        File dir = new File("C:\\Users\\gtu00\\OneDrive\\Desktop\\fwd");
        File destFile = new File(FileUtil.DESKTOP_PATH, "test.html");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile), "utf8"));
        for (File f : dir.listFiles()) {
            String ext = Files.getFileExtension(f.getName()).toLowerCase();
            String datauri = DataUriUtil.getInstance().generateUri(f.getAbsolutePath(), ext);
            writer.write(String.format("%s<img src=\"%s\" />", f.getName(), datauri));
            writer.newLine();
        }
        writer.flush();
        writer.close();
        System.out.println("done...");
    }

    public static DataUriUtil getInstance() {
        return _INST;
    }

    public String generateUri(String filepath, String fileType) {
        try {
            // Toolkit tk = Toolkit.getDefaultToolkit();
            // Image image = tk.getImage(filepath);
            BufferedImage bimage = ImageIO.read(new File(filepath));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(bimage, fileType, baos);

            String imageString = "data:image/" + fileType + ";base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());
            return imageString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public String generateUri(String fileExtension, byte[] byteArry) {
        return "data:image/" + fileExtension + ";base64," + DatatypeConverter.printBase64Binary(byteArry);
    }
}
