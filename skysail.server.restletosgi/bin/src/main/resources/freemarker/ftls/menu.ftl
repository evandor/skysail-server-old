<#import "skysail.server.restletosgi:dump.ftl" as dumper>
<#assign foo = data />

<html>
<head>
	<title>Skysail Server</title>
    <#include "skysail.server.restletosgi:style.css">
</head>
<body>

<table width="100%">
<tr>
  <td width="100">
	<img src="http://62.75.235.75/twenty11/images/skysail_small.png">	  
  </td>
  <td align=left>
	<h1>Skysail RestletOsgi Server - Menu</h1>
  </td>
</tr>
</table>

<#include "skysail.server.restletosgi:navigation.ftl">

<h2>Message</h2>
${message}

<h2>Data</h2>

<#list data as component>
  <#if component.class.simpleName == "TreeNodeData">
	<b>${component_index}.</b> ${component.type} (type):<br>
	&nbsp;-&nbsp;${component.name} (name)<br>
	&nbsp;-&nbsp;${component.clickAction} (clickAction)<br>
	&nbsp;-&nbsp;<a href='../${component.openAction}?media=html'>${component.openAction}</a> (openAction)<br>
	&nbsp;-&nbsp;${component.tooltip} (tooltip)<br>
	<br>
  </#if>
  
  <#if component.class.simpleName == "LinkData">
    <#if component.href?starts_with("http://")>
  	  &nbsp;-&nbsp;<a href='${component.href}'>${component.value}</a><br>
    <#else>
	  &nbsp;-&nbsp;<a href='../${component.href}?media=html'>${component.value}</a><br>
	</#if>
  </#if>  


  <#if component.class.simpleName == "FormData">
    <#list component as field>
      .${field}.
    </#list>
  
  </#if>  

  <#if component.class.simpleName == "GridData">
	<#assign columns = component.columns>
	<table>
	<tr>
	  <#list columns as column>
		<th>${column.columnName}</th>  
	  </#list>
	</tr>
    <#list component.gridData as row>
	  <#assign columns = row.columnData>
	  <tr>
	    <#list columns as columnData>
		<td>${columnData}</td>
		</#list>
	  </tr>  
	</#list>
	
	</table>  
  </#if>  

</#list>

<#include "skysail.server.restletosgi:debug.ftl">

</body>
</html>