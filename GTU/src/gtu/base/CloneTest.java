package gtu.base;


public class CloneTest {

    static class C implements Cloneable {
        private String xxx;
        int ccc;
        
        public C(String xxx, int ccc) {
            super();
            this.xxx = xxx;
            this.ccc = ccc;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            //沒有implements Cloneable 會出現CloneNotSupportedException
            return super.clone();//用Object的native clone做淺層clone
        }

        @Override
        public String toString() {
            return "C [xxx=" + xxx + ", ccc=" + ccc + "]";
        }
    }
    
    public static void main(String[] args){     
        C c = new C("aaa", 33);
        try {
            C c2 = (C)c.clone();
            System.out.println(c2);
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
