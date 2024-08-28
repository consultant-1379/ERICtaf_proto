/*global define, describe, it, expect */
define([
    'app/widgets/RegionHandler/RegionHandler'
], function (RegionHandler) {
    'use strict';

    describe('RegionHandler', function () {

        describe('Methods', function () {
            
            it('getData()', function () {
                var handler = new RegionHandler({title: 'Testing'}),
                    data = handler.getData(); 
                
                expect(data).to.be.an('object');
                expect(data).to.have.property('title', 'Testing');
            });
        });
    });
});