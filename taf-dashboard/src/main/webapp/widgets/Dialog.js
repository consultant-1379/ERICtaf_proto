
define('text!widgets/Dialog/_dialog.html',[],function () { return '<div class="ebDialog">\n    <div class="ebDialog-holder">\n        <div class="ebDialogBox">\n            <div class="ebDialogBox-contentBlock">\n                <div class="ebDialogBox-primaryText"></div>\n                <div class="ebDialogBox-secondaryText"></div>\n            </div>\n            <div class="ebDialogBox-actionBlock">\n                <button class="ebBtn ebBtn_wMargin ebBtn_color_green ebBtn_colored ebDialog-primaryActionButton"></button>\n                <button class="ebBtn ebBtn_wMargin ebBtn_default ebDialog-secondaryActionButton"></button>\n            </div>\n        </div>\n    </div>\n</div>';});

/*global define*/
define('widgets/Dialog/DialogView',[
    "jscore/core",
    "text!./_dialog.html"
], function (core, template) {
    

    var DialogView = core.View.extend({

        // TODO: Should be added to core.View and executed after render()
        afterRender: function () {
            this.header = this.getElement().find('.' + DialogView.EL_HEADER);
            this.content = this.getElement().find('.' + DialogView.EL_CONTENT);
            this.actionBlock = this.getElement().find('.' + DialogView.EL_ACTION_BLOCK);
            this.primaryBtn = this.getElement().find('.' + DialogView.EL_PRIMARY_BTN);
            this.secondaryBtn = this.getElement().find('.' + DialogView.EL_SECONDARY_BTN);
        },

        getTemplate: function () {
            return template;
        },

        getRoot: function () {
            return this.getElement();
        },

        getHeader: function () {
            return this.header;
        },

        getContent: function () {
            return this.content;
        },

        getActionBlock: function () {
            return this.actionBlock;
        },

        getPrimaryButton: function () {
            return this.primaryBtn;
        },

        getSecondaryButton: function () {
            return this.secondaryBtn;
        }

    }, {
        EL_HEADER: 'ebDialogBox-primaryText',
        EL_CONTENT: 'ebDialogBox-secondaryText',
        EL_ACTION_BLOCK: 'ebDialogBox-actionBlock',
        EL_PRIMARY_BTN: 'ebDialog-primaryActionButton',
        EL_SECONDARY_BTN: 'ebDialog-secondaryActionButton'
    });

    return DialogView;

});
/*global define*/
define('widgets/Dialog/Dialog',[
    'widgets/main',
    './DialogView'
], function (core, View) {
    

    /**
     * The Dialog class uses the Ericsson brand assets.
     * The Dialog can be used as message window or popup window with action.
     *
     * @class Dialog
     */
    return core.Widget.extend({

        View: View,

        /**
         * The init method is automatically called by the constructor when using the "new" operator. If an object with
         * key/value pairs was passed into the constructor then the options variable will have those key/value pairs.
         * The following options are accepted:
         *   <ul>
         *       <li>header: a string used as a dialog header. Default is 'Header'.</li>
         *       <li>content: an Element or string used as a dialog content. Default is 'Content'.</li>
         *       <li>visible: a boolean indicating whether dialog should be visible. Default is false.</li>
         *       <li>showPrimaryButton: a boolean indicating whether dialog should have primary action button. Default is true.</li>
         *       <li>primaryButtonCaption: a string used as primary action button caption. Default is 'Save'.</li>
         *       <li>secondaryButtonCaption: a string used as secondary action button caption. Default is 'Cancel'.</li>
         *       <li>type: a string used to define the Dialog type</li>
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

            this.setHeader(this.options.header || 'Header');
            this.setContent(this.options.content || 'Content');
            this.setPrimaryButtonCaption(this.options.primaryButtonCaption || 'Save');
            this.setSecondaryButtonCaption(this.options.secondaryButtonCaption || 'Cancel');
            this.setDialogType(this.options.type || 'default');

            if (this.options.showPrimaryButton === false) {
                this.hidePrimaryButton();
            }

            this.visible = false;
            if (this.options.visible === true) {
                this.show();
            }
        },

        /**
         * Sets header for the dialog.
         *
         * @method setHeader
         * @param {String} headerText
         */
        setHeader: function (headerText) {
            this.view.getHeader().setText(headerText);
        },

        /**
         * Sets content for the dialog.
         *
         * @method setContent
         * @param {Element|String} content
         */
        setContent: function (content) {
            if (typeof(content) === 'string') {
                this.view.getContent().setText(content);
            } else if (content instanceof core.Element) {
                var children = this.view.getContent().children();
                if (children.length > 0) {
                    children.forEach(function (child) {
                        child.detach();
                    });
                }
                this.view.getContent().setText('');
                this.view.getContent().append(content);
            } else {
                throw new Error('Content for Dialog should be Element or String!');
            }
        },

        /**
         * Sets primary button caption for the dialog.
         *
         * @method setPrimaryButtonCaption
         * @param {String} buttonCaption
         */
        setPrimaryButtonCaption: function (buttonCaption) {
            this.getPrimaryButton().setText(buttonCaption);
        },

        /**
         * Sets secondary button caption for the dialog.
         *
         * @method setSecondaryButtonCaption
         * @param {String} buttonCaption
         */
        setSecondaryButtonCaption: function (buttonCaption) {
            this.getSecondaryButton().setText(buttonCaption);
        },

        /**
         * Sets the dialog type.
         * Available values: default, warning and error
         *
         * @method setDialogType
         * @param {String} type
         */
        setDialogType: function (type) {
            if (type === 'warning' || type === 'error') {
                this.view.getHeader().setModifier('type', type);
            } else {
                this.view.getHeader().removeModifier('type');
            }
        },

        /**
         * Hides primary button for the dialog.
         *
         * @method hidePrimaryButton
         */
        hidePrimaryButton: function () {
            this.getPrimaryButton().detach();
        },

        /**
         * Shows primary button for the dialog.
         *
         * @method showPrimaryButton
         */
        showPrimaryButton: function () {
            this.getPrimaryButton().detach();
            this.getSecondaryButton().detach();

            this.view.getActionBlock().append(this.getPrimaryButton());
            this.view.getActionBlock().append(this.getSecondaryButton());
        },

        /**
         * A shorthand for dialog.view.getPrimaryButton().
         *
         * @method getPrimaryButton
         * @return {Element}
         */
        getPrimaryButton: function () {
            return this.view.getPrimaryButton();
        },

        /**
         * A shorthand for dialog.view.getSecondaryButton().
         *
         * @method getSecondaryButton
         * @return {Element}
         */
        getSecondaryButton: function () {
            return this.view.getSecondaryButton();
        },

        /**
         * Shows the dialog.
         *
         * @method show
         */
        show: function () {
            this.visible = true;

            var body = core.Element.wrap(document.body);
            body.append(this.getElement());
        },

        /**
         * Hides the dialog.
         *
         * @method hide
         */
        hide: function () {
            if (this.isVisible()) {
                this.visible = false;
                this.getElement().detach();
            }
        },

        /**
         * Returns boolean of the dialog visible state.
         *
         * @method isVisible
         * @return {boolean}
         */
        isVisible: function () {
            return this.visible;
        }

    });

});
define('widgets/Dialog', ['widgets/Dialog/Dialog'], function (main) { return main; });
