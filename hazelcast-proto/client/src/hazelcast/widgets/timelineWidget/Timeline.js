/*global define, console, prompt*/
define([
    'jscore/core',
    '../../ext/timeline/TimelineAMD'
], function (core, Timeline) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        onViewReady: function () {
            this.addEventHandler('changeItem', this.onDataChange, this);
            this.addEventHandler('addItem', this.onAddItem, this);
        },

        onAttach: function () {
            this.timeline = new Timeline(this.getElement().element);
            this.timeline.draw([], this.options.options);

            this.timeline.addListener('select', this.onSelect.bind(this));
            this.timeline.addListener('add', this.onAdd.bind(this));
        },

        setData: function (data) {
            this.data = data;
            this.timeline.setData(data);

            // Update visible area for items
            this.timeline.setVisibleChartRangeAuto();
        },

        onSelect: function () {
            var rowIndex = getSelectedRow(this.timeline);

            var rowData = this.data[rowIndex];
            if (rowData !== undefined) {
                console.log('item ' + rowIndex + ' selected');
                this.trigger('showItem', rowData);
            } else {
                console.log('no item selected');
                this.trigger('itemUnselected');
            }
        },

        onAddItem: function (item) {
            var data = this.timeline.getData();
            var newIndex = data.push(item);

            this.timeline.setData(data);
            this.timeline.selectItem(newIndex - 1);
            this.timeline.trigger('add');
        },

        onDataChange: function (data) {
            var rowIndex = getSelectedRow(this.timeline);

            this.timeline.setData(data);
            this.timeline.selectItem(rowIndex);

            console.log('item ' + rowIndex + ' data changed');
        },

        onAdd: function () {
            var rowIndex = getSelectedRow(this.timeline);
            console.log('item ' + rowIndex + ' create');

            var rowData = this.data[rowIndex];
            if (rowData !== undefined) {
                rowData.id = getNewId(this.data);
                this.trigger('saveItem', rowData);
            } else {
                // cancel adding the item
                this.timeline.cancelAdd();
            }
        }

    });

    function getSelectedRow(timeline) {
        var row;
        var sel = timeline.getSelection();
        if (sel.length) {
            if (sel[0].row !== undefined) {
                row = sel[0].row;
            }
        }
        return row;
    }

    function getNewId(data) {
        var biggerIndex = 0;
        data.forEach(function (item) {
            if (item.id) {
                biggerIndex = Math.max(biggerIndex, item.id);
            }
        });
        return biggerIndex + 1;
    }

});