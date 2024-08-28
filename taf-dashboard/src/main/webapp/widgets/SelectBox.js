
define('text!widgets/SelectBox/_selectBox.html',[],function () { return '<div class="ebSelect">\n    <button class="ebBtn ebBtn_default">\n        <span class="ebSelect-value"></span>\n        <i class="ebIcon ebIcon_right ebIcon_downArrow"></i>\n    </button>\n</div>';});

/*global define*/
define('widgets/SelectBox/SelectBoxView',[
    "jscore/core",
    "text!./_selectBox.html"
], function (core, template) {
    

    var SelectBoxView = core.View.extend({

        // TODO: Should be added to core.View and executed after render()
        afterRender: function () {
            this.value = this.getElement().find('.' + SelectBoxView.EL_VALUE);
            this.button = this.getElement().find('.' + SelectBoxView.EL_BUTTON);
            this.icon = this.getElement().find('.' + SelectBoxView.EL_ICON);
        },

        getTemplate: function () {
            return template;
        },

        getRoot: function () {
            return this.getElement();
        },

        getValue: function () {
            return this.value;
        },

        getButton: function () {
            return this.button;
        },

        getIcon: function () {
            return this.icon;
        }

    }, {
        'EL_VALUE': 'ebSelect-value',
        'EL_BUTTON': 'ebBtn',
        'EL_ICON': 'ebIcon'
    });

    return SelectBoxView;

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
define('widgets/SelectBox/SelectBox',[
    'widgets/main',
    './SelectBoxView',
    'widgets/ComponentList'
], function (core, View, ComponentList) {
    

    /**
     * The SelectBox class uses the Ericsson brand assets.
     *
     * @class SelectBox
     */
    return core.Widget.extend({

        View: View,

        /**
         * The init method is automatically called by the constructor when using the "new" operator. If an object with
         * key/value pairs was passed into the constructor then the options variable will have those key/value pairs.
         * The following options are accepted:
         *   <ul>
         *       <li>value: a string used as a selected value of the selectBox</li>
         *       <li>items: an array used as a list of available items in the selectBox</li>
         *       <li>enabled: boolean indicating whether selectBox should be enabled. Default is true.</li>
         *       <li>modifiers: an array used to define modifiers for the SelectBox.
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
                this.view.getButton().addEventHandler('click', this._onSelectBoxClick, this);
            }

            this.setValue(this.options.value || '');
            this.setItems(this.options.items || []);
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
         * Remove the event handler from the combobox.
         *
         * @method removeEventHandler
         * @param {String} eventID
         */
        removeEventHandler : function (eventID ) {
            this.getElement().removeEventHandler(eventID);
        },

        /**
         * Sets value for the selectBox.
         *
         * @method setValue
         * @param {String} value
         */
        setValue: function (value) {
            this.view.getValue().setText(value);
        },

        /**
         * Returns value for the selectBox.
         *
         * @method getValue
         * @return {String} value
         */
        getValue: function () {
            return this.view.getValue().getText();
        },

        /**
         * Sets items for the selectBox. If items array is empty than field is disabled.
         *
         * @method setItems
         * @param {Array} items
         */
        setItems: function (items) {
            if (items.length > 0) {
                if (this.componentList === undefined) {
                    this.componentList = new ComponentList({
                        items: items
                    });
                    this.componentList.attachTo(this.getElement());
                    this.componentList.addEventHandler('itemSelected', this._onComponentListClick, this);
                } else {
                    this.componentList.setItems(items);
                }
            } else {
                this.disable();
            }
        },

        /**
         * Enables the selectBox.
         *
         * @method enable
         */
        enable: function () {
            this.getElement().removeModifier('disabled');
            this.view.getButton().setAttribute('disabled', false);
            this.view.getButton().addEventHandler('click', this._onSelectBoxClick, this);
        },

        /**
         * Disables the selectBox.
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
         * An event which is executed when clicked on the selectBox
         *
         * @method _onSelectBoxClick
         * @private
         */
        _onSelectBoxClick: function () {
            this.view.getButton().trigger('focus');
        },

        /**
         * An event which is executed when a value is selected from componentList
         *
         * @method _onComponentListClick
         * @private
         */
        _onComponentListClick: function () {
            var selectedVal = this.componentList.getSelectedValue();
            if(selectedVal !== this.getValue()){
                this.setValue(selectedVal);
                this.getElement().trigger('change');
            }
        }

    });

});

define('widgets/SelectBox', ['widgets/SelectBox/SelectBox'], function (main) { return main; });
