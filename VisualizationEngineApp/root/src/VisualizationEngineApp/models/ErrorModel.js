define([
	'jscore/ext/mvp',
	'jscore/ext/utils/base/underscore'
], function (mvp, _) {
	
	return mvp.Model.extend({
		toJSON: function () {
			return _.clone(this.attributes);
		}
	});
});
