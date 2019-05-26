package gtu.mobi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.rr.mobi4java.MobiDocument;
import org.rr.mobi4java.MobiReader;

import gtu.file.FileUtil;

public class MobiReaderTest001 {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        // https://github.com/rrauschenbach/mobi-api4java

        MobiDocument mobiDoc = new MobiReader().read(new File("/media/gtu001/OLD_D/gtu001_dropbox/Dropbox/guava/電子書/Anthony Doerr-All the Light we Cannot See-Simon & Schuster Export (2014).mobi"));
        String text = mobiDoc.getTextContent();
        FileUtil.saveToFile(new File(FileUtil.DESKTOP_DIR, "xxxx.htm"), text, "UTF8");
        System.out.println("done...");
    }

    public static class MobiBookHandler {
        String allContent;
        Map<String, byte[]> imgMap = new HashMap<String, byte[]>();
        List<String> pages = new ArrayList<String>();
        int pageIndex = -1;

        private void initImage(MobiDocument mobiDoc) {
            Pattern ptn = Pattern.compile("recindex\\=\"(\\d+)\"", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher mth = ptn.matcher(allContent);
            while (mth.find()) {
                String img = mth.group(1);
                byte[] bytes = mobiDoc.getImages().get(Integer.parseInt(img) - 1);
                imgMap.put(img, bytes);
            }
        }

        private void initPages() {
            Pattern ptn = Pattern.compile("\\<\\/?mbp\\:pagebreak\\/?\\>", Pattern.DOTALL | Pattern.MULTILINE);
            Matcher mth = ptn.matcher(allContent);
            String tmpPage = "";
            int startPos = 0;
            while (mth.find()) {
                tmpPage = StringUtils.substring(allContent, startPos, mth.start());
                startPos = mth.end();
                if (StringUtils.isNotBlank(tmpPage)) {
                    pages.add(tmpPage);
                }
            }
        }

        public MobiBookHandler(MobiDocument mobiDoc) throws IOException {
            this.allContent = mobiDoc.getTextContent();
            this.initImage(mobiDoc);
            this.initPages();
        }

        public void next() {
            if (pageIndex >= pages.size() - 1) {
                pageIndex = pages.size() - 1;
            } else {
                pageIndex++;
            }
        }

        public void previous() {
            if (pageIndex <= 0) {
                pageIndex = 0;
            } else {
                pageIndex--;
            }
        }

        public String getPage() {
            return pages.get(pageIndex);
        }
    }
}
