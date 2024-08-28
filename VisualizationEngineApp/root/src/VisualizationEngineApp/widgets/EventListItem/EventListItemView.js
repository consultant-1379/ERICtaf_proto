define([
	'jscore/core',
	'template!./EventListItem.html'
], function (core, template) {
    'use strict';

    return core.View.extend({
		
        getTemplate: function () {
            return template(this.options.presenter.getData());
        }

    });
});
