/*global define, describe, it, expect */
define([
    'app/config/config',
    'jscore/ext/utils/base/underscore'
], function (config, _) {
    'use strict';

    describe("Config", function () {
        var allNames = [];
        
        // override inherent config.views with one made for test 
        config.views = [{
                name: 'PieView',
                factory: 'pieRegion',
                description: "Pie chart that shows statistical information"
            }, {
                name: 'FlowView',
                factory: 'flowRegion',
                description: "Shows new events in a flow"
            }];
        
        describe('Method getViewNames()', function () {
            it('should give us a non-empty list when called', function () {
                allNames = config.getViewNames();
                expect(allNames.length).to.not.equal(0);
            });
            it('should give all the view names in an array', function () {
                expect(allNames).to.deep.equal(["PieView", "FlowView"]);
            });
        }),
        
        /*
        describe('Fetching images', function () {
            it("should give us no errors when loading the images", function() {
                var img = document.createElement('img');
            
                _.each(allNames, function (n) {
                    img.src = config.getViewImage(n);
                });
            });
        }),
        */
        
        describe('Method getViewFactory()', function () {
            it("should give us the correct factories when called", function () {
                expect("pieRegion").to.be.equal(config.getViewFactory("PieView"));
                expect("flowRegion").to.be.equal(config.getViewFactory("FlowView"));
            });
            it("should not return anything if the name doesn't exist", function () {
                expect(undefined).to.be.equal(config.getViewFactory("FloorView"));
            });
        }),
        
        describe('Method getViewDescription()', function () {
            it("should give us the correct descriptions when called", function () {
                expect("Pie chart that shows statistical information").to.be.equal(config.getViewDescription("PieView"));
                expect("Shows new events in a flow").to.be.equal(config.getViewDescription("FlowView"));
            });
            it("should not return anything if the name doesn't exist", function () {
                expect(undefined).to.be.equal(config.getViewDescription("FloorView"));
            });
        });
    });
});
