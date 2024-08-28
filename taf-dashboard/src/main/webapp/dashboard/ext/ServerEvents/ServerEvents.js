define(function() {

  var callbacks = $.Callbacks();
  var sse = new EventSource('/sse');
  var snapshot = [];

  $.getJSON('/snapshot', function(data) {
    snapshot = data;
    sse.onmessage = function(e) {
      var lines = e.data.trim().split('\n');
      return _.each(lines, function(line) {
        var eventObject = JSON.parse(line);
        callbacks.fire(eventObject);
      });
    };
  });

  return {
    subscribe: function() {
      var fns = _.toArray(arguments);
      _.each(fns, function(fn) {
        _.each(snapshot, function(eventObject) {
          _.defer(_.bind(fn, callbacks), eventObject);
        });
      });
      callbacks.add.apply(this, fns);
    },
    unsubscribe: callbacks.remove
  };

});
