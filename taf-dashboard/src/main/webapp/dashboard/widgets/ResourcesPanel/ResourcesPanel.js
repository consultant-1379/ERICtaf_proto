define([
  'jscore/core',
  '../../ext/ServerEvents/ServerEvents',
  '../TimeSeriesChart/TimeSeriesChart',
  './ResourcesPanelView'
], function(core, ServerEvents, TimeSeriesChart, View) {

  var SECOND = 1000;
  var MINUTE = 60 * SECOND;

  var collectKV = function(kvArray) {
    return _.reduce(kvArray, function(result, kvPair) {
      _.each(kvPair, function(value, key) {
        result[key] = value;
      });
      return result;
    }, {});
  };

  return core.Widget.extend({

    View: View,

    init: function(type, unit) {
      this.type = type;
      this.chart = new TimeSeriesChart({
        yaxis: {
          min: 0,
          show: false
        },
        processors: {
          filter: function(dpt, index, dpts) {
            var passed = _.last(dpts).timestamp - dpt.timestamp;
            return passed < 5 * MINUTE;
          },
          sortBy: 'timestamp',
          groupBy: 'metric',
          mapGroup: function(dpt) {
            return [dpt.timestamp, dpt.value];
          },
          reduceLabels: function(labels, dpt, index) {
            labels[index] = moment(dpt.timestamp).format('YYYY-MM-DD HH:mm:ss') +
              '\n' + dpt.metric + ' ' + dpt.value;
            return labels;
          }
        }
      });
    },

    onAttach: function() {
      this.chart.attachTo(this.getElement());
      var self = this;
      ServerEvents.subscribe(function(eventObject) {
        if (eventObject.eventType === 'EiffelMonitoringDataEvent') {
          _.each(eventObject.eventData.data, function(data, type) {
            if (type === self.type) {
              self.monitoringDataEvent(eventObject, data);
            }
          });
        }
      });
    },

    monitoringDataEvent: function(eventObject, data) {
      var self = this;
      var timestamp = new Date(eventObject.eventTime);
      _.each(collectKV(data), function(value, metric) {
        self.chart.datapoint({
          metric: metric,
          value: value,
          timestamp: timestamp
        });
      });
      self.chart.update();
    }

  });

});
