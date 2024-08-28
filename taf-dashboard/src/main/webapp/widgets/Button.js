
define('text!widgets/Button/_button.html',[],function () { return '<button class="ebBtn"></button>';});

/*global define*/
define('widgets/Button/ButtonView',[
    "jscore/core",
    "text!./_button.html"
], function (core, template) {
    

    return core.View.extend({

        getTemplate: function () {
            return template;
        }

    });

});

/*global define*/
define('widgets/Button/Button',[
    'widgets/main',
    './ButtonView'
], function (core, View) {
    

    /**
     * The Button class wraps the Ericsson brand assets button in a Widget.
     *
     * @class Button
     */
    return core.Widget.extend({

        View: View,

        /**
         * The init method is automatically called by the constructor when using the "new" operator. If an object with
         * key/value pairs was passed into the constructor then the options variable will have those key/value pairs.
         *
         * The following options are accepted:
         *   <ul>
         *       <li>caption: a string used as a button caption.</li>
         *       <li>enabled: boolean indicating whether button should be enabled. Default is true.</li>
         *       <li>modifiers: an array used to define modifiers for the Button. (Asset Library)
         *          <a name="modifierAvailableList"></a>
         *          <br>E.g: modifiers:[{name: 'foo'}, {name: 'bar', value:'barVal'}]
         *          <ul style="padding-left: 15px;">
         *              <li>wMargin: adds a 6px margin to either side (Asset Library)</li>
         *              <li>disabled: disabled (equiv DOM attribute disabled) (Asset Library)</li>
         *              <li>small: size variation small</li>
         *              <li>large: size variation large</li>
         *              <li>disabled: disabled</li>
         *              <li>colored: mandatory if you specify one of the next additional color modifiers:
         *              <br>color_darkGreen, color_green, color_orange, color_red, color_purple, color_paleBlue
         *              <br>E.g: modifiers:[{name: 'colored'}, {name: 'color', value:'green'}]
         *              <br>(List of colors defined in the Asset Library)
         *              </li>
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
            this.setCaption(this.options.caption || 'Button');
            this.setModifiers(this.options.modifiers || []);
            if (this.options.enabled === false) {
                this.disable();
            }

            this.addEventHandler('click', this._onClick, this);
        },

        /**
         * Add event handler to the button.
         *
         * @method addEventHandler
         * @param {String} action
         * @param {Function} callback
         * @param {Object} context
         * @return {String} eventID
         */
        addEventHandler: function (action, callback, context) {
            return this.getElement().addEventHandler(action, callback, context);
        },

        /**
         * Remove the event handler from the button.
         *
         * @method removeEventHandler
         * @param {String} eventID
         */
        removeEventHandler : function (eventID ) {
            this.getElement().removeEventHandler(eventID);
        },

        /**
         * Sets caption for the button.
         *
         * @method setCaption
         * @param {String} caption
         */
        setCaption: function (caption) {
            this.view.getElement().setText(caption);
        },

        /**
         * Enables the button.
         *
         * @method enable
         */
        enable: function () {
            this.getElement().setAttribute('disabled', null);
            this.getElement().removeModifier('disabled');
        },

        /**
         * Disables the button.
         *
         * @method disable
         */
        disable: function () {
            this.getElement().setAttribute('disabled', 'disabled');
            this.getElement().setModifier('disabled');
        },

        /**
         * Add a single modifier to the widget.<br>
         * <a href="#modifierAvailableList">see available modifiers</a>
         *
         * @method setModifier
         * @param {String} key
         * @param {String|int|boolean} value
         */
        setModifier: function (key, value) {
            this.getElement().setModifier(key, value);
        },

        /**
         * A methods, which allows to define a list of modifiers to the widget.<br>
         * <a href="#modifierAvailableList">see available modifiers</a>
         *
         * @method setModifiers
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
         * An event which is executed when clicked on combobox helper button
         *
         * @method _onHelperClick
         * @private
         */
        _onClick: function () {
            this.trigger('click');
        }

    });

});

define('widgets/Button', ['widgets/Button/Button'], function (main) { return main; });
