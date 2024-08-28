/*global define*/
define([
    'jscore/core',
    'template!./_serversList.html'
], function (core, template) {
    'use strict';

    return core.View.extend({

        getTemplate: function () {
            return template({template: this.options.template});
        }

    });

});