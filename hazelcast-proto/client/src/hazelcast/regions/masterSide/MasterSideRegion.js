/*global define*/
define([
    'jscore/core',
    '../../widgets/serversBlock/ServersBlock',
    '../../widgets/runBlock/RunBlock',
    '../../widgets/metricsBlock/MetricsBlock'
], function (core, ServersBlock, RunBlock, MetricsBlock) {
    'use strict';

    return core.Region.extend({

        onStart: function () {
            this.serversBlock = new ServersBlock();
            this.serversBlock.attachTo(this.getElement());

            this.runBlock = new RunBlock({
                context: this.options.context,
                timelineCollection: this.options.timelineCollection
            });
            this.runBlock.attachTo(this.getElement());

            this.metricsBlock = new MetricsBlock();
            this.metricsBlock.attachTo(this.getElement());

            this.getContext().eventBus.subscribe('showModel', this.onShowModel, this);
            this.getContext().eventBus.subscribe('itemUnselected', this.onItemUnselected, this);

            this.runBlock.addEventHandler('deleteModel', this.onDeleteModel, this);
            this.runBlock.addEventHandler('saveModel', this.onSaveModel, this);
            this.runBlock.addEventHandler('addItem', this.onAddItem, this);
        },

        onShowModel: function (model) {
            this.runBlock.trigger('showModel', model);
        },

        onItemUnselected: function () {
            this.runBlock.trigger('clearFields');
        },

        onDeleteModel: function (model) {
            this.getContext().eventBus.publish('deleteModel', model);
        },

        onSaveModel: function (model) {
            this.getContext().eventBus.publish('saveModel', model);
        },

        onAddItem: function (item) {
            this.getContext().eventBus.publish('addItem', item);
        }

    });

});