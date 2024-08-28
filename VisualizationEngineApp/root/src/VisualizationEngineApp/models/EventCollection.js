define([
	'jscore/ext/mvp',
	'jscore/ext/utils/base/underscore'
], function (mvp, _) {
	// Holding a collection of events from the message bus. 
	return mvp.Collection.extend({
		init: function () {
		},

		deleteByDestination: function (destination) {
            var modelsToRemove = [];

			this.each(function (item) {
                if (item.getAttribute('destination') !== undefined) {
                    if (_.contains(item.getAttribute('destination'), destination)) {
                        var destinations = item.getAttribute('destination');

                        if (destinations.length == 1) {
                            modelsToRemove.push(item);
                        } else {
                            item.setAttribute('destination', _.without(destinations, destination));
                        }
                    }
                }
            }.bind(this));

            this.removeModel(modelsToRemove, {silent: true});
        }
    });
});
