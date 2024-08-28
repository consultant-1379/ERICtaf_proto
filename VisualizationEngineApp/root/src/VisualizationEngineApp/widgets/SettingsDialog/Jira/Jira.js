define([
    'jscore/core',
    './JiraView',
    'widgets/Button',
    'jscore/ext/utils/base/underscore',
    'widgets/Notification',
    'jscore/base/jquery'
], function(core, View, Button, _, Notification, $) {
    return core.Widget.extend({
        View: View,

        init: function () {
            this.button = new Button({
                caption: 'Load JIRA data',
                modifiers: [
                    {name: 'wMargin'},
                    {name: 'default'},
                    {name: 'color_darkBlue'}
                ],
                enabled: true
            });
        },

        onAttach: function() {
         
        },

        onViewReady: function() {
            this.epicIdInput = this.getElement().find("#epic");

            this.button.addEventHandler('click', function() {
                if(!this.fieldsEmpty()) {
                    this.addJiraSubscription(this.epicIdInput.getValue());
                    this.addEventSubscription('domainId:test.execution');
                }
            }.bind(this));

            this.epicIdInput.addEventHandler('keyup', function (data) {            
                if (data.originalEvent.keyCode === 13) {
                     this.addJiraSubscription(this.epicIdInput.getValue());
                     this.addEventSubscription('domainId:test.execution');
                }
            }.bind(this));

            this.button.attachTo(this.getElement().find(".eaVisualizationEngineApp-wVEJiraSubscription-buttonDiv"));
            
        },

        fieldsEmpty: function () {
            return this.epicIdInput.getValue() === "";
        },
        
        addEventSubscription: function (subscription) {
            this.trigger('addSubscriptionEvent', subscription);
        },

        addJiraSubscription: function(epicId) {
            this.trigger('jiraSubscriptionEvent', epicId);
        },

        showErrorMessage: function(error) {
            var userInput = error.serverData.epicId;
            $(".eaVisualizationEngineApp-wVEJiraSubscription-errorDiv").empty();
            var jiraErrorNotification = new Notification({
                label: "   No data for " + userInput,
                content: "error",
                color: "red",
                showCloseButton: false,
                autoDismiss: true,
                autoDismissDuration: 5000
            });
            jiraErrorNotification.attachTo(this.getElement().find(".eaVisualizationEngineApp-wVEJiraSubscription-errorDiv"));
        },

        preConfigure: function(subscription) {
            if (this.checkValue(subscription)) {
                this.addSubscription(subscription);
            }
        }
    });
});
