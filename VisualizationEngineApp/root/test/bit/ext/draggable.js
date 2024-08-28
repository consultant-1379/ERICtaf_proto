/*global define, describe, it, expect */
define([
    'jscore/ext/utils/base/underscore',
    'app/ext/draggable',
    'app/widgets/VisualizationSelect/VisualizationSelect'
], function (_, draggable, VisualizationSelect) {
    'use strict';

    describe('draggable', function () {
        var target;
        
        beforeEach(function () {
            target = new VisualizationSelect();            
            _.extend(target, draggable);
        });
        
        it('should be defined', function () {
            expect(draggable).not.to.be.undefined;
        });

        it('should give it\'s target an initDrag method', function () {    
            expect(target).to.have.property('initDrag');
        });  
        
        it('should give it\'s target a bindDragStartEvent method', function () {    
            expect(target).to.have.property('bindDragStartEvent');
        }); 
        
        it('should give it\'s target a bindDragEndEvent method', function () {    
            expect(target).to.have.property('bindDragEndEvent');
        });     
    });
});