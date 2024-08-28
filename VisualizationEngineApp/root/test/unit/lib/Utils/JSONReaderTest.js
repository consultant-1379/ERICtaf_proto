define([
	'app/lib/Utils/JSONReader',
	'app/lib/Template/MessageBusTemplate',
	'app/lib/eiffelEvents',
], function (JSONReader, MessageBusTemplate, EiffelEvent) {
    'use strict';
	/*
	Test reads a Template .js file in order to return an array. If the template is changed this test may break.
	*/
    describe('JSONReader', function () {

        describe('Methods', function () {
            
            it('Read Json Event from lib/Template/MessageBusTemplate.js File', function () {
            
			var jsonArray = [MessageBusTemplate.getTemplate()] 
			
			var EventKeys = new Array()
			EventKeys = JSONReader.getJSONKeys(jsonArray)
			JSONReader.printToConsole(EventKeys)
									
                expect(EventKeys[0]).equal("domainId");
				expect(EventKeys[5]).equal("eventData");
				expect(EventKeys[1]).equal("eventId");
				expect(EventKeys[6]).equal("eventData.jobInstance");
				expect(EventKeys[12]).equal("eventData.optionalParameters.inc");
				expect(EventKeys.length).equal(18);
						
            });
			/*
				Test reads a Template .js file in order to return an array. If the template is changed this test may break.
			*/
			it('Read second Json Event from EiffelEvent.js VersionedEiffelEvent() and pick at intervals to check', function () {
            
			var jsonArray = [EiffelEvent.VersionedEiffelEvent()] 
			
			var EventKeys = new Array()
			EventKeys = JSONReader.getJSONKeys(jsonArray)		
									
                expect(EventKeys[0]).equal("eiffelMessageVersions");
				expect(EventKeys[9]).equal("eiffelMessageVersions.3.1.2.0.16.eventData.jobExecutionId");
				expect(EventKeys[15]).equal("eiffelMessageVersions.3.1.2.0.16.eventData.optionalParameters.track");
				expect(EventKeys[20]).equal("eiffelMessageVersions.2.1.2.0.16");
				expect(EventKeys[29]).equal("eiffelMessageVersions.2.1.2.0.16.eventData.resultCode");
				expect(EventKeys.length).equal(58);
						
            });
        });
    });
});