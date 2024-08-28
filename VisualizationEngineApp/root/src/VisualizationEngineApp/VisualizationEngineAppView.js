define([
    'jscore/core',
    'text!./VisualizationEngineApp.html',
    'styles!./VisualizationEngineApp.less'
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
