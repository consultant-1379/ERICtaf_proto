define([
    'jscore/core',
    'text!./Subscription.html',
    'styles!./Subscription.less'
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