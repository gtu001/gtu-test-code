package gtu.binary;

import java.io.FileOutputStream;

public class CmdEncodingDisplay {
    private static final String BOM = "\ufeff";//
    private static final String TEST_STRING//
            = "ASCII     abcde xyz\n"//
                    + "German    äöü ÄÖÜ ß\n"//
                    + "Polish    ąęźżńł\n"//
                    + "Russian   абвгдеж эюя\n"//
                    + "CJK       你好\n";//

    public static void main(String[] args) throws Exception {
        String[] encodings = new String[] { "UTF-8", "UTF-16LE", "UTF-16BE", "UTF-32LE", "UTF-32BE" };

        for (String encoding : encodings) {
            System.out.println("== " + encoding);

            for (boolean writeBom : new Boolean[] { false, true }) {
                System.out.println(writeBom ? "= bom" : "= no bom");

                String output = (writeBom ? BOM : "") + TEST_STRING;
                byte[] bytes = output.getBytes(encoding);
                System.out.write(bytes);
                FileOutputStream out = new FileOutputStream("uc-test-" + encoding + (writeBom ? "-bom.txt" : "-nobom.txt"));
                out.write(bytes);
                out.close();
            }
        }
    }
}
