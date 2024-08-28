/*global define*/
define([
    'jscore/ext/mvp',
    './TimelineItemModel'
], function(mvp, TimelineItemModel) {
    'use strict';

    return mvp.Collection.extend({

        Model: TimelineItemModel

    });

});