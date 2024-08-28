define([
    'jscore/core',
    'template!./EventList.html',
    'styles!./EventList.less',
    'app/widgets/EventListItem/EventListItem'
], function(core, template, style, EventListItem) {
    
    return core.View.extend({
        getTemplate: function() {
            return template(this.options.presenter.getData());
        },
        
        getStyle: function() {
            return style;
        },
                
        addListItem: function (message) {
            var ul = this.getElement().find('ul');

            // Create a new list item and attach it to the region
            var eventItem = new EventListItem(message);
            eventItem.attachTo(ul);
        }
    });
});