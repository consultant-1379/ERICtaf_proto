/*global define, console*/
define([
    'jscore/core',
    'jscore/ext/binding',
    './MetricsBlockView',
    './MetricsModel'
], function (core, binding, View, MetricsModel) {
    'use strict';

    return core.Widget.extend({

        View: View,

        onViewReady: function () {
            this.view.afterRender();

            this.model = new MetricsModel();

            binding.bind(this.model, 'timeMin', this.view.getTimeMin(), 'text');
            binding.bind(this.model, 'timeMax', this.view.getTimeMax(), 'text');
            binding.bind(this.model, 'timeMean', this.view.getTimeMean(), 'text');
            binding.bind(this.model, 'throughput', this.view.getThroughput(), 'text');
            binding.bind(this.model, 'successCount', this.view.getSuccessRate(), 'text');
            binding.bind(this.model, 'failureCount', this.view.getFailureRate(), 'text');
            binding.bind(this.model, 'skippedCount', this.view.getSkippedRate(), 'text');
            binding.bind(this.model, 'total', this.view.getTotal(), 'text');

            this.model.fetch();
            this.model.addEventHandler('change', this.onModelChange, this);
        },

        onModelChange: function () {
            var successPercent = this.model.getSuccessPercent(),
                failurePercent = this.model.getFailurePercent(),
                skippedPercent = this.model.getSkippedPercent();

            appendPercent.call(this, this.view.getSuccessRate(), successPercent);
            appendPercent.call(this, this.view.getFailureRate(), failurePercent);
            appendPercent.call(this, this.view.getSkippedRate(), skippedPercent);
        }

    });

    function appendPercent(field, percent) {
        if (percent !== 'NaN') {
            var oldText = field.getText();
            field.setText(oldText + ' (' + percent + '%)');
        }
    }

});