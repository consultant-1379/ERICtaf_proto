
define('text!widgets/Dropdown/_dropdown.html',[],function () { return '<div class="ebDropdown">\n    <button class="ebBtn ebBtn_default">\n        <span class="ebDropdown-caption">Actions</span>\n        <i class="ebIcon ebIcon_right ebIcon_downArrow"></i>\n    </button>\n</div>';});

/*global define*/
define('widgets/Dropdown/DropdownView',[
    "jscore/core",
    "text!./_dropdown.html"
], function (core, template) {
    

    var DropdownView = core.View.extend({

        // TODO: Should be added to core.View and executed after render()
        afterRender: function () {
            this.caption = this.getElement().find('.' + DropdownView.EL_CAPTION);
            this.button = this.getElement().find('.' + DropdownView.EL_BUTTON);
            this.icon = this.getElement().find('.' + DropdownView.EL_ICON);
        },

        getTemplate: function () {
            return template;
        },

        getRoot: function () {
            return this.getElement();
        },

        getCaption: function () {
            return this.caption;
        },

        getButton: function () {
            return this.button;
        },

        getIcon: function () {
            return this.icon;
        }

    }, {
        'EL_CAPTION': 'ebDropdown-caption',
        'EL_BUTTON': 'ebBtn',
        'EL_ICON': 'ebIcon'
    });

    return DropdownView;

});
define('text!widgets/ComponentList/_componentList.html',[],function () { return '<div class="ebComponent-List"></div>';});

define('text!widgets/ComponentList/_componentListItem.html',[],function () { return '<div class="ebComponent-List-item"></div>';});

/*global define*/
define('widgets/ComponentList/ComponentListView',[
    'jscore/core',
    'text!./_componentList.html',
    'text!./_componentListItem.html'
], function (core, template, listItemTemplate) {
    

    return core.View.extend({

        getTemplate: function () {
            return template;
        },

        // TODO: Should be added to core.View and executed after render()
        afterRender: function () {},

        addItem: function (item) {
            var $itemEl = core.Element.parse(listItemTemplate);
            $itemEl.setText(item);
            $itemEl.setAttribute('title', item);

            this.getElement().append($itemEl);
            return $itemEl;
        }

    });

});
/*global define*/
define('widgets/ComponentList/ComponentList',[
    'widgets/main',
    './ComponentListView'
], function (core, View) {
    

    /**
     * The ComponentList class uses the Ericsson brand assets.
     *
     * @private
     * @class ComponentList
     */
    return core.Widget.extend({

        View: View,

        /**
         * The init method is automatically called by the constructor when using the "new" operator. If an object with
         * key/value pairs was passed into the constructor then the options variable will have those key/value pairs.
         * The following options are accepted:
         *   <ul>
         *       <li>items: an array used as list of available items in the combobox</li>
         *   </ul>
         *
         * @method init
         * @param {Object} options
         */
        init: function (options) {
            this.options = options || {};
            this.selectedItem = '';
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

            if (this.options.items !== undefined) {
                this.setItems(this.options.items);
            }
        },

        /**
         * Add event handle for the component list
         *
         * @method addEventHandler
         * @param {String} action
         * @param {Function} callback
         * @param {Object} context
         */
        addEventHandler: function (action, callback, context) {
            this.getElement().addEventHandler(action, callback, context);
        },

        /**
         * Sets values to the component list
         *
         * @method setItems
         * @param {Array} items
         */
        setItems: function (items) {
            if (items.length === 0) {
                return;
            }
            this.items = items;

            items.forEach(function (item, index) {
                var $item = this.view.addItem(item);
                $item.addEventHandler('click', function () {
                    this._onListItemClicked.call(this, index);
                }, this);
            }, this);
        },

        /**
         * Returns current selected value
         *
         * @method getSelectedValue
         * @return {String}
         */
        getSelectedValue: function () {
            return this.selectedItem;
        },

        /**
         * An event which is executed when on the list item is clicked
         *
         * @method _onListItemClicked
         * @param {int} index
         * @private
         */
        _onListItemClicked: function (index) {
            this.selectedItem = this.items[index];
            this.getElement().trigger('itemSelected');
        }

    });

});

define('widgets/ComponentList', ['widgets/ComponentList/ComponentList'], function (main) { return main; });

/*global define*/
define('widgets/Dropdown/Dropdown',[
    'widgets/main',
    './DropdownView',
    'widgets/ComponentList'
], function (core, View, ComponentList) {
    

    /**
     * The Dropdown class uses the Ericsson brand assets.
     *
     * @class Dropdown
     */
    return core.Widget.extend({

        View: View,

        /**
         * The init method is automatically called by the constructor when using the "new" operator. If an object with
         * key/value pairs was passed into the constructor then the options variable will have those key/value pairs.
         * The following options are accepted:
         *   <ul>
         *       <li>caption: a string used as a dropdown caption</li>
         *       <li>actions: an object used as a list of available dropdown actions
         *       <br>e.g. actions: <b>{</b> 'Message': function(){alert('Foo')} <b>}</b> </li>
         *       <li>enabled: boolean indicating whether dropdown should be enabled. Default is true.</li>
         *       <li>modifiers: an array used to define modifiers for the Dropdown.
         *       <a name="modifierAvailableList"></a>
         *          <br>E.g: modifiers:[{name: 'foo'}, {name: 'bar', value:'barVal'}]
         *          <ul style="padding-left: 15px;">
         *              <li>wMargin: adds a 6px margin to either side (Asset Library)</li>
         *              <li>disabled: disabled (Asset Library)</li>
         *          </ul>
         *       </li>
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

            if (this.options.enabled === false) {
                this.disable();
            } else {
                this.view.getButton().addEventHandler('click', this._onDropdownClick, this);
            }

            this.setCaption(this.options.caption || '');
            this.setActions(this.options.actions || {});
            this.setModifiers(this.options.modifiers || []);
        },

        /**
         * Add event handler to the dropdown.
         *
         * @method addEventHandler
         * @param {String} action  (change|focus|click)
         * @param {Function} callback
         * @param {Object} context
         * @return {String} eventID
         */
        addEventHandler: function (action, callback, context) {
            return this.getElement().addEventHandler(action, callback, context);
        },

        /**
         * Shorthand to add event handler for change action.
         *
         * @method addChangeHandler
         * @param {Function} callback
         * @param {Object} context
         * @return {String} eventID
         */
        addChangeHandler: function (callback, context) {
            return this.addEventHandler('change', callback, context);
        },

        /**
         * Remove the event handler from the dropdown.
         *
         * @method removeEventHandler
         * @param {String} eventID
         */
        removeEventHandler : function (eventID ) {
            this.getElement().removeEventHandler(eventID);
        },

        /**
         * Sets caption for the dropdown.
         *
         * @method setCaption
         * @param {String} caption
         */
        setCaption: function (caption) {
            this.view.getCaption().setText(caption);
        },

        /**
         * Sets actions for the dropdown. If actions array is empty than dropdown is disabled.
         *
         * @method setActions
         * @param {Object} actions
         */
        setActions: function (actions) {
            this.actions = actions;
            var keys = Object.keys(actions);
            if (keys.length > 0) {
                if (this.componentList === undefined) {
                    this.componentList = new ComponentList({
                        items: keys
                    });
                    this.componentList.attachTo(this.getElement());
                    this.componentList.addEventHandler('itemSelected', this._onComponentListClick, this);
                } else {
                    this.componentList.setItems(keys);
                }
            } else {
                this.disable();
            }
        },

        /**
         * Enables the dropdown.
         *
         * @method enable
         */
        enable: function () {
            this.getElement().removeModifier('disabled');
            this.view.getButton().setAttribute('disabled', false);
            this.view.getButton().addEventHandler('click', this._onDropdownClick, this);
        },

        /**
         * Disables the dropdown.
         *
         * @method disable
         */
        disable: function () {
            this.getElement().setModifier('disabled');
            this.view.getButton().setAttribute('disabled', true);
            this.view.getButton().removeEventHandler('click');
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
         * An event which is executed when clicked on the dropdown
         *
         * @method _onDropdownClick
         * @private
         */
        _onDropdownClick: function () {
            this.view.getButton().trigger('focus');
        },

        /**
         * An event which is executed when an action is selected from componentList
         *
         * @method _onComponentListClick
         * @private
         */
        _onComponentListClick: function () {
            var selectedActionName = this.componentList.getSelectedValue();
            this.actions[selectedActionName]();
            this.getElement().trigger('change');
        }

    });

});

define('widgets/Dropdown', ['widgets/Dropdown/Dropdown'], function (main) { return main; });
