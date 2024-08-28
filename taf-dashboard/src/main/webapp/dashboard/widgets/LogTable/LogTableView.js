define([
  'jscore/core',
  'text!./LogTable.html',
  'styles!./LogTable.less'
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
