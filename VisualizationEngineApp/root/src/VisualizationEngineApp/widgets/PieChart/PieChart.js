define([
    'chartlib/widgets/PieChart',
    'app/ext/PieChartExt',
    'jscore/ext/utils/base/underscore'
], function (PieChart, PieChartExt, _) {
    'use strict';
    
    return PieChart.extend({
        
        onViewReady: function () {
            this.pie = new PieChartExt({
                parent: this.getElement(),
                settings: this.options.settings,
                data: this.options.data
            });
            
            if (this.options.onClick !== undefined) {
                this.pie.addEventHandler('click', this.options.onClick.callback, this.options.onClick.context);
            }
            
            // this.pie.text(this.pie.dataset);
        }
        
        // update: function (data) {
            // this.pie.update(data);
            // this.pie.text(data);
        // }
    });
});