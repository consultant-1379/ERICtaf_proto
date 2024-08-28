define([
    'jscore/ext/utils/base/underscore'
], function (_) {
    var thumbBase = "./resources/img/";
    return {
        views: [
            {
                name: 'ListView',
                factory: 'listRegion',
                description: "Simple view for debugging. Shows event type and timestamp of new events",
                thumbnail: thumbBase+"thumb_listview.jpg"
            }, {
                name: 'PieView',
                factory: 'pieRegion',
                description: "Pie chart that shows statistical information on event type",
                thumbnail: thumbBase+"thumb_pieview.jpg"
            }, {
                name: 'FlowView',
                factory: 'flowRegion',
                description: "Shows new events in a flow. Uses arrows to connect those events that are related to each other",
                thumbnail: thumbBase+"thumb_flowview.jpg"
            }, {
                name: 'JobView',
                factory: 'jobRegion',
                description: "Clusters events together on a user-provided criteria and shows only the latest event",
                thumbnail: thumbBase+"thumb_jobview.jpg"
            }, {
                name: 'TableView',
                factory: 'tableRegion',
                description: "A table for collecting and manipulating new events",
                thumbnail: thumbBase+"thumb_tableview.jpg"
            }, {
                name: 'TreeView',
                factory: 'treeRegion',
                description: "A tree view for collecting and manipulating new events",
                thumbnail: thumbBase+"thumb_underconstruction.gif"
			}, {
                name: 'TreeMap',
                factory: 'treeMapRegion',
                description: "A Tree Map",
                thumbnail: thumbBase+"thumb_treemap.jpg"
            },
        ],

        defaultViewSize: 2,

        getViewNames: function() {
            return _.map(this.views, function (view) {
                return view.name;
            });
        },

        getViewFactory: function(name) {
            var item = (_.find(this.views, function (view) {
                return view.name === name;
            }));
            return item ? item.factory : undefined;
        },
        
        getViewDescription: function (name) {
            var item = (_.find(this.views, function (view) {
                return view.name === name;
            }));
            return item ? item.description : undefined;
        },
        
        getViewImage: function (name) {
            var item = (_.find(this.views, function (view) {
                return view.name === name;
            }));
            return item ? item.thumbnail : undefined;
        }
    };
});