/*global define*/
define([
    'jscore/core',
    'jscore/ext/binding',
    'jscore/ext/net',
    './RunBlockView',
    './RunnerModel',
    '../../TimelineItemModel',
    'widgets/SelectBox'
], function (core, binding, net, View, RunnerModel, TimelineItemModel, SelectBox) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true */

        View: View,

        init: function () {
            this.currentModel = new TimelineItemModel();
        },

        onViewReady: function () {
            this.view.afterRender();

            this.runnerBox = new SelectBox({
                items: [
                    {name: 'Class',  value: 'class', title: 'Class'},
                    {name: 'Main',   value: 'main', title: 'Main'},
                    {name: 'TestNG', value: 'testNG', title: 'TestNG'}
                ],
                modifiers: [
                    {name: 'width', value: 'small'},
                    {name: 'wMargin', prefix: 'eb'}
                ]
            });
            this.runnerBox.attachTo(this.view.getRunnerSelect());

            this.model = new RunnerModel();

            binding.bind(this.model, 'testware', this.view.getTestwareInput(), 'value');
            this.runnerBox.addEventHandler('change', function () {
                var runnerBoxValue = this.runnerBox.getValue();
                if (runnerBoxValue) {
                    this.model.setAttribute('runner', runnerBoxValue.value);
                }
            }, this);

            bindModel.call(this);

            this.view.getDeleteButton().addEventHandler('click', this.onDeleteButtonClick, this);
            this.view.getSaveButton().addEventHandler('click', this.onSaveButtonClick, this);
            this.view.getRunButton().addEventHandler('click', this.onRunButtonClick, this);

            this.addEventHandler('showModel', this.onShowModel, this);
            this.addEventHandler('clearFields', this.onClearFields, this);
        },

        onDeleteButtonClick: function () {
            this.trigger('deleteModel', this.selectedModel);
            this.onClearFields();
        },

        onSaveButtonClick: function () {
            if (this.selectedModel) {
                // update model
                this.selectedModel.setAttribute(this.currentModel.toJSON());
                this.trigger('saveModel', this.selectedModel);
            } else {
                // add new item
                this.trigger('addItem', this.currentModel.toJSON());
            }
        },

        onRunButtonClick: function () {
            this.model.setAttribute('testSteps', this.options.timelineCollection);
            this.model.save({
                success: function(data) {
                    console.log(data);
                },
                error: function(msg) {
                    console.error("Error: " + msg);
                }
            });

//            var data = this.model.toJSON();
//            data.testSteps = this.options.timelineCollection.toJSON();
//
//            console.log('clicked', this.model, data);
//
//            net.ajax({
//                type: 'post',
//                url: '/api/test',
//                dataType: 'json',
//                data: data,
//                success: function(data) {
//                    console.log(data);
//                },
//                error: function(msg) {
//                    console.error("Error: " + msg);
//                }
//            });
        },

        onShowModel: function (model) {
            this.onClearFields();
            this.selectedModel = model;
            this.currentModel.setAttribute(model.toJSON());

            console.log(model.id);

            this.view.getSaveButton().setText('Save schedule');
            this.view.getDeleteButton().setModifier('show');
        },

        onClearFields: function () {
            if (!this.selectedModel) {
                return;
            }
            this.selectedModel = undefined;

            this.currentModel.setAttribute({
                content: '',
                repeatCount: '',
                vUsers: '',
                from: '',
                start: '',
                until: '',
                end: '',
                key1: '',
                value1: '',
                key2: '',
                value2: '',
                key3: '',
                value3: ''
            });

            this.view.getSaveButton().setText('Add schedule');
            this.view.getDeleteButton().removeModifier('show');
        }

    });

    function bindModel () {
        binding.bind(this.currentModel, 'content', this.view.getNameInput(), 'value');
        binding.bind(this.currentModel, 'repeatCount', this.view.getRepeatCountInput(), 'value');
        binding.bind(this.currentModel, 'vUsers', this.view.getVUsersInput(), 'value');
        binding.bind(this.currentModel, 'from', this.view.getFromDateInput(), 'value');
        binding.bind(this.currentModel, 'until', this.view.getToDateInput(), 'value');

        binding.bind(this.currentModel, 'key1', this.view.getKey1Input(), 'value');
        binding.bind(this.currentModel, 'value1', this.view.getValue1Input(), 'value');

        binding.bind(this.currentModel, 'key2', this.view.getKey2Input(), 'value');
        binding.bind(this.currentModel, 'value2', this.view.getValue2Input(), 'value');

        binding.bind(this.currentModel, 'key3', this.view.getKey3Input(), 'value');
        binding.bind(this.currentModel, 'value3', this.view.getValue3Input(), 'value');
    }

});