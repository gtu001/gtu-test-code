sqlserver_datetime_時間欄位比對
---

SELECT  *
FROM ZV_PAY_ALL  
where   datediff(second,'1990-1-1', agent_receive_time)  >  datediff(second,'1990-1-1', convert(datetime, '2016/10/23', 111) ) 


SELECT  *
FROM ZV_PAY_ALL  
where   datediff(second,'1990-1-1', agent_receive_time)  >  datediff(second,'1990-1-1', convert(datetime, '2016-10-23 20:44:11', 120) ) 


SELECT  *
FROM ZV_PAY_ALL  
where   datediff(second,'1990-1-1', agent_receive_time)  >  datediff(second,'1990-1-1', convert(datetime, '2016-10-23 20:44:11.500', 121) ) 

