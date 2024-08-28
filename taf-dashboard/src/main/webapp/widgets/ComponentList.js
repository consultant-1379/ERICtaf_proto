
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
