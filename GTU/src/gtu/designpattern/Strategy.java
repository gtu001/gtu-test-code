package gtu.designpattern;

/**
 * 策略模式
 * 也較政策模式Policy Pattern
 * Define a family of algorithms, encapsulate each one , and make them interchangeable.
 * 將每個演算法都封裝起來，使他們可以互換，都實作著相同介面
 * 
 * context封裝腳色
 * strategy抽象策略腳色
 * 
 * 優點：
 * 1.演算法可以自由切換
 * 2.避免使用多呈條件判斷
 * 3.擴展性良好
 * 
 * 使用情境:
 * 1.多個類別只有在演算法或行為上稍有不同情境
 * 2.演算法需要自由切換情境
 * 3.需要屏蔽演算法規則的場警
 */
public class Strategy {
    
    //趙雲出場了，根據諸葛亮給他的交代，依次拆開妙計
    public static void main(String[] args){
        Context context;
        //剛到吳國的時候拆第一個
        context = new Context(new BackDoor());
        context.operate();
        //劉備樂不思蜀了，拆第二個
        context = new Context(new GivenGreenLight());
        context.operate();
        //孫權的小兵追了，怎辦，拆第三個
        context = new Context(new BlockEnemy());
        context.operate();
    }

    interface IStrategy {
        //每個錦囊妙計都是一個可執行的演算法
        void operate();
    }
    
    //喬國老開後門
    static class BackDoor implements IStrategy{
        @Override
        public void operate() {
            System.out.println("找喬國老幫忙，讓吳國泰給孫權施加壓力");
        }
    }
    
    //吳國太開綠燈
    static class GivenGreenLight implements IStrategy {
        @Override
        public void operate() {
            System.out.println("求吳國太開綠燈，放行!");
        }
    }
    
    //孫夫人斷後
    static class BlockEnemy implements IStrategy {
        @Override
        public void operate() {
            System.out.println("孫夫人斷後，擋住追兵");
        }
    }
    
    //錦囊
    static class Context {
        //構造函式，你要使用哪個妙計
        private IStrategy strategy;
        Context (IStrategy strategy){
            this.strategy = strategy;
        }
        //使用計謀了，看我出招了
        void operate(){
            this.strategy.operate();
        }
    }
}
