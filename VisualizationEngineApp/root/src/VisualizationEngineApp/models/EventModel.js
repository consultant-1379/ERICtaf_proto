define([
	'jscore/ext/mvp',
	'jscore/ext/utils/base/underscore'
], function (mvp, _) {
	// Represents one event from the message bus. Here it's possible to add functions for manipulating the events.
	return mvp.Model.extend({
		toJSON: function () {
			return _.clone(this.attributes);
		}
	});
});
