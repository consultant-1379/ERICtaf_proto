module.exports = function (logger) {
	var util = require('util'),
		eventEmitter = require('events').EventEmitter,
		amqp = require('amqp'),
		config = require('../../config/config.json');
		
	var rabbitMQSource = function() {
		var rabbitMQ = amqp.createConnection({host: config.messageBus.host, port: config.messageBus.port});
  
        rabbitMQ.on('ready', function() {
            logger.info('Connected to MB');
        
            var q = rabbitMQ.queue('', function(queue) {
                logger.info('Queue ' + queue.name + ' is open');
        
                queue.bind(config.messageBus.exchange, config.messageBus.bindingKey);
                queue.on('queueBindOk', function() {
                    logger.info('Bind queue to ' + config.messageBus.exchange + ' exchange done');
                });
                queue.subscribe(function(message) {
                    logger.debug('Message from MB: ' + message.data.toString());
                    this.emit('newEvent', message.data.toString());
                }.bind(this));
            }.bind(this));     
        }.bind(this));
    };
    
    util.inherits(rabbitMQSource, eventEmitter);
        
    return rabbitMQSource;
};