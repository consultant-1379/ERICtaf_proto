/*global define, describe, it, expect */
define([
    'app/widgets/TreeChart/TreeChart',
    'jscore/ext/utils/base/underscore',
	'app/lib/testEvents'
], function (TreeChart, _,Events) {
    'use strict';

    describe('TreeChart', function () {

        describe('Methods', function () {
            
            it('createTreeNode()', function () {
                 var event = Events.Event2();
                var obj = new TreeChart();
				var color = 'green';
                var result = obj.createTreeNode(event.eventType, color, event.eventId,event.eventData.jobExecutionId);

                expect(result.name).to.eql('EiffelJobFinishedEvent');
				expect(result.color).to.eql('green');
				expect(result.id).to.eql('d565c60f-0707-4d4c-8e67-18c71d2f440c');
				expect(result.jobExecutionId).to.eql('544');
				expect(result.children).to.eql([]);			
				
            });
			
			 it('setAspectRatio()', function () {
                 
                var obj = new TreeChart();
				obj.firstRun = false;
				obj.setAspectRatio(5);
				expect(obj.aspectRatio).to.eql(0.5);			
				
            });
			
			it('setDefaultValues()', function () {
                 
                var obj = new TreeChart();
				obj.firstRun = false;
				
				
				obj.aspectRatio = 0.6;
				obj.jobEvent = 'testCaseEvent';
				obj.eiffelEvent = 'TestSuiteEvent';
				
				obj.setDefaultValues();
				
				expect(obj.aspectRatio).to.be.undefined;	
				expect(obj.jobEvent).to.eql('EiffelJobFinishedEvent');
				expect(obj.eiffelEvent).to.eql('Suite');				
				
            });
            
            it('handleEvent(eventType: String)', function () {
                var event1 = Events.Event1()
                var event2 = Events.Event2()
				var event3 = Events.Event3()
				
                var obj = new TreeChart();
				
				obj.handleEvent(event1);
				obj.handleEvent(event2);
				obj.handleEvent(event3);
				
                expect(obj.root.children[0].children[0].id).to.equal(event1.eventId);
				expect(obj.root.children[0].children[1].id).to.equal(event3.eventId);

            });
        });
    });
});
