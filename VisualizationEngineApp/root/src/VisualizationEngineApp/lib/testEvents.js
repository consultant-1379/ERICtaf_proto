define(function() {
    'use strict';

    return {
        Event1: function() {
            return {
                "domainId": "test.execution",
				"eventId": "867de639-f963-4de7-ae77-2215ee32e456",
				"eventTime": "2013-10-30T12:05:58.513Z",
				"eventType": "EiffelJobStartedEvent",
				"inputEventIds": [],
				"eventData": {
					"jobInstance": "TAF Jenkins Job",
					"jobExecutionId": "123",
					"jobExecutionNumber": "5",
					"optionalParameters": {}
				}
            };
        },
        
        Event2: function() {
            return {
                "domainId": "test.execution",
				"eventId": "e1c967dd-9366-41e6-b405-db4af8a75915",
				"eventTime": "2013-10-30T12:05:59.015Z",
				"eventType": "EiffelJobStepStartedEvent",
				"inputEventIds": [],
				"eventData": {
					"expectedDurationInSeconds": "30",
					"expectedNumberOfChildren": "1",
					"eventId": "8160cfff-6bcc-4414-bcff-acf03a2843e6",
					"parentId": "867de639-f963-4de7-ae77-2215ee32e456",
					"jobInstance": "TAF Jenkins Job",
					"jobExecutionId": "123",
					"jobExecutionNumber": "5",
					"optionalParameters": {}
				}
            };
        },
        
        Event3: function() {
            return {
                "domainId": "test.execution",
				"eventId": "5b1dba58-dd45-4159-8a2d-6b742b6c2397",
				"eventTime": "2013-10-30T12:05:59.516Z",
				"eventType": "EiffelTestSuiteStartedEvent",
				"inputEventIds": [],
				"eventData": {
					"type": "Functional",
					"name": "MySuite",
					"eventId": "bc9becb2-e4da-495f-b029-d23e178bf918",
					"parentId": "e1c967dd-9366-41e6-b405-db4af8a75915",
					"jobInstance": "TAF Jenkins Job",
					"jobExecutionId": "123",
					"jobExecutionNumber": "5",
					"optionalParameters": {}
				}
            };
        },
        
            
        Event4: function() {
            return {
                "domainId": "test.execution",
				"eventId": "0e3ab5b7-2bc3-44e8-a82e-54b8ea40eec5",
				"eventTime": "2013-10-30T12:05:56.007Z",
				"eventType": "EiffelTestCaseStartedEvent",
				"inputEventIds": [],
				"eventData": {
					"testId": "CIP-123",
					"testName": "MyTest",
					"parameters": {
						"name": "key",
						"value": "value"
					},
					"eventId": "2dfb9fce-5403-4a47-b9f8-8427212163e2",
					"parentId": "5b1dba58-dd45-4159-8a2d-6b742b6c2397",
					"jobInstance": "TAF Jenkins Job",
					"jobExecutionId": "123",
					"jobExecutionNumber": "5",
					"optionalParameters": {}
				}
                
            };
        },
        
        Event5: function() {
            return {
                "domainId": "test.execution",
				"eventId": "4aeb8e65-2b26-465f-aa40-0ef4fffb5000",
				"eventTime": "2013-10-30T12:05:56.508Z",
				"eventType": "EiffelTestCaseFinishedEvent",
				"inputEventIds": ["0e3ab5b7-2bc3-44e8-a82e-54b8ea40eec5"],
				"eventData": {
					"eventId": "64e36f07-ad01-4c7d-a48c-c7eaee0064bc",
					"parentId": "5b1dba58-dd45-4159-8a2d-6b742b6c2397",
					"resultCode": "SUCCESS",
					"logReferences": {},
					"optionalParameters": {}
				}
            };
        },
		
		 Event6: function() {
            return {
                "domainId": "test.execution",
				"eventId": "3f760b82-7aa5-4d1c-94e6-2115dfffa541",
				"eventTime": "2013-10-30T12:05:57.010Z",
				"eventType": "EiffelTestSuiteFinishedEvent",
				"inputEventIds": ["5b1dba58-dd45-4159-8a2d-6b742b6c2397"],
				"eventData": {
					"eventId": "5326807a-0dd7-4bf4-b7f3-96de1885221c",
					"parentId": "e1c967dd-9366-41e6-b405-db4af8a75915",
					"resultCode": "SUCCESS",
					"logReferences": {},
					"optionalParameters": {}
				}
            };
        },
        
        Event7: function() {
            return {
               "domainId": "test.execution",
				"eventId": "c2e13ae7-ee93-41d7-a12e-21ba8aae1741",
				"eventTime": "2013-10-30T12:05:57.511Z",
				"eventType": "EiffelJobStepFinishedEvent",
				"inputEventIds": ["e1c967dd-9366-41e6-b405-db4af8a75915"],
				"eventData": {
					"eventId": "1eacde91-3abb-46a9-b332-21a6f6acfbd3",
					"parentId": "867de639-f963-4de7-ae77-2215ee32e456",
					"resultCode": "SUCCESS",
					"logReferences": {},
					"optionalParameters": {}
				}
            };
        },
		Event8: function() {
            return {
                "domainId": "test.execution",
				"eventId": "2f927e85-05b1-4250-8bbd-bf2eb5e3fb7f",
				"eventTime": "2013-10-30T12:05:58.012Z",
				"eventType": "EiffelJobFinishedEvent",
				"inputEventIds": ["867de639-f963-4de7-ae77-2215ee32e456"],
				"eventData": {
					"jobInstance": "TAF Jenkins Job",
					"jobExecutionId": "123",
					"jobExecutionNumber": "5",
					"resultCode": "SUCCESS",
					"logReferences": {},
					"optionalParameters": {}
				}
            };
        },
		Event9: function() {
            return {
                "domainId": "test.execution",
				"eventId": "4aebbe65-2b26-465f-aa40-0ef4fffb5000",
				"eventTime": "2013-10-30T12:05:56.508Z",
				"eventType": "EiffelTestCaseFinishedEvent",
				"inputEventIds": ["0e3ab5b7-2bc3-44e8-a82e-54b8ea40eec5"],
				"eventData": {
					"eventId": "64e36f07-ad01-4c7d-a48c-c7eaee0064bc",
					"parentId": "5b1dba58-dd45-4159-8a2d-6b742b6c2397",
					"resultCode": "SUCCESS",
					"logReferences": {},
					"optionalParameters": {}
				}
            };
        },
		 Event10: function() {
            return {
                "domainId": "test.execution",
				"eventId": "3f760b82-7aa5-4d1c-94e6-2115dfffa541",
				"eventTime": "2013-10-30T12:05:57.010Z",
				"eventType": "EiffelTestSuiteFinishedEvent",
				"inputEventIds": ["5b1dba58-dd45-4159-8a2d-6b742b6c2397"],
				"eventData": {
					"eventId": "5326807a-0dd7-4bf4-b7f3-96de1885221c",
					"parentId": "c2e13ae7-ee93-boom-a12e-21ba8aae1741",
					"resultCode": "SUCCESS",
					"logReferences": {},
					"optionalParameters": {}
				}
            };
        },
        
        Event11: function() {
            return {
               "domainId": "test.execution",
				"eventId": "c2e13ae7-ee93-boom-a12e-21ba8aae1741",
				"eventTime": "2013-10-30T12:05:57.511Z",
				"eventType": "EiffelJobStepFinishedEvent",
				"inputEventIds": ["e1c967dd-936n-41e6-b405-db4af8a75915"],
				"eventData": {
					"eventId": "1eacde91-3abb-46a9-b332-21a6f6acfbd3",
					"parentId": "867de639-f963-4de7-ae77-2215ee32e456",
					"resultCode": "SUCCESS",
					"logReferences": {},
					"optionalParameters": {}
				}
            };
        }
    };
});