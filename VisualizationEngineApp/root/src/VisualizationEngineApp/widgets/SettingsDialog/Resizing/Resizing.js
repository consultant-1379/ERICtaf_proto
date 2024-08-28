define([
    'jscore/core',
    './ResizingView',
    'app/widgets/SpinnerHorizontal/SpinnerHorizontal'
], function (core, View, Spinner) {

    return core.Widget.extend({
        View: View,

        init: function () {
            this.spinner = new Spinner({
                min: typeof(this.options.min) === "number" ? this.options.min : 1,
                max: typeof(this.options.max) === "number" ? this.options.max : 12,
                value: this.options.span,
                show_percent: this.options.show_percent ? this.options.show_percent : false
            });
            this.spinner.triggerOnValueChange();

            this.spinner.addEventHandler('changeSizeEvent', function (e) {
                this.trigger('changeSizeEvent', e);
            }.bind(this));
        },

        onViewReady: function () {
            this.spinner.attachTo(this.getElement());
        },
        
        getViewSettings: function () {
            return {title: this.options.title?this.options.title:"Resize"};
        }
    });
});