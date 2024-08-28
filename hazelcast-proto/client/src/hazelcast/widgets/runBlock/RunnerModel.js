/*global define*/
define([
    'jscore/ext/mvp'
], function (mvp) {

    'use strict';

    return mvp.Model.extend({

        url: 'api/test',

        getTestware: function() {
            return this.getAttribute('testware');
        }

    });

});