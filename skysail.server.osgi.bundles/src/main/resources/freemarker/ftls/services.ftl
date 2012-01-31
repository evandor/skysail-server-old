<#include "skysail.server.osgi.bundles:head.ftl">
<body>

	<script src="${contextPath}static/js/jquery-1.7.1.min.js"></script>
	<script>
    
   </script>

  <#include "skysail.server:navigation.ftl">
  <div id="container">
    <#assign info = "List of all available Services" />
    <#include "skysail.server.osgi.bundles:title.ftl">
	
	<div id="fsi"><h2>
        	<span>&nbsp;&nbsp;total of ${totalResults} hits for your query, showing page ${page}</span></h2>
    </div>

	<#list data as component>
	  
	  <#if component.class.simpleName == "GridData">
		<#assign gridColumns = component.columns>
		<#assign request = origRequest?split("?")[0] />
        
      <form action="#">
      <table>
        <#include "skysail.server.osgi.bundles:caption.ftl">
        <#include "skysail.server.osgi.bundles:colgroup.ftl">
        <#include "skysail.server.osgi.bundles:thead.ftl">
        <#include "skysail.server.osgi.bundles:tfoot.ftl">

		<tbody>
		
		<#include "skysail.server.osgi.bundles:search.ftl">
		
		<#assign counter = 0 />
	    <#list component.gridData as row>
		  <#assign columns = row.columnData>
		  <#assign counter = counter + 1 />
		  <#if (counter % 2 == 1)>
		    <tr>
		  <#else>
		    <tr class="odd" />
		  </#if>
		    
		  <#list columns as columnData>
		    <#if columnData_index == 0>
			  <!-- ommit id column -->
		    <#elseif columnData_index == 1>
		      <!--<td><img src="${contextPath}static/img/types.gif">&nbsp;<a href='/services/${columns[0]}/'>${columnData?replace(";","<br>\n")}</a></td>-->
		      <td>
		      <#list columnData?split(",") as links>
		        <img src="${contextPath}static/img/service.gif" align="top">&nbsp;<a href="${columns[0]}/">${links}</a></br>
		      </#list>
		      </td>
			  <#elseif columnData_index == 2>
		        <td><img src="${contextPath}static/img/bundle.gif" align="top">&nbsp;${columnData}</td>
			  <#elseif columnData_index == 3>
			  <#elseif columnData_index == 4>
   		        <td>${columnData?replace(";","<br>\n")}</td>
			  <#else>
				
			  </#if>
			</#list>
			<td>to be done</td>
		  </tr>  
		</#list>
		</tbody>
		</table>  
		</form>
	  </#if>  
	
	</#list>
	
	
	<#include "skysail.server:debug.ftl">
    
	
	
  </div>

</body>
</html>