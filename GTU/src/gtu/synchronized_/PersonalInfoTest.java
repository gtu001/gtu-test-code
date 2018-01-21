package gtu.synchronized_;

public class PersonalInfoTest {
    public static void main(String[] args) {
        final PersonalInfo person = new PersonalInfo();

        // 假设会有两个线程可能更新person对象
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    person.setNameAndID("Justin Lin", "J.L");
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                while (true)
                    person.setNameAndID("Shang Hwang", "S.H");
            }
        });

        System.out.println("开始测试.....");

        thread1.start();
        thread2.start();
    }
}

class PersonalInfo {
    private String name;
    private String id;
    private int count;

    public PersonalInfo() {
        name = "nobody";
        id = "N/A";
    }

    public void setNameAndID(String name, String id) {
        this.name = name;
        this.id = id;
        if (!checkNameAndIDEqual()) {
            System.out.println(count + ") illegal name or ID.....");
        }
        count++;
    }

    private boolean checkNameAndIDEqual() {
        return (name.charAt(0) == id.charAt(0)) ? true : false;
    }
}