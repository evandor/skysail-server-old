<#include "skysail.server.osgi.bundles:head.ftl">
<body>

	<script src="/static/js/jquery-1.7.1.min.js"></script>
	<script>
    
   </script>

  <#include "skysail.server:header.ftl">
  <div id="container">
    <#assign info = "List of all available Services" />
    <#include "skysail.server.osgi.bundles:title.ftl">
	
	<div id="fsi"><h2>
        	<span>&nbsp;&nbsp;total of ${totalResults} hits for your query, showing page ${page}</span></h2>
    </div>
	
	<#include "skysail.server:debug.ftl">
	
	<#list data as component>
	  
	  <#if component.class.simpleName == "GridData">
		<#assign gridColumns = component.columns>
		<#assign request = origRequest?split("?")[0] />
		<form action="#">
		<table>
		<#include "skysail.server.osgi.bundles:caption.ftl">
		<thead>
		  <tr>
			<th scope="col" width="400px">ID</th>  
			<th scope="col" width="200px">Symbolic Name</th>  
			<th scope="col" width="400px">Version</th>
			<th scope="col" width="100px">State</th>  
		  </tr>
		</thead>

		<tfoot>
			<tr>
				<th scope="row">Total</th>
				<td colspan="3">&nbsp;</td>
			</tr>
		</tfoot>


		<tbody>
		<tr>
		  <td class="search" width="50">
			<input type="text" name="id" value='<#if gridColumns["id"]??>${gridColumns["id"].filterValue}</#if>' />
		  </td>
		  <td class="search">
			<input type="text" name="f_ImplementingBundle" value='<#if gridColumns["implementingBundle"]??>${gridColumns["implementingBundle"].filterValue}</#if>' />
		  </td>  
		  <td class="search">
			<input type="text" name="f_UsingBundle" value='<#if gridColumns["serviceName"]??>${gridColumns["serviceName"].filterValue}</#if>' />
		  </td>  
		   <td class="search">
			<input type="submit" value="Search"/>
		  </td>  
		</tr>
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
			  <td>${columnData}</td>
		    <#elseif columnData_index == 1>
			  <td>${columnData}</td>
			<#elseif columnData_index == 2>
		        <td><img src="/static/img/bundle.gif">&nbsp;${columnData}</td>
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
	
	</#list>
  </div>

</body>
</html>