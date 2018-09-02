package gtu.designpattern;

public class Command_ {

    public static void main(String[] args) {
        Invoker xiaoSan = new Invoker();
        System.out.println("------客戶要求增加一項需求------");
        Command command = new AddRequirementCommand();
        xiaoSan.setCommand(command);
        xiaoSan.action();
        //將一個請求封裝成一個物件，從而讓你使用不同的請求把客戶端參數化，把請求排進住列或者記錄下來，並且提供撤銷命令和復原的功能
        //Encapsulate a request as an object, thereby letting you parameterize clients with different requests, queue or log requests, and support undoable operations.
    }

    static abstract class Group {
        abstract void find();

        abstract void add();

        abstract void delete();

        abstract void change();

        abstract void plan();
    }

    static class RequirementGroup extends Group {

        @Override
        void find() {
            System.out.println("找到需求組");
        }

        @Override
        void add() {
            System.out.println("客戶要求增加一個需求");
        }

        @Override
        void delete() {
            System.out.println("客戶要求刪除一個需求");
        }

        @Override
        void change() {
            System.out.println("客戶要求修改一個需求");
        }

        @Override
        void plan() {
            System.out.println("客戶要求需求變更計畫");
        }
    }

    static class PageGroup extends Group {

        @Override
        void find() {
            System.out.println("找到美工組");
        }

        @Override
        void add() {
            System.out.println("客戶要求增加一個頁面");
        }

        @Override
        void delete() {
            System.out.println("客戶要求刪除一個頁面");
        }

        @Override
        void change() {
            System.out.println("客戶要求修改一個頁面");
        }

        @Override
        void plan() {
            System.out.println("客戶要求頁面變更計畫");
        }
    }

    static class CodeGroup extends Group {

        @Override
        void find() {
            System.out.println("找到代碼組");
        }

        @Override
        void add() {
            System.out.println("客戶要求增加一個功能");
        }

        @Override
        void delete() {
            System.out.println("客戶要求刪除一個功能");
        }

        @Override
        void change() {
            System.out.println("客戶要求修改一個功能");
        }

        @Override
        void plan() {
            System.out.println("客戶要求代碼變更計畫");
        }
    }

    static abstract class Command {
        protected RequirementGroup rg = new RequirementGroup();
        protected PageGroup pg = new PageGroup();
        protected CodeGroup cg = new CodeGroup();

        public abstract void execute();
    }

    //增加需求命令
    static class AddRequirementCommand extends Command {
        @Override
        public void execute() {
            super.rg.find();//找到需求組
            super.rg.add();//增加需求
            super.rg.plan();//給出計畫
        }
    }

    //刪除頁面的命令
    static class DeletePageCommand extends Command {
        @Override
        public void execute() {
            super.pg.find();//找到美工組
            super.pg.delete();//刪除一個頁面
            super.pg.plan();//給出計畫
        }
    }

    //負責人
    static class Invoker {
        private Command command;

        public void setCommand(Command command) {
            this.command = command;
        }

        public void action() {
            this.command.execute();
        }
    }
}
