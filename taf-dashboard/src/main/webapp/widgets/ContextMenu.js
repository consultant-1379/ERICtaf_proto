
define('text!widgets/ContextMenu/_contextMenu.html',[],function () { return '<div class="ebContextMenu">\n    <div class="ebContextMenu-ExpandBtn" tabindex="1">\n        <i class="ebIcon ebIcon_medium ebIcon_menu"></i>\n        <div class="ebContextMenu-Dropdown"></div>\n    </div>\n</div>';});

/*global define*/
define('widgets/ContextMenu/ContextMenuView',[
    'jscore/core',
    'text!./_contextMenu.html'
], function(core, template) {
    

    var ContextMenuView =  core.View.extend({

        /*jshint validthis:true*/

        // TODO: Should be added to core.View and executed after render()
        afterRender: function () {
            this.outerEl = this.getElement().find('.' + ContextMenuView.OUTER_CLASS);
            this.expandBtn = this.getElement().find('.' + ContextMenuView.EXPANDBTN_CLASS);
            this.dropdown = this.getElement().find('.' + ContextMenuView.DROPDOWN_CLASS);
            this.expandBtnIcon = this.expandBtn.find('.ebIcon');
        },

        getTemplate: function() {
            return template;
        },

        setTitle: function(caption) {
            this.outerEl.setText(caption);
            this.outerEl.setAttribute('title', caption);
        },

        getTitle: function() {
            return this.outerEl.getText();
        },

        getOuterEl: function() {
            return this.outerEl;
        },

        getExpandBtn: function() {
            return this.expandBtn;
        },

        getDropdown: function() {
            return this.dropdown;
        },

        showDropdown: function(visible) {
            this.dropdown.setModifier('visible', '' + visible);
        },

        enableMenu: function(enable) {
            if(enable) {
                this.expandBtnIcon.removeModifier('disabled');
                this.expandBtnIcon.setModifier('interactive');
            } else {
                this.expandBtnIcon.removeModifier('interactive');
                this.expandBtnIcon.setModifier('disabled');
            }
        }

    }, {
        OUTER_CLASS: 'ebContextMenu',
        EXPANDBTN_CLASS: 'ebContextMenu-ExpandBtn',
        DROPDOWN_CLASS: 'ebContextMenu-Dropdown'
    });

    return ContextMenuView;
});

define('text!widgets/ContextMenu/ContextMenuItem/_contextMenuItem.html',[],function () { return '<div class="ebContextMenuItem"></div>';});

/*global define*/
define('widgets/ContextMenu/ContextMenuItem/ContextMenuItemView',[
    'jscore/core',
    'text!./_contextMenuItem.html'
], function(core, template) {
    

    return core.View.extend({

        // TODO: Should be added to core.View and executed after render()
        afterRender: function () {
            this.outerEl = this.getElement();
        },

        getTemplate: function() {
            return template;
        },

        getOuterEl: function() {
            return this.outerEl;
        }

    });

});

/*global define*/
define('widgets/ContextMenu/ContextMenuItem/ContextMenuItem',[
    'jscore/core',
    './ContextMenuItemView'
], function (core, View) {
    

    /**
     * The ContextMenuItem is a widget used as part of the ContextMenu Widget. It is used to create entries on the dropdown of the ContextMenu.
     *
     * @class ContextMenuItem
     */
    return core.Widget.extend({

        View: View,

        /**
         * The init method is automatically called by the constructor when using the "new" operator. If an object with
         * key/value pairs was passed into the constructor then the options variable will have those key/value pairs.
         *
         * The following options are accepted:
         *   <ul>
         *       <li>title: the text displayed on the ContextMenuItem. Default is "A ContextMenuItem Widget"</li>
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

            //this.onClick();
            //add click handler to trigger 'onClick' event
            this.setTitle(this.options.title || 'A ContextMenuItem Widget');
        },

        /**
         * This method sets the title of the ContextMenuItem
         *
         * @method setTitle
         * @param {String|core.Element} title
         */
        setTitle: function (title) {
            if (title instanceof core.Element) {
                this.getOuterEl().append(title);
            } else {
                this.getOuterEl().append(core.Element.parse(title));
            }
        },

        /**
         * This method sets a max width on the widget
         *
         * @method limitWidth
         * @param {Boolean} value
         */
        limitWidth: function (value) {
            this.getOuterEl().setModifier('limitWidth', '' + value);
        },

        /**
         * This adds an event handler for mouse clicks, which triggers the "onClick" event
         *
         * @method onClick
         */
        onClick: function (callback) {
            this.getElement().addEventHandler('click', function() {
                callback.call();
            }, this);
        },

        /**
         * This method returns the outer element. Used for testing only.
         * @method getOuterEl
         * @private
         */
        getOuterEl: function () {
            return this.view.getOuterEl();
        }

    });

    /* ++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS ++++++++++++++++++++++++++++++++++++++++++ */


});

/*global define*/
define('widgets/ContextMenu/ContextMenu',[
    'widgets/main',
    './ContextMenuView',
    './ContextMenuItem/ContextMenuItem'
], function (core, View, ContextMenuItem) {
    

    /**
     * The ContextMenu class uses the Ericsson brand assets.
     *
     * @class ContextMenu
     */
    return core.Widget.extend({

        View: View,

        /**
         * The init method is automatically called by the constructor when using the "new" operator. If an object with
         * key/value pairs was passed into the constructor then the options variable will have those key/value pairs.
         *
         * The following options are accepted:
         *   <ul>
         *       <li>enabled: boolean indicating whether ContextMenu should be enabled. Default is true.</li>
         *       <li>maxItems: integer that determines the maximum number of items in the ContextMenu. Default is 10.</li>
         *       <li>limitWidth: integer that determines if the dropdown width is limited. If set to true, the width will be limited to 100px, if set to false the width will be unlimited. Default is false.</li>
         *       <li>position: (default is left || right) String that specifies which side of the icon the dropdown is aligned with.</li>
         *       <li>items: an array containing objects that may be used to create ContextMenuItems for the dropdown</li>
         *   </ul>
         *
         * @method init
         * @param {Object} options
         */
        init: function (options) {
            this.options = options || {};
            this.inputItems = this.options.items || [];
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

            // set up the ContextMenu widget
            if (this.options.enabled !== undefined && !this.options.enabled) {
                this.disable();
            } else {
                this.enable();
            }

            this.maxItems = this.options.maxItems || 10;
            this.limitItemWidth = this.options.limitItemWidth || false;
            this.position = this.options.position || 'default';

            this.setPosition(this.position);

            showDropdown.call(this, false);
            addExpandHandler.call(this);
            addBlurHandler.call(this);
            this.itemArray = [];
            addContextMenuItems.call(this);
        },

        /**
         * This method adds a ContextMenuItem to the ContextMenu widget
         *
         * @method addContextMenuItem
         *
         * @param {String|core.Element|ContextMenuItem} item
         */
        addContextMenuItem: function (item) {
            if (this.itemArray.length < this.maxItems) {
                if (item instanceof ContextMenuItem) {
                    this.itemArray.push(item);
                } else {
                    this.itemArray.push(
                        new ContextMenuItem({title: item})
                    );
                }
                this.itemArray[this.itemArray.length - 1].attachTo(this.view.getDropdown());
                this.itemArray[this.itemArray.length - 1].limitWidth(this.limitItemWidth);
            }
        },

        /**
         * This method removes the item stored at the specified index in the ContextMenu widget
         * @method removeItem
         * @param {int} index
         */
        removeItem: function (index) {
            if (this.itemArray.length !== 0) {
                this.itemArray[index].destroy();
                this.itemArray.splice(index, 1);
            }
        },

        /**
         * This method removes the last item stored in the ContextMenu widget
         * @method removeLastItem
         */
        removeLastItem: function () {
            if (this.itemArray.length !== 0) {
                this.itemArray[this.itemArray.length - 1].destroy();
                this.itemArray.splice(this.itemArray.length - 1, 1);
            }
        },

        /**
         * This method clears all ContextMenuItems stored in the ContextMenu widget
         * @method clearItems
         */
        clearItems: function () {
            for (var i = this.itemArray.length - 1; i > -1; i--) {
                this.itemArray[i].destroy();
                this.itemArray.splice(i, 1);
            }
        },

        /**
         * This method enables the ContextMenu
         *
         * @method enable
         */
        enable: function () {
            this.enableMenu(true);
        },

        enableMenu: function (enable) {
            this.enabled = enable;
            this.view.enableMenu(enable);
        },

        /**
         * This method disables the ContextMenu
         *
         * @method disable
         */
        disable: function () {
            this.enableMenu(false);
        },

        /**
         * This method sets the position of the dropdown for the ContextMenu. two values are possible: "default", and "right"
         * If "default" is specified, the dropdown will be aligned with the left side of the icon. If "right" is specified, the
         * dropdown will be aligned with the right side of the icon.
         *
         * @method setPosition
         */
        setPosition: function(position) {
            this.getDropdown().setModifier('side', position);
        },

        /**
         * This method returns the dropdown element. Used for testing only.
         * @method getDropdown
         * @private
         */
        getDropdown: function () {
            return this.view.getDropdown();
        },

        /**
         * This method returns the expand button element. Used for testing only.
         * @method getExpandBtn
         * @private
         */
        getExpandBtn: function () {
            return this.view.getExpandBtn();
        }

    });

    /* ++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS ++++++++++++++++++++++++++++++++++++++++++ */

    function addContextMenuItems() {
        /*jshint validthis:true*/
        if (this.inputItems.length > 0) {
            for (var i = 0; i < this.inputItems.length; i++) {
                this.addContextMenuItem(this.inputItems[i]);
            }
        }
    }

    function addBlurHandler() {
        /*jshint validthis:true*/
        this.view.getExpandBtn().addEventHandler('blur', function () {
            if (!this.dropdownHasMouse) {
                showDropdown.call(this, false);
            } else {
                this.getElement().trigger('focus');
            }
        }, this);
    }

    function addExpandHandler() {
        /*jshint validthis:true*/
        this.getExpandBtn().addEventHandler('click',  function () {
            if (this.enabled && this.itemArray.length > 0) {
                showDropdown.call(this, !this.dropdownShown);
            }

            this.view.getDropdown().addEventHandler('mouseenter', function () {
                this.dropdownHasMouse = true;
            }, this);

            this.view.getDropdown().addEventHandler('mouseleave', function () {
                this.dropdownHasMouse = false;
            }, this);
        }.bind(this));
    }

    function showDropdown(visible) {
        /*jshint validthis:true*/
        this.view.showDropdown(visible);
        this.dropdownShown = visible;
    }
});
define('widgets/ContextMenu', ['widgets/ContextMenu/ContextMenu'], function (main) { return main; });
