define([
    'require',
    'chartlib/base/d3',
    'chartlib/widgets/PieChart'
], function (require, d3) {

    var Drawing = require('chartlib/widgets/ext/PieChartExt');

    return Drawing.extend({
        
        text: function(data) {
            this.slices.selectAll('text').remove();
            
            this.slices.data(this.pie(data));
            this.slices.append('text')
                .attr('transform', function (d) {
                    d.innerRadius = this.settings.ir;
                    d.outerRadius = this.settings.or;
                    return 'translate(' + this.arc.centroid(d) + ')';
                }.bind(this))
                .attr('class', 'text')
                .attr('text-anchor', 'middle')
                .attr('font-size', '1.2em')
                .text(function(d) {
                    return d.data.value;
                });
        },
        
        addEventHandler: function(event, callback, context) {
            var ctx = context || null;
            
            this.chart.selectAll('g.slice')
                .on(event, function() {
                    callback.call(ctx, this.dataset, d3.event);
                }.bind(this));
        }
    });
});