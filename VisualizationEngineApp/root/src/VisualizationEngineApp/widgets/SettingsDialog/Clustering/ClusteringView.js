define([
    'jscore/core',
    'text!./Clustering.html',
    'styles!./Clustering.css'
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