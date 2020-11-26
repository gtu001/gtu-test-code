package gtu.serialization;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectSerailizeUtil {
    
    public interface WriteObject {
        void write(ObjectOutputStream oos) throws Exception;
    }
    
    public interface ReadObject {
        void read(ObjectInputStream oos) throws Exception;
    }

    public static void write(File file, WriteObject tWriteObject) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            tWriteObject.write(oos);
            oos.flush();
        } catch (Exception ex) {
            throw new RuntimeException("write ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void read(File file, ReadObject tReadObject) {
        ObjectInputStream oos = null;
        try {
            oos = new ObjectInputStream(new FileInputStream(file));
            tReadObject.read(oos);
        } catch (Exception ex) {
            throw new RuntimeException("read ERR : " + ex.getMessage(), ex);
        } finally {
            try {
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
