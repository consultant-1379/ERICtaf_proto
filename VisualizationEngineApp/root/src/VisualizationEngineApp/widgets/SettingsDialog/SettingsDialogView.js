define([
    'jscore/core',
    'text!./SettingsDialog.html',
    'styles!./SettingsDialog.less'
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