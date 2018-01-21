package gtu.json;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonDemo {
 
     public static void main(String[] args) throws JSONException {
 
         // 利用bean來產生一個對應的JSONObject
         PeopleBean Mary = new PeopleBean("Mary", 20, false);
         JSONObject jsonObjectMary = new JSONObject(Mary);
         System.out.println("1: " + jsonObjectMary);
 
         // 利用map的對照來產生JSONObject
         Map map = new HashMap();
         map.put("name", "Jacky");
         map.put("age", 30);
         map.put("gender", true);
         JSONObject jsonObjectJacky = new JSONObject(map);
         System.out.println("2: " + jsonObjectJacky);
 
         // 每個JSONObject都能利用JSONObject.put(key, value) 來增加屬性
         jsonObjectJacky.put("height", 180);
         System.out.println("3: " + jsonObjectJacky);
 
         // 利用 JSON 的字串產生 JSONObject
         String jackyJsonString = jsonObjectJacky.toString();
         JSONObject jsonObjectJackyFromString = new JSONObject(
                                                jackyJsonString);
 
         // JSONObject.get(key) 可取得 JSONObject中對應的值
         // 找不到的話會丟出 org.json.
         //JSONException如下面註解掉那行
         System.out.println("4: " + jsonObjectJackyFromString.get(
                            "name"));
         // System.out.println(jsonObjectJackyFromString.get("weight"));
 
         // 由JSONObject.names()取回names的JSONArray
         JSONArray jsonArrayNames = jsonObjectJacky.names();
         System.out.println("5: " + jsonArrayNames);
 
         // JSONArray可藉由JSONArray.put(value) 來增加Array中的值
         jsonArrayNames.put("weight");
         System.out.println("6: " + jsonArrayNames);
     }
     
     private static class PeopleBean {
         
         private String name;
         private int age;
         private boolean gender;
     
         public PeopleBean(String name, int age, boolean gender) {
             setPeopleBean(name, age, gender);
         }
     
         public String getName() {
             return name;
         }
     
         public void setName(String name) {
             this.name = name;
         }
     
         public int getAge() {
             return age;
         }
     
         public void setAge(int age) {
             this.age = age;
         }
     
         public boolean getGender() {
             return gender;
         }
     
         public void setGender(boolean gender) {
             this.gender = gender;
         }
     
         /***************************************************************/
         public void setPeopleBean(String name, int age, boolean gender) {
             setName(name);
             setAge(age);
             setGender(gender);
         }
     }
 }