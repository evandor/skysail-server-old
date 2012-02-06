
        <#assign foo = gridColumns />
        <#assign visibleColumnCount = 1 />
        <thead>
          <tr>
          <#list gridColumns?values as value>
              <#-- ignore columns with width <= 0, but show column if width is not defined -->
              <#if (!value.width?? || value.width > 0) >
                <#assign visibleColumnCount = visibleColumnCount + 1 />
                <th scope="col">${value.name}</th>
              <#else>
              </#if><#-- avoiding whitespaces -->
          </#list>
          <th scope="col">Action</th>
          </tr>
        </thead>
