package gtu.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import gtu.file.FileUtil;

public class DataUriUtil {

    private static final DataUriUtil _INST = new DataUriUtil();

    private DataUriUtil() {
    }

    public static void main(String[] args) throws IOException {
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

    public boolean savePicFromDataUri(String uri, File picFile) {
        try {
            Pattern ptn = Pattern.compile("data\\:image\\/(.*?)\\;base64\\,(.*)");
            Matcher mth = ptn.matcher(uri);
            if (mth.find()) {
                String fileType = mth.group(1);
                String base64Uri = mth.group(2);
                // byte[] decodedString =
                // org.apache.commons.codec.binary.Base64.decodeBase64(new
                // String(base64Uri).getBytes("UTF-8"));

                byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Uri);
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));

                // write the image to a file
                ImageIO.write(img, "svg", picFile);
                return true;
            }
            return false;
        } catch (Exception ex) {
            throw new RuntimeException(e);
        }
    }
}
