define([
  'jscore/core',
  'text!./TimeSeriesChart.html',
  'styles!./TimeSeriesChart.less'
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
