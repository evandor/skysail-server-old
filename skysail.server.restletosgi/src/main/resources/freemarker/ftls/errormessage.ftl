<#import "skysail.server:dump.ftl" as dumper>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Skysail Server Errorpage</title>
	
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
  <div id="container">
    <#assign info = "Sorry, there has been an error processing your request ;(" />
    <#include "skysail.server.osgi.bundles:title.ftl">
	
	<div><h2><span>&nbsp;&nbsp;Please make sure to check the server error log as well for more details.</span></h2></div>
  
  <h2>Message</h2>
  <pre>${message}</pre>

  
  </div>
</body>
</html>