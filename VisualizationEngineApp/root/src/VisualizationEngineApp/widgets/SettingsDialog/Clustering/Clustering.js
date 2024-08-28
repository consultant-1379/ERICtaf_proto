define([
    'jscore/core',
    './ClusteringView',
    'app/ext/htmlMenu',
    'jscore/ext/utils/base/underscore'
], function (core, View, menu, _) {

    return core.Widget.extend({
    	
        View: View,
        
        init: function () {
             _.extend(this, menu);
        },
        
        onViewReady: function () {
            var menuHTML = this.getElement().find("#menu");
            this.createMenu(menuHTML);
        },
        
        changeClusterBase: function(clusterBase) {
        	this.trigger('clusterBaseEvent', clusterBase);
        }

    });
});