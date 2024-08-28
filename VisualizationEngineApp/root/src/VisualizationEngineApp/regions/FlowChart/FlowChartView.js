define([
    "jscore/core",
    "template!./FlowChart.html",
    "styles!./FlowChart.less"
], function(core, template, style) {
    
    return core.View.extend({
        getTemplate: function() {
            return template(this.options.presenter.getData());
        },
        
        getStyle: function() {
            return style;
        }
    });
});