define(function(require) {

  var core = require('jscore/core');
  var View = require('./TafDashboardView');
  var StatusPanel = require('./widgets/StatusPanel/StatusPanel');
  var ResourcesPanel = require('./widgets/ResourcesPanel/ResourcesPanel');
  var LogPanel = require('./widgets/LogPanel/LogPanel');
  var ConfidencePanel = require('./widgets/ConfidencePanel/ConfidencePanel');

  return core.App.extend({

    View: View,

    onStart: function() {
      this.confidencePanel = new ConfidencePanel();
      this.statusPanel = new StatusPanel();
      this.cpuPanel = new ResourcesPanel('Resources-OS-CPU');
      this.memoryPanel = new ResourcesPanel('Resources-OS-Memory');
      this.logPanel = new LogPanel();
    },

    onAttach: function() {
      this.view.afterRender();
      this.confidencePanel.attachTo(this.view.confidenceContainer);
      this.statusPanel.attachTo(this.view.statusContainer);
      this.cpuPanel.attachTo(this.view.cpuContainer);
      this.memoryPanel.attachTo(this.view.memoryContainer);
      this.logPanel.attachTo(this.view.logContainer);
    }

  });

});
