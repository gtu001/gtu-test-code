package gtu.pdf.pdfbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.apache.pdfbox.text.PDFTextStripper;

public class PDFTextStripperUtil {

    /**
     * @param fileName
     * @return complete file data as string
     * @throws NullPointerException
     *             if fileName is null
     */
    public static Optional<String> getDataAsString(final String fileName) {
        if (Objects.isNull(fileName)) {
            throw new NullPointerException("fileName shouldn't be null");
        }

        try (final PDDocument pdDoc = PDDocument.load(new File(fileName))) {

            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setLineSeparator("\n");
            stripper.setAddMoreFormatting(true);

            return Optional.of(stripper.getText(pdDoc));

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }

    }

    public static Optional<String> getDataAsString(final String fileName, final int startPage, final int endPage) {
        if (Objects.isNull(fileName)) {
            throw new NullPointerException("fileName shouldn't be null");
        }

        if (startPage < 1 || endPage < 1 || endPage < startPage) {
            throw new IllegalArgumentException("startPage, endPage must >= 1 and  endPage >= startPage");
        }

        try (final PDDocument pdDoc = PDDocument.load(new File(fileName))) {

            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setLineSeparator("\n");
            stripper.setAddMoreFormatting(true);
            stripper.setStartPage(startPage);
            stripper.setEndPage(endPage);

            return Optional.of(stripper.getText(pdDoc));

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public static Optional<String> getDataAsStringFromStartPage(String fileName, int startPage) {
        if (Objects.isNull(fileName)) {
            throw new NullPointerException("fileName shouldn't be null");
        }

        if (startPage < 1) {
            throw new IllegalArgumentException("startPage must >= 1");
        }

        try (final PDDocument pdDoc = PDDocument.load(new File(fileName))) {
            int noOfPages = pdDoc.getNumberOfPages();
            return getDataAsString(fileName, startPage, noOfPages);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public static Optional<String> getDataAsStringTillEndPage(String fileName, int endPage) {
        if (Objects.isNull(fileName)) {
            throw new NullPointerException("fileName shouldn't be null");
        }

        if (endPage < 1) {
            throw new IllegalArgumentException("endPage must >= 1");
        }

        return getDataAsString(fileName, 1, endPage);
    }

    public static Optional<Integer> getNumberOfPages(String fileName) {
        if (Objects.isNull(fileName)) {
            throw new NullPointerException("fileName shouldn't be null");
        }
        try (final PDDocument pdDoc = PDDocument.load(new File(fileName))) {
            return Optional.of(pdDoc.getNumberOfPages());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public static Optional<Map<String, Object>> getDocumentBasicMetaData(final String fileName) {
        if (Objects.isNull(fileName)) {
            throw new NullPointerException("fileName shouldn't be null");
        }

        try (final PDDocument pdDoc = PDDocument.load(new File(fileName))) {
            PDDocumentInformation docInfo = pdDoc.getDocumentInformation();
            Set<String> keys = docInfo.getMetadataKeys();

            Map<String, Object> map = new HashMap<>();

            for (String key : keys) {
                map.put(key, docInfo.getPropertyStringValue(key));
            }

            return Optional.of(map);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    // PDF documents can have XML metadata associated with them. Following
    // classes are used to extract the XML meta data.
    //
    // PDDocumentCatalog
    // PDPage
    // PDXObject
    // PDICCBased
    // PDStream
    // Following snippet is used to get catalog metadata from PDDocumentCatalog.
    public static Optional<List<String>> getCatalogMetaData(final String fileName) {
        if (Objects.isNull(fileName)) {
            throw new NullPointerException("fileName shouldn't be null");
        }

        try (final PDDocument pdDoc = PDDocument.load(new File(fileName))) {
            PDDocumentCatalog catalog = pdDoc.getDocumentCatalog();
            PDMetadata metadata = catalog.getMetadata();
            return getMeatData(metadata);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }

    }

    private static Optional<List<String>> getDataFromStream(InputStream in) {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            List<String> data = new ArrayList<>();
            String str;

            while ((str = br.readLine()) != null) {
                data.add(str);
            }
            return Optional.of(data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }

    }

    private static Optional<List<String>> getMeatData(PDMetadata metadata) {
        if (metadata == null) {
            System.out.println("There is no meta data associated");
            return Optional.empty();
        }

        try (InputStream in = metadata.createInputStream()) {
            return getDataFromStream(in);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    // Following snippet is used to get meta data of a PDF page.
    public static Optional<List<String>> getPDPageMetaData(final String fileName, int pageIndex) {
        if (Objects.isNull(fileName)) {
            throw new NullPointerException("fileName shouldn't be null");
        }

        if (pageIndex < 1) {
            throw new IllegalArgumentException("pageIndex must >= 1");
        }

        try (final PDDocument pdDoc = PDDocument.load(new File(fileName))) {

            if (pageIndex > pdDoc.getNumberOfPages()) {
                throw new IllegalArgumentException("pageIndex : " + pageIndex + " must <= " + pdDoc.getNumberOfPages());
            }

            PDPage pdPage = pdDoc.getPage(pageIndex);
            PDMetadata metadata = pdPage.getMetadata();
            return getMeatData(metadata);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }
}