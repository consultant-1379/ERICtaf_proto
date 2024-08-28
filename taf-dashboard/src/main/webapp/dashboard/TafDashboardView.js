define([
  'jscore/core',
  'text!./TafDashboard.html',
  'styles!./TafDashboard.less'
], function(core, template, style) {
  return core.View.extend({

    getTemplate: function() {
      return template;
    },

    getStyle: function() {
      return style;
    },

    afterRender: function() {
      this.confidenceContainer = this.element.find('.eaTafDashboard-panel-confidence');
      this.statusContainer = this.element.find('.eaTafDashboard-panel-status');
      this.cpuContainer = this.element.find('.eaTafDashboard-panel-cpu');
      this.memoryContainer = this.element.find('.eaTafDashboard-panel-memory');
      this.logContainer = this.element.find('.eaTafDashboard-panel-log');
    }

  });

});
