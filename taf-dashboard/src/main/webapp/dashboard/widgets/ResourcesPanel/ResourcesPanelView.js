define([
  'jscore/core',
  'text!./ResourcesPanel.html',
  'styles!./ResourcesPanel.less'
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
