define([
    'jscore/core',
    'template!./TreeChart.html',
    'text!./TreeChart.css'
], function (core, template, style) {

    return core.View.extend({

        getTemplate: function () {
            return template(this.options.presenter.getData());
        },

        getStyle: function () {
            return style;
        }
    });
});
