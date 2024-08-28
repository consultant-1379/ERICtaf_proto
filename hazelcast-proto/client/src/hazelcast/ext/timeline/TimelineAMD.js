/*global define, links*/
define([
    'jscore/core',
    '../../../3pp/timeline/Timeline',
    'styles!../../../3pp/timeline/_timeline.less',
    'styles!../../../3pp/timeline/_template.less'
], function (core, timeline, styles, template) {
    'use strict';

    var LocalView = core.View.extend({
        getStyle: function () {
            return styles + template;
        }
    });

    var localView = new LocalView();
    localView.render();

    // Events should work
    links.Timeline.prototype.addListener = function (event, callback) {
        links.events.addListener(this, event, callback);
    };

    links.Timeline.prototype.removeListener = function (event, callback) {
        links.events.removeListener(this, event, callback);
    };

    return links.Timeline;

});