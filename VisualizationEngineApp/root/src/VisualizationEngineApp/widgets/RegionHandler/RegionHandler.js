define([
    'jscore/core',
    './RegionHandlerView',
    'widgets/Dialog',
    'app/widgets/Icon/Icon',
    'app/widgets/SettingsDialog/SettingsDialog',
    'app/widgets/SettingsDialog/Resizing/Resizing',
    'app/widgets/SettingsDialog/Subscription/Subscription',
	'app/widgets/SettingsDialog/AspectRatio/AspectRatio',
	'app/widgets/SettingsDialog/DefaultSettings/DefaultSettings',
    'app/widgets/SettingsDialog/Clustering/Clustering',
    'app/widgets/SettingsDialog/Jira/Jira'
], function(core, View, Dialog, Icon, SettingsDialog, Resizer, Subscriber, AspectRatio, DefaultSettings, Clustering, Jira) {
    'use strict';

    return core.Widget.extend({
        View: View,

        init: function() {  
              
            // Resize function
            this.resizer = new Resizer({span: this.options.span, show_percent: true});
            
            // Subscription function
            this.subscriber = new Subscriber();

            this.jiraSubscriber = new Jira();
			
            // Aspect Ratio function
            this.aspectRatio = new AspectRatio();
			
			this.defaultSettings = new DefaultSettings();

            // Clustering function
            this.clustering = new Clustering();
			

            // Settings icon
            this.settingsIcon = new Icon({icon: "ebIcon_settings", text: "settings", onclickevent: function() { this.settingsDialog.showDialog(); }.bind(this) });

            // Removal icon
            this.removeIcon = new Icon({icon: "ebIcon_close", text: "remove", onclickevent: function(e) { this.trigger("removeRegionEvent", e); } });
            
            if (this.options.title === "jobRegion" || this.options.title === "JobView") {
                var columns = new Resizer({title: "Columns", span: 2});
                columns.addEventHandler('changeSizeEvent', function (e) {
                    this.trigger('changeColumnSize', e);
                }, this);

                var fadeout = new Resizer({'min':0, 'max':255, 'title': "Fadeout nodes in (h)"});
                fadeout.addEventHandler('changeSizeEvent', function (e) {
                    this.trigger('changeFadeoutEvent', e);
                }, this);

                this.settingsDialog = new SettingsDialog({header: (this.options.title + '').toUpperCase() + ' REGION SETTINGS', secondaryCaption: 'OK', views: [this.resizer, columns, fadeout, this.aspectRatio, this.subscriber, this.clustering, this.defaultSettings]});
            } 

            else if(this.options.title === "treeMapRegion" || this.options.title === "TreeMap") {
                this.settingsDialog = new SettingsDialog({header: (this.options.title + '').toUpperCase() + ' REGION SETTINGS', secondaryCaption: 'OK', views: [this.resizer, this.jiraSubscriber]});
            }

            else {
                this.settingsDialog = new SettingsDialog({header: (this.options.title + '').toUpperCase() + ' REGION SETTINGS', secondaryCaption: 'OK', views: [this.resizer, this.aspectRatio, this.subscriber, this.defaultSettings]});
            }
        },
                
        onViewReady: function() {			
            // Settings icon
            this.settingsIcon.attachTo(this.getElement().find(".regionBar-rightButton"));

            // Removal icon
            this.removeIcon.attachTo(this.getElement().find(".regionBar-rightButton"));

            // Event handlers
            this.eventHandlers();					
        },
		
        eventHandlers: function () {
            this.resizer.addEventHandler('changeSizeEvent', function (e) {
                    this.trigger('changeSizeEvent', e);
            }.bind(this));
			
			this.aspectRatio.addEventHandler('changeAspectRatioEvent', function (e) {
                    this.trigger('changeAspectRatioEvent', e);
					
            }.bind(this));
			
			this.defaultSettings.addEventHandler('DefaultSettingsEvent', function (e) {
                if(e === true){
					this.trigger('DefaultSettingsEvent', e);
					this.aspectRatio.hide();
				}
				else {
					this.trigger('DefaultSettingsEvent', e);
					this.aspectRatio.show();
				}							
            }.bind(this));
            
            this.removeIcon.addEventHandler('removeRegionEvent', function(e) {
                    this.trigger('removeRegionEvent', e);
            }.bind(this));

            this.subscriber.addEventHandler('addSubscriptionEvent', function(e) {
                    this.trigger('addSubscriptionEvent', e);
            }.bind(this));
            
            this.subscriber.addEventHandler('removeSubscriptionEvent', function(e) {
                    this.trigger('removeSubscriptionEvent', e);
            }.bind(this));
            
            this.subscriber.addEventHandler('getSubscriptionEvent', function(e) {
                    this.trigger('getSubscriptionEvent', e);
            }.bind(this)); 
            
            this.clustering.addEventHandler('clusterBaseEvent', function(e) {
            	this.trigger('clusterBaseEvent', e);
            }.bind(this));

            this.jiraSubscriber.addEventHandler('jiraSubscriptionEvent', function(e) {
                this.trigger('jiraSubscriptionEvent', e);
            }.bind(this));

            this.jiraSubscriber.addEventHandler('addSubscriptionEvent', function(e) {
                this.trigger('addSubscriptionEvent', e);
            }.bind(this));
        },

        propogateJiraError: function(data) {
            this.jiraSubscriber.showErrorMessage(data);
        },

        openSettingsDialog: function () {
            this.settingsDialog.showDialog();
        },
                
        getData: function() {
            return {title: this.options.title}; 
        },
		getSubscriber: function(){
		  return this.subscriber;
		}
    });
});
