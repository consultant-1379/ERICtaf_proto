module.exports = function(app, io, eventHandler, logger) {
    'use strict';

    app.get('/probe', function(req, resp) {
        var activeSubs = {
            "subscriptions": {
                "unique": eventHandler.getUniqueSubscriptions(io), 
                "raw": io.sockets.manager.rooms
            }
        };
        resp.send(activeSubs);
    });
}