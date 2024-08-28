module.exports = function(logger) {
    
    var incomingRequest = function(socket) {
        socket.on('subscribe', function(topic) {
            logger.debug('Subscriptions.js - Subscribe request: %s', topic);
            socket.join(topic);
        });
        socket.on('unsubscribe', function(topic) {
            logger.debug('Subscriptions.js - Unsubscribe request: %s', topic);
            socket.leave(topic);
        });
    };
    
    return {
        incomingRequest: incomingRequest
    };
};