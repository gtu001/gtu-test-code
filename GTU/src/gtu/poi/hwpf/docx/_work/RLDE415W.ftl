isql $1 <<!

DROP TABLE ${table_name};
CREATE TABLE ${table_name}( 

<#list row as ii>
${ii.name} ${ii.type} NOT NULL,
</#list>

)
in $2 extent size $3 next size $4
lock mode row; 
create unique index ${table_name}_p_key on ${table_name}(${concatField(pks)});


!

<#function concatField row>
        <#local rtn = "">
        <#list row as ii>
                <#local rtn = rtn +", "+ ii.name>
        </#list>
        <#if rtn?substring(0,1) == ",">
                <#local rtn = rtn?substring(1)>
        </#if>
        <#return rtn>
</#function>