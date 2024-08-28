module.exports = function(logger, config) {
    var _ = require('underscore'),
        supportedEiffelVersion = config.eiffel.supportedVersion;
        
    // Private helper
    var dispatch = function() {
        var funs = _.toArray(arguments),
            size = funs.length;
        
        return function() {
            var ret,
                args = _.toArray(arguments);
                
            for (var funIndex=0; funIndex<size; funIndex++) {
                var fun = funs[funIndex];
                ret = fun.apply(fun, args);
                
                if (ret) {
                    return ret;
                }
            }
            
            return ret;
        };
    };
        
    var process = function(message, io) {
        var parsedMessage;

        try {
            parsedMessage = JSON.parse(message.eiffelMessage);
        } catch (e) {
            logger.error('EventHandler.js - Error parsing eiffel message: %s', e.message);
            logger.debug('EventHandler.js - Eiffel message: %s', message.eiffelMessage);
            return;
        }
        
        try {
            var latestSupportedMessage = getLatestSupportedVersion(parsedMessage.eiffelMessageVersions);
            logger.debug('EventHandler.js - Emitting eiffel message version %s', latestSupportedMessage);
            emitToSubscribers(io, parsedMessage.eiffelMessageVersions[latestSupportedMessage]);
            io.sockets.in('all').emit('message', createResponseMessage(parsedMessage.eiffelMessageVersions[latestSupportedMessage], 'all'));
        } catch (e) {
            logger.error('EventHandler.js - Error processing eiffel message: %s', e.message);
            return;
        }

        // io.sockets.in('all').emit('message', createResponseMessage(parsedMessage.eiffelMessageVersions[latestSupportedMessage], 'all'));
    };
    
    var getUniqueSubscriptions = function(io) {
        return _.chain(_.keys(io.sockets.manager.rooms))
            .filter(function(item) {
                return item !== '' && item !== '/all' && item !== '/All' && item !== '/ALL';
            })
            .map(function(item) {
                return item.replace('/', '');
            })
            .uniq()
            .value();
    };
    
    var emitToSubscribers = function(io, event) {
        _.map(getUniqueSubscriptions(io), function(topic) {
            var ands = topic.split('&&'),
                matchTopic = _.reduce(ands, function(verdict, checkTopic) {
                    if (/^!/.test(checkTopic)) {
                        return verdict && !shouldEmit(checkTopic.replace(/^!/, ''), event);
                    } else {
                        return verdict && shouldEmit(checkTopic, event);
                    }
                }, true);
            
            if (matchTopic) {
                logger.debug('EventHandler.js - Emitting to clients subscribing to: %s', topic);
                io.sockets.in(topic).emit('message', createResponseMessage(event, topic));
            }
        });
    };
    
    var getLatestSupportedVersion = function(message) {
        return _.chain(message)
            .keys()
            .tap(function(keys) {
                logger.debug('EventHandler.js - Found eiffel versions %s, supported major version is %d', keys.join(', '), supportedEiffelVersion);
            })
            .filter(function(item) {
                var version = item.split('.');
                return supportedEiffelVersion >= parseInt(version[0], 10);
            }, this)
            .sortBy(function(item) {
                var version = item.split('.').join('');
                return -parseInt(version, 10);
            })
            .value()[0];
    };
    
     var shouldEmit = function(topic, event) {
        var temp = topic.split(':'),
            wantedKey = temp[0],
            wantedValue = temp[1],
            foundValue = _.reduce(wantedKey.split('.'), function(keyArray, key) {
                keyArray = keyArray[key];
                return keyArray;
            }, event),
            check = dispatch(wantOnlyKey, foundValueMatchWantedValue, foundValueMatchWantedValueInArray);
        
        logger.debug('EventHandler.js - Wanted key: %s, Wanted value: %s, Found value: %s', wantedKey, wantedValue, foundValue);
        
        if (foundValue !== undefined) {
            if (check(wantedKey, wantedValue, foundValue)) {
                logger.debug('EventHandler.js - This is a match');
                return true;
            }
        }
        return false;
    };
    
    var wantOnlyKey = function(wantedKey, wantedValue, foundValue) {
        return wantedValue === undefined;
    };
    
    var foundValueMatchWantedValue = function(wantedKey, wantedValue, foundValue) {
        var re = new RegExp('^' + wantedValue + '$');
        return re.test(foundValue);
    };
    
    var foundValueMatchWantedValueInArray = function(wantedKey, wantedValue, foundValue) {
        return _.contains(foundValue, wantedValue);
    };

    var createResponseMessage = function(message, topic) {
        return {
            eiffelMessage: message,
            serverData: {
                topic: topic
            }
        };
    };
    
    return {
        process: process,
        getUniqueSubscriptions: getUniqueSubscriptions,
        emitToSubscribers: emitToSubscribers,
        getLatestSupportedVersion: getLatestSupportedVersion,
        shouldEmit: shouldEmit,
        createResponseMessage: createResponseMessage
    };
    
};