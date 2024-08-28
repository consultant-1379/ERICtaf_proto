define([
  'jscore/core',
  './TimeSeriesChartView'
], function(core, View) {

  var arrayPush = Array.prototype.push;
  replaceArray = function(before, after) {
    before.length = 0;
    arrayPush.apply(before, after);
  };

  var constant = function(value) {
    return function() {
      return value;
    };
  };

  return core.Widget.extend({

    View: View,

    init: function() {
      this.datapoints = [];
      this.tooltips = [];
      this.options = $.extend(true, {
        grid: {
          borderWidth: 1,
          borderColor: '#999',
          hoverable: true
        },
        series: {
          shadowSize: 0
        },
        xaxis: {
          show: false
        },
        processors: {
          filter: constant(true),
          sortBy: _.identity,
          map: _.identity,
          groupBy: constant(null),
          mapGroup: _.identity,
          reduceLabels: _.identity
        }
      }, this.options);
      this.update = _.debounce(this.update, 100);
    },

    onAttach: function() {
      var self = this;
      var $el = $(this.getElement()._getHTMLElement());
      var tooltip = $el.find('.eaTafDashboard-TimeSeriesChart-tooltip')
        .appendTo('body')
        .hide();
      $el.bind("plothover", function(event, pos, item) {
        tooltip.hide();
        if (item) {
          tooltip
            .text(self.tooltip(item.seriesIndex, item.dataIndex))
            .html(tooltip.text().replace('\n', '<br>'))
            .css({
              left: pos.pageX - 5,
              top: pos.pageY - 30
            })
            .show();
        }
      });
      this.update();
    },

    datapoint: function(dpt) {
      this.datapoints.push(dpt);
      var dpts = _.filter(this.datapoints, this.options.processors.filter);
      replaceArray(this.datapoints, dpts);
    },

    tooltip: function(seriesIndex, dataIndex) {
      var seriesTooltips = this.tooltips[seriesIndex];
      if (_.isArray(seriesTooltips)) {
        return seriesTooltips[dataIndex];
      }
      return null;
    },

    update: function() {
      var self = this;
      var el = this.getElement()._getHTMLElement();
      var index = 0;
      var dpts = _(this.datapoints).chain()
        .sortBy(this.options.processors.sortBy)
        .map(this.options.processors.map)
        .groupBy(this.options.processors.groupBy)
        .value();

      var labels = this.options.labels || _.keys(dpts);
      dpts = _.sortBy(dpts, function(dptGroup, label) {
        return labels.indexOf(label);
      });

      var tooltips = [];
      dpts = _.map(dpts, function(dptGroup, index) {
        tooltips[index] = _.reduce(dptGroup, self.options.processors.reduceLabels, []);
        return {
          data: _.map(dptGroup, self.options.processors.mapGroup),
          label: labels[index]
        };
      });
      this.tooltips = tooltips;
      $.plot(el, dpts, this.options);
    },

  });

});
