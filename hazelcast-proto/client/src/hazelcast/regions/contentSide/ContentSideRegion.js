/*global define*/
define([
    'jscore/core',
    '../../widgets/timelineWidget/Timeline',
    '../../../3pp/dateFormat/date.format' // not AMD module...
], function (core, Timeline) {
    'use strict';

    return core.Region.extend({

        onStart: function () {
            this.timelineCollection = this.options.timelineCollection;

            var currentDate = new Date();

            // Hours & Minutes & Seconds should be 0
            currentDate.setHours(0);
            currentDate.setMinutes(0);
            currentDate.setSeconds(0);
            currentDate.setMilliseconds(0);

            this.timelineWidget = new Timeline({
                options: {
                    'width': 'auto',
                    'height': '300px',
                    'zoomMin': 1000,                            // one second in milliseconds
                    'zoomMax': 1000 * 60 * 60 * 24 * 31,        // one month in milliseconds
                    'showButtonNew': true,
                    'showNavigation': true,
                    'axisOnTop': true,
                    'min': currentDate
                }
            });

            setTimeout(function () {
                this.timelineWidget.attachTo(this.getElement());
                this.timelineWidget.setData(this.timelineCollection.toJSON());
            }.bind(this), 1);

            this.timelineWidget.addEventHandler('saveItem', this.onSaveItem, this);
            this.timelineWidget.addEventHandler('showItem', this.onShowItem, this);
            this.timelineWidget.addEventHandler('itemUnselected', this.onItemUnselected, this);

            this.getContext().eventBus.subscribe('addItem', this.onAddItem, this);
            this.getContext().eventBus.subscribe('saveModel', this.onSaveModel, this);
            this.getContext().eventBus.subscribe('deleteModel', this.onDeleteModel, this);
        },

        onSaveItem: function (item) {
            this.timelineCollection.addModel(item);
            var model = this.timelineCollection.getModel(item.id);
            this.getContext().eventBus.publish('showModel', model);
        },

        onShowItem: function (item) {
            var model = this.timelineCollection.getModel(item.id);
            this.getContext().eventBus.publish('showModel', model);
        },

        onItemUnselected: function () {
            this.getContext().eventBus.publish('itemUnselected');
        },

        onAddItem: function (item) {
            if (!item.content) {
                return;
            }

            if (item.from) {
                item.start = new Date(item.from);
            } else {
                item.start = new Date();
                item.from = getFormattedDate(item.start);
            }

            item.start = item.from ? new Date(item.from) : new Date();

            if (item.until) {
                item.end = new Date(item.until);
            }

            this.timelineWidget.trigger('addItem', item);
        },

        onSaveModel: function (model) {
            var content = model.getAttribute('content'),
                fromDate = model.getAttribute('from'),
                untilDate = model.getAttribute('until');

            if (!content || !fromDate) {
                return;
            }
            model.setAttribute('start', new Date(fromDate));
            model.setAttribute('end', untilDate ? new Date(untilDate) : undefined);

            this.timelineWidget.trigger('changeItem', this.timelineCollection.toJSON());
        },

        onDeleteModel: function (model) {
            this.timelineCollection.removeModel(model);
            this.timelineWidget.setData(this.timelineCollection.toJSON());
        }

    });

    function getFormattedDate(date) {
        return date.format('yyyy/mm/dd HH:MM:ss');
    }

});