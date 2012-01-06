<#include "skysail.server.osgi.bundles:head.ftl">
<body>

<#include "skysail.server:navigation.ftl">
  <div id="container">
    <#assign info = "List of all available Services" />
    <#include "skysail.server.osgi.bundles:title.ftl">

  <#list data as component>
  
  	  <#if component.class.simpleName == "GridData">
		<#assign gridColumns = component.columns>
		<#assign request = origRequest?split("?")[0] />
        <form action="#">
		<table>
		<#include "skysail.server.osgi.bundles:caption.ftl">
		<thead>
		  <tr>
			<th scope="col" width="400px">Key</th>  
			<th scope="col" width="200px">Value</th>  
		  </tr>
		</thead>

		<tfoot>
			<tr>
				<th scope="row" colspan="2">&nbsp;</th>
			</tr>
		</tfoot>

		<tbody>
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
			  <th>${columnData}</th>
		    <#elseif columnData_index == 1>
			  <td>${columnData}</td>
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