/*global define, setTimeout*/
define([
    'jscore/core',
	'./HazelCastView',
    './regions/masterSide/MasterSideRegion',
    './regions/contentSide/ContentSideRegion',
    './TimelineCollection',
    '../3pp/dateFormat/date.format' // not AMD module
], function (core, View, MasterSideRegion, ContentSideRegion, TimelineCollection) {
    'use strict';

	return core.App.extend({

		View: View,

		onStart: function () {
            this.view.afterRender();

            var timelineCollection = this.createCollection();

            this.masterRegion = new MasterSideRegion({
                context: this.getContext(),
                timelineCollection: timelineCollection
            });
            this.masterRegion.start(this.view.getMasterSide());

            this.contentRegion = new ContentSideRegion({
                context: this.getContext(),
                timelineCollection: timelineCollection
            });
            this.contentRegion.start(this.view.getContentSide());
		},

        createCollection: function () {
            var currentDate = new Date();

            // dummy dates
            var firstDate = new Date(),
                lastDay = new Date(),
                startDate = new Date(),
                endDate = new Date();

            firstDate.setHours(10);
            lastDay.setDate(currentDate.getDate() + 10);
            startDate.setDate(currentDate.getDate() + 2);
            endDate.setDate(currentDate.getDate() + 6);

            return new TimelineCollection([
                {'id': 1,
                    'from': getFormattedDate.call(this, startDate),
                    'start': startDate,
                    'until': getFormattedDate.call(this, endDate),
                    'end': endDate,
                    'content': 'Test'}
            ]);
        }

    });

    function getFormattedDate(date) {
        return date.format('yyyy/mm/dd HH:MM:ss');
    }

});


