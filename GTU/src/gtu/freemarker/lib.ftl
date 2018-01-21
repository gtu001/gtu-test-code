<#macro copyright date> 
 <p>Copyright (C) ${date} Julia Smith. All rights reserved. 
  <br>Email: ${mail}</p> 
</#macro> 
<#assign mail = "jsmith@acme.com"> 


<#function libfunc val>
        <#return "libfunc = " + val>
</#function>


<#setting date_format="yyyyMMdd">
<#setting time_format="HHmmss">
<#setting datetime_format="yyyyMMddHHmmss">
<#macro mingoDate date>����${date[0..3]?number-1911}�~${date[4..5]?number}��${date[6..7]?number}��</#macro>

<#-- ����~ -->
<#function mingoDate1 date>
        <#if date?is_date>
                <#local nd=date?date>
        <#elseif date?is_string && date?matches('\\d{8}')>
                <#local nd=date>
        <#else>
                <#return "error_date">
        </#if>
        <#local year=nd[0..3]?number-1911>
        <#local month=nd[4..5]?number>
        <#local dddd=nd[6..7]?number>
        <#return "${year}�~${month}��${dddd}��">
</#function>

<#-- �褸�~ -->
<#function ciDate1 date>
        <#if date?is_date>
                <#local nd=date?date>
        <#elseif date?is_string && date?matches('\\d{8}')>
                <#local nd=date>
        <#else>
                <#return "error_date">
        </#if>
        <#local year=nd[0..3]?number?c>
        <#local month=nd[4..5]?number>
        <#local dddd=nd[6..7]?number>
        <#return "${year}/${month}/${dddd}">
</#function>

