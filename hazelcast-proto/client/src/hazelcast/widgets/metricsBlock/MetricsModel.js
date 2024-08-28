/*global define*/
define([
    'jscore/ext/mvp'
], function (mvp) {
    'use strict';

    return mvp.Model.extend({

        url: 'api/metrics',

        getTimeMin: function() {
            return this.getAttribute('timeMin');
        },

        getTimeMax: function() {
            return this.getAttribute('timeMax');
        },

        getTimeMean: function() {
            return this.getAttribute('timeMean');
        },

        getThroughput: function() {
            return this.getAttribute('throughput');
        },

        getSuccessCount: function() {
            return this.getAttribute('successCount');
        },

        getFailureCount: function() {
            return this.getAttribute('failureCount');
        },

        getSkippedCount: function() {
            return this.getAttribute('skippedCount');
        },

        getSuccessPercent: function() {
            return this.getAttribute('successPercent');
        },

        getFailurePercent: function() {
            return this.getAttribute('failurePercent');
        },

        getSkippedPercent: function() {
            return this.getAttribute('skippedPercent');
        },

        getTotal: function() {
            return this.getAttribute('total');
        }

    });

});