<#include "skysail.server.osgi.bundles:head.ftl">

<body>

  <#include "skysail.server:navigation.ftl">
  <div id="container">
    <#assign info = "List of all available Services (found ${totalResults})" />
    <#include "skysail.server.osgi.bundles:title.ftl">
    
    <#list data as component>
      
      <#if component.class.simpleName == "GridData">
        <#assign gridColumns = component.columns>
        
      <form action="#">
      <table>
        <#include "skysail.server.osgi.bundles:caption.ftl">
        <#include "skysail.server.osgi.bundles:colgroup.ftl">
        <#include "skysail.server.osgi.bundles:thead.ftl">
        <#include "skysail.server.osgi.bundles:tfoot.ftl">

        <tbody>

        <#include "skysail.server.osgi.bundles:search.ftl">

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
              <td>
              <#list columnData?split(",") as links>
                <img src="${contextPath}static/img/service.gif" align="top">&nbsp;<a href="${columns[0]}/">${links}</a></br>
              </#list>
              </td>
              <#elseif columnData_index == 2>
                <td><img src="${contextPath}static/img/bundle.gif" align="top">&nbsp;${columnData}</td>
              <#elseif columnData_index == 3>
                <td>${columnData}</td>
              <#elseif columnData_index == 4>
                   <td>${columnData?replace(";","<br>\n")}</td>
              <#else>
                
              </#if>
            </#list>
            <td>to be done</td>
          </tr>  
        </#list>
        </tbody>
        </table>  
        </form>
      </#if>  
    
    </#list>
    
    
    <#include "skysail.server:debug.ftl">
  </div>

</body>
</html>