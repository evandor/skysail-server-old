
        <#assign foo = gridColumns />
        <#assign visibleColumnCount = 1 />
        
        <thead>
          <tr>
          <#assign columnCounter = 0 />
          <#list gridColumns?values as value>
              
              <#-- handle default -->
              <#assign sorting = "" />
              <#if (value.sorting?? && value.sorting > 0) >
                <#assign sorting = "<img src='"+ contextPath + "static/img/down.gif' title='" + value.sorting +"'>" />
              <#elseif (value.sorting?? && value.sorting < 0) >
                <#assign sorting = "<img src='"+ contextPath + "static/img/up.gif' title='" + value.sorting +"'>" />
              </#if>
              
              <#if (sortingRepresentation?length > 3)>
                <#assign column = sortingRepresentation?substring(2,sortingRepresentation?length -1)?split("|") >
                  <#assign sortValue = column[columnCounter]?number />
                  <#if (sortValue?? && sortValue > 0) >
                     <#assign sorting = "<img src='"+ contextPath + "static/img/down.gif' title='[" + sortValue +"]'>" />
                  <#elseif (sortValue?? && sortValue < 0) >
                     <#assign sorting = "<img src='"+ contextPath + "static/img/up.gif' title='[" + sortValue +"]'>" />
                  <#elseif (sortValue?? && sortValue == 0)>
                     <#assign sorting = "" />
                  </#if>
              </#if>
              
              
              <#-- ignore columns with width <= 0, but show column if width is not defined -->
              <#if (!value.width?? || value.width > 0) >
                <#assign visibleColumnCount = visibleColumnCount + 1 />
                <#if (value.sorting??) >
                  <th scope="col"><a href='${request}${sortingRepresentation}toggleSorting=${columnCounter}'>${value.name} ${sorting}</a></th>
                <#else>
                  <th scope="col">${value.name} ${sorting}</th>
                </#if>
              <#else>
              </#if><#-- avoiding whitespaces -->
              <#assign columnCounter = columnCounter + 1 />
          </#list>
          <th scope="col">Action</th>
          </tr>
        </thead>
