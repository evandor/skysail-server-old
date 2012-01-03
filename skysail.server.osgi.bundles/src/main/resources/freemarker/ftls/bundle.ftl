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


<#include "skysail.server:navigation.ftl">

<h2>Message</h2>
${message}a

<#include "skysail.server:debug.ftl">

<h2>Data</h2>

<#list data as component>
  
  <#if component.class.simpleName == "FormData">
    <#assign fields = component.fields>
    <table>
    <tr>
      <th>Location</th>
      <td>${fields.location.value}</td>
    </tr>
    <tr>
      <th>Symbolic Name</th>
      <td>${fields.symbolicName.value}</td>
    </tr>
    <tr>
      <th>Id</th>
      <td>${fields.id.value}</td>
    </tr>
    <tr>
      <th>Status</th>
      <td>${fields.state.value}</td>
    </tr>
    <tr>
      <th>Version</th>
      <td>${fields.version.value}</td>
    </tr>
    <tr>
      <th>Last Modified</th>
      <td>${fields.lastModified.value}</td>
    </tr>
    <tr>
      <th>&nbsp;</th>
      <td><a href='importedPackages/'>Imported Packages</a></td>
    </tr>
    <tr>
      <th>&nbsp;</th>
      <td><a href='exportedPackages/'>Exported Packages</a></td>
    </tr>
    <tr>
      <th>&nbsp;</th>
      <td><a href='bundleHeader/'>Bundle Header</a></td>
    </tr>
    <tr>
      <th>&nbsp;</th>
      <td><a href='registeredServices/'>Registered Services</a></td>
    </tr>
    <tr>
      <th>&nbsp;</th>
      <td><a href='consumedServices/'>Consumed Services</a></td>
    </tr>
    </table>
  </#if> 

</#list>

</body>
</html>