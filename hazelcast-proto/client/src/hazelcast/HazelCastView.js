/*global define*/
define([
    'jscore/core',
	'text!./_hazelcast.html',
	'styles!./_hazelcast.less'
], function (core, template, styles) {
    'use strict';

	return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.contentSide = element.find('.eaHazelCast-ContentSide');
            this.masterSide = element.find('.eaHazelCast-MasterSide');
        },

		getTemplate: function() {
			return template;
		},

		getStyle: function() {
			return styles;
		},

        getMasterSide: function () {
            return this.masterSide;
        },

        getContentSide: function () {
            return this.contentSide;
        }

	});

});
