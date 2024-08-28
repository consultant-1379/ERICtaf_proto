
define('text!widgets/Accordion/_accordion.html',[],function () { return '<div class="ebAccordion">\n    <h4 class="ebAccordion-header">\n        <button class="ebIcon ebAccordion-button" title="Open"></button>\n        <span class="ebAccordion-title"></span>\n    </h4>\n\n    <ul class="ebAccordion-list"></ul>\n</div>';});

define('text!widgets/Accordion/_accordionListItem.html',[],function () { return '<li class="ebAccordion-listItem"></li>';});

/*global define*/
define('widgets/Accordion/AccordionView',[
    "jscore/core",
    "text!./_accordion.html",
    "text!./_accordionListItem.html"
], function (core, template, listItemTemplate) {
    

    var AccordionView = core.View.extend({

        // TODO: Should be added to core.View and executed after render()
        afterRender: function () {
            this.title = this.getElement().find('.' + AccordionView.EL_TITLE);
            this.button = this.getElement().find('.' + AccordionView.EL_BUTTON);
            this.list = this.getElement().find('.' + AccordionView.EL_LIST);
            this.header = this.getElement().find('.' + AccordionView.EL_HEADER);

            this.getButton().setModifier('downArrow');

            this.listHeight = 0;
        },
        getTemplate: function () {
            return template;
        },
        getTitle: function () {
            return this.title;
        },
        getButton: function () {
            return this.button;
        },
        getList: function () {
            return this.list;
        },
        getHeader: function () {
            return this.header;
        },
        setEvents: function () {
            this.headerEvtId = this.header.addEventHandler('click', this._onHeaderClick, this);
        },
        removeEvents: function () {
            if (this.headerEvtId) {
                this.header.removeEventHandler(this.headerEvtId);
            }
        },
        setItems: function (items) {
            items.forEach(function (item) {
                this.addItem(item);
            }.bind(this));
        },

        addItem: function (item) {
            var itemElt = core.Element.parse(listItemTemplate);

            if (item instanceof core.Element) {
                itemElt.append(item);
            }
            else if (typeof(item) === 'string') {
                itemElt.setText(item);
                itemElt.setAttribute('title', item);
            }

            this.getList().append(itemElt);
            return itemElt;
        },

        _onHeaderClick: function () {
            if (this.getList().getAttribute('data-expanded') === 'expanded') {
                this.foldList();
            }
            else {
                this.expandList();
            }
        },
        recalculateHeight: function () {
            var listHeight = 0;
            this.list.children().forEach(function (item) {
                listHeight += item.getProperty('clientHeight');
            });
            this.listHeight = listHeight;
        },
        foldList: function () {
            this.getButton().removeModifier('upArrow');
            this.getButton().setModifier('downArrow');
            this.getList().setAttribute('data-expanded', 'folded');
            this.getList().setStyle('max-height', '0');
        },
        expandList: function () {
            this.recalculateHeight();

            this.getButton().removeModifier('downArrow');
            this.getButton().setModifier('upArrow');

            this.getList().setAttribute('data-expanded', 'expanded');
            this.getList().setStyle('max-height', this.listHeight + 'px');
        }
    }, {
        'EL_HEADER': 'ebAccordion-header',
        'EL_TITLE': 'ebAccordion-title',
        'EL_BUTTON': 'ebAccordion-button',
        'EL_LIST': 'ebAccordion-list'
    });

    return AccordionView;
});

/*global define*/
define('widgets/Accordion/Accordion',[
    'widgets/main',
    './AccordionView'
], function (core, View) {
    

    /**
     * The Accordion class wraps a menu entry and its sub-menu list in a Widget.
     *
     * @class Accordion
     */
    return core.Widget.extend({

        View: View,

        /**
         * The init method is automatically called by the constructor when using the "new" operator. If an object with
         * key/value pairs was passed into the constructor then the options variable will have those key/value pairs.
         *
         * The following options are accepted:
         *   <ul>
         *       <li>title: a string used for the accordion header.</li>
         *       <li>items: a list of strings or ext.dom.Element used as a accordion items.</li>
         *       <li>enabled: boolean indicating whether the accordion should be enabled. Default is true.</li>
         *       <li>modifiers: an array used to define modifiers for the Accordion.</li>
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

            this.setTitle(this.options.title || 'Accordion Header');

            if (this.options.enabled === false) {
                this.disable();
            } else {
                this.enable();
            }


            this.setItems(this.options.items || []);

            this.setModifiers(this.options.modifiers || []);
        },

        /**
         * Sets title for the Accordion
         *
         * @method setTitle
         * @param {String} title
         */
        setTitle: function (title) {
            if (typeof(title) === 'string') {
                this.view.getTitle().setText(title);
            } else if (title instanceof core.Element) {
                var tagName = title._getHTMLElement().tagName.toLowerCase();

                if (tagName === 'a' || tagName === 'span' || tagName === 'div') {
                    this.view.getTitle().append(title);
                }
            }
        },

        /**
         * Sets items for the Accordion. If items array is empty then accordion is disabled. The list can contain string, ext.dom.Element or both mixed.
         *
         * @method setItems
         * @param {Array} items
         */
        setItems: function (items) {
            if (items && items.length > 0) {
                this.view.getButton().setProperty('disabled', false);
                this.view.setItems(items);
            } else {
                this.view.getButton().setProperty('disabled', true);
            }
        },

        /**
         * Enables the accordion.
         *
         * @method enable
         */
        enable: function () {
            this.getElement().setProperty('disabled', false);
            this.getElement().removeModifier('disabled');
            this.view.getHeader().removeModifier('disabled');
            this.view.getButton().removeModifier('disabled');
            this.view.setEvents();
        },

        /**
         * Disables the accordion.
         *
         * @method disable
         */
        disable: function () {
            this.getElement().setProperty('disabled', true);
            this.getElement().setModifier('disabled');
            this.view.getHeader().setModifier('disabled');
            this.view.getButton().setModifier('disabled');
            this.view.removeEvents();
        },

        /**
         * A methods, which allows to define all modifiers
         *
         * @param {Array} modifiers Contains objects {name: {String}[, value: {String}]}
         */
        setModifiers: function (modifiers) {
            modifiers.forEach(function (modifier) {
                this.setModifier(modifier.name, modifier.value);
            }, this);
        },

        /**
         * A shorthand for accordion.getElement().setModifier().
         *
         * @method setModifier
         * @param {String} key
         * @param {String|int|boolean} value
         */
        setModifier: function (key, value) {
            this.getElement().setModifier(key, value);
        },

        /**
         * A shorthand for accordion.getElement().removeModifier().
         *
         * @method removeModifier
         * @param {String} key
         */
        removeModifier: function (key) {
            this.getElement().removeModifier(key);
        }
    });

});

define('widgets/Accordion', ['widgets/Accordion/Accordion'], function (main) { return main; });
