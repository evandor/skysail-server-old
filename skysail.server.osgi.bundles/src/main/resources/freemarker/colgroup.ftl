
<#list gridColumns?keys as key>
    <#assign tableColumn = gridColumns[key] />
    <#-- ignore columns with width <= 0, but show column if width is not defined -->
    <#if (!tableColumn.width?? || tableColumn.width > 0) >
        <colgroup span="1" style="width:<#if tableColumn.width??>${tableColumn.width}<#else>100</#if>px;" />
    </#if>
</#list>
