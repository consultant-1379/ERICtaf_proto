/*global define, describe, it, expect */
define([
    'app/ext/dropComponent',
], function (dropComponent) {
    'use strict';

    describe('dropComponent', function () {
         
        it('should be defined', function () {
            expect(dropComponent).not.to.be.undefined;
        });

        it('should bind callbacks to drop and dragover events', function () {
            var element = {addEventHandler: function() {}},
                mock = sinon.mock(element);
                
            mock.expects('addEventHandler').withArgs('drop');
            mock.expects('addEventHandler').withArgs('dragover');
            
            dropComponent.createDropArea(mock.object, undefined, undefined);
            mock.verify();  
            mock.restore();          
        });
    });
});