define([
    'jscore/core',
    'text!./DefaultSettings.html',
    'styles!./DefaultSettings.less'
], function (core, template, style) {

    return core.View.extend({
        getTemplate: function () {
            return template;
        },

        getStyle: function () {
            return style;
        }
    });
});