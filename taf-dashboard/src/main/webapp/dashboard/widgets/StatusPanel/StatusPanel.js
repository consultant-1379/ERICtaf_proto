define([
  'jscore/core',
  '../../ext/ServerEvents/ServerEvents',
  '../TimeSeriesChart/TimeSeriesChart',
  './StatusPanelView'
], function(core, ServerEvents, TimeSeriesChart, View) {

  var SECOND = 1000;
  var MINUTE = 60 * SECOND;

  return core.Widget.extend({

    View: View,

    init: function() {
      this.testCases = {};
      this.chart = new TimeSeriesChart({
        colors: ['#89ba17', '#e32119'],
        labels: ['success', 'failure'],
        lines: {
          show: false
        },
        bars: {
          show: true,
          lineWidth: 1,
          barWidth: 0.9
        },
        processors: {
          filter: function(dpt, index, dpts) {
            var passed = _.last(dpts).finished - dpt.finished;
            return passed < 1 * MINUTE;
          },
          sortBy: 'finished',
          map: function(dpt, index) {
            return _.extend({index: index}, dpt);
          },
          groupBy: 'result',
          mapGroup: function(dpt) {
            return [dpt.index, dpt.time];
          },
          reduceLabels: function(labels, dpt, index) {
            labels[index] = dpt.name +
              '\n' + dpt.result + ' ' + dpt.time + ' s';
            return labels;
          }
        }
      });
    },

    onAttach: function() {
      this.chart.attachTo(this.getElement());
      var self = this;
      ServerEvents.subscribe(function(eventObject) {
        switch (eventObject.eventType) {
          case 'EiffelTestCaseStarted':
            self.testCaseStarted(eventObject);
            break;
          case 'EiffelTestCaseFinished':
            self.testCaseFinished(eventObject);
            break;
        }
      });
    },

    testCaseStarted: function(eventObject) {
      this.testCases[eventObject.eventId] = {
        name: eventObject.eventData.title,
        started: new Date(eventObject.eventTime),
        data: eventObject.eventData
      };
    },

    testCaseFinished: function(eventObject) {
      var parentId = eventObject.inputEventIds[0];
      var testCase = this.testCases[parentId];
      delete this.testCases[parentId];
      if (!testCase) {
        return;
      }
      var result = eventObject.eventData.result.toLowerCase();
      var finished = new Date(eventObject.eventTime);
      this.chart.datapoint({
        result: result,
        name: testCase.name,
        time: (finished - testCase.started) / 1000,
        finished: finished,
      });
      this.chart.update();
    }

  });

});
