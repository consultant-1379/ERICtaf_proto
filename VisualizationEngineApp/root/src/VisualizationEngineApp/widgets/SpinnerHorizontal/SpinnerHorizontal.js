/*global define*/
define([
    'widgets/Spinner',
    './SpinnerHorizontalView'
], function (Spinner, View) {
       
    return Spinner.extend({
        View: View,
                
        onAttach: function () {
            var ebInput = this.getElement().find('.ebInput');


            this.ebInput_percent = this.getElement().find('.ebInput_percent');
            this.ebInput_percent.setAttribute("value", (Math.round(100 * ebInput.getValue() / 12)) + "%");

            if (this.options.show_percent === true) {
                ebInput.setStyle("display", "none");
            }else {
                this.ebInput_percent.setStyle("display", "none");
            }
            
        },
                
        triggerSetValue: function (size) {
            this.setValue(size);
            this.trigger('changeSizeEvent', this.value);
        },
        
        triggerOnValueChange: function () {
            this.view.getUpButton().addEventHandler('mouseup', function() {
                this.trigger('changeSizeEvent', this.value);
                this.ebInput_percent.setAttribute("value", (Math.round(100 * this.value / 12)) + "%");
            }, this);
            this.view.getDownButton().addEventHandler('mouseup', function() {
                this.trigger('changeSizeEvent', this.value);
                this.ebInput_percent.setAttribute("value", (Math.round(100 * this.value / 12)) + "%");
            }, this);
        }
    });
});