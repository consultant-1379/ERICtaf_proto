define([
    'jscore/core',
    './ExistingSubscriptionRowView'
], function (core, View) {
    'use strict';

    return core.Widget.extend({
        View: View,

        init: function () {
        },

        getData: function () {
            // Return all the models attributes so it can be passed to the template
            return this.options;
        }

    });
});
