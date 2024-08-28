define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    './ViewSelectView',
    'app/config/config',
    'widgets/SelectBox'
], function (core, _, View, config, SelectBox) {
    'use strict';

    return core.Widget.extend({

        View: View,

        init: function () {
            // var items = _.map(config.views, function (view) {
            //     return view.name;
            // });

            this.selectBox = new SelectBox({
                value: config.views[0].name,
                items: config.getViewNames()
            });
        },

        onViewReady: function () {
            var dropdown = this.getElement().find(".eaVisualizationEngineApp-wVEViewSelect-dropdown");
            
            this.selectBox.attachTo(dropdown);
            
            // View description handling
            var descriptionText = this.getElement().find(".eaVisualizationEngineApp-wVEViewSelect-descriptionText");
            descriptionText.setText(config.getViewDescription(this.getValue()));
            this.selectBox.addEventHandler('change', function () {
                descriptionText.setText(config.getViewDescription(this.getValue()));
            }, this);
            
            // View thumbnail handling
            var thumbnailImage = this.getElement().find(".eaVisualizationEngineApp-wVEViewSelect-thumbnailImage");
            thumbnailImage.setAttribute("src", config.getViewImage(this.getValue()));
            this.selectBox.addEventHandler('change', function () {
                thumbnailImage.setAttribute("src", config.getViewImage(this.getValue()));
            }, this);
        },

        getValue: function () {
            return this.selectBox.getValue();
        }
    });
});