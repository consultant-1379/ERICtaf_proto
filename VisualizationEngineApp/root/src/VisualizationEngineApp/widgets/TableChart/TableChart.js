define(['jscore/core',
    './TableChartView',
    'widgets/Table'
], function(core, View, Table) {
    'use strict';

    return core.Widget.extend({
        View: View,
        onAttach: function() {
            this.tableWidget =
                    new Table({columns: [
                    {title: "Time", attribute: "timestamp"},
                    {title: "Ev  entType", attribute: "evtype"},
                    {title: "Event data", attribute: "eventdata"}
                ]});

            this.tableWidget.attachTo(this.getElement());


        },
        updateTableData: function(event) {
            //ToDo: implement function.

            var data = [{timestamp: event.eventTime,
                    evtype: event.eventType, eventdata: event.eventData.jobInstance}];

            this.tableWidget.addData(data);
        }
    });

});
