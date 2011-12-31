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
    <#assign info = "Use the following links to get information about the running OSGi framework." />
    <#include "skysail.server.osgi.bundles:title.ftl">
	
	<#include "skysail.server.restletosgi:debug.ftl">
	
	<#list data as component>
	
	   <#if component.class.simpleName == "LinkData">
	    <#if component.href?starts_with("http://")>
	  	  &nbsp;-&nbsp;<a href='${component.href}'>${component.value}</a><br>
	    <#else>
		  &nbsp;-&nbsp;<a href='../${component.href}?media=html'>${component.value}</a><br>
		</#if>
	  </#if>  
	</#list>
	
  </div>

</body>
</html>