SET LINESIZE 256


spool test001_result.txt;

select /*+ gather_plan_statistics */ * from (
select t.*
 from t_prem_arap t, t_contract_product g
 where 1=1
 and t.policy_id = g.policy_id
 and t.item_id = g.item_id
 and g.master_id is null
 and g.liability_state = 1
 and (prem_notice_indi is null or prem_notice_indi not in (2)) 
 and fee_type = 41 
 and fee_status in (0, 3, 4)
 and sysdate >= t.due_time + 7 
 and t.pay_balance > 0 
 and t.policy_id = 1
);


select *
from
    table (dbms_xplan.display_cursor (format=>'ALLSTATS LAST'));
 
 
 
spool end;
 