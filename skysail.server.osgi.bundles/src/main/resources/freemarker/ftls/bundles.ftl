<#import "skysail.server.restletosgi:dump.ftl" as dumper>
<#assign foo = data />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Skysail Server</title>
	
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="keywords" content="skysail" />
	<meta name="description" content="skysail" />
	<meta name="robots" content="follow, all" />
	<meta name="language" content="English" />
	<meta http-equiv="content-language" content="en" />
    
    <#include "skysail.server.restletosgi:style.css">
</head>
<body>
  <#include "skysail.server.restletosgi:header.ftl">
  <div id="container">
    <#include "skysail.server.osgi.bundles:title.ftl">
	
	<h3>${message} - ${totalResults} hits</h3>
	
	<#include "skysail.server.restletosgi:debug.ftl">
	
	<#list data as component>
	  
	  <#if component.class.simpleName == "GridData">
		<#assign columns = component.columns>
		<form action="#">
		<table>
		<tr>
		  <#list columns as column>
			<th>${column.columnName}</th>  
		  </#list>
		  <th>Maven Project</th>
		</tr>
		<tr>
		  <#list columns as column>
			<td>
			<#if column.columnName == "symbolicName">
			<input type="text" name="filterSymbolicName" value="skysail" />
			<input type="submit" value="filter"/>
			</#if>
			</td>  
		  </#list>
		  <td>&nbsp;</td>
		</tr>
	    <#list component.gridData as row>
		  <#assign columns = row.columnData>
		  <tr>
		    <#list columns as columnData>
			<td>
			  <#if columnData_index == 1>
			      <a href='${columns[0]}/'>${columnData}</a>
			  <#else>
				  ${columnData}
			  </#if>
			</td>
			</#list>
			<td><#if columns[1]?starts_with("skysail.")>
				<a href='http://www.evandor.de:8787/job/${columns[1]}/' target='_blank'>project</a>&nbsp;
				<a href='http://www.evandor.de:8787/job/${columns[1]}/site/${columns[1]}/' target='_blank'>site</a>&nbsp;			
				<a href='http://www.evandor.de:9000/dashboard/index/de.evandor:${columns[1]}' target='_blank'>Sonar</a>&nbsp;			
				</#if>
			</td>
		  </tr>  
		</#list>
		
		</table>  
		</form>
	  </#if>  
	
	</#list>
	<#include "skysail.server.restletosgi:navigation.ftl">
  </div>

</body>
</html>