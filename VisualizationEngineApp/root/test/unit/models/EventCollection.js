/*global define, describe, it, expect */
define([
	'app/models/EventCollection',
    'app/models/EventModel'
], function (EventCollection, EventModel) {
    'use strict';

    describe('EventCollection', function () {

        describe('Methods', function () {
        	var collection;

        	beforeEach(function () {
        		collection = new EventCollection();
        	});

            it('deleteByDestination(destination: String)', function () {
            	collection.addModel(new EventModel({
            		id: 1,
            		data: 'Test1',
            		destination: ['region1']
            	}));

            	collection.addModel(new EventModel({
            		id: 2,
            		data: 'Test2',
            		destination: ['region1', 'region2']
            	}));

            	collection.deleteByDestination('region1');

            	expect(collection.size()).to.equal(1);
            	expect(collection._collection.get(2).getAttribute('destination').length).to.equal(1);

            	collection.deleteByDestination('region2');
            	expect(collection.size()).to.equal(0);
            });
        });
    });
});