/*global define, describe, it, expect */
define([
    'app/models/EventModel'
], function (EventModel) {
    'use strict';

    describe("EventModel", function () {
        
        it('should be defined', function () {
            expect(EventModel).not.to.be.undefined;
        });
        
        it('should have toJSON property', function () {
            var model = new EventModel();
            expect(model).to.have.property('toJSON');
        });        
    });
});