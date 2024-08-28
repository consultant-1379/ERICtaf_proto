define([
    'jscore/core',
    './IconView'
], function(core, View) {
    'use strict';
    return core.Widget.extend({
        View: View,

        /*
        getDefinedCss: function(s) {
            if(!document.styleSheets || s === undefined) return '';
            if(typeof s === 'string') s= RegExp('\\b'+s+'\\b','i'); // (Older?) IE capitalizes html selectors 

            var A, S, DS= document.styleSheets, n= DS.length, SA= [], tem = [];
            while(n){
                S= DS[--n];
                A= (S.rules)? S.rules: S.cssRules;
                for(var i= 0, L= A.length; i<L; i++){
                        tem= A[i].selectorText? [A[i].selectorText, A[i].style.cssText]: [A[i]+''];
                        if(s.test(tem[0])) SA[SA.length]= tem;
                }
            }
            return SA.join('\n\n');
        },
        */

        onViewReady: function () {
            if (typeof(this.options.onclickevent) === "function") {
                this.element.addEventHandler('click', function(e) {
                    this.options.onclickevent.call(this, e); //?
                }, this);
            }
            if (this.options.icon && this.options.text) {
                this.element.setAttribute("title", this.options.text);
            }
        },
        
        faultCheck: function() {
            this.isClickable = (typeof(this.options.onclickevent) === "function") ? " ebIcon_interactive" : "";
            
            if (!this.options.icon) {    // if there is a rule
                this.options.text = "WARNING: Icon not specified";
                this.options.icon = "ebIcon_warning";
            }
        },
        
        getTemplateData: function() {
            this.faultCheck();
            return {
                icon: "ebIcon ebIcon_medium " + this.options.icon + this.isClickable,
                text: (this.options.text !== undefined) ? this.options.text : this.options.icon
            };
        }
    });
});
