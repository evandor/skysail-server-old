<html>
<head>
	<title>Skysail Server: Error occured</title>
    <#include "skysail.server.restletosgi:style.ftl">
</head>
<body>

<h1>Skysail RestletOsgi Server - Menu</h1>

<h2>Navigation</h2>
  
[<a href="javascript:history.back()">back</a>] [<a href="/">Home</a>]
[<a href="?method=OPTIONS" target="_blank">Wadl Documentation</a>]
[<a href="http://www.evandor.de:8080" target="_blank">Wiki</a>]
[<a href="http://www.evandor.de:8787" target="_blank">Hudson</a>]
[<a href="http://www.evandor.de:8081/nexus/index.html" target="_blank">Nexus</a>]

<h2>Message</h2>
${message}

<h2>Data</h2>

Error occured:<br><br>
${message}

</body>
</html>