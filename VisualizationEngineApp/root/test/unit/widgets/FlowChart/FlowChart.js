/*global define, describe, it, expect */
define([
    'app/widgets/FlowChart/FlowChart',
    'jscore/ext/utils/base/underscore'
], function (FlowChart, _) {
    'use strict';

    describe('FlowChart', function () {

        describe('Methods', function () {
            
            it('getSettings()', function () {
                var retrievedSettings,
                    expectedSettings = {
                        edge_show_arrow: true,
                        node_draggable: true,
                        data: []
                    };
                var obj = new FlowChart();
               
                retrievedSettings = obj.getSettings();

                expect(expectedSettings).to.eql(retrievedSettings);
            });
            
            it('handleEvent(eventType: String)', function () {
                var event1 = {
                    "domainId": "kista",
                    "eventId": "d45cca9f-94ca-4fab-a83c-386b3c1660ff",
                    "eventType": "EiffelArtifactModifiedEvent2",
                    "eventTime": "2013-06-28T08:56:24.992Z",
                    "eventSource": "FlowEventsDemo"
                };
                var obj = new FlowChart();
                
                // override the refresh function
                obj.refresh = function () {};
                
                expect(obj.getSettings().data[0]).to.equal(undefined);
                obj.handleEvent(event1);
                expect(obj.getSettings().data[0].name).to.equal(event1.eventId);

            });
        });
    });
});