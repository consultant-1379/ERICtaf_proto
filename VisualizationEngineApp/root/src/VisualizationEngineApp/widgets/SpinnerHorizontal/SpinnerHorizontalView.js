define([
    'text!./SpinnerHorizontal.html',
    'widgets/utils/dataNameUtils',
    'widgets/Spinner'
], function (template, dataNameUtils) {
    var SpinnerView = require('widgets/Spinner/SpinnerView');

    return SpinnerView.extend({
        getTemplate: function () {
            return dataNameUtils.translate(null, template, this);
        }
    });
});