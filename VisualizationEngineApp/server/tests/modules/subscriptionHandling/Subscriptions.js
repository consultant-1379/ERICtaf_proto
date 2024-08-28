var sinon = require('sinon'),
    config = require('../../../config/config.json'),
    io = require('socket.io').listen(config.test.port, {log: false}),
    should = require('chai').should(),
    helper = require('../../testHelper');

describe("SubscriptionHandler", function () {
    var logStub = {
            error: sinon.stub(),
            debug: sinon.stub()
        },
        subscriptions = require('../../../modules/subscriptionHandling/Subscriptions')(logStub);
        
    describe('#incomingRequest()', function () {
        
        it('should call join and leave on subscribe and unsubscribe events', function(done) {
            var testTopic = 'Test';
            
            io.sockets.on('connection', function(socket) {
                socket.on('disconnect', function() {
                    done();
                });
                
                socket.join = function(topic) { 
                    topic.should.equal(testTopic);
                };
                
                socket.leave = function(topic) { 
                    topic.should.equal(testTopic);
                    helper.close();
                };
                
                subscriptions.incomingRequest(socket);
                helper.makeSubscription(testTopic);
                helper.makeUnsubscription(testTopic);
            });
            helper.makeConnection();
        });
    });
});