package gtu.jaxb;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

public class GenerateJavaToXsd {
    
    static class Foo {
        String value;
    }
    static class Bar {
        Foo foo;
        String value;
    }

    public static void main(String[] args) throws JAXBException, IOException{
        final File baseDir = new File("C:/Users/gtu001/Desktop/");
        //需要把目錄xxxx123建立並在底下建立檔案schema1.xsd,內容為空白就好

        class MySchemaOutputResolver extends SchemaOutputResolver {
            public Result createOutput( String namespaceUri, String suggestedFileName ) throws IOException {
                System.out.println("namespaceUri = " + namespaceUri);
                System.out.println("suggestedFileName = " + suggestedFileName);
                
                File xlsFile = new File(baseDir,suggestedFileName);
                if(!xlsFile.exists()){
                    System.out.println(xlsFile);
                    xlsFile.createNewFile();
                }
                
                return new StreamResult(xlsFile);
            }
        }

        JAXBContext context = JAXBContext.newInstance(Foo.class, Bar.class);
        context.generateSchema(new MySchemaOutputResolver());
        System.out.println("done...");
    }
}
