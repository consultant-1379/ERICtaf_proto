module.exports = (function() {
    var io = require('socket.io-client'),
        config = require('../config/config.json'),
        socket;
    
    return { 
        makeConnection: function() {
            socket = io.connect('127.0.0.1:' + config.test.port);
        },
    
        makeSubscription: function(topic) {
            socket.emit('subscribe', topic);
        },
        
        makeUnsubscription: function(topic) {
            socket.emit('unsubscribe', topic);
        },
    
        close: function() {
            socket.disconnect();
        }
    };
})();
