
/*global define*/
define('widgets/InfoPopup/ext/ext.dom',[
], function () {
    

    var dom = {};

    dom.getTagName = function (element) {
        return element._getHTMLElement().tagName.toLowerCase();
    };


    dom.getWindowDimensions = function () {
        return {
            height: window.innerHeight,
            width: window.innerWidth
        };
    };

    dom.getElementDimensions = function (elt) {
        var nativeElt = elt._getHTMLElement();
        return {
            height: nativeElt.clientHeight,
            width: nativeElt.clientWidth
        };
    };

    dom.stopPropagation = function (e) {
        e.originalEvent.stopPropagation();
    };
    dom.getMousePosEvt = function (e) {
        var event = e.originalEvent || window.event;
        var mousePos = {
            left: event.clientX,
            top: event.clientY
        };
        return mousePos;
    };

    return dom;

});
define('text!widgets/InfoPopup/_infoPopup.html',[],function () { return '<div class="ebInfoPopup" tabindex="1">\n\t<div class="ebInfoPopup-infoIcon"><i class="ebIcon ebIcon_medium ebIcon_info"></i></div>\n\t<div class="ebInfoPopup-content">\n        <div class="ebInfoPopup-closeButton"><i class="ebIcon ebIcon_x ebIcon_interactive"></i></div>\n\t\t<div class="ebInfoPopup-contentText"></div>\n\t\t<div class="ebInfoPopup-arrowShadow"></div>\n\t\t<div class="ebInfoPopup-arrowBorder"></div>\n\t</div>\n</div>\n';});

/*global define*/
define('widgets/InfoPopup/InfoPopupView',[
    'jscore/core',
    './ext/ext.dom',
    'text!./_infoPopup.html'
], function (core, domExt, template) {
    

    var InfoPopupView = core.View.extend({

        /*jshint validthis:true*/

        // TODO: Should be added to core.View and executed after render()
        afterRender: function () {
            var element = this.getElement();
            this.outerEl = element.find('.' + InfoPopupView.OUTER_CLASS);
            this.popupContent = element.find('.' + InfoPopupView.CONTENT_CLASS);
            this.closeButton = element.find('.' + InfoPopupView.CLOSE_BUTTON);
            this.infoButton = element.find('.' + InfoPopupView.INFO_BUTTON);
            this.infoButtonIcon = this.infoButton.find('.ebIcon');
        },

        getTemplate: function () {
            return template;
        },

        setVisible: function (isVisible) {
            this.outerEl.setModifier('visible', '' + isVisible);
        },

        setCorner: function (input) {
            this.outerEl.setModifier('corner', input);
        },

        calcCorner: function (dimensions, offset) {
            //use dimensions and offset to determine which variation of popup to show
            var outerElHeight = domExt.getElementDimensions(this.outerEl).height;

            if ((dimensions.width - offset.left) < domExt.getElementDimensions(this.outerEl).width) {
                if ((dimensions.height - offset.top) < outerElHeight + 50) {
                    this.setCorner('bottomRight');
                }
                else {
                    this.setCorner('topRight');
                }
            }
            else {
                if ((dimensions.height - offset.top) < outerElHeight + 50) {
                    this.setCorner('bottomLeft');
                }
                else {
                    this.setCorner('default');
                }
            }
        },

        setPopupContent: function (content) {
            if (typeof content === 'string') {
                var contentEl = core.Element.parse('<span>' + content + '</span>');
                this.popupContent.append(contentEl);
            }
        },

        getPopupContent: function () {
            return this.popupContent;
        },

        getOuter: function () {
            return this.outerEl;
        },

        getCloseButton: function () {
            return this.closeButton;
        },

        getInfoButton: function () {
            return this.infoButton;
        },

        enablePopup: function (enable) {
            if(enable) {
                this.infoButtonIcon.removeModifier('disabled');
                this.infoButtonIcon.setModifier('interactive');
            } else {
                this.infoButtonIcon.removeModifier('interactive');
                this.infoButtonIcon.setModifier('disabled');
            }
        }

    }, {
        OUTER_CLASS: 'ebInfoPopup-content',
        CONTENT_CLASS: 'ebInfoPopup-contentText',
        CLOSE_BUTTON: 'ebInfoPopup-closeButton',
        INFO_BUTTON: 'ebInfoPopup-infoIcon'
    });

    return InfoPopupView;

});
/*global define*/
define('widgets/InfoPopup/InfoPopup',[
    'widgets/main',
    './ext/ext.dom',
    './InfoPopupView'
], function (core, domExt, View) {
    


    /**
     * The InfoPopup class uses the Ericsson brand assets.
     *
     * @class InfoPopup
     */
    return core.Widget.extend({

        /*jshint validthis:true*/

        View: View,

        /**
         * The init method is automatically called by the constructor when using the "new" operator. If an object with
         * key/value pairs was passed into the constructor then the options variable will have those key/value pairs.
         *
         * The following options are accepted:
         *   <ul>
         *       <li>content: the content displayed on the InfoPopup, may contain HTML or plain text. Default is "InfoPopup"</li>
         *       <li>enabled: boolean indicating whether InfoPopup should be enabled. Default is true.</li>
         *       <li>persistent: boolean indicating whether InfoPopup should be persistent. Default is true.</li>
         *       <li>corner: String which determines the corner of the InfoPopup.
         *          <br>Possible values are auto, default, topRight, bottomLeft and bottomRight. The auto option allows the InfoPopup to automatically determine the corner. Default is auto.</li>
         *   </ul>
         *
         * @method init
         * @param {Object} options
         */
        init: function (options) {
            this.options = options || {};
            this.popupHasMouse = false;
        },

        /**
         * Overrides method from widget.
         * Executes every time, when added back to the screen.
         *
         * @method onViewReady
         * @private
         */
        onViewReady: function () {
            // TODO: not yet in jsCore. Will be removed in the future.
            this.view.afterRender();
            this.setContent(this.options.content || 'InfoPopup');
            this.persistent = this.options.persistent || false;

            if (this.options.enabled !== undefined) {
                this.enablePopup(this.options.enabled);
            }
            else {
                this.enablePopup(true);
            }
            this.corner = this.options.corner || 'auto';
            if ((this.corner === 'default') || (this.corner === 'topRight') || (this.corner === 'bottomLeft') || (this.corner === 'bottomRight')) {
                this.view.setCorner(this.corner);
            }
            else {
                this.corner = 'auto';
            }
            this.setVisible(false);
            addOpenHandler.call(this);
            addCloseHandler.call(this);
            if (!this.persistent) {
                addBlurHandler.call(this);
            }
        },

        /**
         * Sets the contents of the InfoPopup
         * Can be plain text or HTML
         *
         * @method setContent
         * @param {String} content
         */
        setContent: function (content) {
            this.view.setPopupContent(content);
        },

        /**
         * This method sets the visibility of the InfoPopup
         *
         * @method setVisible
         * @param {Boolean} isVisible
         */
        setVisible: function (isVisible) {
            this.view.setVisible(isVisible);
            this.visible = isVisible;
        },

        /**
         * This method enables the InfoPopup
         *
         * @method enable
         */
        enable: function () {
            this.enablePopup(true);
        },

        /**
         * This method disables the InfoPopup
         *
         * @method disable
         */
        disable: function () {
            this.enablePopup(false);
            this.setVisible(false);
        },

        enablePopup: function (enable) {
            this.enabled = enable;
            this.view.enablePopup(enable);
        }

    });

    /* ++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS ++++++++++++++++++++++++++++++++++++++++++ */

    function calcCorner(mousePos) {
        this.view.calcCorner(domExt.getWindowDimensions(), mousePos); //pass dimensions and mouse position to calcCorner fn in view
    }

    function addCloseHandler() {
        this.view.getCloseButton().addEventHandler('click', function (e) {
            if (this.enabled) {
                domExt.stopPropagation(e);
                this.setVisible(false);
            }
        }, this);
    }

    function addBlurHandler() {
        this.getElement().addEventHandler('blur', function () {
            if (!this.popupHasMouse) {
                this.setVisible(false);
            } else {
                this.getElement().trigger('focus');
            }
        }, this);
    }

    function addOpenHandler() {
        this.view.getInfoButton().addEventHandler('click', function (e) {
            if (this.enabled) {
                domExt.stopPropagation(e);
                if (this.visible) {
                    this.setVisible(false);
                }
                else {
                    this.setVisible(true);
                    if (this.corner === 'auto') {
                        calcCorner.call(this, domExt.getMousePosEvt(e));
                    }
                }
            }
        }, this);

        this.view.getOuter().addEventHandler('mouseenter', function () {
            this.popupHasMouse = true;
        }, this);

        this.view.getOuter().addEventHandler('mouseleave', function () {
            this.popupHasMouse = false;
        }, this);
    }

});
define('widgets/InfoPopup', ['widgets/InfoPopup/InfoPopup'], function (main) { return main; });
