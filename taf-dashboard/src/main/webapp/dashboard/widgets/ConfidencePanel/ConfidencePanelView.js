define([
  'jscore/core',
  'text!./ConfidencePanel.html',
  'styles!./ConfidencePanel.less'
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
