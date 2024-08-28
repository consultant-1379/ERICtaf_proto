/*global define, describe, it, expect */
define([
    'jscore/ext/utils/base/underscore',
	'app/lib/CommunicationHandler',
    'app/models/EventCollection'
], function (_, CommunicationHandler, EventCollection) {
    'use strict';

    describe('CommunicationHandler', function () {
    	var ch,
            onStub,
            emitStub;

        beforeEach(function () {
            onStub = sinon.stub();
            emitStub = sinon.stub();
            sinon.stub(io, 'connect').returns({on: onStub, emit: emitStub});

            ch = new CommunicationHandler({
                host: 'test.ericsson.se',
                port: 8080,
                collections: {
                    eventCollection: new EventCollection()
                }
            });
        });
        
        afterEach(function () {
            io.connect.restore();
            onStub = undefined;
            emitStub = undefined;
        });

        describe('Methods', function() {
        	
        	it('constructor()', function() {
        		sinon.assert.calledOnce(io.connect);
        	});

        	it('listen()', function() {
	            ch.listen();

				sinon.assert.calledOnce(onStub);	            
        	});

        	it('subscribeToServer(region: String, topic: String)', function() {
	            ch.subscribeToServer('testRegion', 'test');

	            sinon.assert.calledOnce(emitStub);
                sinon.assert.calledWith(emitStub, 'subscribe', 'test');
        	});

            it('unsubscribeToServer(region: String, topic: String)', function() {
                ch.subscribeToServer('testRegion', 'test');
                ch.unsubscribeToServer('testRegion', 'test');

                sinon.assert.calledTwice(emitStub);
                sinon.assert.calledWith(emitStub, 'unsubscribe', 'test');

                ch.subscribeToServer('testRegion', 'test1');
                ch.subscribeToServer('testRegion', 'test2');
                ch.subscribeToServer('testRegion', 'test3');
                ch.unsubscribeToServer('testRegion');

                sinon.assert.callCount(emitStub, 8);
            });

            it('_handleIncomingMessage(data: Object)', function() {
                var data = {
                    eiffelMessage: {
                        "id": 1,
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
                    serverData: {
                        topic: 'eventData.jobExecutionId:276'
                    }
                };

                ch._addToWatchedSubscriptions('testRegion', 'eventData.jobExecutionId:276');
                ch._addToWatchedSubscriptions('testRegion', 'eventType:EiffelJobFinishedEvent');
                ch._addToWatchedSubscriptions('anotherRegion', 'notHere:someValue');
                ch._handleIncomingMessage(data);

                expect(ch.eventCollection.size()).to.equal(1);
                expect(ch.eventCollection.getModel('c1').getAttribute('destination').length).to.equal(1);
                expect(_.contains(ch.eventCollection.getModel('c1').getAttribute('destination'), 'testRegion')).to.be.true;
                expect(_.contains(ch.eventCollection.getModel('c1').getAttribute('destination'), 'anotherRegion')).to.be.false;
            });

            it('_addToWatchedSubscriptions(region: String, topic: String)', function() {
                ch._addToWatchedSubscriptions('testRegion1', 'test1');
                expect(ch.watchedSubscriptions['testRegion1'].length).to.equal(1);

                ch._addToWatchedSubscriptions('testRegion1', 'test2');
                expect(ch.watchedSubscriptions['testRegion1'].length).to.equal(2);

                ch._addToWatchedSubscriptions('testRegion2', 'test1');
                expect(ch.watchedSubscriptions['testRegion2'].length).to.equal(1);
            });

            it('_removeFromWatchedSubscriptions(region: String, topic: String)', function() {
                ch._addToWatchedSubscriptions('testRegion1', 'test1');
                ch._addToWatchedSubscriptions('testRegion1', 'test2');
                ch._addToWatchedSubscriptions('testRegion2', 'test1');

                ch._removeFromWatchedSubscriptions('testRegion1', 'test2');
                expect(ch.watchedSubscriptions['testRegion1'].length).to.equal(1);
                expect(_.contains(ch.watchedSubscriptions['testRegion1'], 'test1')).to.be.true;
                expect(_.contains(ch.watchedSubscriptions['testRegion1'], 'test2')).to.be.false;

                ch._removeFromWatchedSubscriptions('testRegion2', 'test1');
                expect(ch.watchedSubscriptions['testRegion2'].length).to.equal(0);
            });

            it('_isWatched(topic: String)', function() {
                expect(ch._isWatched('dontExist')).to.be.false;

                ch._addToWatchedSubscriptions('testRegion1', 'test1');
                expect(ch._isWatched('test1')).to.be.true;

                ch._addToWatchedSubscriptions('testRegion2', 'test1');
                expect(ch._isWatched('test1')).to.be.true;

                ch._removeFromWatchedSubscriptions('testRegion1', 'test1');
                expect(ch._isWatched('test1')).to.be.true;

                ch._removeFromWatchedSubscriptions('testRegion2', 'test1');
                expect(ch._isWatched('test1')).to.be.false;
            });
        });
    });
});