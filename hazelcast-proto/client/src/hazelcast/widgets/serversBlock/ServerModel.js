/*global define*/
define([
    'jscore/ext/mvp'
], function (mvp) {
    'use strict';

    return mvp.Model.extend({

        getName: function() {
            return this.getAttribute('name');
        }

    });

});