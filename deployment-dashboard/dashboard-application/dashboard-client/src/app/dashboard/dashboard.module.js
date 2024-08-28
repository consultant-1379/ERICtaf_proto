angular.module('app.dashboard', []).factory('sse', function($rootScope) {
    var sse = new EventSource('/api/stream');
    return {
        addEventListener: function(eventName, callback) {
            sse.addEventListener(eventName, function() {
                var args = arguments;
                $rootScope.$apply(function() {
                    callback.apply(sse, args);
                });
            });
        }
    };
});
