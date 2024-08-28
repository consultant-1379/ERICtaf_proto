
define(['jscore/core',
    'text!./TableChart.html'
], function(core, template) {
    'use strict';

    return core.View.extend({
        getTemplate:
                function() {
                    return template;
                }
//           getStyle:
//                   // Not used for now
//                    function () {
//                        return style;
//                    }
    });
});
