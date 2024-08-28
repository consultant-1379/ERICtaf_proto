var sinon = require('sinon'),
    config = require('../../../config/config.json');

describe("EventHandler", function () {
    var logStub = {
            error: sinon.stub(),
            debug: sinon.stub()
        },
        eventHandler = require('../../../modules/eventHandling/EventHandler')(logStub, config);

    describe('#process()', function () {
        var emitStub = sinon.stub(),
            inStub = sinon.stub().returns({emit: emitStub}),
            socketStub = {
                sockets: {
                    in: inStub,
                    manager: {
                        rooms: []
                    }
                }
            },
            event = {
                eiffelMessage: '{\"eiffelMessageVersions\":{\"2.1.2.0.16\": {\"domainId\": \"kista\",\"eventId\": \"07a348dc-b239-41c1-8c7e-5f4cdea49f5a\",\"eventType\": \"EiffelJobStartedEvent\",\"eventTime\": \"2013-08-02T10:31:35.303Z\",\"eventSource": \"Testing\",\"inputEventIds\": [\"55555555-b0fc-43a6-98de-e0572fad10bd\",\"66666666-b0fc-43a6-98de-e0572fad10bd\"],\"eiffelData\": [{\"jobExecutionNumber\": 3800,\"jobInstance\": \"EiffelCmdLineApp\",\"jobExecutionId\": \"731c2e10-92fb-460d-89f0-2c2de9f10e20\",\"context\": [\"ekrisva.test\"],\"version\": \"1.0.0\",\"eventSource\": \"EventEmitterDemo\"}]}}}'
            },
            brokenEvent = {
                eiffelMessage: '{\"eiffelMessageVersions\":{\"2.1.2.0.16\": {\"domainId\": \"kista\",\"eventId\": \"07a348dc-b239-41c1-8c7e-5f4cdea49f5a\"\"eventType\": \"EiffelJobStartedEvent\",eventTime: \"2013-08-02T10:31:35.303Z\",\"eventSource": \"Testing\",\"inputEventIds\": [\"55555555-b0fc-43a6-98de-e0572fad10bd\",\"66666666-b0fc-43a6-98de-e0572fad10bd\"],\"eiffelData\": [{\"jobExecutionNumber\": 3800,\"jobInstance\": \"EiffelCmdLineApp\",\"jobExecutionId\": \"731c2e10-92fb-460d-89f0-2c2de9f10e20\",\"context\": [\"ekrisva.test\"],\"version\": \"1.0.0\",\"eventSource\": \"EventEmitterDemo\"}]}'  
            };
        
        afterEach(function() {
            emitStub.reset();
            inStub.reset();
        });
                
        it('should send eiffel message to subscribers on all', function() {
            var eiffel = JSON.parse(event.eiffelMessage).eiffelMessageVersions['2.1.2.0.16'],
                testMsg = eventHandler.createResponseMessage(eiffel, 'all');

            eventHandler.process(event, socketStub);

            sinon.assert.calledOnce(inStub);
            sinon.assert.calledOnce(emitStub);
            sinon.assert.calledWith(inStub, 'all');
            sinon.assert.calledWith(emitStub, 'message', testMsg);
        });
        
        it('should not send or throw error on broken eiffel message', function() {
            eventHandler.process(brokenEvent, socketStub);
            
            sinon.assert.notCalled(inStub);
            sinon.assert.notCalled(emitStub);
        });
    });
    
    describe('#getUniqueSubscriptions', function () {
        
        it('should return a unique list of subscriptions', function () {
            var ioStub = {
                    sockets: {
                        manager: { 
                            rooms: {
                                '/all': null,
                                '/key:value': null,
                                '/key1:value1': null,
                                '/key1:value1': null,
                                '/key2:value2': null
                            }
                        }
                    }
                };
            
            eventHandler.getUniqueSubscriptions(ioStub).should.have.length(3);
        });
    });

    describe('#emitToSubscribers', function () {

        it('should handle &&', function () {
            var emitStub = sinon.stub(),
                inStub = sinon.stub().returns({emit: emitStub}),
                socketStub = {
                    sockets: {
                        in: inStub,
                        manager: {
                            rooms: {
                                '/eventType:EiffelJobFinishedEvent&&eventData.jobExecutionId:276&&eventData.resultCode:SUCCESS': null
                            }
                        }
                    }
                },
                event = {
                    "eventType":"EiffelJobFinishedEvent",
                    "eventData": { 
                        "jobInstance":"LMBaselineBuilder_rnc_main_89.1_Trigger",
                        "jobExecutionId":"276",
                        "resultCode": "SUCCESS",
                        "inputEventIds": ["233744bf-5daf-4d32-839d-cc73e6000fa8", "f1759b0c-a107-4b82-9a4d-be7fd04f6ec7"],
                        "optionalParameters": {
                            "org": "rnc",
                            "proj": "main"
                        }
                    }
                },
                testMsg = eventHandler.createResponseMessage(event, 'eventType:EiffelJobFinishedEvent&&eventData.jobExecutionId:276&&eventData.resultCode:SUCCESS');

            eventHandler.emitToSubscribers(socketStub, event);

            sinon.assert.calledWith(emitStub, 'message', testMsg);
        });

        it('should handle !', function () {
            var emitStub = sinon.stub(),
                inStub = sinon.stub().returns({emit: emitStub}),
                socketStub = {
                    sockets: {
                        in: inStub,
                        manager: {
                            rooms: {
                                '/eventType:EiffelJobFinishedEvent&&eventData.jobExecutionId:276&&!eventData.resultCode:SUCCESS': null
                            }
                        }
                    }
                },
                event = {
                    "eventType":"EiffelJobFinishedEvent",
                    "eventData": { 
                        "jobInstance":"LMBaselineBuilder_rnc_main_89.1_Trigger",
                        "jobExecutionId":"276",
                        "resultCode": "SUCCESS",
                        "inputEventIds": ["233744bf-5daf-4d32-839d-cc73e6000fa8", "f1759b0c-a107-4b82-9a4d-be7fd04f6ec7"],
                        "optionalParameters": {
                            "org": "rnc",
                            "proj": "main"
                        }
                    }
                };
                
                eventHandler.emitToSubscribers(socketStub, event);

                sinon.assert.notCalled(emitStub);
        });
    });
    
    describe('#getLatestSupportedVersion', function () {
        
        it('should return the highest supported version when supported major version is 2', function () {
            var versions = {
                '4.3.5.0.15': null,
                '3.3.6.1.13': null,
                '2.1.2.0.16': null,
                '2.1.1.0.10': null,
                '2.1.1.0.6': null,
                '1.5.3.2.9': null
            };
            
            eventHandler.getLatestSupportedVersion(versions).should.equal('2.1.2.0.16');
        });
    });
    
    describe('#shouldEmit', function () {
        var testMessage = {
            "eventType":"EiffelJobFinishedEvent",
            "eventData": { 
                "jobInstance":"LMBaselineBuilder_rnc_main_89.1_Trigger",
                "jobExecutionId":"276",
                "resultCode": "SUCCESS",
                "inputEventIds": ["233744bf-5daf-4d32-839d-cc73e6000fa8", "f1759b0c-a107-4b82-9a4d-be7fd04f6ec7"],
                "optionalParameters": {
                    "org": "rnc",
                    "proj": "main"
                }
            }
        };
        
        it('should find an existing key:value pair in a message', function () {
            eventHandler.shouldEmit('eventType:EiffelJobFinishedEvent', testMessage).should.be.true;
            eventHandler.shouldEmit('eventData.resultCode:SUCCESS', testMessage).should.be.true;
            eventHandler.shouldEmit('eventData.jobInstance', testMessage).should.be.true;
            eventHandler.shouldEmit('eventData.optionalParameters.org:rnc', testMessage).should.be.true;
        });
        
        it('should fail to find non-existing key:value pair in a message', function () {
            eventHandler.shouldEmit('notThere:nothing', testMessage).should.be.false;
            eventHandler.shouldEmit('eventType:nothing', testMessage).should.be.false;
            eventHandler.shouldEmit('eventData.optionalParameters.nothing', testMessage).should.be.false;
        });
        
        it('should find a key:value pair when the value is in a list', function () {
            eventHandler.shouldEmit('eventData.inputEventIds:233744bf-5daf-4d32-839d-cc73e6000fa8', testMessage).should.be.true;
        });

        it('should find a key:value pair when the value is a RegExp', function () {
            eventHandler.shouldEmit('eventType:EiffelJob', testMessage).should.be.false;
            eventHandler.shouldEmit('eventType:EiffelJob.+', testMessage).should.be.true;
            eventHandler.shouldEmit('eventType:Ei..elJobFinished.+', testMessage).should.be.true;
        });
    });
});