define([
    'jscore/core',
    'template!./RegionHandler.html',
    'text!./RegionHandler.css'
], function(core, template, style) {
    'use strict';
    return core.View.extend({
        getTemplate: function () {
            return template(this.options.presenter.getData());
        },

        getStyle: function() {
            return style;
        }
    });
});