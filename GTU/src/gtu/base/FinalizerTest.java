package gtu.base;

public class FinalizerTest {
    
    //Sole purpose of this object is to finalize outer Foo object
    private final Object finalizerGuardian = new Object(){
        @Override
        protected void finalize() throws Throwable {
            //Finalize outer Foo object
            //在此區塊終結外部enclosingClass的資源
            //這種寫法可以避免子類忽略不做super.finalize
            //然後外部可以不用實作finalize
        }
    };

    @Override
    protected void finalize() throws Throwable {
        try{
            //Finalize subclass state
            //先終結子類的資源..若發生異常也保證父類可以被執行
        }finally{
            super.finalize();
        }
    }
}
