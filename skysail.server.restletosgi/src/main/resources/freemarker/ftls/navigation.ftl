
<table style="border:1px solid black; width:100%">
  <tr>
  	<td style="background-color:#CCDDEE; width:150px">
	  [<a href="javascript:history.back()">&lt;&lt;</a>] [<a href="/">Home</a>] [<a href="javascript:history.forward()">&gt;&gt;</a>]
  	</td>
  	<td style="background-color:#CCDDEE; width:150px">
      <!--[<a href="?method=OPTIONS" target="_blank">Wadl Documentation</a>]-->
  	</td>
  	<td style="background-color:#CCDDEE; width:300px">
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
  	</td>
  	<td style="background-color:#CCDDEE; text-align:right">
		[<a href="http://wiki.twentyeleven.de" target="_blank">Wiki</a>]
		[<a href="http://jira.twentyeleven.de" target="_blank">Jira</a>]
		[<a href="http://hudson.twentyeleven.de" target="_blank">Jenkins</a>]
		[<a href="http://nexus.twentyeleven.de/nexus/index.html" target="_blank">Nexus</a>]
		[<a href="http://sonar.twentyeleven.de" target="_blank">Sonar</a>]
		[<a href="http://orion.twentyeleven.de" target="_blank">Orion</a>]
  	</td>
  </tr>
</table>
