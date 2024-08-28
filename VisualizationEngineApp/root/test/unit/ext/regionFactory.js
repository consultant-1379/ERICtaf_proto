/*global define, describe, it, expect */
define([
    'app/ext/regionFactory',
    'app/regions/EventList/EventList',
    'app/regions/PieChart/PieChart',
    'app/regions/FlowChart/FlowChart',
    'app/models/EventCollection'
], function (regionFactory, EventList, PieRegion, FlowRegion, EventCollection) {
    'use strict';

    describe('regionFactory', function () {

        describe('Methods', function () {
            
            it('create(type: String, options: Object)', function () { 
                var region;
                   
                expect(regionFactory.create.bind(null, 'blah', {})).to.throw(Error);
            
                region = regionFactory.create('listRegion', {context: null, collections: {eventCollection: {addEventHandler: function() {}}}, title: 'ListView'});
                expect(region.options.title).to.equal('ListView');

                region = regionFactory.create('pieRegion', {context: null, collections: {eventCollection: {addEventHandler: function() {}}}, title: 'PieView'});
                expect(region.options.title).to.equal('PieView');
                
                region = regionFactory.create('flowRegion', {context: null, collections: {eventCollection: {addEventHandler: function() {}}}, title: 'FlowView'});
                expect(region.options.title).to.equal('FlowView');
            });  
        });
    });
});