<#include "skysail.server.osgi.bundles:head.ftl">
<body>

  <#include "skysail.server:navigation.ftl">

  <div id="container">
    <#assign info = "List of all available Bundles (found ${totalResults})" />
    <#include "skysail.server.osgi.bundles:title.ftl">
	
	
	  
	  <#if data.class.simpleName == "GridData">
		<#assign gridColumns = data.columns.asList>
		
		<form action="#">
		<table>
        <#include "skysail.server:grid/caption.ftl">
        <#include "skysail.server:grid/colgroup.ftl">
        <#include "skysail.server:grid/thead.ftl">
        <#include "skysail.server:grid/tfoot.ftl">

		<tbody>

        <#include "skysail.server.osgi.bundles:search.ftl">

		<#assign counter = 0 />
	    <#list data.rows as row>
		  <#assign columns = row.columnData>
		  <#assign counter = counter + 1 />
		  <#if (counter % 2 == 1)>
		    <tr>
		  <#else>
		    <tr class="odd" />
		  </#if>
		    
		  <#list columns as columnData>
		    <#if columnData_index == 0>
			  <td>${columnData}</td>
		    <#elseif columnData_index == 1>
			  <td><a href='${columns[0]}/'>${columnData}</a></td>
			<#elseif columnData_index == 2>
		        <td><img src="${contextPath}static/img/bundle.gif">&nbsp;${columnData}</td>
			<#elseif columnData_index == 3>
			  <td>${columnData}</td>
			<#elseif columnData_index == 4>
   		        <td>${columnData?replace(";","<br>\n")}</td>
			<#else>
				
			  </#if>
			</#list>
		  </tr>  
		</#list>
		</tbody>
		</table>  
		</form>
	  </#if>  
	
	

  <#include "skysail.server:debug.ftl">

</body>
</html>