define(function() {
	'use strict';
	
	var bindEvent = function(event, callback, context) {
		context.draggableElement.addEventHandler(event, function(e) {
			callback.call(context, e);
		});			
	};
	
	return {
		initDrag: function(el) {
			this.draggableElement = el;
						
			el.setAttribute('draggable', 'true');
			// This if-statement is only needed as long as IE9 support is necessary
			if (typeof el.element.dragDrop !== "undefined") {
				bindEvent('selectstart', function() {
                    el.element.dragDrop();
					return false;
				}, this);	
			}
		},
		
		bindDragStartEvent: function(callback) {
			bindEvent('dragstart', callback, this);
		},
		
		bindDragEndEvent: function(callback) {
			bindEvent('dragend', callback, this);			
		} 
	};
});
