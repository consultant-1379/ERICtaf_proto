define([
    './EventListView',
    'app/regions/BaseRegion/BaseRegion'
], function (View, BaseRegion) {
    // List Region is meant to list all the eventTypes that are sent on the MB
    return BaseRegion.extend({
    
        View: View,
		
		init: function () {
            this.eventCollection = this.options.collection;	
        },
        
        getData: function() {
            return {
                uid: this.uid,
                span: 'span' + this.options.span
            };
        },

        onNewModel: function(model) {		
            this.view.addListItem(model);		
        }
    });
});
