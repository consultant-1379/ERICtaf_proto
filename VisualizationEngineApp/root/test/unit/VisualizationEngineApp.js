/*global define, describe, it, expect */
define([
	'app/VisualizationEngineApp'
], function (VisualizationEngineApp) {
    'use strict';

    describe('VisualizationEngineApp', function () {

        describe('Methods', function () {
            
            it('init()', function () {
                var app = new VisualizationEngineApp();
                
                expect(app.activeRegions).to.be.empty;
            });
        });
    });
});