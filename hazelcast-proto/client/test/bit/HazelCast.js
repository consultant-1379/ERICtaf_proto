/*global define, describe, it, expect */
define([
	'hazelcast/HazelCast'
], function (HazelCast) {
    'use strict';

    describe('HazelCast', function () {

        it('HazelCast should be defined', function () {
            expect(HazelCast).not.to.be.undefined;
        });

    });

});
