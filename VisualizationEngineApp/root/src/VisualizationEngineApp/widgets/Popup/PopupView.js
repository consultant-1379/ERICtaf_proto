define([
    'jscore/core',
    'jscore/ext/utils/base/underscore',
    'text!./Popup.html',
    'styles!./Popup.less'
], function(core, _, template, style) {
    return core.View.extend({
        getTemplate: function() {
            return template;
        },
        
        getStyle: function() {
            return style;
        },
        
        getContentElement: function() {
            return this.getElement().find('.eaVisualizationEngineApp-wPopup-content');
        },
        
        getCloseElement: function() {
            return this.getElement().find('.eaVisualizationEngineApp-wPopup-close');
        },
        
        setPopupContent: function(content) {
            if (typeof content === 'string') {
                var contentEl = core.Element.parse(content);
                this.getContentElement().append(contentEl);
            }
        },
        
        clearPopupContent: function() {
            _.each(this.getContentElement().children(), function(item) {
                item.detach();
            });
        },
        
        setPosition: function(position) {
            this.getElement().setStyle({
                top: position.y + "px",
                left: position.x + "px"
            });
        }
    });
});