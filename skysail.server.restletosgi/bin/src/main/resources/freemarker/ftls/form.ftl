<#import "skysail.server.restletosgi:dump.ftl" as dumper>
<#assign foo = data />

<html>
<head>
	<title>Skysail Server Form</title>
    <#include "skysail.server.restletosgi:style.ftl">
</head>
<body>

<h1>Skysail RestletOsgi Server - Form</h1>

<#include "skysail.server.restletosgi:navigation.ftl">

<h2>Message</h2>
${message}

<h2>Data</h2>
<#assign fields = data[0].fields>
<#assign keys = fields?keys>
<table>
<#list keys as key>
  <tr><td><b>${key}</b>:</td><td>${data[0].fields[key].value}</td></tr> 
</#list>
</table>

<#include "skysail.server.restletosgi:debug.ftl">

</body>
</html>