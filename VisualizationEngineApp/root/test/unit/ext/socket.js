/*global define, describe, it, expect */
define([
    'app/ext/socket'
], function (socketExt) {
    'use strict';

    describe("socket", function () {

        describe('Methods', function () {
            var onStub = sinon.stub(),
                emitStub = sinon.stub();

            beforeEach(function () {
                sinon.stub(io, 'connect').returns({on: onStub, emit: emitStub});
            });
            
            afterEach(function () {
                onStub.reset();
                emitStub.reset();
                io.connect.restore();
            });

            it('connect(:url String)', function () {               
                socketExt.connect('http://test.com');

                sinon.assert.calledOnce(io.connect);
                sinon.assert.calledWith(io.connect, 'http://test.com');            
            });

            it('on(:event String, :callback Function)', function () {
                socketExt.connect('http://test.com');
                socketExt.on('event', undefined);

                sinon.assert.calledOnce(onStub);
                sinon.assert.calledWith(onStub, 'event');
            });

            it('emit(:event String, :message String)', function () {
                socketExt.connect('http://test.com');
                socketExt.emit('event', 'text');
                
                sinon.assert.calledOnce(emitStub);
                sinon.assert.calledWith(emitStub, 'event', 'text');
            });            
        });
    });
});