<#include "skysail.server.osgi.bundles:head.ftl">
<body>

<#include "skysail.server:navigation.ftl">
  <div id="container">
    <#assign info = "Bundle Details" />
    <#include "skysail.server.osgi.bundles:title.ftl">

 
  
  	  <#if data.class.simpleName == "FormData">
		<#assign entries = data.getFields() />
		
        <form action="#">
		<table>
		
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
	    <#list entries?keys as key>
	      <#assign field = entries[key] />
		  <#assign counter = counter + 1 />
		  <#if (counter % 2 == 1)>
		    <tr>
		  <#else>
		    <tr class="odd" />
		  </#if>
            <td>${key}</td><td>${field}</td>
		  </tr>  
		</#list>
		</tbody>
		</table>  
		</form>
	  </#if>  
 
  </div>

</body>
</html>