define([
    './FlowChartView',
    'app/widgets/FlowChart/FlowChart',
    'app/regions/BaseRegion/BaseRegion'
], function (View, FlowChart, BaseRegion) {

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
            this.flowChart.updateFlowData(model.attributes);
        },

        afterOnStart: function () {
            //create the flow chart
            this.flowChart = new FlowChart({eventCollection: this.eventCollection, aspectRatio: this.aspectRatio});
            this.flowChart.attachTo(this.getElement());
            
            this.flowChart.addEventHandler('onLoad', function (e) {
                this.resizeChart();
            }.bind(this));
			
			this.getRegionHandler().addEventHandler('changeAspectRatioEvent', function (e) {			
                this.flowChart.setAspectRatio(e);
            }, this);
			
			this.getRegionHandler().addEventHandler('DefaultSettingsEvent', function (e) {			
                this.flowChart.setDefaultValues();
            }, this);
			
        },

        resizeChart: function() {
            var width = this.getElement().element.clientWidth;
            this.element.setStyle("height", Math.max(width * this.aspectRatio, 300));
            this.flowChart.tabs.addTab("");                         // do whatever floats your boat
            this.flowChart.tabs.removeLastTab();                         // do whatever floats your boat
        }
    });
});