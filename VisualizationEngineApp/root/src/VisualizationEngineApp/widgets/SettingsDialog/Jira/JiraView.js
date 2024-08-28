define([
    'jscore/core',
    'text!./Jira.html',
    'styles!./Jira.less'
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