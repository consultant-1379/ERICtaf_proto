/*global define, describe, it, expect */
define([
    'app/widgets/SettingsDialog/Subscription/Subscription'
], function (Subscription) {
    'use strict';

    describe('Subscription', function () {

        describe('Methods', function () {
            var subscription = new Subscription();
            
                var goodMessage1 = "all",
                    goodMessage2 = "hello:world";
                
                var badMessage1 = "ball",
                    badMessage2 = "",
                    badMessage3 = "::::",
                    badMessage4 = "!#¤%&/()=?`^*_;@£$€{[]}\\";
                
                it('checkValue() good strings', function () {
                    expect(subscription.checkValue(goodMessage1)).to.equal(true);
                    expect(subscription.checkValue(goodMessage2)).to.equal(true);
                });
                it('checkValue() bad strings', function () {
                    expect(subscription.checkValue(badMessage1)).to.equal(false);
                    expect(subscription.checkValue(badMessage2)).to.equal(false);
                    expect(subscription.checkValue(badMessage3)).to.equal(false);
                    expect(subscription.checkValue(badMessage4)).to.equal(false);
                });
        });
    });
});