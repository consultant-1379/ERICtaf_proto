
define('text!widgets/Pagination/Pagination.html',[],function () { return '<div style="width:100%">\n\t<ul class="ebPagination">\n\t\t<li class="ebPagination-previous"><a class="ebPagination-previousA" href="#"></a></li>\n\t\t<li>\n\t\t\t<ul class="ebPagination ebPagination-pages"></ul>\n\t\t</li>\n\t\t<li class="ebPagination-next"><a class="ebPagination-nextA" href="#"></a></li>\n\t</ul>\n</div>';});

define('widgets/Pagination/PaginationView',[
	"jscore/core",
	"text!./Pagination.html"
], function(core, template) {

	return core.View.extend({

		getTemplate: function() {
			return template;
		},
		
		getPreviousLi: function() {
			return this.getElement().find(".ebPagination-previous");
		},
		
		getPrevious: function() {
			return this.getElement().find(".ebPagination-previousA");
		},
		getWrapper: function() {
			return this.getElement();
		},
		
		getPages: function() {
			return this.getElement().find(".ebPagination-pages");
		},
		
		getNext: function() {
			return this.getElement().find(".ebPagination-nextA");
		}
	});

});
define('widgets/Pagination/Pagination',[
	"widgets/main",
	"./PaginationView"
], function(core, View) {

	/**
	*	This widget uses the pagination brand asset to create a widget which will produce clickable page numbers which developers can react to.
	*
	*	<strong>Events:</strong>
	*	<ul>
	*		<li>pagechange - Triggers when the highlighed page has changed. Passes the page number as a parameter.</li>
	*	</ul>
	*
	*	<strong>Options:</strong>
	*	<ul>
	*		<li>pages: number of page links to create</li>
	*		<li>selectedPage: the number of the page to be highlighted as selected/li>
	*		<li>onPageChange: Function callback, triggers when a page has been clicked.</li>
	*		<li>url: If string, the href for each page link will be "#&lt;url&gt;/&lt;pageNumber&gt;". If function, a string is expected to be returned (the page number is passed into the function). If no url is specified, then the location will not change.</li>
		</ul>
	*	
	*	@class Pagination
	*/
	var Pagination = core.Widget.extend({
	
		View: View,
		
		/**
		*	Initialises events and creates the links
		*
		*	@method onViewReady
		*	@private
		*/
		onViewReady: function() {
			this.totalPages = this.options.pages || 0;
			this.selectedPage = this.options.selectedPage || 1;
			this.url = this.options.url;
			
			this.view.getPrevious().addEventHandler("click", function(e) {
				this.previousPageClickHandler(e);
			}.bind(this));
			
			this.view.getNext().addEventHandler("click", function(e) {
				this.nextPageClickHandler(e);
			}.bind(this));
			
			setTimeout(function() {
				this.setPage(this.selectedPage);
			}.bind(this), 1);
			
			window.addEventListener("resize", function() {
				this.redraw();
			}.bind(this), false);
			
		},

		/**
		*	Click handler for the previous page button
		*
		*	@method previousPageClickHandler
		*	@private
		*	@param {Event} e
		*/
		previousPageClickHandler: function(e) {
			this.previousPage();
			this.view.getPrevious().setAttribute("href", "#"+this.getUrl(this.selectedPage));
			if (!this.url) {
				e.preventDefault();
			}
		},

		/**
		*	Click handler for the next page button
		*
		*	@method nextPageClickHandler
		*	@private
		*	@param {Event} e
		*/
		nextPageClickHandler: function(e) {
			this.nextPage();
			this.view.getNext().setAttribute("href", "#"+this.getUrl(this.selectedPage));
			if (!this.url) {
				e.preventDefault();
			}
		},

		/**
		*	Click handler for page buttons
		*
		*	@method pageClickHandler
		*	@private
		*	@param {Integer} pageNumber
		*	@param {Event} e
		*/
		pageClickHandler: function(pageNumber, e) {
			this.setPage(pageNumber);
			if (!this.url) {
				e.preventDefault();
			}
		},
		
		/**
		*	Calculates how the widget should be drawn.
		*
		*	@method calculateDrawParameters
		*	@private
		*/
		calculateDrawParameters: function() {
			var visible = this.calculateVisiblePages();
			
			// Don't render any boundaries if the total visible space is greater than the actual number of pages
			if (this.totalPages <= visible) {
				this.boundaryLeft = {
					visible: false
				};
				this.boundaryRight = {
					visible: false
				};
			}
			
			// If the selected page is near the right end
			else if (this.selectedPage > this.totalPages - visible + 2 && this.totalPages - visible !== 0) {
				this.boundaryLeft = {
					index : this.totalPages - visible + 3,
					visible: true
				};
				
				this.boundaryRight = {
					visible: false
				};
			} 
			
			// If the selected page is near the left end
			else if (this.selectedPage < visible - 2) {
				this.boundaryLeft = {
					visible: false
				};
				
				this.boundaryRight = {
					index: visible - 2,
					visible: true
				};
			}
			
			// If the selected page is somewhere in the middle
			else {
				this.boundaryLeft = {
					index : this.selectedPage - Math.ceil(visible / 2) + 3,
					visible: true
				};
				
				this.boundaryRight = {
					index: this.selectedPage + Math.floor(visible / 2) - 2,
					visible: true
				};
			}
			
		},
		
		/**
		*	Calculates how many page links are visible on the screen
		*
		*	@method calculateVisiblePages
		*	@private
		*/
		calculateVisiblePages: function() {
			var computedStyle = window.getComputedStyle(this.view.getPreviousLi()._getHTMLElement());
			var marginLeft = parseInt(computedStyle.getPropertyValue("margin-left"), 10);
			var marginRight = parseInt(computedStyle.getPropertyValue("margin-right"), 10);
			var totalMargin = marginLeft + marginRight;
			var parentWidth = this.view.getWrapper().getProperty("offsetWidth") - totalMargin; // 2px margin
			var pageWidth = this.view.getPrevious().getProperty("offsetWidth") + totalMargin; // 2px margin
			return Math.floor((parentWidth - pageWidth * 2) / pageWidth);
		},
		
		/**
		*	Clears and renders the widget
		*
		*	@method redraw
		*/
		redraw: function() {
			this.calculateDrawParameters();
			this.removePages();

			this.addPage(1, this.selectedPage == 1);
			if (this.boundaryLeft.visible) {
				this.addDots();
			}
			
			var start = this.boundaryLeft.visible? this.boundaryLeft.index : 2;
			var end = this.boundaryRight.visible? this.boundaryRight.index : this.totalPages;
			for (var i = start; i <= end; i++) {
				this.addPage(i, this.selectedPage == i);
			}
			
			if (this.boundaryRight.visible) {
				this.addDots();
				this.addPage(this.options.pages, this.selectedPage == this.totalPages);
			}
		},
		
		/**
		*	Appends a "..." element to the list of pages
		*
		*	@method addDots
		*	@private
		*/
		addDots: function() {
			var dotsElem = core.Element.parse("<li class='ebPagination-entry'><a href='#'>...</a></li>");
			
			dotsElem.children()[0].addEventHandler("click", function(e) {
				e.preventDefault();
			}.bind(this));
			
			this.view.getPages().append(dotsElem);
		},
		
		/**
		*	Selects the previous page and triggers callback
		*
		*	@method previousPage
		*/
		previousPage: function() {
			if (this.selectedPage > 1) {
				this.selectedPage--;
				this.setPage(this.selectedPage);
			}
		},
		
		/**
		*	Selects the next page and triggers callback
		*
		*	@method nextPage
		*/
		nextPage: function() {
			if (this.selectedPage < this.totalPages) {
				this.selectedPage++;
				this.setPage(this.selectedPage);
			}
		},
		
		/**
		*	Adds a page element with the specified page number to the list of pages. If selected is true, it will be highlighted.
		*
		*	@method addPage
		*	@private
		*	@param {Integer} pageNumber
		*	@param {Boolean} selected
		*/
		addPage: function(pageNumber, selected) {
			var pageElem = core.Element.parse("<li class='ebPagination-entry'><a href='#"+this.getUrl(pageNumber)+"'>"+pageNumber+"</a></li>");
			
			pageElem.children()[0].addEventHandler("click", function(e) {
				this.pageClickHandler(pageNumber, e);
			}.bind(this));
			this.view.getPages().append(pageElem);
			

			if (selected) {
				pageElem.setModifier("current");
			}
			
		},
		
		/**
		*	If the url option is a string, it will return <url>/<pageNumber>. If the url is a function, it will pass the page number into that function and expect a string to be returned.
		*
		*	@method getUrl
		*	@private
		*	@param {Integer} pageNumber
		*	@return {String} url
		*/
		getUrl: function(pageNumber) {
			if (this.url && typeof this.url == "string") {
				return this.url + "/" + pageNumber;
			} else if (this.url &&  typeof this.url == "function") {
				return this.url(pageNumber);
			} else {
				return "";
			}
		},
		
		/**
		*	Triggers the callback passing the pageNumber as a parameter
		*
		*	@method triggerCallback
		*	@private
		*	@param {Integer} pageNumber
		*/
		triggerCallback: function(pageNumber) {
			if (this.options.onPageChange) {
				this.options.onPageChange(pageNumber);
			}
			this.trigger("pagechange", pageNumber);
		},
		
		/**
		*	Empties out the list of pages
		*
		*	@method removePages
		*	@private
		*/
		removePages: function() {
			var pages = this.view.getPages().children();
			var len = pages.length;
			while (len--) {
				pages[len].remove();
			}
		},
		
		/**
		*	Selects the page and forces a draw.
		*
		*	@method setPage
		*	@param {Integer} pageNumber
		*/
		setPage: function(pageNumber) {
			if (pageNumber > 0 && pageNumber <= this.totalPages) {
				this.selectedPage = pageNumber;
				this.redraw();	
				this.triggerCallback(pageNumber);
			}
		}

		/**
         * Adds the pagination widget to the parent element
         *
         * @method attachTo
         * @param {Element} parent
         * @example
         *    pagination.attachTo(this.getElement());
         */

        /**
         * Places the detached pagination Widget back into the defined parent element.
         *
         * @method attach
         * @example
         *  pagination.attach();
         */

        /**
         * Removes the pagination Widget from the parent element, but does not destroy the Widget. DOM events will still work when Widget is attached back.
         *
         * @method detach
         * @example
         *  pagination.detach();
         */

        /**
         * Bind an event handling function to an event.
         *
         * @method addEventHandler
         * @param {String} eventName
         * @param {Function} fn
         * @return {String} id
         * @example
         * pagination.addEventHandler("pagechange", function(pageNumber) {
		 *     console.log("Current Page Number: " + pageNumber);
		 * });
         */

        /**
         * Unbind an event handling function from an event.
         *
         * @method removeEventHandler
         * @param {String} eventName
         * @param {String} id

         * @example
         *    pagination.removeEventHandler("pagechange", eventId);
         */

        /**
         * Removes the Widget root Element from the DOM.
         *
         * @method destroy
         *
         * @example
         *    pagination.destroy();
         */
	
	});

	return Pagination;
});
define('widgets/Pagination', ['widgets/Pagination/Pagination'], function (main) { return main; });
