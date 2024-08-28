define([
    'jscore/core',
    'text!./ViewSelect.html',
    'styles!./ViewSelect.less'
], function(core, template, style) {

    return core.View.extend({
        getTemplate: function() {
            return template;
        },

        getStyle: function() {
            return style;
        }
    });
});