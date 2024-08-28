define([
  'jscore/core',
  './LogTableView'
], function(core, View) {

  var log = function(type, args) {
    var self = this;
    var el = $(this.getElement()._getHTMLElement());
    var body = el.find('tbody');
    var row = $('<tr></tr>').addClass('eaTafDashboard-LogTable-' + type);
    _.forEach(args, function(arg, i) {
      $('<td></td>')
        .text(arg)
        .appendTo(row);
    });
    var parent = el.parent();
    var scrollSize = el.height() - parent.height();
    var scrollDown = scrollSize < parent.scrollTop() + 10;
    body.append(row);
    var children = body.children();
    var overflow = children.length - this.options.maxRows;
    if (overflow > 0) {
      children.slice(0, overflow).remove();
    }
    if (scrollDown) {
      parent.scrollTop(el.height());
    }
  };

  return core.Widget.extend({

    View: View,

    init: function() {
      this.options = $.extend({
        maxRows: 100
      }, this.options);
    },

    log: function() {
      log.apply(this, ['log'].concat(arguments));
    },

    success: function() {
      log.apply(this, ['success'].concat(arguments));
    },

    failure: function() {
      log.apply(this, ['failure'].concat(arguments));
    }

  });

});
