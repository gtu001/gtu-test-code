package gtu.spring;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.FileCopyUtils;

public class ResourceTest {

    public static void main(String[] args) throws IOException {

        Resource res1 = new FileSystemResource("C:/Users/gtu001/Desktop/workspace/GTU/src/log4j.properties");
        Resource res2 = new ClassPathResource("log4j.properties");
        //        Resource res3 = new ServletContextResource(null, null);

        EncodedResource encRes = new EncodedResource(res1, "UTF8");
        String context = FileCopyUtils.copyToString(encRes.getReader());

        System.out.println(res1.getFilename());
        System.out.println(res2.getFilename());
        System.out.println(context);
    }

}
