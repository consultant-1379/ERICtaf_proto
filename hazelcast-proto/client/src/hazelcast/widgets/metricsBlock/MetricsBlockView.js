/*global define*/
define([
    'jscore/core',
    'text!./_metricsBlock.html',
    'styles!./_metricsBlock.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.timeMin = element.find('.eaHazelCast-MetricsBlock-timeMin');
            this.timeMax = element.find('.eaHazelCast-MetricsBlock-timeMax');
            this.timeMean = element.find('.eaHazelCast-MetricsBlock-timeMean');
            this.throughput = element.find('.eaHazelCast-MetricsBlock-throughput');
            this.successRate = element.find('.eaHazelCast-MetricsBlock-successRate');
            this.failureRate = element.find('.eaHazelCast-MetricsBlock-failureRate');
            this.skippedRate = element.find('.eaHazelCast-MetricsBlock-skippedRate');
            this.total = element.find('.eaHazelCast-MetricsBlock-total');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getTimeMin: function () {
            return this.timeMin;
        },

        getTimeMax: function () {
            return this.timeMax;
        },

        getTimeMean: function () {
            return this.timeMean;
        },

        getThroughput: function () {
            return this.throughput;
        },

        getSuccessRate: function () {
            return this.successRate;
        },

        getFailureRate: function () {
            return this.failureRate;
        },

        getSkippedRate: function () {
            return this.skippedRate;
        },

        getTotal: function () {
            return this.total;
        }

    });

});