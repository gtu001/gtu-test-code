<#import "/lib.ftl" as my>  

<#function getColVal col>
         <#local rtn = "">
         <#if column_dateLst?seq_contains(col)>
                <#local rtn = "TO_DATE(':${col}','YYYY/MM/DD')">
         <#elseif column_timestampLst?seq_contains(col)>
                <#local rtn = "TO_TIMESTAMP(':${col}','YYYY-MM-DD HH24:MI:SS.NNNNNN')">
         <#else>
                <#local rtn = "':${col}'">
         </#if>
         <#return rtn>
</#function>

<#function getWhereCol col>
         <#local rtn = "">
         
         <#-- 變數不存在給初始值 -->
         <#if !column_dateLst??>
                <#assign column_dateLst = []>
         </#if>
         <#if !column_timestampLst??>
                <#assign column_timestampLst = []>
         </#if>
         
         <#if column_dateLst?seq_contains(col) || column_timestampLst?seq_contains(col)>
                <#local rtn = "VARCHAR_FORMAT(T1.${col}, 'YYYY/MM/DD')">
         <#else>
                <#local rtn = "${col}">
         </#if>
         <#return rtn>
</#function>


-----------------------------------------------------------------------



SELECT T1.*, 
	D1.NAME AS UPDT_NM, U1.DIV_SHORT_NAME AS DIV_NO_NM 
  FROM ${main_schema}.${main_table} T1
  LEFT JOIN CXLHR.DTA0_EMPLOYEE_WORK D1 ON T1.UPDT_ID = D1.EMPLOYEE_ID 
  LEFT JOIN  CXLHR.DTZ0_UNIT_WORK U1 ON T1.DIV_NO = U1.DIV_NO
 WHERE 1 = 1 
 <#list searchColumns as col>
        <#if searchColumns_like?seq_contains(col)>
               [ and ${getWhereCol(col)} like ${getColVal(col)} ]
        <#else>
               [ and ${getWhereCol(col)} = ${getColVal(col)} ]
        </#if>
 </#list>
  WITH UR
  
  
-----------------------------------------------------------------------


INSERT INTO ${main_schema}.${main_table}
(
<#list insertColumns as col>
        <#if col?is_last>
                ${col}
        <#else>
                ${col},
        </#if>
</#list>
) 
VALUES (
<#list insertColumns as col>
        <#if col?is_last>
                ${getColVal(col)}
        <#else>
                ${getColVal(col)},
        </#if>
</#list>
	)
WITH UR


-----------------------------------------------------------------------


UPDATE ${main_schema}.${main_table} T1
SET 
   
<#list updateColumns as col>
        <#if col?is_last>
                T1.${col} = ${getColVal(col)}
        <#else>
                T1.${col} = ${getColVal(col)},
        </#if>
</#list>
 
WHERE 1=1
<#list updateColumns_where as col>
       [ and T1.${col} = ${getColVal(col)} ]
</#list>
	
	WITH UR
	

-----------------------------------------------------------------------

DELETE 
  FROM ${main_schema}.${main_table} t1
 WHERE  1=1 
  <#list deleteColumns_where as col>
       [ and T1.${getWhereCol(col)} = ${getColVal(col)} ]
  </#list>
  WITH UR
  
  
-----------------------------------------------------------------------