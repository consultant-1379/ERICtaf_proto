define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    './JobChartView',
    'app/widgets/JobChart/JobChart',
    'app/regions/BaseRegion/BaseRegion'
], function (core, _, View, JobChart, BaseRegion) {

    return BaseRegion.extend({
        
        View: View,
        
        init: function () {
            this.eventCollection = this.options.collection;
        },    
		/*
		afterOnStartMethod will initialise all job chart functions
		*/
		afterOnStart: function () {
			this.getRegionHandler().addEventHandler('changeAspectRatioEvent', function (e) {			
                this.jobChart.setAspectRatio(e);
            }, this);
			
			 this.getRegionHandler().addEventHandler('DefaultSettingsEvent', function (e) {			
                this.jobChart.setDefaultValues();
            }, this);
            
            this.getRegionHandler().addEventHandler('changeColumnSize', function (e) {
                this.jobChart.changeColumns(e);
            }, this);
			
			this.getRegionHandler().addEventHandler('changeSizeEvent', function (e) {
                this.jobChart.redraw();
            }, this);
            
            this.getRegionHandler().addEventHandler('clusterBaseEvent', function (clusterBase) {
            	this.jobChart.clearActiveData();
            	this.jobChart.setClusterBase(clusterBase);
            	this.jobChart.selectFromEventCollection();
            }, this);
            

            this.getRegionHandler().addEventHandler('changeFadeoutEvent', function (e) {
                this.jobChart.setFadeout(e*60*60);
            }, this);
            
            //create the flow chart
            this.jobChart = new JobChart({eventCollection: this.eventCollection});
            this.jobChart.attachTo(this.getElement());

            if(typeof this.resizable === 'function') {
                this.resizable(this.getElement());
            }
            
            this.jobChart.addEventHandler('onLoad', function (e) {
                this.resizeChart();
            }, this);
		},

        getData: function() {
            return {
                uid: this.uid,
                span: 'span' + this.options.span
            };
        },
        
        onNewModel: function (model) {
            // Update chart data when new events are received
            this.jobChart.update(model.attributes);
            
        },
        
        resizeChart: function() {
            if (this.jobChart.firstRun === false) {
                this.jobChart.redraw();
            }
            

                
        }
    });
});