/*global define*/
define([
    'jscore/core',
    'text!./_runBlock.html',
    'styles!./_runBlock.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            var element = this.getElement();
            this.runnerSelect = element.find('.eaHazelCast-RunBlock-runnerSelect');
            this.testwareInput = element.find('.eaHazelCast-RunBlock-testwareInput');

            this.nameInput = element.find('.eaHazelCast-RunBlock-nameInput');
            this.repeatCountInput = element.find('.eaHazelCast-RunBlock-repeatCountInput');
            this.vUsersInput = element.find('.eaHazelCast-RunBlock-vUsersInput');
            this.fromDateInput = element.find('.eaHazelCast-RunBlock-fromDateInput');
            this.toDateInput = element.find('.eaHazelCast-RunBlock-toDateInput');

            this.key1Input = element.find('.eaHazelCast-RunBlock-key1Input');
            this.value1Input = element.find('.eaHazelCast-RunBlock-value1Input');
            this.key2Input = element.find('.eaHazelCast-RunBlock-key2Input');
            this.value2Input = element.find('.eaHazelCast-RunBlock-value2Input');
            this.key3Input = element.find('.eaHazelCast-RunBlock-key3Input');
            this.value3Input = element.find('.eaHazelCast-RunBlock-value3Input');

            this.deleteBtn = element.find('.eaHazelCast-RunBlock-deleteBtn');
            this.saveBtn = element.find('.eaHazelCast-RunBlock-saveBtn');
            this.runBtn = element.find('.eaHazelCast-RunBlock-runBtn');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getRunnerSelect: function () {
            return this.runnerSelect;
        },

        getTestwareInput: function () {
            return this.testwareInput;
        },

        getNameInput: function () {
            return this.nameInput;
        },

        getRepeatCountInput: function () {
            return this.repeatCountInput;
        },

        getVUsersInput: function () {
            return this.vUsersInput;
        },

        getFromDateInput: function () {
            return this.fromDateInput;
        },

        getToDateInput: function () {
            return this.toDateInput;
        },

        getKey1Input: function () {
            return this.key1Input;
        },

        getValue1Input: function () {
            return this.value1Input;
        },

        getKey2Input: function () {
            return this.key2Input;
        },

        getValue2Input: function () {
            return this.value2Input;
        },

        getKey3Input: function () {
            return this.key3Input;
        },

        getValue3Input: function () {
            return this.value3Input;
        },

        getDeleteButton: function () {
            return this.deleteBtn;
        },

        getSaveButton: function () {
            return this.saveBtn;
        },

        getRunButton: function () {
            return this.runBtn;
        }

    });

});