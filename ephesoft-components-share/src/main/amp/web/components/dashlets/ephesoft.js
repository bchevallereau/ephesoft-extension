/**
 * Ephesoft dashlet component.
 *
 * @namespace Alfresco.dashlet
 * @class Alfresco.dashlet.Ephesoft
 */
(function()
{
   /**
    * YUI Library aliases
    */
   var Dom = YAHOO.util.Dom,
      Event = YAHOO.util.Event;

   /**
    * Alfresco Slingshot aliases
    */
   var $html = Alfresco.util.encodeHTML;

   /**
    * SimpleDocList constructor.
    *
    * @param {String} htmlid The HTML id of the parent element
    * @return {Alfresco.dashlet.Ephesoft} The new SimpleDocList instance
    * @constructor
    */
   Alfresco.dashlet.Ephesoft = function Ephesoft_constructor(htmlId)
   {
	   Alfresco.dashlet.Ephesoft.superclass.constructor.call(this, "Alfresco.dashlet.Ephesoft", htmlId, ["button", "container", "animation","progressbar"]);

      return this;
   };
   
   YAHOO.extend(Alfresco.dashlet.Ephesoft, Alfresco.component.Base,
   {
	   /**
	    * Variable to do not send request if Share is already updating.
	    * @type boolean
	    */
	   alreadyUpdating: false,
	   
	   /**
	    * Variable to indicate that Ephesoft is not reachable.
	    * @type boolean
	    */
	   ephesoftReachable: true,
	   
      /**
       * Fired by YUI when parent element is available for scripting
       *
       * @method onReady
       */
      onReady: function Ephesoft_onReady()
      {
    	  var me = this;
    	  if (this.ephesoftReachable) {
    		  me.reloadDataTable();
    		  setTimeout(function() {
    			  me.onReady();
    		  }, 2000);
    	  }
      },

      /**
       * Generate base webscript url.
       * Can be overridden.
       *
       * @method getWebscriptUrl
       */
      getWebscriptUrl: function Ephesoft_getWebscriptUrl()
      {
         return Alfresco.constants.PROXY_URI + "bataon/batchInstance/status/active";
      },

      /**
       * Reloads the DataTable
       *
       * @method reloadDataTable
       */
      reloadDataTable: function Ephesoft_reloadDataTable()
      {
    	 if (!this.alreadyUpdating) {
    		 this.alreadyUpdating = true;
    		 Alfresco.util.Ajax.request(
    				 {
    					 url: this.getWebscriptUrl(),
    					 successCallback:
    					 {
    						 fn: this.onListLoaded,
    						 scope: this
    					 },
    					 failureCallback:
    					 {
    						 fn: this.onListLoadFailed,
    						 scope: this
    					 },
    					 scope: this
    				 });
    	 }
      },
      
      /**
       * List loaded successfully
       * @method onListLoaded
       * @param p_response {object} Response object from request
       */
      onListLoaded: function Activities_onListLoaded(p_response, p_obj)
      {
    	 var batchInstanceList = Dom.get(this.id + "-batchInstances");
    	 batchInstanceList.innerHTML = "";
    	  
         var html = p_response.serverResponse.responseText;
         if (YAHOO.lang.trim(html).length > 0)
         {
        	 var data = YAHOO.lang.JSON.parse(p_response.serverResponse.responseText)
        	 
        	 for (var i = 0; i < data.items.length; i++) {
        	 	var item = data.items[i];
        	 	batchInstanceList.innerHTML += this.renderBatchInstance(item);
        	 	// Generate progress bar
				var id = this.id + '-batchInstance-' + item.id;
        	 	var pb = new YAHOO.widget.ProgressBar({value: 100  * item.achievedPercentage}).render(id + '-progressbar');
        	 }
         }
         this.alreadyUpdating = false;
      },
      
      /**
       * List loaded unsuccessfully
       * @method onListLoadFailed
       */
      onListLoadFailed: function Activities_onListLoaded()
      {
    	 var batchInstanceList = Dom.get(this.id + "-batchInstances");
    	 batchInstanceList.innerHTML = '<div class="error-message">' + this.msg('ephesoft.error.connexion') + '</div>';
         this.ephesoftReachable = false;
      },
      
      /**
       * Render Batch Instance
       *
       * @method renderBatchInstance
       * @param oData {object}
       */
      renderBatchInstance: function Ephesoft_renderCellDetail(oData)
      {
         var desc = "";
		 var id = this.id + '-batchInstance-' + oData.id;
		 
		 desc += '<div><table width="100%"><tr>';
		 desc += '<td id="' + id + '-statusImage" class="batchInstance-status batchInstance-status-' + oData.status + '"></td>';
		 
		 desc += '<td id="' + id + '" class="batchInstance-item">';
         
         desc += '<table width="100%"><tr>';
         desc += '<td align="left" class="batchInstanceId">' + oData.id + '</td>';
         desc += '<td align="right" class="field-value">' + oData.status + '</td>';         
         desc += '</tr></table>';
         
         desc += '<div id="' + id + '-progressbar" class="progressbar-' + oData.status + '"></div>';

         desc += '<span class="field-label">' + this.msg('ephesoft.field.batchClass') + ':</span><span class="field-value"> ' + (oData.batchClassDescription == null ? 'N/A' : oData.batchClassDescription) + '</span> - ';
         desc += '<span class="field-label">' + this.msg('ephesoft.field.reviewer') + ':</span><span class="field-value"> ' + (oData.reviewer == null ? 'N/A' : oData.reviewer) + '</span> - ';
         desc += '<span class="field-label">' + this.msg('ephesoft.field.validator') + ':</span><span class="field-value"> ' + (oData.validator == null ? 'N/A' : oData.validator) + '</span>';
        
         desc += '</td>';
         
         desc += '<td id="' + id + '-actions" class="batchInstance-actions">';
         
         if (oData.reviewUrl != null)
        	 desc += '<a href="' + oData.reviewUrl + '" target="_blank"><div class="reviewUrl"></div></a>';
         
         desc += '</td>';
		 
         desc += '</tr></table></div>';
         
         return desc;
      }

   });
})();