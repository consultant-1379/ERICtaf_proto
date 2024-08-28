
define('text!widgets/Spinner/_spinner.html',[],function () { return '<table data-namespace="ebSpinner">\n    <tr>\n        <td data-name="label"></td>\n        <td data-name="holder">\n            <button data-name="up" class="ebIcon ebIcon_button ebIcon_upArrow ebIcon_interactive"></button>\n            <input type="text" data-name="input" class="ebInput"/>\n            <button data-name="down" class="ebIcon ebIcon_button ebIcon_downArrow ebIcon_interactive"></button>\n        </td>\n        <td data-name="value"></td>\n    </tr>\n</table>';});

/*global define*/
define('widgets/utils/dataNameUtils',[
    'jscore/core'
], function (core) {
    

    return {
        prefix: '',

        translate: function(prefix, template, view) {
            if ((typeof template) === 'string') {
                template = core.Element.parse(template);
            }
            this.prefix = prefix;
            this.translateElement.call(this, template, view, true);
            this.translateChildren.call(this, template, view);
            return template;
        },

        translateElement: function(element, view, isRoot) {
            if (isRoot) {
                if (this.prefix === undefined || this.prefix === null) {
                    this.prefix = element.getAttribute('data-namespace');
                    element._getHTMLElement().removeAttribute('data-namespace');
                    element.setAttribute('class', this.prefix);
                }
            }

            var name = element.getAttribute('data-name');
            var type = element.getAttribute('data-type');

            var classes = element.getAttribute('class');

            if (classes !== undefined) {
                classes = classes.split(' ');
            } else {
                classes = [];
            }

            if (type) {
                classes.push(this.namespace + '-' + type);
                element._getHTMLElement().removeAttribute('data-type');
                if (view[type] === undefined) {
                    view[type] = [];
                }

                view[type].push(element);
            }

            if (name) {
                classes.push(this.prefix + '-' + name);
                // Prevents a conflicts with existing methods
                if (view[name] === undefined) {
                    view[name] = element;
                } else {
                    throw new Error('Name already in use: ' + name);
                }
                element._getHTMLElement().removeAttribute('data-name');
            }

            element.setAttribute('class', classes.join(' '));

            if (isRoot && name) {
                this.prefix = this.prefix + '-' + name;
            }
        },

        translateChildren: function(template, view) {
            var children = template.children();

            for (var i = 0, l = children.length; i < l; i++) {
                var child = children[i];
                this.translateElement.call(this, child, view, false);

                if (child.children().length > 0) {
                    this.translateChildren.call(this, child, view);
                }
            }
        }
    };

});

/*global define*/
define('widgets/Spinner/SpinnerView',[
    'jscore/core',
    'text!./_spinner.html',
    'widgets/utils/dataNameUtils'
], function (core, template, dataNameUtils) {
    

    var SpinnerView = core.View.extend({

        getTemplate: function () {
            return dataNameUtils.translate(null, template, this);
        },

        // TODO: Should be added to core.View and executed after render()
        afterRender: function () {
            this.ebInput = this.getElement().find('.' + SpinnerView.EL_INPUT_CLASS);
        },

        getRoot: function () {
            return this.getElement();
        },

        getPrefix: function () {
            return this[SpinnerView.EL_PREFIX];
        },

        getPostfix: function () {
            return this[SpinnerView.EL_POSTFIX];
        },

        getUpButton: function () {
            return this[SpinnerView.EL_UP_BUTTON];
        },

        getDownButton: function () {
            return this[SpinnerView.EL_DOWN_BUTTON];
        },

        getInput: function () {
            return this[SpinnerView.EL_INPUT];
        },

        getEbInput: function () {
            return this.ebInput;
        }

    }, {
        'EL_PREFIX': 'label',
        'EL_POSTFIX': 'value',
        'EL_UP_BUTTON': 'up',
        'EL_DOWN_BUTTON': 'down',
        'EL_INPUT': 'input',
        'EL_INPUT_CLASS': 'ebInput'
    });

    return SpinnerView;

});
/*global define*/
define('widgets/utils/parserUtils',[], function () {
    

    /**
     * The parserUtils used to parse strings.
     *
     * @class parserUtils
     * @namespace utils
     */
    return {
        /**
         * Parse string to integer param
         *
         * @method parseInt
         * @param {String} value String which requires parsing
         * @return {int} Parsed integer
         */
        parseInt: function (value) {
            // Since JS parseInt is not very restrictive, we have to make our own, as "0r" for example would be treated as 0
            // see https://developer.mozilla.org/en-US/docs/JavaScript/Reference/Global_Objects/parseInt at the end

            if (/^\-?([0-9]+|Infinity)$/.test(value)) {
                return Number(value);
            }
            return NaN;
        }
    };
});
/*global define*/
define('widgets/Spinner/Spinner',[
    'widgets/main',
    './SpinnerView',
    'widgets/utils/parserUtils'
], function (core, View, parserUtils) {
    

    /**
     * The Spinner class uses the Ericsson brand assets.
     *
     * @class Spinner
     */
    return core.Widget.extend({

        /*jshint validthis:true*/

        View: View,

        /**
         * The init method is automatically called by the constructor when using the "new" operator. If an object with
         * key/value pairs was passed into the constructor then the options variable will have those key/value pairs.
         * The following options are accepted:
         *   <ul>
         *       <li>value:     an integer used as a default value for the Spinner. Default is min value or 0.</li>
         *       <li>min:       an integer used as a min value for the Spinner. Default is 0.</li>
         *       <li>max:       an integer used as a max value for the Spinner.</li>
         *       <li>prefix:    a string used as a label for the Spinner.</li>
         *       <li>postfix:   a string used as a value type for the Spinner.</li>
         *       <li>inputSize: a string used to define size for the Spinner input field.</li>
         *   </ul>
         *
         * @method init
         * @param {Object} options
         */
        init: function (options) {
            this.options = options || {};

            this.minDelay = 100;
            this.maxDelay = 400;
            this.delayStep = 50;
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

            // Always start with minimal value
            if (this.options.min === undefined) {
                this.options.min = 0;
            }
            // If not defined default value, start with minimum value
            if (this.options.value === undefined) {
                this.options.value = this.options.min;
            }

            this.setValue(this.options.value);
            this.setPrefix(this.options.prefix || '');
            this.setPostfix(this.options.postfix || '');
            this.setInputSize(this.options.inputSize || 'miniW');

            this.view.getUpButton().addEventHandler('mousedown', startAutoIncrement, this);
            this.view.getUpButton().addEventHandler('mouseup', endAutoIncrement, this);
            this.view.getUpButton().addEventHandler('mouseout', endAutoIncrement, this);
            this.view.getDownButton().addEventHandler('mousedown', startAutoDecrement, this);
            this.view.getDownButton().addEventHandler('mouseup', endAutoDecrement, this);
            this.view.getDownButton().addEventHandler('mouseout', endAutoIncrement, this);

            this.view.getInput().addEventHandler('keypress', handleKeyPress, this);
            this.view.getInput().addEventHandler('keyup', handleKeyUp, this);
        },

        /**
         * Sets the text that appears in front of Spinner input
         *
         * @method setPrefix
         * @param {String} text Text in front of Spinner input
         */
        setPrefix: function (text) {
            if (text && text.trim() !== '') {
                var prefix = this.view.getPrefix();
                prefix.setStyle('display', 'table-cell');
                prefix.setText(text);
            }
        },

        /**
         * Sets the text that appears in behind of Spinner input
         *
         * @method setPostfix
         * @param {String} text Text behind Spinner input
         */
        setPostfix: function (text) {
            if (text && text.trim() !== '') {
                var postfix = this.view.getPostfix();
                postfix.setStyle('display', 'table-cell');
                postfix.setText(text);
            }
        },

        /**
         * Sets the value that will be visible in Spinner
         *
         * @method setValue
         * @param {int|String} value Integer or String containing Integer
         */
        setValue: function (value) {
            if (value !== undefined && value !== null) {
                if (value instanceof String || typeof(value) === 'string') {
                    var num = parserUtils.parseInt(value);
                    if (!isNaN(num)) {
                        this.value = getInRange.call(this, num);
                        update.call(this);
                    }
                } else if (/[0-9]|-/.test(value)) {
                    this.value = getInRange.call(this, value);
                    update.call(this);
                }
            }
        },

        /**
         * Returns the value that is presented in Spinner input field
         *
         * @method getValue
         * @return {int}
         */
        getValue: function () {
            return this.value;
        },

        /**
         * A method which allows to define input field size
         *
         * @param {String} inputSize Can be selected from available sizes: ['miniW', 'smallW', 'longW', 'xLongW']
         */
        setInputSize: function (inputSize) {
            this.view.getEbInput().setModifier(inputSize);
        }

    });

    /* ++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS ++++++++++++++++++++++++++++++++++++++++++ */

    function inRange(value) {
        var min = this.options.min;
        var max = this.options.max;

        if (max === undefined) {
            return value >= min;
        } else {
            return value <= max && value >= min;
        }
    }

    function getInRange(value) {
        value = value > this.options.max ? this.options.max : value;
        return value < this.options.min ? this.options.min : value;
    }

    function handleKeyPress(event) {
        var key = String.fromCharCode(event.which);
        if(event.which < 47  || (event.which >= 112 && event.which <= 123)|| event.ctrlKey) {
            // Since Firefox is very restrictive on these keys, we have to manually allow for special keys like backspace
            // Here is good reading about madness about keycodes http://unixpapa.com/js/key.html
            // + more info on keycodes http://www.webonweboff.com/tips/js/event_key_codes.aspx
            return false;
        }

        // Regular expression to test if provided value is integer (can be also negative)
        var isInteger = /-?[0-9]/;
        return isInteger.test(key);
    }

    function handleKeyUp(event) {
        var value = parserUtils.parseInt(this.view.getInput().getValue());

        if (!inRange.call(this, value)) {
            // Roll back value to old one
            update.call(this);
        } else {
            this.value = value;
            applyModifiers.call(this);
        }
    }

    function autoIncrement() {
        clearTimeout(this.autoIncrementTimeout);
        this.autoIncrementTimeout = undefined;

        if (increment.call(this)) {
            if (this.autoIncrementDelay >= this.minDelay) {
                this.autoIncrementDelay -= this.delayStep;
            }

            this.autoIncrementTimeout = setTimeout(
                autoIncrement.bind(this),
                this.autoIncrementDelay
            );
        }
    }

    function startAutoIncrement() {
        this.autoIncrementDelay = this.maxDelay;
        this.autoIncrementTimeout = setTimeout(
            autoIncrement.bind(this),
            this.autoIncrementDelay
        );
    }

    function endAutoIncrement() {
        if (this.autoIncrementTimeout) {
            clearTimeout(this.autoIncrementTimeout);
            increment.call(this);
            this.autoIncrementTimeout = undefined;
        }
    }

    function increment() {
        if (inRange.call(this, this.value + 1)) {
            this.value++;
            update.call(this);
            return true;
        }
        return false;
    }

    function autoDecrement() {
        clearTimeout(this.autoDecrementTimeout);
        this.autoDecrementTimeout = undefined;

        if (decrement.call(this)) {
            if (this.autoDecrementDelay >= this.minDelay) {
                this.autoDecrementDelay -= this.delayStep;
            }

            this.autoDecrementTimeout = setTimeout(
                autoDecrement.bind(this),
                this.autoDecrementDelay
            );
        }
    }

    function startAutoDecrement() {
        this.autoDecrementDelay = this.maxDelay;
        this.autoDecrementTimeout = setTimeout(
            autoDecrement.bind(this),
            this.autoDecrementDelay
        );
    }

    function endAutoDecrement() {
        if (this.autoDecrementTimeout) {
            clearTimeout(this.autoDecrementTimeout);
            decrement.call(this);
            this.autoDecrementTimeout = undefined;
        }
    }

    function decrement() {
        if (inRange.call(this, this.value - 1)) {
            this.value--;
            update.call(this);
            return true;
        }
        return false;
    }

    function update() {
        // Set input value
        this.view.getInput().setValue(this.value);

        // Enable/Disable up/down buttons
        applyModifiers.call(this);
    }

    function applyModifiers() {
        // Up button
        if (inRange.call(this, this.value + 1)) {
            this.view.getUpButton().setAttribute('disabled', false);
        } else {
            this.view.getUpButton().setAttribute('disabled', true);
        }

        // Down button
        if (inRange.call(this, this.value - 1)) {
            this.view.getDownButton().setAttribute('disabled', false);
        } else {
            this.view.getDownButton().setAttribute('disabled', true);
        }
    }
});
define('widgets/Spinner', ['widgets/Spinner/Spinner'], function (main) { return main; });
