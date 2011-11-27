<#import "skysail.server.restletosgi:dump.ftl" as dumper>
<#assign foo = data />

<html>
<head>
	<title>Skysail Server</title>
    <#include "skysail.server.restletosgi:style.ftl">
</head>
<body>

<h1>Skysail RestletOsgi Server - Menu</h1>

<#include "skysail.server.restletosgi:navigation.ftl">

<h2>Message</h2>
${message}

<#include "skysail.server.restletosgi:debug.ftl">

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
    </table>
  </#if> 

</#list>

</body>
</html>