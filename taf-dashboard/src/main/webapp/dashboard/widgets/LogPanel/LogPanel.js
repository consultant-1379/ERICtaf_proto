define([
  'jscore/core',
  '../../ext/ServerEvents/ServerEvents',
  '../LogTable/LogTable',
  './LogPanelView'
], function(core, ServerEvents, LogTable, View) {

  var formatTime = function(time) {
    return moment(time).format('YYYY-MM-DD HH:mm:ss');
  };

  var defaultParent = {
    name: '???',
    time: null,
    parent: null,
  };

  return core.Widget.extend({

    View: View,

    init: function() {
      this.parents = {};
      this.table = new LogTable();
    },

    onAttach: function() {
      this.table.attachTo(this.getElement());
      var self = this;
      ServerEvents.subscribe(function(eventObject) {
        switch (eventObject.eventType) {
          case 'EiffelTestCaseStarted':
            return self.testCaseStarted(eventObject);
          case 'EiffelTestCaseFinished':
            return self.testCaseFinished(eventObject);
          case 'EiffelTestTestAction':
            return self.testTestAction(eventObject);
        }
      });
    },

    testCaseStarted: function(eventObject) {
      var parentId = eventObject.inputEventIds[0];
      var parent = this.parents[parentId] || defaultParent;
      var name = eventObject.eventData.title;
      var time = Date.parse(eventObject.eventTime);
      this.parents[eventObject.eventId] = {
        name: name,
        time: time,
        parent: parent
      };
      this.table.log(
        formatTime(time),
        name,
        'Started',
        'Suite: ' + eventObject.eventData.optionalParameters.suiteName
      );
    },

    testCaseFinished: function(eventObject) {
      var parentId = eventObject.inputEventIds[0];
      var parent = this.parents[parentId] || defaultParent;
      var time = Date.parse(eventObject.eventTime);
      var logType = 'log';
      var result = eventObject.eventData.result.toLowerCase();
      if (this.table[result]) {
        logType = result;
      }
      this.table[logType](
        formatTime(time),
        parent.name,
        'Finished' + (parent.time ? ' in ' + ((time - parent.time) / 1000) + ' s' : ''),
        'Suite: ' + eventObject.eventData.optionalParameters.suiteName
      );
      delete this.parents[parentId];
    },

    testTestAction: function(eventObject) {
      var parentId = eventObject.inputEventIds[0];
      var parent = this.parents[parentId];
      if (!parent) {
        return;
      }
      var time = Date.parse(eventObject.eventTime);
      this.table.log(
        formatTime(time),
        eventObject.eventData.optionalParameters.level,
        eventObject.eventData.message,
        'Test: ' + parent.name
      );
    }

  });

});
