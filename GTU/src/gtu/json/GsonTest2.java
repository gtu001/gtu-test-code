package gtu.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

public class GsonTest2 {

    public static void main(String[] args) throws IOException {
        File jsonFile = new File("D:/gtu-test-code/GTU/src/gtu/json/GsonTestResources2.json");
        String jsonStr = FileUtils.readFileToString(jsonFile);

        Gson gson = new Gson();
        java.lang.reflect.Type listType = new TypeToken<ArrayList<MyJsonObj>>() {}.getType();
        ArrayList<MyJsonObj> jsonArr = gson.fromJson(jsonStr, listType);
        for(MyJsonObj obj : jsonArr){
           System.out.println("obj chanel:" + obj.getChanelStr());
           System.out.println("obj start time:" + obj.getStartTime());
           System.out.println("obj end time:" + obj.getEndTime());
           System.out.println("obj week:" + obj.getWeekStr());
           System.out.println("obj DJ:" + obj.getDjStr());
        }
        System.out.println("done...");
    }

    class MyJsonObj {
        @SerializedName("chanel")
        private String chanelStr;

        @SerializedName("week")
        private String weekStr;

        @SerializedName("start_time")
        private String startTime;

        @SerializedName("end_time")
        private String endTime;

        @SerializedName("DJ")
        private String djStr;

        public String getChanelStr() {
            return chanelStr;
        }

        public void setChanelStr(String chanelStr) {
            this.chanelStr = chanelStr;
        }

        public String getWeekStr() {
            return weekStr;
        }

        public void setWeekStr(String weekStr) {
            this.weekStr = weekStr;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getDjStr() {
            return djStr;
        }

        public void setDjStr(String djStr) {
            this.djStr = djStr;
        }
    }
}
