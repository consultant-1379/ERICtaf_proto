module.exports = function () {
	var util = require('util'),
		eventEmitter = require('events').EventEmitter,
		events = require('../../public/client/VisualizationEngineApp/src/lib/eiffelEvents'),
		eventList = [events.EiffelJobFinishedEvent, events.EiffelArtifactNewEvent, events.EiffelJobQueuedEvent, events.EiffelJobStartedEvent, events.EiffelArtifactModifiedEvent];
	
	var randomEventGenerator = function() {
		var max = eventList.length - 1;
		
		setInterval(function () {
			var event = Math.round(Math.random() * max);
			this.emit('newEvent', JSON.stringify(eventList[event]()));		
		}.bind(this), 2000);
	};
	
	util.inherits(randomEventGenerator, eventEmitter);
		
	return randomEventGenerator;
};