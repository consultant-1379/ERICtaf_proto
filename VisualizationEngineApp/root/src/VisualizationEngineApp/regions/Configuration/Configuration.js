define([
    'jscore/core',
    './ConfigurationView',
    'app/config/config',
    'app/widgets/AddView/AddView'
], function (core, View, config, AddView) {

    return core.Region.extend({
        
        View: View,
    
        onStart: function () {
            var addView = new AddView();

            addView.addEventHandler('createNewView', function(view) {
                var regionSettings = {title: view, factory: config.getViewFactory(view), span: config.defaultViewSize};

                this.getEventBus().publish('createRegion', regionSettings);
            }, this);
            addView.attachTo(this.getElement());
        }
    });
});