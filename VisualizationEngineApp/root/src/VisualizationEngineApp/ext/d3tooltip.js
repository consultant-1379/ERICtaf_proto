define([
    'thirdparty/d3',
    './base/d3-tip'
], function (d3) {
    // d3.tip
    // Copyright (c) 2013 Justin Palmer
    //
    // Tooltips for d3.js SVG visualizations
    // Public - contructs a new tooltip
    //
    // Returns a tip
    return {
        addTooltip: function(element, settings) {
            element.tip = new d3.tip();

            d3.select(element)
                .call(element.tip)
                .on('click', function () {
                    if (element.tip.shown()) {
                        element.tip.hide();
                    }
                    else {
                        element.tip.html(settings.content);
                        element.tip.show();
                    }
                }.bind(element));
                
                element.tip.style("z-index", 100);
                
            if (settings.direction) {
                element.tip.direction(settings.direction);
            }
            
            if (settings.offset) {
                element.tip.offset(settings.offset);
            }
        }
    };
});
