
        <#assign foo = gridColumns />
        <#assign visibleColumnCount = 1 />
        
        <thead>
          <tr>
          <#list gridColumns?values as value>
          
              <#assign sorting = "" />
              <#if (value.sorting?? && value.sorting > 0) >
                <#assign sorting = "<img src='"+ contextPath + "static/img/down.gif' title='" + value.sorting +"'>" />
              <#elseif (value.sorting?? && value.sorting < 0) >
                <#assign sorting = "<img src='"+ contextPath + "static/img/up.gif' title='" + value.sorting +"'>" />
              </#if>
              <#-- ignore columns with width <= 0, but show column if width is not defined -->
              <#if (!value.width?? || value.width > 0) >
                <#assign visibleColumnCount = visibleColumnCount + 1 />
                <#if (value.sorting??) >
                  <th scope="col"><a href='${origRequest}${sortingRepresentation}&toggleSorting=${value.name}'>${value.name} ${sorting}</a></th>
                <#else>
                  <th scope="col">${value.name} ${sorting}</th>
                </#if>
              <#else>
              </#if><#-- avoiding whitespaces -->
          </#list>
          <th scope="col">Action</th>
          </tr>
        </thead>
