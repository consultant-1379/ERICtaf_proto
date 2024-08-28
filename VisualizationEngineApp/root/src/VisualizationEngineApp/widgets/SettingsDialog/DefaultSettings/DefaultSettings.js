define([
    'jscore/core',
    './DefaultSettingsView'
], function (core, View) {

    return core.Widget.extend({
        View: View,

        init: function () {
            this.boxChecked = false;       
        },
		
        onViewReady: function () {
            var el = this.getElement().find('.eaVisualizationEngineApp-defaultSettingsCheckBox');		
			
			el.addEventHandler('click', function (e) {
				this.boxChecked = document.getElementsByClassName('eaVisualizationEngineApp-defaultSettingsCheckBox')[0].checked;
				this.trigger('DefaultSettingsEvent', this.boxChecked);				
            }.bind(this));
        }
		
    });
});