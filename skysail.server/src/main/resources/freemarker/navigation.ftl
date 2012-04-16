  <div class="nav">
    <a class="brightlink" href="/">Home</a>
    <#if request??>
    <#assign link = "" />
    <#assign splits = request?split("/") />
    <#list splits as part>
      <#assign link = link + part + "/"/>
      <#if (part_index < 3)>
      	
      <#elseif (part_index < splits?size - 1)>
        &gt; <a class="brightlink" href="${link}">${part}</a>
      </#if>
    </#list>
    </#if>
  </div>
  
  <div class="navright"><span id="serverload">server load average</span>&nbsp;<span id="servertime">server time</span></div>