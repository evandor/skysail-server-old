<#import "skysail.server:dump.ftl" as dumper>
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
    
    <#include "skysail.server:style.css">
</head>
<body>
  <#include "skysail.server:header.ftl">
  <div id="container">
    <#include "skysail.server.osgi.bundles:title.ftl">
	
	<h3>${message} - ${totalResults} hits</h3>
	

	<h3>Data</h3>
	
	<#include "skysail.server:debug.ftl">
	
	<#list data as component>
	  
	  <#if component.class.simpleName == "GridData">
		<#assign columns = component.columns>
		<form action="#">
		<table>
		<tr>
		  <#list columns as column>
			<th>${column.columnName}</th>  
		  </#list>
		  <th>Packages</th>
		</tr>
		<tr>
		  <#list columns as column>
			<td>
			<#if column.columnName == "Package">
			<input type="text" name="filterPackage" value="" />
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
			  <#if columnData_index == 0>
				  <img src="${contextPath}static/img/package.gif">&nbsp;<b>${columnData}</b>
			  <#elseif columnData_index == 2>
			      <#assign bundle = columnData?split(" ") />
			      <#if (bundle?size > 1)>
				      <#assign bundleId = bundle[2] />
				      <a href='../bundles/${bundleId}/'>${columnData}</a>
				  <#else>
					<img src="${contextPath}static/img/bundle.gif">&nbsp;${columnData}
				  </#if>
			  <#elseif columnData_index == 3>
			      ${columnData?replace(";","<br>\n")}
			  <#else>
				  ${columnData}
			  </#if>
			</td>
			</#list>
		  </tr>  
		</#list>
		
		</table>  
		</form>
	  </#if>  
	
	</#list>
  </div>

</body>
</html>