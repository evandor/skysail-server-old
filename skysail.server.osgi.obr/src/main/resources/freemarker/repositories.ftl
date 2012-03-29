<#include "skysail.server.osgi.obr:head.ftl">

<body>

  <#include "skysail.server:navigation.ftl">
  <div id="container">
    <#assign info = "List of all available Services (found ${totalResults})" />
    <#include "skysail.server.osgi.obr:title.ftl">
    
      <#if data.class.simpleName == "GridData">
        <#assign gridColumns = data.columns>
        
      <form action="#">
      <table>
        <#include "skysail.server:grid/caption.ftl">
        <#include "skysail.server:grid/colgroup.ftl">
        <#include "skysail.server:grid/thead.ftl">
        <#include "skysail.server:grid/tfoot.ftl">

        <tbody>

        <#include "skysail.server.osgi.obr:search.ftl">

        <#assign counter = 0 />
        <#list data.gridData as row>
          <#assign columns = row.columnData>
          <#assign counter = counter + 1 />
          <#if (counter % 2 == 1)>
            <tr>
          <#else>
            <tr class="odd" />
          </#if>
            
          <#list columns as columnData>
            <td>${columnData}</td>
          </#list>
          </tr>  
        </#list>
        </tbody>
        </table>  
        </form>
      </#if>  
    
   
    
    
    <#include "skysail.server:debug.ftl">
  </div>

</body>
</html>