define([
    'jscore/core',
    'template!./Icon.html',
    'styles!./Icon.less'
], function(core, template, style) {
    'use strict';
    return core.View.extend({
        getTemplate: function () {
            return template(this.options.presenter.getTemplateData());
        },

        getStyle: function() {
            return style;
        }
    });
});
