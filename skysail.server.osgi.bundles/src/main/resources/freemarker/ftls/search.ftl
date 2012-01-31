
		<tr>
		  <#list gridColumns?keys as key>
		  <td class="search">
			<input type="text" name="${key}" value='<#if gridColumns[key]??>${gridColumns[key].filterValue}</#if>' />
		  </td>
		  </#list>
		  <td class="search">
			<input type="submit" value="Search"/>
		  </td>  
		</tr>
