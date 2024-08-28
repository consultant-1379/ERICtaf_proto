define([
  'jscore/core',
  '../../ext/ServerEvents/ServerEvents',
  'chartlib/widgets/DonutChart',
  './ConfidencePanelView'
], function(core, ServerEvents, DonutChart, View) {

  var makeData = function(percent) {
    return {
      items: [
        {
          label: 'success',
          value: percent,
        },
        {
          label: 'failure',
          value: 100 - percent
        }
      ],
      centerText: percent + '%'
    };
  };

  return core.Widget.extend({

    View: View,

    init: function() {
      this.chart = new DonutChart({
        settings: {
          colorPalette: ['#89ba17', '#e32119'],
          w: 200,
          h: 200,
          centerText: {
            styles: {
              'font-size': '3rem',
            }
          }
        },
        data: makeData(0, 100)
      });
    },

    onAttach: function() {
      this.chart.attachTo(this.getElement());
      var self = this;
      ServerEvents.subscribe(function(eventObject) {
        if (eventObject.eventType === 'EiffelConfidenceLevelModifiedEvent') {
          var percent = parseInt(eventObject.eventData.optionalParameters.confidenceDonut, 10);
          if (!isNaN(percent)) {
            var data = makeData(percent);
            self.chart.update(data);
          }
        }
      });
    }

  });

});
