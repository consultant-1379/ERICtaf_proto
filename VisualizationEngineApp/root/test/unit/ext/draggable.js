/*global define, describe, it, expect */
define([
    'app/ext/draggable',
    'app/widgets/VisualizationSelect/VisualizationSelect'
], function (draggable, VisualizationSelect) {
    'use strict';

    describe('draggable', function () {
        
        describe('Methods', function () {
            
            it('initDrag(el: Element)', function () {
                var element = {setAttribute: function() {}, element: function() {}},    
                    mock = sinon.mock(element);
                
                mock.expects('setAttribute').withExactArgs('draggable', 'true');
                draggable.initDrag(mock.object);
                mock.verify();
                mock.restore();
            });      
        });
    });
});