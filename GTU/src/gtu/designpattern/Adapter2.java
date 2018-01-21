package gtu.designpattern;

import java.util.HashMap;
import java.util.Map;

/**
 * 適配器 Convert the interface of a class into another interface clients expect.
 * Adapter lets classes work together that couldn otherwise because of
 * incompatible interfaces.
 * 將一個類別的介面轉換成客戶端所期待的另一種介面，從而使原本因介面而不匹配而無法再一起工作的兩個類別能夠再一起工作
 * 
 * 三個腳色 Target目標腳色 Adaptee源腳色 Adapter適配器腳色
 * 
 * 優點 1.可以讓兩個沒有任何關係的類別再一起執行，只要是配器這個腳色能夠搞定他們就行 2.增加了類別的透明度 3.提升了類別重利用度 4.靈活性非常好
 * 
 * 適用情境 有動機修改一個已經上線的介面時~(還在設計階段時最好不要考慮它,而是為了解絕正在服役的專案問題)
 */
public class Adapter2 {

    public static void main(String[] args) {
        //老闆要取得美眉的電話時
        //沒有與外部系統連接時是這樣寫的
        IUserInfo youngGirl1 = new UserInfo();
        youngGirl1.getMobileNumber();
        //取得另一個系統的資料
        IUserInfo youngGirl2 = new OuterUserInfo();
        youngGirl2.getMobileNumber();

        //↓↓↓↓↓↓↓↓↓ 延伸
        IOuterUserHomeInfo iouterUserHomeInfo = new OuterUserHomeInfo();
        IOuterUserBaseInfo iouterUserBaseInfo = new OuterUserBaseInfo();
        IOuterUserOfficeInfo iouterUserOfficeInfo = new OuterUserOfficeInfo();
        IUserInfo youngGirl3 = new OuterUserInfo2(iouterUserHomeInfo, iouterUserBaseInfo, iouterUserOfficeInfo);
        youngGirl3.getMobileNumber();
    }

    //員工資訊界面
    interface IUserInfo {
        String getUserName();

        String getHomeAddress();

        String getMobileNumber();

        String getOfficeTelNumber();

        String getJobPosition();

        String getHomeTelNumber();
    }

    static class UserInfo implements IUserInfo {
        @Override
        public String getUserName() {
            System.out.println("員工姓名...");
            return null;
        }

        @Override
        public String getHomeAddress() {
            return "員工地址...";
        }

        @Override
        public String getMobileNumber() {
            return "員工手機電話...";
        }

        @Override
        public String getOfficeTelNumber() {
            return "員工辦公室電話...";
        }

        @Override
        public String getJobPosition() {
            return "員工工作地點...";
        }

        @Override
        public String getHomeTelNumber() {
            return "員工家裡電話...";
        }
    }

    //勞動資源公司的人員資訊界面
    interface IOuterUser {
        Map getUserBaseInfo();

        Map getUserOfficeInfo();

        Map getUserHomeInfo();
    }

    static class OuterUser implements IOuterUser {
        @Override
        public Map getUserBaseInfo() {
            HashMap baseInfoMap = new HashMap();
            baseInfoMap.put("userName", "這個員工叫混世魔王..");
            baseInfoMap.put("mobileNumber", "這個員工電話是..");
            return baseInfoMap;
        }

        @Override
        public Map getUserOfficeInfo() {
            HashMap homeInfo = new HashMap();
            homeInfo.put("homeTelNumber", "員工的家庭電話是..");
            homeInfo.put("homeAddress", "員工的家庭地址是..");
            return homeInfo;
        }

        @Override
        public Map getUserHomeInfo() {
            HashMap officeInfo = new HashMap();
            officeInfo.put("jobPosition", "這個人的職位是Boss");
            officeInfo.put("officeTelNumber", "員工的辦公室電話是..");
            return null;
        }
    }

    //中轉角色
    static class OuterUserInfo extends OuterUser implements IUserInfo {
        private Map baseInfo = super.getUserBaseInfo();
        private Map homeInfo = super.getUserHomeInfo();
        private Map officeInfo = super.getUserOfficeInfo();

        @Override
        public String getUserName() {
            return (String) baseInfo.get("userName");
        }

        @Override
        public String getHomeAddress() {
            return (String) homeInfo.get("homeAddress");
        }

        @Override
        public String getMobileNumber() {
            return (String) baseInfo.get("mobileNumber");
        }

        @Override
        public String getOfficeTelNumber() {
            return (String) officeInfo.get("officeTelNumber");
        }

        @Override
        public String getJobPosition() {
            return (String) officeInfo.get("jobPosition");
        }

        @Override
        public String getHomeTelNumber() {
            return (String) homeInfo.get("homeTelNumber");
        }
    }

    // -------------------- ↓↓↓↓↓↓↓↓↓ 延伸
    //    若另一系統是三個介面對應一個介面
    interface IOuterUserBaseInfo {
        Map getUserBaseInfo();
    }

    interface IOuterUserOfficeInfo {
        Map getUserOfficeInfo();
    }

    interface IOuterUserHomeInfo {
        Map getUserHomeInfo();
    }

    static class OuterUserHomeInfo implements IOuterUserHomeInfo {
        @Override
        public Map getUserHomeInfo() {
            HashMap officeInfo = new HashMap();
            officeInfo.put("jobPosition", "這個人的職位是Boss");
            officeInfo.put("officeTelNumber", "員工的辦公室電話是..");
            return null;
        }
    }

    static class OuterUserBaseInfo implements IOuterUserBaseInfo {
        @Override
        public Map getUserBaseInfo() {
            HashMap baseInfoMap = new HashMap();
            baseInfoMap.put("userName", "這個員工叫混世魔王..");
            baseInfoMap.put("mobileNumber", "這個員工電話是..");
            return baseInfoMap;
        }
    }

    static class OuterUserOfficeInfo implements IOuterUserOfficeInfo {
        @Override
        public Map getUserOfficeInfo() {
            HashMap homeInfo = new HashMap();
            homeInfo.put("homeTelNumber", "員工的家庭電話是..");
            homeInfo.put("homeAddress", "員工的家庭地址是..");
            return homeInfo;
        }
    }

    static class OuterUserInfo2 implements IUserInfo {

        IOuterUserHomeInfo iouterUserHomeInfo;
        IOuterUserBaseInfo iouterUserBaseInfo;
        IOuterUserOfficeInfo iouterUserOfficeInfo;

        private Map baseInfo;
        private Map homeInfo;
        private Map officeInfo;

        OuterUserInfo2(IOuterUserHomeInfo iouterUserHomeInfo, IOuterUserBaseInfo iouterUserBaseInfo, IOuterUserOfficeInfo iouterUserOfficeInfo) {
            this.iouterUserHomeInfo = iouterUserHomeInfo;
            this.iouterUserBaseInfo = iouterUserBaseInfo;
            this.iouterUserOfficeInfo = iouterUserOfficeInfo;
            baseInfo = iouterUserBaseInfo.getUserBaseInfo();
            homeInfo = iouterUserHomeInfo.getUserHomeInfo();
            officeInfo = iouterUserOfficeInfo.getUserOfficeInfo();
        }

        @Override
        public String getUserName() {
            return (String) baseInfo.get("userName");
        }

        @Override
        public String getHomeAddress() {
            return (String) homeInfo.get("homeAddress");
        }

        @Override
        public String getMobileNumber() {
            return (String) baseInfo.get("mobileNumber");
        }

        @Override
        public String getOfficeTelNumber() {
            return (String) officeInfo.get("officeTelNumber");
        }

        @Override
        public String getJobPosition() {
            return (String) officeInfo.get("jobPosition");
        }

        @Override
        public String getHomeTelNumber() {
            return (String) homeInfo.get("homeTelNumber");
        }
    }
}