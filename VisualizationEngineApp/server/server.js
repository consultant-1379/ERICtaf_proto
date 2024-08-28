// Declarations
var numCPUs = require('os').cpus().length,
    cluster = require('cluster'),
    redis = require('socket.io/node_modules/redis'),
    amqp = require('amqp'),
    log4js = require('log4js'),
    express = require('express'),
    http = require('http'),
    fs = require('fs'),
    config = require('./config/config.json'),
    app = express(),

    currentWorker = 1,
    server = http.createServer(app);
    // writeStream = fs.createWriteStream('./'+ config.server.logDir + '/connections.log', {
       // flags: 'a',
       // encoding: 'utf8',
       // mode: 0666
    // });

// Configuration
app.configure(function () {
    // app.use(express.logger({format: 'default', stream: writeStream}));
    app.use(express.static(__dirname + '/public'));
});
    
log4js.configure({
    appenders: [
        { type: 'console', category: 'console' },
        { type: 'file', filename: config.server.logDir + '/' + config.server.logFile, category: 'veserver', maxLogSize: 10240000, backups: 3 }
    ]
});
var logger = log4js.getLogger('veserver');
logger.setLevel(config.logLevel);

// Require our event logic
var EventSource = require('./modules/eventHandling/' + config.server.eventSource)(logger),
    eventHandler = require('./modules/eventHandling/EventHandler')(logger, config),
    subscriptions = require('./modules/subscriptionHandling/Subscriptions')(logger),
    workerUtils = require('./modules/workerUtils')(logger);

// Tasks run on main process
if (cluster.isMaster) {
    for (var i = 0; i < numCPUs; i++) {
        cluster.fork()
        .on('online', workerUtils.workerOnline);
    }
    
    var eventSource = new EventSource();
    
    eventSource.on('newEvent', function (data) {
        workerUtils.getNextWorker(cluster.workers, currentWorker).send({eiffelMessage: data});
        currentWorker = currentWorker == numCPUs ? 1 : ++currentWorker;
    });
    
    cluster.on('exit', function(worker, code, signal) {
        logger.error('server.js - Worker ' + worker.id + ' died');
        cluster.fork()
        .on('online', function() {
            logger.info('server.js - Replacement worker online');
        });
    });
    
// Tasks run on worker processes
} else if (cluster.isWorker){
    var RedisStore = require('socket.io/lib/stores/redis');
    var pub = redis.createClient();
    var sub = redis.createClient(); 
    var client = redis.createClient();

    var io = require('socket.io').listen(server, {
        store: new RedisStore({
            redisPub : pub,
            redisSub : sub,
            redisClient : client
        }),
        log: false
    });

    require('./routes/routes')(app, io, eventHandler, logger);
    
    server.listen(config.server.port, function () {
        logger.info('server.js - Worker-%s listening on %s', cluster.worker.id, config.server.port);
    });
    
    io.sockets.on('connection', function(socket) {
        subscriptions.incomingRequest(socket);
    });
    
    process.on('message', function(message) {
        logger.debug('server.js - Eiffel message received at worker');
        eventHandler.process(message, io);
    });
}
