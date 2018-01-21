package gtu.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 2012/1/8 对象序列号 Externalizable
 */
public class SerializableList extends ArrayList implements Externalizable {

    public void writeExternal(ObjectOutput out) throws IOException {

        out.writeInt(size());
        for (int i = 0; i < size(); i++) {
            if (get(i) instanceof Serializable) {
                out.writeObject(get(i));
            } else {
                out.writeObject(null);
            }
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        int elementCount = in.readInt();
        this.ensureCapacity(elementCount);
        for (int i = 0; i < elementCount; i++) {
            this.add(in.readObject());
        }
    }

    public static void main(String[] args) throws Exception {

        SerializableList list = new SerializableList();
        list.add("Element 1");
        list.add(new Integer(9));
        list.add(new URL("http://www.oreilly.com/"));

        // not Serializable
        list.add(new Socket("www.oreilly.com", 80));

        list.add("Element 5");
        list.add(new Integer(9));
        list.add(new URL("http://www.oreilly.com/"));

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream temp = new ObjectOutputStream(bout);
        temp.writeObject(list);
        temp.close();

        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        ObjectInputStream oin = new ObjectInputStream(bin);
        List out = (List) oin.readObject();
        Iterator iterator = out.iterator();
        while (iterator.hasNext()) {
            System.out.println("[read]" + iterator.next());
        }
    }
}