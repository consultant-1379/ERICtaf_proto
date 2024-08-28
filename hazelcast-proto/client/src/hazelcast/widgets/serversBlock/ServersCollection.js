/*global define*/
define([
    'jscore/ext/mvp',
    './ServerModel'
], function(mvp, ServerModel) {
    'use strict';

    return mvp.Collection.extend({

        Model: ServerModel,

        url: 'api/cluster',

        init: function(options) {

        }

    });

});