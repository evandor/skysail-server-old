<#import "skysail.server:dump.ftl" as dumper>
<#assign foo = data />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Skysail OSGi Bundle Repository</title>
	
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="keywords" content="skysail" />
    <meta name="description" content="skysail" />
    <meta name="robots" content="follow, all" />
    <meta name="language" content="English" />
    <meta http-equiv="content-language" content="en" />
    
    <style type='text/css'  media='all'>
        @import '${contextPath}static/css/osgimonitor.css';
    </style>
    
    <script type="text/javascript">
        var config = {
            contextPath: '/cometd/'
        };
    </script>
    
    <script type="text/javascript" src="${contextPath}static/js/dojo/dojo.js.uncompressed.js"></script>
    <script type="text/javascript" src="${contextPath}static/js/serverload.js"></script>
    <script type="text/javascript" src="${contextPath}static/js/servertime.js"></script>
   
</head>
