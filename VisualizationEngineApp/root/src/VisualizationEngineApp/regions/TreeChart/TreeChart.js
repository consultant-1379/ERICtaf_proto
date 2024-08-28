define([
    './TreeChartView',
    'app/widgets/TreeChart/TreeChart',
    'app/regions/BaseRegion/BaseRegion'
], function (View, TreeChart, BaseRegion) {

    return BaseRegion.extend({

        View: View,

        init: function () {
            this.aspectRatio = 1;
            this.eventCollection = this.options.collection;
        },

        getData: function() {
            return {
                uid: this.uid,
                span: 'span' + this.options.span
            };
        },

        onNewModel: function (model) {
            // Update chart data when new events are received				
            this.treeChart.updateTreeData(model.attributes);
        },

        afterOnStart: function () {
            //create the tree chart
            this.treeChart = new TreeChart({eventCollection: this.eventCollection, aspectRatio: this.aspectRatio});
            this.treeChart.attachTo(this.getElement().find(".eaVisualizationEngineApp-rTreeChartArea-treeChart"));
			this.getRegionHandler().addEventHandler('changeSizeEvent', function () {
                this.treeChart.redraw();
            }, this);

        }


    });
});
