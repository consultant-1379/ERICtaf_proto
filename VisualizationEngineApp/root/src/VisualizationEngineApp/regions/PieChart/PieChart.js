define([
    'jscore/ext/utils/base/underscore',
    './PieChartView',
    'app/widgets/PieChart/PieChart',
    'app/widgets/Popup/Popup',
    'app/regions/BaseRegion/BaseRegion',
    'template!app/widgets/PieChart/PieChartPopup.html'
], function (_, View, PieChart, Popup, BaseRegion, PopupTemplate) {

    return BaseRegion.extend({
        
        View: View,
        
        init: function () {
            this.pieData = [];
            this.pieChart = null;
            this.pieRegionSettings = {
                pieSettings: {
                    color: 'eriRainbow',
                    w: 0,
                    h: 0    
                },
                pieAspect: 0.8
            };
            this.eventCollection = this.options.collection;
			
			this.defaultPieAspect = this.pieRegionSettings.pieAspect;
        },

        afterOnStart: function () {
            this.el = this.getElement().find('.eaVisualizationEngineApp-rPieChartArea-pie');
            this.pieRegionSettings.pieSettings.w = this.el.element.clientWidth;
            this.pieRegionSettings.pieSettings.h = Math.floor(this.pieRegionSettings.pieSettings.w * this.pieRegionSettings.pieAspect);

            // Create a new pie chart with supplied settings and attach it to the this region
            this.pieChart = new PieChart({
                settings: this.pieRegionSettings.pieSettings,
                data: this.pieData,
                onClick: {
                    callback: this.pieClickHandler,
                    context: this
                }
            });
            this.pieChart.attachTo(this.el);
            
            this.getEventBus().subscribe('scroll', function() {
                if (this.popup !== undefined) {
                    this.popup.destroy();
                }
            }, this);
            
            // Resized events are sent both for region and browser resizing
            this.getEventBus().subscribe('resized', this.resizePie, this);	
				
			this.getRegionHandler().addEventHandler('changeAspectRatioEvent', function (e) {			
                this.setAspectRatio(e);
            }, this);
			
			this.getRegionHandler().addEventHandler('DefaultSettingsEvent', function (e) {			
				this.setDefaultValues();
            }, this);
			
        },
        
        getData: function() {
            return {
                uid: this.uid,
                span: 'span' + this.options.span
            };
        },

        onNewModel: function (model) {
            // Update each pie charts data when new events are received
            // In this case only the eventType is passed on as data to the pie chart
						
            var data = _.find(this.pieData, function(item) {return item.label === model.getAttribute('eventType');});
            
            if (data) {
                data.value++;
                this.pieChart.update(this.pieData);
            } else {
                this.pieChart.destroy();
                this.pieData.push({label: model.getAttribute('eventType'), value: 1});
                this.pieChart = new PieChart({
                    settings: this.pieRegionSettings.pieSettings,
                    data: this.pieData,
                    onClick: {
                        callback: this.pieClickHandler,
                        context: this
                    }
                });
                this.pieChart.attachTo(this.el);
            }
            
            // If the popup is visible update it too
            if (this.popup !== undefined) {
                // Add a percentage to a copy of pie data to not cause any side effects
                var dataset = this.pieData;
                addPercentageToDataset(dataset);                
                this.popup.updateContent(PopupTemplate({dataset: dataset}));
            }
        },
        
        resizePie: function() {
            var width = this.el.element.clientWidth;
            this.pieRegionSettings.pieSettings.w = width;
            this.pieRegionSettings.pieSettings.h = Math.floor(width * this.pieRegionSettings.pieAspect);
            this.pieChart.redraw({
                w: this.pieRegionSettings.pieSettings.w,
                h: this.pieRegionSettings.pieSettings.h
            });
        },
		/*
		Method Sets values back to the default value
		*/
		setDefaultValues: function() {
            this.pieRegionSettings.pieAspect = this.defaultPieAspect;
            this.resizePie();
        },
		/*
		Method Sets Aspect Ratio and resizes the chart
		*/
		setAspectRatio: function(aspectRatio) {
            this.pieRegionSettings.pieAspect = (aspectRatio/10);
            this.resizePie();
        },
        
        pieClickHandler: function(dataset, mouse) {
            if (this.popup !== undefined) {
                this.popup.destroy();
            }
            
            addPercentageToDataset(dataset);
            
            this.popup = new Popup({content: PopupTemplate({dataset: dataset}), position: {x: mouse.clientX+15, y: mouse.clientY-5}});
            this.popup.attachTo(this.el);
            this.popup.addEventHandler('close', function() {
                this.popup.destroy();
            }, this);
        }
    });
    
    function addPercentageToDataset(dataset) {
        var sumEvents = _.reduce(dataset, function(sum, item) {
            return sum += item.value;
        }, 0);
        
        _.map(dataset, function(item) {
            item.percent = Math.round((item.value / sumEvents) * 100, -1);
        });
    }
});