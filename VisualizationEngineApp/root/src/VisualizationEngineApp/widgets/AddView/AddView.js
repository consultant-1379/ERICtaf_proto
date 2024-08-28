define([
    'jscore/core',
    './AddViewView',
    'app/widgets/SettingsDialog/SettingsDialog',
    'app/widgets/SettingsDialog/ViewSelect/ViewSelect'
], function (core, View, SettingsDialog, ViewSelect) {
    'use strict';

    return core.Widget.extend({
        View: View,

        init: function () {
            this.viewSelect = new ViewSelect();

            this.settingsDialog = new SettingsDialog({header: 'Select view', secondaryCaption: 'OK', views: [this.viewSelect]});
        },

        onViewReady: function () {
            var el = this.getElement().element;

            el.addEventListener('click', function () {
                this.settingsDialog.showDialog();
            }.bind(this));

            this.settingsDialog.addEventHandler('okClicked', function () {
                this.trigger('createNewView', this.viewSelect.getValue());
            }, this);
        }
    });
});