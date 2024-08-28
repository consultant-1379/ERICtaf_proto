define([
    'jscore/core',
    'template!./Resizing.html',
    'styles!./Resizing.less'
], function (core, template, style) {

    return core.View.extend({
        getTemplate: function () {
            return template(this.options.presenter.getViewSettings());
        },

        getStyle: function () {
            return style;
        }
    });
});