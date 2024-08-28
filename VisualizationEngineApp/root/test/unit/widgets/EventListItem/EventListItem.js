/*global define, describe, it, expect */
define([
    'app/widgets/EventListItem/EventListItem',
    'app/models/EventModel'
], function (EventListItem, EventModel) {
    'use strict';

    describe("EventListItem", function () {

        describe('Methods', function () {
            
            it('getData()', function () {
                var model = new EventModel({attr1: 'Hello', attr2: 'world', attr3: 22}),
                    json = new EventListItem(model).getData();
                    
                expect(json).to.be.an('object');
                expect(json).to.have.property('attr1', 'Hello');
                expect(json).to.have.property('attr2', 'world');
                expect(json).to.have.property('attr3', 22);
            });
        });
    });
});