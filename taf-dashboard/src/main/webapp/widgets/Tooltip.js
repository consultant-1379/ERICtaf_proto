
/*global define*/
define('widgets/Tooltip/ext/ext.dom',[
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
define('text!widgets/Tooltip/_tooltip.html',[],function () { return '<div class="ebTooltip">\n    <div class="ebTooltip-outer">\n        <div class="ebTooltip-contentText"></div>\n    </div>\n</div>\n';});

/*global define*/
define('widgets/Tooltip/TooltipView',[
    'jscore/core',
    './ext/ext.dom',
    'text!./_tooltip.html'
], function(core,domExt, template) {
    

    var TooltipView =  core.View.extend({

        // TODO: Should be added to core.View and executed after render()
        afterRender: function () {
            var element = this.getElement();
            this.outerEl = element.find('.' + TooltipView.OUTER_CLASS);
            this.content = element.find('.' + TooltipView.CONTENT_CLASS);
        },

        getTemplate: function() {
            return template;
        },

		setVisible: function (isVisible) {
            this.getElement().setModifier('visible', '' + isVisible);
		},

		setCorner: function(input) {
            this.outerEl.setModifier('corner', input);
		},

        calcCorner: function(dimensions, offset) {
			//use dimensions and offset to determine which variation of tooltip to show
            var outerElHeight = domExt.getElementDimensions(this.outerEl).height;

			if ( (dimensions.width - offset.left) < domExt.getElementDimensions(this.outerEl).width + 30) {
				if ( (dimensions.height - offset.top) < outerElHeight + 30 ) {
                    updateCorner.call(this, 'bottomRight');
				}
				else {
                    updateCorner.call(this, 'topRight');
				}
			}
			else {
				if ( (dimensions.height - offset.top) < outerElHeight + 30 ) {
                    updateCorner.call(this, 'bottomLeft');
				}
				else {
                    updateCorner.call(this, 'default');
				}
			}
		},

        setContentText: function(caption) {
            this.content.setText(caption);
        },

        getContent: function() {
            return this.content;
        },

		setPosition: function(l, t) {
            this.getElement().setStyle({
                left: l,
                top: t
            });
		}

    }, {
        OUTER_CLASS: 'ebTooltip-outer',
        CONTENT_CLASS: 'ebTooltip-contentText'
    });

    return TooltipView;

    /* ++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS ++++++++++++++++++++++++++++++++++++++++++ */

    function updateCorner(cornerName) {
        /*jshint validthis:true*/
        if (this.currentCorner !== cornerName) {
            this.setCorner(cornerName);
            this.currentCorner = cornerName;
        }
    }

});
/*global define*/
define('widgets/Tooltip/Tooltip',[
    'widgets/main',
    './ext/ext.dom',
    './TooltipView'
], function (core, domExt, View) {
    

    /**
     * The Tooltip class uses the Ericsson brand assets.
     *
     * @class Tooltip
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
         *       <li>contentText: the text displayed on the tooltip. Default is "Tooltip"</li>
         *       <li>enabled: boolean indicating whether tooltip should be enabled. Default is true.</li>
         *       <li>modifiers: an array used to define modifiers for the Tooltip. (Asset Library)
         *          <a name="modifierAvailableList"></a>
         *          <br>E.g: modifiers:[{name: 'foo'}, {name: 'bar', value:'barVal'}]
         *          <ul style="padding-left: 15px;">
         *              <li>size: (small | large) size variation</li>
         *          </ul>
         *       </li>
         *       <li>parent: sets the parent of the tooltip.</li>
         *   </ul>
         *
         * @method init
         * @param {Object} options
         */
        init: function (options) {
            this.options = options || {};
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

            this.setContentText(this.options.contentText || 'Tooltip');
            this.setModifiers(this.options.modifiers || []);

            if (this.options.enabled !== undefined) {
                this.enabled = this.options.enabled;
            }
            else {
                this.enabled = true;
            }

            this.setVisible(false);
            if (this.options.parent) {
                this.parent = this.options.parent;
                this.addHoverHandler(this.parent);
            }
        },

        /**
         * This method sets the contents of the Tooltip
         *
         * @method setContentText
         * @param {String} caption
         */
        setContentText: function (caption) {
            this.view.setContentText(caption);
        },

        /**
         * This method sets the visibility of the Tooltip
         *
         * @method setVisible
         * @param {Boolean} isVisible
         */
        setVisible: function (isVisible) {
            this.view.setVisible(isVisible);
        },

        /**
         * This method enables the Tooltip
         *
         * @method enable
         */
        enable: function () {
            this.enabled = true;
        },

        /**
         * This method disables the Tooltip
         *
         * @method disable
         */
        disable: function () {
            this.enabled = false;
            this.setVisible(false);
        },

        /**
         * Add a single modifier to the widget.<br>
         * <a href="#modifierAvailableList">see available modifiers</a>
         *
         * @method setModifier
         * @param {String} key
         * @param {String|boolean|int} value
         */
        setModifier: function (key, value) {
            this.getElement().setModifier(key, value);
        },

        /**
         * A methods, which allows to define a list of modifiers to the widget.<br>
         * <a href="#modifierAvailableList">see available modifiers</a>
         *
         * @param {Array} modifiers Contains objects {name: {String}[, value: {String}]}
         */
        setModifiers: function (modifiers) {
            modifiers.forEach(function (modifier) {
                this.setModifier(modifier.name, modifier.value);
            }, this);
        },

        /**
         * Remove the modifier 'key' from the widget modifiers.
         *
         * @method removeModifier
         * @param {String} key
         */
        removeModifier: function (key) {
            this.getElement().removeModifier(key);
        },


        /**
         * This method adds the event listeners required to make the tooltip function.
         * It accepts the parent element as input.
         * This method is called automatically if the parent is passed into the constructor.
         *
         * @method addHoverHandler
         * @param {Object} parent
         */
        addHoverHandler: function (parent) {
            if (parent) {
                this.parent = parent;
            }
            this.parent.addEventHandler('mouseover', function (e) {
                if (this.enabled) {
                    domExt.stopPropagation(e);
                    var mousePos = domExt.getMousePosEvt(e);
                    setPosition.call(this, mousePos.left, mousePos.top);
                    calcCorner.call(this, mousePos);
                    this.setVisible(true);
                }
            }.bind(this));

            this.parent.addEventHandler('mousemove', function (e) {
                if (this.enabled) {
                    domExt.stopPropagation(e);
                    var mousePos = domExt.getMousePosEvt(e);
                    setPosition.call(this, mousePos.left, mousePos.top);
                    calcCorner.call(this, mousePos);
                }
            }.bind(this));

            this.parent.addEventHandler('mouseout', function (e) {
                if (this.enabled) {
                    domExt.stopPropagation(e);
                    this.setVisible(false);
                }
            }.bind(this));
        }

    });

    /* ++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS ++++++++++++++++++++++++++++++++++++++++++ */

    function calcCorner(mousePos) {
        this.view.calcCorner(domExt.getWindowDimensions(), mousePos); //pass dimensions and mouse position to calcCorner fn in view
    }


    function setPosition(left, top) {
        this.view.setPosition(left, top);
    }

});
define('widgets/Tooltip', ['widgets/Tooltip/Tooltip'], function (main) { return main; });
