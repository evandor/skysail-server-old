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

	<script src="/static/js/jquery-1.7.1.min.js"></script>
	<script>
    
   </script>

  <#include "skysail.server.restletosgi:header.ftl">
  <div id="container">
    <#assign info = "List of all available Services" />
    <#include "skysail.server.osgi.bundles:title.ftl">
	
	<div id="fsi"><h2>
        	<span>&nbsp;&nbsp;total of ${totalResults} hits for your query, showing page ${page}</span></h2>
    </div>

	<#include "skysail.server.restletosgi:debug.ftl">
	
	<#list data as component>
	  
	  <#if component.class.simpleName == "GridData">
		<#assign gridColumns = component.columns>
		<form action="#">
		<table>

		<caption>
		  [ &lt; 1 2 3 4 &gth; ] xml json
		  <#assign request = origRequest?split("?")[0] />
			[<a href="${request}?media=xml">as XML</a>]
			[<a href="${request}?media=json">as JSON</a>]
			<!--[<a href="${request}?media=text">as Text</a>]-->
		  <#if debug>
			 [<a href="${origRequest}?replace("&debug","").replace("?debug","")>debug off</a>]
		  <#else>
			  <#if origRequest?contains("?")>
			    [<a href="${origRequest}&debug">debug</a>]
			  <#else>
			    [<a href="${origRequest}?debug">debug</a>]
			  </#if>
		  </#if>
		</caption>

		<thead>
		  <tr>
			<th scope="col" width="400px">Service Name</th>  
			<th scope="col" width="200px">defined in Bundle</th>  
			<th scope="col" width="400px">used in Bundle(s)</th>
			<th scope="col" width="100px">Actions</th>  
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
		  <td>
			<input type="text" name="serviceName" value='<#if gridColumns["serviceName"]??>${gridColumns["serviceName"].filterValue}</#if>' />
		  </td>
		  <td>
			<input type="text" name="f_ImplementingBundle" value='<#if gridColumns["implementingBundle"]??>${gridColumns["implementingBundle"].filterValue}</#if>' />
		  </td>  
		  <td>
			<input type="text" name="f_UsingBundle" value='<#if gridColumns["serviceName"]??>${gridColumns["serviceName"].filterValue}</#if>' />
		  </td>  
		   <td>
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
				  <!-- ommit id column -->
		      <#elseif columnData_index == 1>
		        <!--<td><img src="/static/img/types.gif">&nbsp;<a href='/services/${columns[0]}/'>${columnData?replace(";","<br>\n")}</a></td>-->
		        <td>
		        <#list columnData?split(",") as links>
		          <img src="/static/img/types.gif">&nbsp;<a href="/services/${links}/">${links}</a></br>
		        </#list>
				</td>
			  <#elseif columnData_index == 2>
		        <td><img src="/static/img/bundle.gif">&nbsp;${columnData}</td>
			  <#elseif columnData_index == 3>
			  <#elseif columnData_index == 4>
   		        <td>${columnData?replace(";","<br>\n")}</td>
			  <#else>
				
			  </#if>
			</#list>
			<td>hi</td>
		  </tr>  
		</#list>
		
		</table>  
		</form>
	  </#if>  
	
	</#list>
  </div>

</body>
</html>