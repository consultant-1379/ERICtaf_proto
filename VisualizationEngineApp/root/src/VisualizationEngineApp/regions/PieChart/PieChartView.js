define([
    "jscore/core",
    "template!./PieChart.html",
    "styles!./PieChart.less"
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