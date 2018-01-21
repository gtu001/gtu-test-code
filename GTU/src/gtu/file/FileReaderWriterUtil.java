package gtu.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.lang3.StringUtils;

public class FileReaderWriterUtil {
    
    public static class WriterZ {
        private File file;
        private String encode;
        private boolean append;
        private BufferedWriter writer;
        
        public static WriterZ newInstance(File file){
            return new WriterZ(file, "utf8", false);
        }
        public static WriterZ newInstance(File file, String encode){
            return new WriterZ(file, encode, false);
        }
        public static WriterZ newInstance(File file, String encode, boolean append){
            return new WriterZ(file, encode, append);
        }
        
        private WriterZ(File file, String encode, boolean append){
            this.file = file;
            this.encode = encode;
            this.append = append;
            if(StringUtils.isBlank(this.encode)){
                this.encode = "utf8";
            }
        }
        
        public void init(){
            try{
                this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, this.append), this.encode));
            }catch(Exception ex){
                throw new RuntimeException(ex);
            }
        }
        
        public void writeLine(String line){
            try{
                writer.write(line + '\n');
            }catch(Exception ex){
                throw new RuntimeException(ex);
            }
        }
        
        public void write(String line){
            try{
                writer.write(line);
            }catch(Exception ex){
                throw new RuntimeException(ex);
            }
        }
        
        public void flush(){
            try{
                writer.flush();
            }catch(Exception ex){
                throw new RuntimeException(ex);
            }
        }
        
        public void writeAndFlush(String line){
            try{
                writer.write(line);
                writer.flush();
            }catch(Exception ex){
                throw new RuntimeException(ex);
            }
        }
        
        public void close(){
            try{
                writer.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }
    
    public interface ReaderZLine {
        boolean readLine(String line);
    }

    public static class ReaderZ {
        private File file;
        private String encode;
        private BufferedReader reader;
        
        public static ReaderZ newInstance(File file){
            return new ReaderZ(file, "utf8");
        }
        public static ReaderZ newInstance(File file, String encode){
            return new ReaderZ(file, encode);
        }
        
        public ReaderZ(File file, String encode){
            this.file = file;
            this.encode = encode;
            if(StringUtils.isBlank(this.encode)){
                this.encode = "utf8";
            }
        }
        
        public void init(){
            try{
                this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), this.encode));
            }catch(Exception ex){
                throw new RuntimeException(ex);
            }
        }
        
        public String readLine(){
            try {
                return reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        public void read(ReaderZLine readerZLine){
            try {
                for(String line = null; (line = reader.readLine())!=null;){
                    if(!readerZLine.readLine(line)){
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        public void close(){
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
