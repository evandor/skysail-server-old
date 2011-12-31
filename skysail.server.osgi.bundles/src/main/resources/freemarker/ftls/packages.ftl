<#include "skysail.server.osgi.bundles:head.ftl">
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
				  <img src="/static/img/package.gif">&nbsp;<b>${columnData}</b>
			  <#elseif columnData_index == 2>
			      <#assign bundle = columnData?split(" ") />
			      <#if (bundle?size > 1)>
				      <#assign bundleId = bundle[2] />
				      <a href='../bundles/${bundleId}/'>${columnData}</a>
				  <#else>
					<img src="/static/img/bundle.gif">&nbsp;${columnData}
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