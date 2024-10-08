/*global define*/
define([
    'widgets/main',
    './SystemBarView',
    './SystemBarButton/SystemBarButton'
], function (core, View, SystemBarButton) {
    

    /**
     * System Bar is fixed-height widget containing econ logo, configurable
     * system name and Ericsson branded colored stripe.
     * Please note that if your application is supposed to run within JSCore
     * Container, you shouldn't use the SystemBar, as it is provided by the
     * container. Otherwise, include it at the top of a web page. As it should
     * always be on the screen, make sure it doesn't scroll with the rest of
     * the page.
     *
     * @private
     * @class SystemBar
     */
    return core.Widget.extend({

        View: View,

        /**
         * The init method is automatically called by the constructor when using the "new" operator. If an object with
         * key/value pairs was passed into the constructor then the options variable will have those key/value pairs.
         * The following options are accepted:
         *   <ul>
         *       <li>name: a string used as a system name. Defaults to 'OSS'.</li>
         *   </ul>
         *
         * @method init
         * @param {Object} options
         */
        init: function (options) {
            this.config = options;
        },

        onViewReady: function () {
            if (this.config && this.config.name) {
                this.view.setName(this.config.name);
            }
        },

        /**
         * Adds a new button to the system bar. The button will appear on the right
         * side of the system bar. Returns the button created.
         * The following options are accepted:
         *   <ul>
         *       <li>caption: a string used as a button caption.</li>
         *       <li>image: an image to be shown on the left side of the button.</li>
         *       <li>action: a funciton to be called when the button is clicked. N/A if url is used.</li>
         *       <li>url: URL where the button should point to. N/A if action is used.</li>
         *   </ul>
         *
         * @method addButton
         * @param {Object} options
         * @return {Object} button
         */
        addButton: function (options) {
            var button = new SystemBarButton(options);
            button.attachTo(this.getElement());
            return button;
        },

        /**
         * Sets system name to be displayed in the system bar.
         *
         * @method setName
         * @param {String} name
         */
        setName: function (name) {
            this.view.setName(name);
        }

    });
});
