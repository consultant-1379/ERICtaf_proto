/*global define, describe, it, expect */
define([
    'app/ext/LocationController'
], function (LocationExt) {
    'use strict';

    describe('LocationController', function () {
        var lc = {};
        
        afterEach(function () {
            window.location.hash = '';
        });        
        
        beforeEach(function() {
            lc = new LocationExt();
            window.location.hash = '';
        });
        
        describe('Methods', function () {
            
            it('parseHash(hash: String)', function () {
                var hash = 'listRegion/5/pieRegion/6/flowRegion/12',
                    parsedHash;
                    
                parsedHash = lc.parseHash(hash);

                expect(lc.parseHash.bind(lc, hash)).to.not.throw(Error);
                expect(parsedHash[0]).to.have.length(3);
                expect(parsedHash[0][0].factory).to.equal('listRegion');
                expect(parsedHash[0][0].span).to.equal('5');
                expect(parsedHash[0][1].factory).to.equal('pieRegion');
                expect(parsedHash[0][1].span).to.equal('6');
                expect(parsedHash[0][2].factory).to.equal('flowRegion');
                expect(parsedHash[0][2].span).to.equal('12');
                
                hash = 'listRegion/5/pieRegion/blah/37/12';
                
                expect(lc.parseHash.bind(lc, hash)).to.throw(Error);
            });
            
            it('appendToHash(hashPart: String)', function () {
                var hashPart = 'listRegion/6/flowRegion/12';
                
                lc.appendToHash(hashPart);
                expect(window.location.hash).to.equal('#main/listRegion/6/flowRegion/12');
                
                hashPart = 'pieRegion/3';
                lc.appendToHash(hashPart);
                expect(window.location.hash).to.equal('#main/listRegion/6/flowRegion/12/pieRegion/3');
            });
            
            it('cutFromHash(index: int)', function () {
                var index = 0; 
                window.location.hash = '#main/listRegion/6/flowRegion/12/pieRegion/3';
                
                lc.cutFromHash(index);
                expect(window.location.hash).to.equal('#main/flowRegion/12/pieRegion/3');
                
                index = 1;
                lc.cutFromHash(index);
                expect(window.location.hash).to.equal('#main/flowRegion/12');
            });
        });
    });
});