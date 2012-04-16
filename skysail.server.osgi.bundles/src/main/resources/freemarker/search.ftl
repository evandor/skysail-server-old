
        <tr>
          <#list gridColumns as value>
          <#-- ignore columns with width <= 0, but show column if width is not defined -->
          <#if (!value.width?? || value.width > 0) >
          <td class="search">
            <input type="text" name="${value.name}" value='<#if value??>${value.filterValue}</#if>' />
          </td>
          </#if>
          </#list>
          <td class="search">
            <input type="submit" value="Search"/>
          </td>  
        </tr>
