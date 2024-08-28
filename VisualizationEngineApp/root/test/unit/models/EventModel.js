/*global define, describe, it, expect */
define([
    'app/models/EventModel'
], function (EventModel) {
    'use strict';

    describe("EventModel", function () {
        describe('Methods', function() {
            it('toJSON()', function () {
                var json = new EventModel({attr1: 'Hello', attr2: 'world'}).toJSON();
                
                expect(json).to.be.an('object');
                expect(json).to.have.property('attr1', 'Hello');
                expect(json).to.have.property('attr2', 'world');
            });
        });
    });
});