  <div class="header">
    <a class="brightlink" href="/">Home</a>
    <#if origRequest??>
    <#assign link = "" />
    <#assign splits = origRequest?split("/") />
    <#list splits as part>
      <#assign link = link + part + "/"/>
      <#if (part_index < 3)>
      	
      <#elseif (part_index < splits?size - 1)>
        &gt; <a class="brightlink" href="${link}">${part}</a>
      </#if>
    </#list>
    </#if>
  </div>