package _temp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import gtu.file.FileUtil;

public class Test58 {

    public static void main(String[] args) {
        List<SM> godLst = getFiles("GodFIles.txt");
        List<SM> myLst = getFiles("MyFiles.txt");

        for (SM m : myLst) {
            if (!godLst.contains(m)) {
                System.out.println(m.full);
            }
        }
        System.out.println("done..");
    }

    static class SM {
        String name;
        String full;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SM other = (SM) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }
    }

    private static List<SM> getFiles(String filename) {
        List<String> lst = FileUtil.loadFromFile_asList(new File("C:/Users/wistronits/Desktop/", filename), "utf8");
        List<SM> lst2 = new ArrayList<SM>();
        for (String strVal : lst) {
            SM d = new SM();
            d.name = new File(strVal).getName();
            d.full = strVal;
            lst2.add(d);
        }
        return lst2;
    }
}
