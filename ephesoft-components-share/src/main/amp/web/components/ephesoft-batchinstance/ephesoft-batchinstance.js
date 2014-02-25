/**
 * @class Alfresco.EphesoftBatchInstance
 */
(function() {

	var Dom = YAHOO.util.Dom, Selector = YAHOO.util.Selector, Event = YAHOO.util.Event;

	Alfresco.EphesoftBatchInstance = function(htmlId) {

		Alfresco.EphesoftBatchInstance.superclass.constructor.call(this, "Alfresco.EphesoftBatchInstance", htmlId, [ "button", "menu", "container", "datasource", "datatable", "paginator", "json" ]);

		return this;
	};

	YAHOO.extend(Alfresco.EphesoftBatchInstance, Alfresco.component.Base, {

		// If the zoom is enabled or not
		zoom : false,

		// Save data
		// data: null,

		onReady : function EphesoftBatchInstance_onReady() {
			Alfresco.util.Ajax.request({
				url : Alfresco.constants.URL_RESCONTEXT + "components/ephesoft-batchinstance/samples/data.json",
				successCallback : {
					fn : function(response) {
						var data = JSON.parse(response.serverResponse.responseText);
						// this.data = data;

						// Prepare data for the carousel
						var carouselData = new Array();
						for (var i = 0; i < data.documents.length; i++) {
							var doc = data.documents[i];
							for (var j = 0; j < doc.pages.length; j++) {
								var page = doc.pages[j];
								carouselData.push({
									"content" : "<div class='slide_inner'><a target='_blank' class='photo_link' href='#'><img class='photo' src='" + page.imageUrl + "' alt='" + page.id + "'></a><a target='_blank' class='caption' href='#'>" + doc.documentName + "(" + (j + 1) + "/" + doc.pages.length + ")</a></div>",
									"content_button" : "<div class='thumb ui-draggable ui-sortable-helper'><img src='" + page.thumbnailUrl + "' alt='" + page.id + "'></div><p>" + doc.documentName + "(" + (j + 1) + "/" + doc.pages.length + ")</p>"
								});
							}
						}

						// Display the left panel
						var firstPageNumber = 1;
						for (var i = 0; i < data.documents.length; i++) {
							var doc = data.documents[i];
							this._addDocument(doc, firstPageNumber);
							firstPageNumber += doc.pages.length;
						}

						// Refresh the title
						$(".page-title h1 span").html(data.batchClass);
						$(".page-title h1").append(data.batchID);

						var carousel = $("#pages").agile_carousel({
							// required settings
							carousel_data : carouselData,
							carousel_outer_height : 330,
							carousel_height : 230,
							slide_height : 230,

							// end required settings
							transition_type : "fade",
							transition_time : 600,
							continuous_scrolling : false,
							control_set_1 : "numbered_buttons,previous_button,delete_button,duplicate_button,save_button,zoom_button,next_button",
							change_on_hover : "content_buttons"
						});

						var me = this;

						// Add event on the zoom button
						$(".zoom_button").click(function() {
							me._toggleZoom();
						});

						// Add event on the window resize
						$(window).resize(function() {
							if (me.zoom) {
								me._zoomIn();
							} else {
								me._zoomOut();
							}
						});

						// Fire the resize event on the window
						$(window).trigger("resize");

						// Click on the first document
						$($(".ephesoft-document")[0]).click();

						// Configure the draggable element
						$(".thumbs").sortable({
							revert : false
						});
						$(".thumb").draggable({
							start : function(event, ui) {

							},
							stop : function(event, ui) {
								var thumb = $(event.target);
								thumb.attr('style', 'position:relative;');
							},
							helper : "original"
						});
						$(".thumbs").droppable({
							drop : function(event, ui) {
								$(this).append(ui.draggable);
								$(ui.draggable).removeClass("selected");
							}
						});
						$(".thumb").disableSelection();

						$.fn.editable.defaults.mode = 'inline';
						$('.ephesoft-document-type').editable({
							value : 2,
							source : [ {
								value : 1,
								text : 'I9 Form'
							}, {
								value : 2,
								text : 'eVerify'
							}, {
								value : 3,
								text : 'Supporting Document'
							} ]
						});

					},
					scope : this
				},
				failureMessage : this.msg("message.failloading")
			});
		},

		_addDocument : function EphesoftBatchInstance__addDocument(doc, firstPageNumber) {
			$("#documents").append(this._generateDocument(doc, firstPageNumber));

			$("#documents .ephesoft-document").click(function() {
				if (!$(this).hasClass("selected")) {
					$("#documents .ephesoft-document[id!='" + this.id + "']").each(function() {
						$(this).removeClass("selected");
					});
					// $("#documents .ephesoft-document[id!='" + this.id + "']
					// .thumbs").hide(400);
					$(this).addClass("selected");
					$(this).find(".thumbs").show(400);
					// Click on the first thumb
					$($(this).find(".thumb")[0]).click();
				}
			});

			$("#documents .ephesoft-document .thumb").click(function() {
				if (!$(this).hasClass("selected")) {
					$(".thumb[id!='" + this.id + "']").each(function() {
						$(this).removeClass("selected");
					});
					$(this).addClass("selected");
					$(".slide_number_" + $(this).attr("pageNumber")).click();
				}
			});
		},

		_generateDocument : function EphesoftBatchInstance__generateDocument(doc, firstPageNumber) {
			var html = "";

			html += '<div id="' + doc.documentName + '" class="ephesoft-document" firstPagId="' + doc.pages[0].id + '" reviewed="' + doc.reviewed + '">';
			html += '<span class="ephesoft-document-name">' + doc.documentName + '</span> | '
			html += '<a href="#" id="status" class="ephesoft-document-type" data-type="select" data-pk="1" data-title="' + doc.documentType + '"></a>';

			html += '<div class="thumbs">';
			for (var i = 0; i < doc.pages.length; i++) {
				var page = doc.pages[i];
				html += "<div class='thumb' id='" + page.id + "' pageNumber='" + (firstPageNumber + i) + "'><img src='" + page.thumbnailUrl + "' alt='" + page.id + "'></div>";
			}
			html += '</div>';

			html += '</div>'

			return html;
		},

		_toggleZoom : function EphesoftBatchInstance_toggleZoom() {
			if (this.zoom) {
				this._zoomOut();
			} else {
				this._zoomIn();
			}
			this.zoom = !(this.zoom);
		},

		_zoomIn : function EphesoftBatchInstance__zoomIn() {
			var maxWidth = $("body").width();
			var maxHeight = $("body").height();
			$(".agile_carousel").height(maxHeight - 165);
			$(".agile_carousel img.photo").width(maxWidth - 375);
			$(".agile_carousel img.photo").height("");
		},

		_zoomOut : function EphesoftBatchInstance__zoomOut() {
			var maxHeight = $("body").height();
			$(".agile_carousel").height(maxHeight - 165);
			$(".agile_carousel img.photo").height(maxHeight - 165);
			$(".agile_carousel img.photo").width("");
		}
	});
})();
