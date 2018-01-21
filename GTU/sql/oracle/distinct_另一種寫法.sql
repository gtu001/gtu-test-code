 select                                                                                                      
   t.ACCOUNT_DATE,                                                                                           
   t.ERROR_INFO,                                                                                             
   t.LAPSED_DATE                                                                                              
 from(                                                                                                       
  select                                                                                                     
    row_number() over (partition by tbtd.policy_id,tbtd.unsuccess_id order by tbts.text_date ) row_number ,       
    tbts.account_date as ACCOUNT_DATE,                                                                       
    ttrc.code_desc as ERROR_INFO ,                                                                           
    tcmm.p_lapse_date as  LAPSED_DATE                                                                         
                                                                                                             
  from t_bank_text_detail tbtd                                                                               
  join t_transfer_unsuccess ttu on ttu.config_id = tbtd.unsuccess_id                                         
  join t_transfer_return_code ttrc on ttrc.system_code = ttu.system_code                                     
  join t_contract_master tcmm on tcmm.policy_id = tbtd.policy_id                                             
  left join t_bank tb on  substr(tbtd.bank_code,0,3) = tb.bank_code                                          
  left join t_acquirer_bank ta on ta.bank_id = tb.bank_id and ta.pay_mode = tbtd.pay_mode                    
  left join T_BANK_TRANS_SCHEDULE tbts on tbts.money_id = tbtd.money_id and tbtd.due_time < tbts.text_date   
  where                                      1=1                                                            
 ) t                                                                                                         
 where t.row_number =1                                                                                       
