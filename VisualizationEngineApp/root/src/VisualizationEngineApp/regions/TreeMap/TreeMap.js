define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    './TreeMapView',
    'app/widgets/TreeMap/TreeMap',
    'app/regions/BaseRegion/BaseRegion',
    'jscore/base/jquery'
], function (core, _, View, TreeMap, BaseRegion, $) {

    return BaseRegion.extend({
        
        View: View,
        
        init: function () {
            this.eventCollection = this.options.collection;
        },
        
        afterOnStart: function () {

            this.getEventBus().subscribe('newJiraData', function(data) {
                if(data.destination === this.uid) {
                    var svgArea = this.getElement().find(".eaVEApp-wTreeMap-svgArea").element;
                    $(svgArea).empty();
                    this.treeMap.reset();
                    this.treeMap.buildTreeMap(data, svgArea);
                }
            }.bind(this));

            this.getRegionHandler().addEventHandler('changeSizeEvent', function (e) {
                this.treeMap.resize(e);
            }, this);
            
            this.treeMap = new TreeMap();
            this.treeMap.attachTo(this.getElement());
        },

        getData: function() {
            return {
                uid: this.uid,
                span: 'span' + this.options.span
            };
        },

        onNewModel: function (model) {
            this.treeMap.handleEvents(model.attributes);
        }

    });
});
