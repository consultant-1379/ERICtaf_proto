define([
    './testEvents',
    'jscore/ext/utils/base/underscore'
], function (events, _) {
    'use strict';

	var DebugEventGeneratorModule = (function () {
		var eventList = [events.Event1, events.Event2, events.Event3, events.Event4, events.Event5, events.Event6, events.Event7,events.Event8, events.Event9,events.Event10,events.Event11];
		
		var connect = function (url) {
		};
		
		var on = function (event, callback) {
			var max = eventList.length - 1,
                count = 0,
                intervalId;
                
            window.onkeypress = function(e) {
                if (e.charCode === 32) {
                    e.stopPropagation();
                    e.preventDefault();
                    if (count <= max) {
                        callback(eventList[count](), true);
                        count++;
                    }
                }
                else if (e.charCode === 48) {
                    intervalId = window.setInterval(function () {
                        if (count <= max) {
                            callback(eventList[count](), true);
                            count++;
                        }
                    }, 0);
                }
                else if (e.charCode === 114) {
                    console.log('Resetting events');
                    window.clearInterval(intervalId);
                    count = 0;
                }
            };
		};
		
		var emit = function (event, message) {

		};
		
		return {
			connect: connect,
			on: on,
			emit: emit
		};
		
	})();
	
	return DebugEventGeneratorModule;
});