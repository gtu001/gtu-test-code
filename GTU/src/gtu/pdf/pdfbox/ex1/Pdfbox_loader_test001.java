package gtu.pdf.pdfbox.ex1;

import java.io.File;

import org.apache.commons.lang3.tuple.Pair;

import gtu.pdf.pdfbox.PdfboxUtil;

public class Pdfbox_loader_test001 {

    public static void main(String[] args) {
        Pdfbox_loader_test001 t = new Pdfbox_loader_test001();
        t.new MenuHolder();
        System.out.println("done...");
    }

    
    private class MenuHolder {
        
        private File file1 = new File("C:/Users/wistronits/Desktop/台達/ISA-95/ISA-95.00.02-2010.pdf");
        private File file2 = new File("C:/Users/wistronits/Desktop/台達/ISA-95/ISA-95.00.04-2012.pdf");

        private MenuHolder(){
            String content1 = PdfboxUtil.loadText(file1, Pair.of(3, 7));
            String content2 = PdfboxUtil.loadText(file2, Pair.of(5, 9));
            
            System.out.println(content1);
            System.out.println("--------------------------------------------");
            System.out.println(content2);
        }
    }
}
