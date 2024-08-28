define(function() {
    'use strict';
        
    return {
        createDropArea: function(element, dropEventCallback, dragOverEventCallback) {           
            element.addEventHandler('drop', dropEventCallback);             
            element.addEventHandler('dragover', dragOverEventCallback);
        }
    };
});
