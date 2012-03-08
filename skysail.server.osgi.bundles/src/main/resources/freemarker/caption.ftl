        <caption>
          <ul id="pagination-digg">
          <#if 1 == page >
            <li class="previous-off">«Previous</li>
          <#else>
            <li class="previous"><a href="${request}page=${page-1}&${sortingRepresentation}">«Previous</a></li>
          </#if>

          <#assign pages = (totalResults / pageSize)?ceiling />
          <#list 1..pages as i>
              <#if i == page >
                <li class="active">${i}</li>
              <#else>
                <li class="next"><a title="${request}" href="${request}page=${i}&${sortingRepresentation}">${i}</a></li>
              </#if>
          </#list>
            
          <#if pages == page >
            <li class="next-off">Next »</li>
          <#else>
            <li class="next"><a href="${request}page=${page+1}&${sortingRepresentation}">Next »</a></li>
          </#if>
          
          <li class="next-off">&nbsp;</li>

          <li class="next-off"><input type="text" name="pageSize" value="${pageSize}" class="smallInput" /> results per page</li>
        
          <li class="next-off">&nbsp;</li>
        
          <li class="next"><a href="${request}media=xml">as XML</a></li>
          <li class="next"><a href="${request}media=json">as JSON</a></li>
          <!--[<a href="${request}?media=text">as Text</a>]-->
          <#if debug>
               <li class="next"><a href="${request}?replace("&debug","").replace("?debug","")>debug off</a></li>
          <#else>
              <#if origRequest?contains("?")>
                <li class="next"><a href="${request}debug">debug</a></li>
              <#else>
                <li class="next"><a href="${request}debug">debug</a></li>
              </#if>
          </#if>
          </ul> 
        </caption>
