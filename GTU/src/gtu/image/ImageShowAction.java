package gtu.image;

/**
 * <p>Copyright: Copyright (c) 2001</p>
 * Last modifier: Steven Song
 * Modified Content:
 *       1    show a new page for AckyerImage when request parameters including    "ActiveX"

 * @version: 1.0
 */

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.batik.ext.awt.image.codec.SeekableStream;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.JPEGEncodeParam;
import com.sun.media.jai.codec.TIFFDecodeParam;

public class ImageShowAction {

    /**
     *
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @throws Exception
     */
    private void showImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean ie8 = false;
        String sImgId = request.getParameter("ImageId");
        if (sImgId == null) {
            throw new Exception("ImageId needed!");
        }

        try {
            String header = request.getHeader("User-Agent");
            if (header.indexOf("Trident/4.0") > 0) {
                ie8 = true;
            }
            if (ie8) {
                File tiffFile = fileService.getFile(Long.parseLong(sImgId), FileCst.MAPPING_TYPE__IMAGE);

                ServletOutputStream outs = response.getOutputStream();
                response.setContentType("image/jpeg");
                response.setHeader("Cache-Control", "public");
                response.setHeader("Pragma", "public");

                String output2 = tiffFile.getAbsolutePath().replaceAll(".tiff", ".jpg");
                File jpgFile = new File(output2);
                if (!jpgFile.exists()) {
                    OutputStream os2 = new FileOutputStream(output2);
                    RenderedOp src2 = JAI.create("fileload", tiffFile.getAbsolutePath());
                    JPEGEncodeParam param2 = new JPEGEncodeParam();
                    ImageEncoder enc2 = ImageCodec.createImageEncoder("JPEG", os2, param2);
                    enc2.encode(src2);
                    os2.close();
                }
                try {

                    BufferedInputStream in = new BufferedInputStream(new FileInputStream(jpgFile));
                    ByteArrayOutputStream out = new ByteArrayOutputStream(1024 * 1024);

                    byte[] temp = new byte[1024];
                    int size = 0;
                    while ((size = in.read(temp, 0, 1024)) != -1) {
                        out.write(temp, 0, size);
                    }
                    in.close();

                    outs.write(out.toByteArray());

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            } else {

                byte[] image = fileService.getDocument(Long.parseLong(sImgId), FileCst.MAPPING_TYPE__IMAGE);

                response.setContentType("image/tiff");
                response.setHeader("Cache-Control", "public");
                response.setHeader("Pragma", "public");
                ServletOutputStream out = response.getOutputStream();
                out.write(image);

            }

        } catch (Exception e) {
            e.printStackTrace();
            showFileNotExist(request, response);
        }
    }

    private void showDivaDown(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sFileName = request.getSession().getServletContext().getRealPath("/") + "/ls/image/diva_down_.tiff";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(sFileName);
            File file = new File(sFileName);
            byte[] buff = new byte[(int) file.length()];
            fis.read(buff, 0, buff.length);
            response.setContentType("image/tif");
            response.setHeader("Cache-Control", "public");
            response.setHeader("Pragma", "public");
            ServletOutputStream out = response.getOutputStream();
            out.write(buff);
        } finally {
            if (fis != null)
                fis.close();
        }
    }

    private void showFileNotExist(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sFileName = request.getSession().getServletContext().getRealPath("/") + "/ls/image/no_file_.tiff";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(sFileName);
            File file = new File(sFileName);
            byte[] buff = new byte[(int) file.length()];
            fis.read(buff, 0, buff.length);
            response.setContentType("image/tif");
            response.setHeader("Cache-Control", "public");
            response.setHeader("Pragma", "public");
            ServletOutputStream out = response.getOutputStream();
            out.write(buff);
        } finally {
            if (fis != null)
                fis.close();
        }
    }
}
