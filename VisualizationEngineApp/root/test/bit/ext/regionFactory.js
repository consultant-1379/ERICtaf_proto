/*global define, describe, it, expect */
define([
    'app/ext/regionFactory',
    'app/regions/EventList/EventList',
    'app/regions/PieChart/PieChart',
    'app/regions/FlowChart/FlowChart',
	'app/regions/TreeChart/TreeChart'
], function (regionFactory, EventList, PieRegion, FlowRegion, TreeRegion) {
    'use strict';

    describe('regionFactory', function () {
        
        it('should be defined', function () {
            expect(regionFactory).not.to.be.undefined;
        });
            
        it('should create an EventList region', function () {
            var region = regionFactory.create('listRegion', {context: null, collections: {eventCollection: {addEventHandler: function() {}}}});
            
            expect(region).to.be.an.instanceof(EventList);
        });
        
        it('should create a PieChartArea region', function () {
            var region = regionFactory.create('pieRegion', {context: null, collections: {eventCollection: {addEventHandler: function() {}}}});
            
            expect(region).to.be.an.instanceof(PieRegion);
        });       

        it('should create a FlowChartArea region', function () {
            var region = regionFactory.create('flowRegion', {context: null, collections: {eventCollection: {addEventHandler: function() {}}}});
            
            expect(region).to.be.an.instanceof(FlowRegion);
        });

		it('should create a TreeChartArea region', function () {
					var region = regionFactory.create('treeRegion', {context: null, collections: {eventCollection: {addEventHandler: function() {}}}});
					
					expect(region).to.be.an.instanceof(TreeRegion);
		});   		
    });
});