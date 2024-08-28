define([
    'jscore/core',
    'app/widgets/Icon/Icon',
    './PopupView'
], function(core, Icon, View) {
    return core.Widget.extend({
        View: View,
        
        onViewReady: function() {
            this.view.setPopupContent(this.options.content);
            this.view.setPosition(this.options.position);
            var close = new Icon({icon: "ebIcon_close", text: "Close", onclickevent: function() {
                this.trigger('close');
            }.bind(this)});
            close.attachTo(this.view.getCloseElement());
        },
        
        updateContent: function(content) {
            this.view.clearPopupContent();
            this.view.setPopupContent(content);
        }
    });
});