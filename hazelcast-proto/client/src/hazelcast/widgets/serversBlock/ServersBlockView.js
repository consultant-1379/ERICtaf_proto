/*global define*/
define([
    'jscore/core',
    'text!./_serversBlock.html',
    'styles!./_serversBlock.less'
], function (core, template, styles) {
    'use strict';

    return core.View.extend({

        afterRender: function () {
            this.serversList = this.getElement().find('.eaHazelCast-Servers-list');
        },

        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return styles;
        },

        getServersList: function () {
            return this.serversList;
        }

    });

});