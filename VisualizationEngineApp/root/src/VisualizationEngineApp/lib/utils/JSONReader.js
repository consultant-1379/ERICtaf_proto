/*
JSON reader is designed to read the JSON message keys and return them as an array

*/

define(['jscore/ext/utils/base/underscore'],function(_) {
    'use strict';

    return {
		/*
		Method to get all keys in JSON message 
		@return EventKeys --  an array of keys. 
		*/	
		getJSONKeys: function (jsonArray) {
            var eventKeys = [];
			var	returnKey = new String();				
			for(var i=0;i<jsonArray.length;i++){
				var obj = [];
				obj = jsonArray[i];
				for(var key in obj){			
					eventKeys.push(key);
					if(typeof obj[key] === 'object'){
					eventKeys = this.getJSONKeysRecursive(key,eventKeys,obj[key]);
					}
				}
			}		
			return _.uniq(eventKeys, false)
        },
		/*
		Method to recursively get all keys from a JSON object array
 		*/
		getJSONKeysRecursive: function (key,eventKeys,jsonArray) {	
			var obj = jsonArray;					
					for(var key2 in obj){				
						var returnKey = key+"."+key2
						eventKeys.push(returnKey);
						if(typeof obj[key2] === 'object'){
						eventKeys = this.getJSONKeysRecursive(returnKey,eventKeys,obj[key2]);
						}
					}
							
			return _.uniq(eventKeys, false)
        },
		/*
		Print String values to web console
		*/
		printToConsole: function (printableArray) {	
			 for(var val in printableArray) {
				 console.info(printableArray[val]);
			 }
			
        }
		
		
    };
});