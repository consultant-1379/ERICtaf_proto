define([
    'jscore/core',
    './AspectRatioView',
    'app/widgets/SpinnerHorizontal/SpinnerHorizontal',
	'jscore/base/jquery'
], function (core, View, Spinner,$) {

    return core.Widget.extend({
        View: View,

        init: function () {
            this.spinner = new Spinner({
                min: 1, max: 10, value: 10
            });
            this.spinner.triggerOnValueChange();

            this.spinner.addEventHandler('changeSizeEvent', function (e) {
                this.trigger('changeAspectRatioEvent', e);
            }.bind(this));
            
        },
        
        onAttach: function () {
           $(this.getElement().element).hide();
        },
                        
        hide: function() {
            $(this.getElement().element).fadeOut();
        },

        show: function() {
            $(this.getElement().element).fadeIn();
        },

        onViewReady: function () {
            this.spinner.attachTo(this.getElement());
        }
    });
});