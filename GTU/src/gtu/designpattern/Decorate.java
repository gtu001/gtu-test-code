package gtu.designpattern;

/**
 * 裝飾模式定義
 * Attach additional responsibilities to an object dynamically keeping the same interface.
 * Decorators provide a flexible alternative to subclassing for extending functionality.
 * 動態的給一個物件附加一些額外職責，同時保持相同介面。就擴展功能來說，裝飾模式提供了繼承機制的彈性替代方案.
 * 
 * 使用情境
 * 1.需要擴展一個類別的功能，或者給一個類別添附加功能
 * 2.需要動態的給一個物件增加功能，這些功能可以在動態的撤銷
 * 3.需要為一批兄弟類別進行改裝或加裝功能，首選方案當然是裝飾模式
 */
public class Decorate {

    public static void main(String[] args) {
        //原本的流程
        //        SchoolReport sr = new FouthGradeSchoolReport();
        //        sr.report();
        //        成績太差拒絕簽字
        //修改後的流程
        //        SchoolReport sr = new SugarFouthGradeSchoolReport();
        //        sr.report();
        //        sr.sign("王老五");
        //修飾的寫法
        SchoolReport sr;
        sr = new FouthGradeSchoolReport();
        sr = new HignScoreDecorator(sr);
        sr = new SortScoreDecorator(sr);
        sr.report();
        sr.sign("王老五");
        
    }

    static abstract class SchoolReport {
        //成績單主要展示的是你的成績情況
        abstract void report();

        //成績單要家長簽字,這是最要命的
        abstract void sign(String name);
    }

    static class FouthGradeSchoolReport extends SchoolReport {
        @Override
        void report() {
            System.out.println("尊敬的XXX家長:");
            System.out.println("   ......");
            System.out.println("   國文 62 數學 65 體育 98 自然63");
            System.out.println("   ......");
            System.out.println("                     家長簽名:            ");
        }

        @Override
        void sign(String name) {
            System.out.println("家長簽名為:" + name);
        }
    }

    //簡單的解法
    static class SugarFouthGradeSchoolReport extends FouthGradeSchoolReport {
        //首先定義你要美化的方法,先給老爸說學校最高成績
        private void reportHighScore() {
            System.out.println("這次考試國文最高是75,數學78,自然80");
        }

        //在老爸看完成績單後,我在會報學校的排名情況
        private void reportSort() {
            System.out.println("我的排名是第38名...");
        }

        //由於會報內容已發生變更，所以要重寫父類別
        @Override
        void report() {
            this.reportHighScore();//先說最高成績
            super.report();//接著老爸看成績單
            this.reportSort();//然後告訴老爸學校排名
        }
    }

    //-----------↓↓↓↓↓↓↓↓↓↓---------------複雜的寫法
    static abstract class Decorator extends SchoolReport {
        //首先我要知道是哪個成績單
        SchoolReport sr;

        //構造函示，傳遞成績單過來
        Decorator(SchoolReport sr) {
            this.sr = sr;
        }

        //成績單還是要被看到的
        @Override
        void report() {
            this.sr.report();
        }

        //看完還是要簽名
        @Override
        void sign(String name) {
            this.sr.sign(name);
        }
    }

    static class HignScoreDecorator extends Decorator {
        HignScoreDecorator(SchoolReport sr) {
            super(sr);
        }

        //我要會報最高成績
        private void reportHighScore() {
            System.out.println("這次考試國文最高是75,數學78,自然80");
        }

        @Override
        void report() {
            this.reportHighScore();
            super.report();
        }
    }

    static class SortScoreDecorator extends Decorator {
        SortScoreDecorator(SchoolReport sr) {
            super(sr);
        }

        //告訴老爸學校的排名
        private void reportSort() {
            System.out.println("我的排名是第38名...");
        }

        @Override
        void report() {
            super.report();
            this.reportSort();
        }
    }
}
