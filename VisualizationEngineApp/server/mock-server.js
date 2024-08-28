var redis = require('socket.io/node_modules/redis'),
    log4js = require('log4js'),
    express = require('express'),
    http = require('http'),
    fs = require('fs'),
    config = require('./config/config.json'),
    app = express();

var randomEventIdCounter = 0;

var server = http.createServer(app);
var writeStream = fs.createWriteStream('./logs/connections.log', {
    flags: 'a',
    encoding: 'utf8',
    mode: 0666
});

app.configure(function () {
    app.use(express.logger({format: 'default', stream: writeStream}));
    app.use(express.static(__dirname + '/public'));
});

log4js.configure({
    appenders: [
        { type: 'console', category: 'console' },
        { type: 'file', filename: 'logs/mock_server.log', category: 'veserver', maxLogSize: 10240, backups: 3 }
    ]
});

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

server.listen(config.server.port);

io.sockets.on('connection', function(socket) {
    console.log("client " + socket.id + " connected from " + socket.handshake.address.address);
    socket.on('subscribe', function(topic) {
        socket.join(topic);
        console.log("client " + socket.id + " subscribed to " + topic);
    });
    socket.on('unsubscribe', function(topic) {
        socket.leave(topic);
        console.log("client " + socket.id + " unsubscribed to " + topic);
    });
});

function getRandomEventType(){
    var eventTypes = ["EiffelArtifactNewEvent", "EiffelArtifactModifiedEvent", "EiffelJobQueuedEvent", "EiffelJobStartedEvent", "EiffelJobFinishedEvent"];
    return eventTypes[Math.floor(Math.random()*eventTypes.length)];
}

function getRandomInputEvents(){
    var noParents = Math.floor(Math.random()*3)+1;
    var randomInputEvents = [];
    for (var i = 0; i < noParents; i++){
        var randomEvent = Math.floor(Math.random()*randomEventIdCounter);
        var alreadyInArray = false;
        for (var eventInd in randomInputEvents){
            if (randomInputEvents[eventInd] == randomEvent){
                alreadyInArray = true;
                break;
            }
        }
        if (!alreadyInArray){
            randomInputEvents.push(randomEvent);
        }
    }
    return randomInputEvents;
}

//Only used to mock random events.
function createRandomEvent() {
    randomEventIdCounter++;

    var randomEvent = {
        domainId: "kista",
        eventId: randomEventIdCounter,
        eventType: getRandomEventType(),
        eventTime: new Date(),
        inputEventIds: getRandomInputEvents(),
        "eiffelData": [
            {
                "confidenceLevels": [
                    "CL1",
                    "CL2"
                ],
                "gav": {
                    "groupId": "WMR",
                    "artifactId": "CXP12345",
                    "version": "R1A02"
                },
                "version": "1.0.0"
            }
        ]
    };
    return randomEvent;
}

//Generate a random event every three seconds and send it to the sockets listening for all or the specified eventType
setInterval(function(){
    var randomEvent = createRandomEvent();
    io.sockets.in('all').emit('message', randomEvent);
    io.sockets.in(randomEvent.eventType).emit('message', randomEvent);

},3000);

