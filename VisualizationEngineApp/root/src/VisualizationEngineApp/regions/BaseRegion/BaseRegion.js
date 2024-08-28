define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    'app/widgets/RegionHandler/RegionHandler'
], function (core, _, RegionHandler) {
    return core.Region.extend({

        init: function () {
            this.eventCollection = this.options.collection;
        },

        onStart: function () {
			this.atStart = true;
            this.regionHandler = new RegionHandler({title: this.options.title, span: this.options.span});
            this.regionHandler.attachTo(this.getElement().find('header'));
            
            this.regionHandler.addEventHandler('changeSizeEvent', function (e) {
                this.getEventBus().publish('changeSize', {region: this, value: e});
            }, this);
			
            this.regionHandler.addEventHandler('removeRegionEvent', function () {
                this.getEventBus().publish('killMe', this);
            }, this);

            this.regionHandler.addEventHandler('addSubscriptionEvent', function (topic) {
				if(this.atStart){
					this.getElement().find('[class*="eaVisualizationEngineApp-r"] h1').remove();
					this.atStart = false;
				}
                this.getEventBus().publish('subscribeMessageBus', {
                    topic: topic, 
                    region: this.uid
                });
            }, this);

            this.regionHandler.addEventHandler('removeSubscriptionEvent', function (topic) {
                this.getEventBus().publish('unsubscribeMessageBus', {
                    topic: topic,
                    region: this.uid
                });
            }, this);

            this.regionHandler.addEventHandler('jiraSubscriptionEvent', function(epicId) {
                if (this.atStart) {
                    this.getElement().find('[class*="eaVisualizationEngineApp-r"] h1').remove();
                    this.atStart = false;
                }
                this.getEventBus().publish('subscribeJira', {
                    epicId: epicId,
                    region: this.uid
                });
            }, this);
                        
            this.eventCollection.addEventHandler('add', function (model) {
                // Is the newly added model for this region?
                if (_.contains(model.getAttribute('destination'), this.uid)) {
                    this.onNewModel(model);
                }
            }, this);

            this.eventCollection.addEventHandler('change', function (model) {
                // Is the newly added model for this region?
                if (_.contains(model.getAttribute('destination'), this.uid)) {
                    this.onNewModel(model);
                }
            }, this);

            this.getEventBus().subscribe('jiraError', function(data) {
                this.regionHandler.propogateJiraError(data);
            }.bind(this));
			
            this.afterOnStart();
        },

        afterOnStart: function () {
        },
		
		/*
		@returns the regionHandler
		*/
		getRegionHandler: function () {
			return this.regionHandler;
        },

        onNewModel: function(model) {			
        }
		

    });
});
