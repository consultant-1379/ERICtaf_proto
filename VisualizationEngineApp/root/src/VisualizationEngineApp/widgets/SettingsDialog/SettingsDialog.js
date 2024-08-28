define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    'app/widgets/Icon/Icon',
    'widgets/Dialog',
    './SettingsDialogView'
], function (core, _, Icon, Dialog, View) {
    'use strict';

    return core.Widget.extend({

        View: View,

        init: function() {
            this.dialog = new Dialog({
                header: this.options.header,
                secondaryButtonCaption: this.options.secondaryCaption
            });
            this.dialog.hidePrimaryButton();
            this.dialog.getSecondaryButton().addEventHandler('click', function () {
                this.dialog.hide();
                this.trigger('okClicked');
            }.bind(this));
            this.dialog.getSecondaryButton().setModifier('color', 'green');
            this.dialog.getSecondaryButton().setModifier('large');
        },

        onViewReady: function() {
            var close = new Icon({icon: "ebIcon_close", text: "Close", onclickevent: function() {
                this.hideDialog();
            }.bind(this)});
            close.attachTo(this.getElement().find('.eaVisualizationEngineApp-wVESettingsDialog-close'));

            _.map(this.options.views, function (view) {
                view.attachTo(this.getElement());
            }.bind(this));
            this.dialog.setContent(this.getElement());
        },

        addView: function(view) {
            view.attachTo(this.getElement());
        },

        showDialog: function() {
            this.dialog.show();
        },

        hideDialog: function() {
            this.dialog.hide();  
        }
    });
});