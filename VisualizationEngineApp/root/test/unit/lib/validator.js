/*global define, describe, it, expect */
define([
    'app/lib/validator',
    'jscore/ext/utils/base/underscore'
], function (validator, _) {
    'use strict';

    describe('validator', function () {
        
        describe('Methods', function () {
            
            it('validate(data: Object)', function () {
                var validData = {
                        arg1: 'test',
                        arg2: '5',
                    },
                    invalidData = {
                        arg1: 5,
                        arg2: 0
                    };
                
                validator.config = {
                    arg1: ['isString'],
                    arg2: ['isString', 'isNumericString']
                };
                
                validator.checks.isString = {
                    validate: function(value) {
                        return _.isString(value);
                    },
                    instructions: "the value must be a string"
                };
                
                validator.checks.isNumericString = {
                    validate: function(value) {
                        return /^[0-9]/i.test(value);
                    },
                    instructions: "the value must be a numeric string"
                };
                                                
                expect(validator.validate(validData)).to.be.true;
                expect(validator.validate(invalidData)).to.be.false;
            });
        });
    });
});