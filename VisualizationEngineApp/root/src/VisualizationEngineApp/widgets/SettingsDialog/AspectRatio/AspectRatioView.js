define([
    'jscore/core',
    'text!./AspectRatio.html',
    'styles!./AspectRatio.less'
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