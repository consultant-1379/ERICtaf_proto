define([
    'jscore/base/jquery',
    'thirdparty/jquery-ui-1.10.3'
], function($) {
    
    return {
        
        resizable: function(element) {
            var el = element.element;
            
            $(el).resizable({
                minHeight: 15,
                minWidth: 200
                //aspectRatio: true
            });
        }
    };
});
