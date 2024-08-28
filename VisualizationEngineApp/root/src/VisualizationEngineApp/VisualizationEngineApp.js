define([
    'jscore/core',
    'jscore/ext/mvp',
    'jscore/ext/utils/base/underscore',
    'app/VisualizationEngineAppView',
    'app/models/EventCollection',
    'app/models/JiraCollection',
    'app/models/ErrorCollection',
    'app/ext/regionFactory',
    'app/regions/Configuration/Configuration',
    'app/lib/CommunicationHandler'
], function (core, mvp, _, View, EventCollection, JiraCollection, ErrorCollection, regionFactory, ConfigurationRegion, CommunicationHandler) {

    return core.App.extend({

        View: View,

        init: function () {
            this.activeRegions = [];
            this.eventCollection = new EventCollection();
            this.jiraCollection = new JiraCollection();
            this.errorCollection = new ErrorCollection();
            this.communicationHandler = new CommunicationHandler({
                url: '',
                resource: 'api',
                collections: {
                    eventCollection: this.eventCollection,
                    jiraCollection: this.jiraCollection,
                    errorCollection: this.errorCollection
                }
            });

            this.jiraCollection.addEventHandler('add', function(data) {
                this.getEventBus().publish('newJiraData', data.attributes.message);
            }.bind(this));

            this.errorCollection.addEventHandler('add', function(data) {
                this.getEventBus().publish('jiraError', data.attributes);
                this.errorCollection.removeModel(data);
            }.bind(this));
        },

        onStart: function () {

            // Start listen to messages from the server
            this.communicationHandler.listen();

            // Subscribe for size change requests
            this.getEventBus().subscribe('changeSize', this.changeSize, this);
            // Subscribe to the event bus for user input
            this.getEventBus().subscribe('subscribeMessageBus', this.subscribeMessageBusEvent, this);
            // Subscribe to the event bus for when user removes subscription
            this.getEventBus().subscribe('unsubscribeMessageBus', this.unsubscribeMessageBusEvent, this);
            // Subscribe to the event bus for when the user clicks the remove region button
            this.getEventBus().subscribe('killMe', this.removeRegion, this);
            // Toggle highlight when element start or stop dragging
            this.getEventBus().subscribe('createRegion', this.createRegionAndShowSettings, this);
             // Subscribe to the event bus when a user requests JIRA information
            this.getEventBus().subscribe('subscribeJira', this.retrieveJiraData, this);

            window.addEventListener('resize', function() {
                 this.getEventBus().publish('resized');
            }.bind(this));

            window.addEventListener('scroll', function() {
                 this.getEventBus().publish('scroll');
            }.bind(this));

            // When the current tab or the whole browser is being closed, try to unsubscribe all subscriptions
            window.addEventListener('beforeunload', function() {
                _.map(this.activeRegions, function(region) {
                    this.communicationHandler.unsubscribeToServer(region.uid);
                }, this);
            }.bind(this));

            // Create and start the regions
            this.configurationRegion = new ConfigurationRegion({context: this.getContext()});
            this.configurationRegion.start(this.getElement().find('.eaVisualizationEngineApp-containerConfiguration'));

            if (window.tafDashboard !== null) {
                this.preconfigure(window.tafDashboard);
            }
        },

        removeRegion: function(region) {
            var regionPosition = this.getRegionIndex(region.uid);
			this.activeRegions = _.without(this.activeRegions, region);
            region.stop();
            this.communicationHandler.unsubscribeToServer(region.uid);
            // Delete all models associated only to the region being deleted
            this.eventCollection.deleteByDestination(region.uid);
        },

        changeSize: function (input) {
            var region = input.region, size = input.value;
            region.element.element.className = region.element.element.className.replace(/span\d*/, "span" + size);
            this.getEventBus().publish('resized');
        },

        createRegion: function(data) {
            var region;

            region = regionFactory.create(data.factory, {
                context: this.getContext(),
                collections: {
                    eventCollection: this.eventCollection
                },
                title: data.title,
                factory: data.factory,
                span: data.span
            });

            region.start(this.getElement().find('.eaVisualizationEngineApp-viewContainer'));

            this.activeRegions.push(region);
            return region;
        },
		/**
		 Method Returns the index of the region with uid that was specified to it.
		 @param regionId
		 @return int indexOf value in activeRegions
		*/
		getRegionIndex :function(regionId){
			for(var key in this.activeRegions) {
					if(this.activeRegions[key].uid === regionId) {
					   return this.activeRegions.indexOf(this.activeRegions[key]);
					}
			}
		},
        subscribeMessageBusEvent: function (subscription) {
            // Make a subscription for region source to the server for the topic the user entered  
            this.communicationHandler.subscribeToServer(subscription.region, subscription.topic);
        },

        unsubscribeMessageBusEvent: function (subscription) {
            // Make a unsubscription for region source to the server for the topic the user entered
            this.communicationHandler.unsubscribeToServer(subscription.region, subscription.topic);
        },

        createRegionAndShowSettings: function (data) {
            var region = this.createRegion(data);
            region.getRegionHandler().openSettingsDialog();
        },

        retrieveJiraData: function(subscription) {
            if(!this.epicInJiraCollection(subscription.epicId, subscription.region)) {
                this.communicationHandler.retrieveJiraData(subscription.region, subscription.epicId);
            }
        },

        // WHAT IF DATA CHANGES ON THE SERVER - ONLY A PAGE REFRESH WILL ALLOW NEW DATA TO BE LOADED
        // PERHAPS THIS SHOULD GO
        epicInJiraCollection: function(epicId, requestingRegion) {
            var isCachedEpic = false;
            this.jiraCollection.each(function(epic) {
                var cachedEpicId = epic._model.attributes.message.summary.split(":")[0];
                if(epicId.toLowerCase() === cachedEpicId.toLowerCase()) {
                    isCachedEpic = true;
                    epic._model.attributes.message.destination = requestingRegion;
                    this.getEventBus().publish('newJiraData', epic._model.attributes.message);
                }
            }.bind(this));
            return isCachedEpic;
        },

        preconfigure: function(config) {
            _.forEach(this.activeRegions, function(region) {
                this.removeRegion(region);
            }, this);
            _.forEach(config.regions, function(regionConfig) {
                var region = this.createRegion({
                    title: regionConfig.type,
                    factory: regionConfig.type,
                    span: regionConfig.span
                });
                var regionHandler = region.getRegionHandler();
                _.forEach(regionConfig.publish, function(events, subscriber) {
                    _.forEach(events, function(data, name) {
                        regionHandler[subscriber].trigger(name, data);
                    });
                });
                _.forEach(regionConfig.subscriptions, function(subscription) {
                    regionHandler.getSubscriber().preConfigure(subscription);
                });
            }, this);
        }
    });
});
