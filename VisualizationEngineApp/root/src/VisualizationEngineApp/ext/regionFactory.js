define([
    'jscore/ext/utils/base/underscore',
    'app/ext/resizable',
    'app/regions/EventList/EventList',
    'app/regions/PieChart/PieChart',
    'app/regions/FlowChart/FlowChart',
    'app/regions/JobChart/JobChart',
    'app/regions/TableChart/TableChart',
	'app/regions/TreeChart/TreeChart',
    'app/regions/TreeMap/TreeMap'
], function(_, resizable, ListRegion, PieChartRegion, FlowRegion, JobRegion, TableRegion, TreeRegion, TreeMapRegion) {
    'use strict';
    
    function RegionFactory() {}
    
    RegionFactory.create = function(type, options) {
        var newRegion;
            
        if(typeof RegionFactory[type] !== 'function') {
            throw new Error('Can\'t create ' + type + ', factory method doesn\'t exist');
        }
        
        newRegion = new RegionFactory[type](options);
        
        return newRegion;
    };
    
    // Creates a list region
    RegionFactory.listRegion = function(options) {
        var listRegion;
        
        listRegion = new ListRegion({context: options.context, collection: options.collections.eventCollection, title: options.title, span: options.span});
        listRegion.getHashPart = options.factory + '/' + options.span;

        // Make the list region resizable through our resizable extension
        // _.extend(listRegion, resizable);
        
        return listRegion;
    };
    
    // Creates a pie region
    RegionFactory.pieRegion = function(options) {
        var pieRegion;
        
        pieRegion = new PieChartRegion({context: options.context, collection: options.collections.eventCollection, title: options.title, span: options.span});
        pieRegion.getHashPart = options.factory + '/' + options.span;
        // Make the pie region resizable through our resizable extension
        // _.extend(pieRegion, resizable);

        return pieRegion;
    };

    // Creates a flow region
    RegionFactory.flowRegion = function(options) {
        var flowRegion;

        flowRegion = new FlowRegion({context: options.context, collection: options.collections.eventCollection, title: options.title, span: options.span});
        flowRegion.getHashPart = options.factory + '/' + options.span;
        // Make the flow region resizable through our resizable extension
        // _.extend(flowRegion, resizable);
        
        return flowRegion;
    };
    
    // Creates a flow region
    RegionFactory.jobRegion = function(options) {
        var jobRegion;

        jobRegion = new JobRegion({context: options.context, collection: options.collections.eventCollection, title: options.title, span: options.span});
        jobRegion.getHashPart = options.factory + '/' + options.span;
        // Make the flow region resizable through our resizable extension
        //_.extend(jobRegion, resizable);
        
        return jobRegion;
    };

    // Creates a table region
    RegionFactory.tableRegion = function(options) {
        var tableRegion;

        tableRegion = new TableRegion({context: options.context, collection: options.collections.eventCollection, title: options.title, span: options.span});
        tableRegion.getHashPart = options.factory + '/' + options.span;
        
        return tableRegion;
    };
	
	 // Creates a tree region
    RegionFactory.treeRegion = function(options) {
        var treeRegion;

        treeRegion = new TreeRegion({context: options.context, collection: options.collections.eventCollection, title: options.title, span: options.span});
        treeRegion.getHashPart = options.factory + '/' + options.span;
        
        return treeRegion;
    };

    // Creates a tree map region
    RegionFactory.treeMapRegion = function(options) {
        var treeMapRegion;

        treeMapRegion = new TreeMapRegion({context: options.context, collection: options.collections.eventCollection, title: options.title, span: options.span});
        treeMapRegion.getHashPart = options.factory + '/' + options.span;
        
        return treeMapRegion;
    };
    
    return RegionFactory;
});
