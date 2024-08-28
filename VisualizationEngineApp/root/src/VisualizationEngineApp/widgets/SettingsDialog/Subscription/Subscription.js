define([
    'jscore/core',
    './SubscriptionView',
    './ExistingSubscriptionRow/ExistingSubscriptionRow',
    'widgets/Button',
    'widgets/InfoPopup',
    'jscore/ext/utils/base/underscore'
], function(core, View, ExistingSubscriptionRow, Button, InfoPopup, _) {
    return core.Widget.extend({
        View: View,

        init: function () {
            this.buttonWidget = new Button({
                caption: 'Add subscription',
                modifiers: [
                    {name: 'wMargin'},
                    {name: 'default'},
                    {name: 'color_darkBlue'}
                ],
                enabled: false
            });
            
            this.subList = [];
        },

        onAttach: function() {
            this.buttonWidget.attachTo(this.subDiv);
        },

        onViewReady: function() {
            this.subInput = this.getElement().find(".eaVisualizationEngineApp-wVESubscription-input");
            this.subDiv = this.getElement().find(".eaVisualizationEngineApp-wVESubscription-newDiv");
            this.subExistingDiv = this.getElement().find(".eaVisualizationEngineApp-wVESubscription-existingDiv");     
            this.subInput.addEventHandler('keyup', function (data) {             
				if (this.checkValue(this.subInput.getValue())) {
                    this.buttonWidget.enable();
                    
                    // allow 'enter'-key to add subscription
                    if (data.originalEvent.keyCode === 13) {
                        this.addSubscription(this.subInput.getValue());
                    }
                }
                else {
                    this.buttonWidget.disable();
                }
            }, this);

            this.buttonWidget.addEventHandler('click', function (e) {
                this.addSubscription(this.subInput.getValue());
            }, this);

            this.infoPopupWidget = new InfoPopup({
                content: 'OR - make two subscriptions<br />&nbsp;&nbsp;key1:value1<br />&nbsp;&nbsp;key2:value2<br />AND<br />&nbsp;&nbsp;key1:value1&&key2:value2<br />NOT<br />&nbsp;&nbsp;!key:value'
            });
            this.infoPopupWidget.attachTo(this.getElement().find('.eaVisualizationEngineApp-wVESubscription-info'));
        },

        checkValue: function (v) {
            if ((v.search(/\w\:[\.\w]/) !== -1 || v === "all") && this.subList.indexOf(v) === -1) {
                return true;
            }
            return false;
        },
        
        addSubscription: function (inputVal) {
            // send subscription request to server
            this.trigger('addSubscriptionEvent', inputVal);
            // add to local list of items
            this.subList.push(inputVal);
            // and update the subscription list to add the new item
            var row = new ExistingSubscriptionRow({name: inputVal});
            row.attachTo(this.subExistingDiv);
            row.getElement().find(".eaVisualizationEngineApp-wVESubscriptionRow-btn").addEventHandler("click", function() {
                this.removeSubscription(row);
            }, this);
            // then disable the button
            this.buttonWidget.disable();
        },

        removeSubscription: function (row){
            var subscriptionText = row.options.name;
            //call server to remove the subscription
            this.trigger('removeSubscriptionEvent', subscriptionText);
            //remove from subList
            this.subList = _.without(this.subList, subscriptionText);
            //remove the div that contains the remove button
            row.destroy();
        },
		preConfigure: function(subscription) {
			if (this.checkValue(subscription)) {
				this.addSubscription(subscription);
				}
		}
    });
});
