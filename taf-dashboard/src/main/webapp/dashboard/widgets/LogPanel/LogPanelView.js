define([
  'jscore/core',
  'text!./LogPanel.html',
  'styles!./LogPanel.less'
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
