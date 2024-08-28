define([
    'jscore/ext/utils/base/underscore'
], function(_) {
    'use strict';
    
    return {
        
        config: {},
        
        checks: {},
        
        messages: [],
        
        validate: function(data) {
            var msg, types, isOk,
                checkValid = function(type) {
                    if (!this.checks[type]) {
                        throw new Error('No handler to validate type ' + i);
                    }

                    isOk = this.checks[type].validate(data[i]);
                    if (!isOk) {
                        msg = 'Invalid value for *' + i + '*, ' + this.checks[type].instructions;
                        this.messages.push(msg);
                    }
                };
            
            this.messages = [];
            
            for (var i in data) {
                if (data.hasOwnProperty(i)) {
                    types = this.config[i];
                    
                    if (!types) {
                        continue;
                    }
                    _.map(types, checkValid, this);
                }
            }
            
            return !this.hasErrors();
        },
        
        hasErrors: function() {
            return this.messages.length !== 0;
        }
    };
});