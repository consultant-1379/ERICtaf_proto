define([
    "jscore/core",
    "template!./JobChart.html",
    "styles!./JobChart.less"
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