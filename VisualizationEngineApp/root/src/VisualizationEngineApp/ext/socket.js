define([
    'thirdparty/socket.io'
], function (io) {
    'use strict';

	var socket;

	return {
		connect: function (url, resource) {
			socket = io.connect(url, {resource: resource});
            socket.on('connect_failed', function () {
                console.error('Socket connection failed');
            });
		},

		on: function (event, callback) {
			socket.on(event, callback);
		},

		emit: function (event, message) {
			socket.emit(event, message);
		}
	};
});
