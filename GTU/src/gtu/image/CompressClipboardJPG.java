package gtu.image;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import gtu.file.FileUtil;
import javassist.Modifier;

public class CompressClipboardJPG {

    private static float QUALITY = 0.5f;

    public static void main(String[] args) {
        CompressClipboardJPG t = new CompressClipboardJPG();

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        List<DataFlavor> flavorLst = new ArrayList<DataFlavor>();
        for (Field f : DataFlavor.class.getDeclaredFields()) {
            if (f.getType() == DataFlavor.class && Modifier.isStatic(f.getModifiers())) {
                try {
                    DataFlavor flavor = (DataFlavor) FieldUtils.readField(f, DataFlavor.class, true);
                    flavorLst.add(flavor);
                    System.out.println("flavor : " + f.getName() + "\t" + flavor);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        if (clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
            try {
                BufferedImage buffImage = (BufferedImage) clipboard.getData(DataFlavor.imageFlavor);

                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(buffImage, "jpg", os);
                InputStream is = new ByteArrayInputStream(os.toByteArray());

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                compressImage(is, baos);

                Image image = ImageIO.read(new ByteArrayInputStream(baos.toByteArray()));

                IOUtils.write(baos.toByteArray(), new FileOutputStream(new File(FileUtil.DESKTOP_PATH, "test001.jpg")));

                TransferableImage trans = t.new TransferableImage(image);

                clipboard.setContents(trans, new ClipboardOwner() {
                    @Override
                    public void lostOwnership(Clipboard clipboard, Transferable contents) {
                        System.out.println("Lost Clipboard Ownership");
                    }
                });
            }
            // getData throws this.
            catch (UnsupportedFlavorException ufe) {
                ufe.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private class TransferableImage implements Transferable {

        Image i;

        public TransferableImage(Image i) {
            this.i = i;
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (flavor.equals(DataFlavor.imageFlavor) && i != null) {
                return i;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }

        public DataFlavor[] getTransferDataFlavors() {
            DataFlavor[] flavors = new DataFlavor[1];
            flavors[0] = DataFlavor.imageFlavor;
            return flavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            DataFlavor[] flavors = getTransferDataFlavors();
            for (int i = 0; i < flavors.length; i++) {
                if (flavor.equals(flavors[i])) {
                    return true;
                }
            }
            return false;
        }
    }

    private static void compressImage(InputStream is, OutputStream os) throws IOException {
        // create a BufferedImage as the result of decoding the supplied
        // InputStream
        BufferedImage image = ImageIO.read(is);

        // get all image writers for JPG format
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");

        if (!writers.hasNext())
            throw new IllegalStateException("No writers found");

        ImageWriter writer = (ImageWriter) writers.next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(os);
        writer.setOutput(ios);

        ImageWriteParam param = writer.getDefaultWriteParam();

        // compress to a given quality
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(QUALITY);

        // appends a complete image stream containing a single image and
        // associated stream and image metadata and thumbnails to the output
        writer.write(null, new IIOImage(image, null, null), param);

        // close all streams
        is.close();
        os.close();
        ios.close();
        writer.dispose();
    }

}