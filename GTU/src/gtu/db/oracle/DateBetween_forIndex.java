package gtu.db.oracle;

public class DateBetween_forIndex {

    public static void main(String[] args) {
        //為了要有index效果的日期區間做法(看起來好像對...)
//        SELECT * FROM ex_5203m_n WHERE 1=1
//                and 
//                (PROCESS_DATE > '開始日'  OR (PROCESS_DATE = '開始日'  AND PROCESS_TIME >= '開始時間' ) )  
//                AND  
//                (PROCESS_DATE < '結束日'  OR (PROCESS_DATE = '結束日'  AND PROCESS_TIME <= '結束時間' ) )
        
        //無須憂慮index的寫法..比較正確
//        SELECT * FROM ex_5203m_n WHERE msg_type = 'N5203'  
//                and concat(process_date, process_time) >= '20140701081000' 
//                and  concat(process_date, process_time) <= '20140703081000'
    }
}
