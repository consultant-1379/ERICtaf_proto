define([
    './eiffelEvents',
    'jscore/ext/utils/base/underscore'
], function (events, _) {
    'use strict';

	var RandomEventGeneratorModule = (function () {
		var eventList = [events.EiffelJobFinishedEvent, events.EiffelArtifactNewEvent, events.EiffelJobQueuedEvent, events.EiffelJobStartedEvent, events.EiffelArtifactModifiedEvent],
		// var eventList = [events.VersionedEiffelEvent],
			started = false;
		
		var connect = function (url) {
		};
		
		var on = function (event, callback) {
			var max = eventList.length - 1;
			window.setInterval(function () {
				if (started) {
					var event = _.random(max);
					callback(eventList[event]());
				}		
			}, 2000);
		};
		
		var emit = function (event, message) {
			started = true;
		};
		
		return {
			connect: connect,
			on: on,
			emit: emit
		};
		
	})();
	
	return RandomEventGeneratorModule;
});
