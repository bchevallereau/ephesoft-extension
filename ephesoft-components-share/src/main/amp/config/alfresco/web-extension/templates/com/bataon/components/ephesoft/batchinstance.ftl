<#include "/org/alfresco/include/alfresco-template.ftl" />

<@templateHeader />

<@templateBody>
  <div id="alf-hd">
    <@region id="header" scope="global" />
    <@region id="title" scope="template" />
  </div>
  
  <div id="bd">
    
    <div id="content" class="yui-gb">
      <div id="leftPanel">
        <@region id="ephesoft-batchinstance" scope="template" />
      </div>
	</div>
      
  </div>
  
</@>

<@templateFooter>
  <div id="alf-ft">
    <@region id="footer" scope="global" />
  </div>
</@>