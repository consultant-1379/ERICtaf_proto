define(function() {
    'use strict';

    return {

      getJobStatusMessage: function() {
             return {
                'domainId': 'kista v2.1.2',
                    'eventId': '0f1a2fe5-2f65-4f71-88b1-d46ee3a084f4',
                    'eventTime': '2013-08-15T13:53:45.000Z',
                    'eventType': 'EiffelJobFinishedEvent',
                    'inputEventIds': [],
                    'eventData': {
                        'jobInstance': 'LMBaselineBuilder_rnc_main_89.1_Trigger',
                        'jobExecutionId': '241',
                        'resultCode': 'SUCCESS',
                        'optionalParameters': {
                            'org': 'rnc',
                            'proj': 'main',
                            'inc': '89.1',
                            'track': 'R89A',
                            'ccs': '/proj/ewcdmaci_dev/cis/cilogs/rnc/main/89.1/lmbuild/common/select_config_cs.txt',
                            'dw2cs': '/proj/ewcdmaci_dev/cis/cilogs/rnc/main/89.1/lmbuild/common/main_rnc_db_89.1.txt',
                            'arcprofile': '1',
                            'forced':'true'
							}
						}
            };
        },
    
    getConfidenceLevelUpdateMessage: function() {
    	return {
	            "domainId": "vm18domain", 
	            "eventData": {
	                "confidenceLevels": {
	                    "ACCEPTANCE_TESTING_COMPLETE": "SUCCESS"
	                }, 
	                "gav": {
	                    "artifactId": "ERICeric", 
	                    "groupId": "com.ericsson", 
	                    "version": ""
	                }, 
	                "jobInstance": "aJob",
	                "optionalParameters": {}
	            }, 
	            "eventId": "866be99d-a9bc-485e-98ea-8b9a18675ab7", 
	            "eventTime": "2013-09-25T16:24:22.190Z", 
	            "eventType": "EiffelConfidenceLevelModifiedEvent"
    	};
    }
    };
});