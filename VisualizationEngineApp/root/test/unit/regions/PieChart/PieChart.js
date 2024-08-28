/*global define, describe, it, expect */
define([
    'jscore/core',
    'app/regions/PieChart/PieChart',
    'app/models/EventModel'
], function (core, PieChart, EventModel) {
    'use strict';

    describe('PieChart', function () {

        describe('Methods', function () {
            var element = element = core.Element.parse('<div class="eaVisualizationEngineApp-rPieChartArea-pie"></div>'), 
                pieRegion;

            beforeEach(function() {
                pieRegion = new PieChart({context: {eventBus: {subscribe: function(){}}}, collection: {addEventHandler: function() {}}});
                pieRegion.start(element);
            });
            
            afterEach(function() {
                pieRegion.stop();
            });
                
            it('init()', function() {
                expect(pieRegion.pieData.length).to.equal(0);
                expect(pieRegion.pieChart).to.not.equal(null);
                expect(pieRegion.pieRegionSettings).to.not.equal(null);
            });
            
            it('onNewModel(model: Model)', function() {
                pieRegion.onNewModel(new EventModel({eventType: 'MyEvent'}));
                expect(pieRegion.pieData.length).to.equal(1);
                
                pieRegion.onNewModel(new EventModel({eventType: 'MyOtherEvent'}));
                expect(pieRegion.pieData.length).to.equal(2);
                
                pieRegion.onNewModel(new EventModel({eventType: 'MyEvent'}));
                expect(pieRegion.pieData.length).to.equal(2);
                
                pieRegion.onNewModel(new EventModel({eventType: 'MyYetAnotherEvent'}));
                expect(pieRegion.pieData.length).to.equal(3);
            });
            
            it('pieClickHandler(dataset: Array, mouse: Object)', function() {
                var dataset = [{label: 'MyEvent', value: 1}, {label: 'MyOtherEvent', value: 2}],
                    mouse = {clientX: 0, clientY: 0};
                
                pieRegion.pieClickHandler(dataset, mouse);
                
                expect(dataset[0].percent).to.be.defined;
                expect(dataset[1].percent).to.be.defined;
                expect(dataset[0].percent).to.equal(33);
                expect(dataset[1].percent).to.equal(67);
            });
        });
    });
});