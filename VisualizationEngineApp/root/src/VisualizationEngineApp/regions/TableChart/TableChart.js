define(['./TableChartView',
    'app/widgets/TableChart/TableChart',
    'app/regions/BaseRegion/BaseRegion'
], function(View, TableChart, BaseRegion) {
    // List Region is meant to list all the eventTypes that are sent on the MB
    return BaseRegion.extend({
        View: View,
        init: function() {
            this.aspectRatio = 1;
            this.eventCollection = this.options.collection;
        },
        onNewModel: function(model) {
            // Update chart data when new events are received				
            this.tableChart.updateTableData(model.attributes);
        },
        afterOnStart: function() {
            //create the flow chart
            this.tableChart = new TableChart({eventCollection: this.eventCollection, aspectRatio: this.aspectRatio});
            this.tableChart.attachTo(this.getElement());

            this.tableChart.addEventHandler('onLoad', function(e) {
                this.resizeChart();
            }.bind(this));

            this.getRegionHandler().addEventHandler('changeAspectRatioEvent', function(e) {
                this.tableChart.setAspectRatio(e);
            }, this);

            this.getRegionHandler().addEventHandler('DefaultSettingsEvent', function(e) {
                this.tableChart.setDefaultValues();
            }, this);

        },
        /*        
         onStart: function() {
         
         var traceListItem = new TableChartItem();
         traceListItem.attachTo(this.getElement());
         
         var regionHandler = new RegionHandler({title: this.options.title,
         span: this.options.span});
         regionHandler.attachTo(this.getElement().find('header'));
         
         regionHandler.addEventHandler('changeSizeEvent', function(e) {
         this.getEventBus().publish('changeSize',
         {region: this, value: e});
         }.bind(this));
         
         regionHandler.addEventHandler('removeRegionEvent', function(e) {
         this.getEventBus().publish('killMe', this);
         }.bind(this));
         
         if (typeof this.resizable === 'function') {
         this.resizable(this.getElement());
         }
         
         // Subscribe to new MessageBus events...
         this.getEventBus().subscribe('addEvent', this.addEvent.bind(this));
         
         },
         */
        // Incoming events..
        addEvent: function(model) {
            this.traceListItem.newEvent(model.attributes);
        },
        resizeChart: function() {
            var width = this.getElement().element.clientWidth;
            this.element.setStyle("height", Math.max(width * this.aspectRatio, 300));
        },
        getData: function() {
            return {
                uid: this.uid,
                span: 'span' + this.options.span
            };
        }
    });
});
