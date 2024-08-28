
/*global define*/
define('widgets/Tabs/ext/ext.dom',[
], function () {
    

    var dom = {};

    dom.addWindowResizeHandler = function (callBack, context) {
        return window.addEventListener('resize',callBack, context);
    };

    dom.stopPropagation = function (e) {
        e.originalEvent.stopPropagation();
    };

    dom.getElementDimensions = function (elt) {
        var nativeElt = elt._getHTMLElement();
        return {
            height: nativeElt.clientHeight,
            width: nativeElt.clientWidth
        };
    };
    return dom;
});
define('text!widgets/Tabs/_tabs.html',[],function () { return '<div class="ebTabs">\n    <div class="ebTabs-top">\n        <div class="ebTabs-tabArea"></div>\n        <div class="ebTabs-expandBtn" tabindex="1">\n            <div class="ebTabs-expandBtn-icon"></div>\n            <div class="ebTabs-dropdown"></div>\n        </div>\n    </div>\n    <div class="ebTabs-contentDiv"></div>\n</div>\n';});

/*global define*/
define('widgets/Tabs/TabsView',[
    'jscore/core',
    './ext/ext.dom',
    'text!./_tabs.html'
], function (core, domExt, template) {
    

    var TabsView = core.View.extend({

        // TODO: Should be added to core.View and executed after render()
        afterRender: function () {
            var element = this.getElement();
            this.outerEl = element.find('.' + TabsView.OUTER_CLASS);
            this.topArea = element.find('.' + TabsView.TOP_CLASS);
            this.content = element.find('.' + TabsView.CONTENT_CLASS);
            this.tabArea = element.find('.' + TabsView.TABAREA_CLASS);
            this.expandBtn = element.find('.' + TabsView.EXPANDBTN_CLASS);
            this.dropdown = element.find('.' + TabsView.DROPDOWN_CLASS);
        },

        getTemplate: function () {
            return template;
        },

        showExpandBtn: function (visible) {
            this.expandBtn.setModifier('visible', '' + visible);
        },

        showDropdown: function (visible) {
            this.dropdown.setModifier('visible', '' + visible);
        },

        getWidth: function () {
            return domExt.getElementDimensions(this.getElement()).width;
        },

        getTopArea: function () {
            return this.topArea;
        },

        getTabArea: function () {
            return this.tabArea;
        },

        getContentEl: function () {
            return this.content;
        },

        getOuterEl: function () {
            return this.outerEl;
        },

        getExpBtn: function () {
            return this.expandBtn;
        },

        getDropdown: function () {
            return this.dropdown;
        }

    }, {
        OUTER_CLASS: 'ebTabs',
        TOP_CLASS: 'ebTabs-top',
        TABAREA_CLASS: 'ebTabs-tabArea',
        EXPANDBTN_CLASS: 'ebTabs-expandBtn',
        DROPDOWN_CLASS: 'ebTabs-dropdown',
        CONTENT_CLASS: 'ebTabs-contentDiv'
    });

    return TabsView;

    /* ++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS ++++++++++++++++++++++++++++++++++++++++++ */

});
define('text!widgets/Tabs/widgets/TabItem/_tabItem.html',[],function () { return '<div class = "ebTabItem"></div>';});

/*global define*/
define('widgets/Tabs/widgets/TabItem/TabItemView',[
    'jscore/core',
    'text!./_tabItem.html'
], function(core, template) {
    

    var TabItemView =  core.View.extend({

        /*jshint validthis:true*/

        // TODO: Should be added to core.View and executed after render()
        afterRender: function () {
            this.outerEl = this.getElement();
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

        getWidth: function() {
            return this.getElement().element.clientWidth;
        }

    }, {
        OUTER_CLASS: 'ebTabItem'
    });

    return TabItemView;
});

/*global define*/
define('widgets/Tabs/widgets/TabItem/TabItem',[
    'widgets/main',
    './TabItemView'
], function (core, View) {
    

    /**
     * The TabItem class uses the Ericsson brand assets.
     *
     * @private
     * @class TabItem
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
         *       <li>title: the text displayed on the TabItem. Default is "TabItem"</li>
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
            this.onClick();
            if (this.options.title !== undefined) {
                this.setTitle(this.options.title);
            }
            else {
                this.setTitle('A TabItem Widget');
            }
        },

        /**
         * This method sets the title of the TabItem
         *
         * @method setTitle
         * @param {String} caption
         */
        setTitle: function (caption) {
            this.view.setTitle(caption);
        },

        /**
         * This method returns the title of the TabItem
         *
         * @method getTitle
         */
        getTitle: function () {
            return this.view.getTitle();
        },

        /**
         * This method returns the width of the TabItem Widget, in pixels.
         *
         * @method getWidth
         */
        getWidth: function () {
            return this.view.getWidth();
        },

        /**
         * This method sets the selection of the widget
         *
         * @method setSelected
         * @param {Boolean} selected
         */
        setSelected: function (selected) {
            this.view.getOuterEl().setModifier('selected', '' + selected);
        },

        /**
         * This method sets a max width of 100px on the widget.
         *
         * @method limitTabWidth
         * @param {Boolean} value
         */
        limitTabWidth: function (value) {
            this.view.getOuterEl().setModifier('limitTabWidth', '' + value);
        },

        /**
         * This adds an event handler for mouse clicks, which triggers an onClick event
         *
         * @method onClick
         */
        onClick: function () {
            this.getElement().addEventHandler('click', function() {
                 this.trigger('onClick');
            },this);
        }

    });

    /* ++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS ++++++++++++++++++++++++++++++++++++++++++ */


});

define('text!widgets/Tabs/widgets/TabContent/_tabContent.html',[],function () { return '<div class = "ebTabContent"></div>';});

/*global define*/
define('widgets/Tabs/widgets/TabContent/TabContentView',[
    'jscore/core',
    'text!./_tabContent.html'
], function(core, template) {
    

    var TabContentView =  core.View.extend({

        /*jshint validthis:true*/

        // TODO: Should be added to core.View and executed after render()
        afterRender: function () {
            this.outerEl = this.getElement();
        },

        getTemplate: function() {
            return template;
        },

        setContent: function(content) {
            this.outerEl.setText("");
            this.outerEl.append(content);
        },

        getContent: function() {
            return this.outerEl.getText();
        },

        getOuterEl: function() {
            return this.outerEl();
        }

    }, {
        OUTER_CLASS: 'ebTabContent'
    });

    return TabContentView;

    /* ++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS ++++++++++++++++++++++++++++++++++++++++++ */


});


/*global define*/
define('widgets/Tabs/widgets/TabContent/TabContent',[
    'widgets/main',
    './TabContentView'
], function (core, View) {
    

    /**
     * The TabContent class uses the Ericsson brand assets.
     *
     * @private
     * @class TabContent
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
         *       <li>content: the content displayed in the TabContent widget. Default is "The content widget"</li>
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
            if (this.options.content !== undefined) {
                this.setContent(this.options.content);
            }
            else {
                this.setContent("");
            }
        },

        /**
         * This method sets the content of the TabContent widget
         *
         * @method setContent
         * @param {String|core.Element|core.Widget} content
         */
        setContent: function (content) {
            if (content instanceof core.Widget) {
                content.attachTo(this.getElement());
            }
            else if (content instanceof core.Element) {
                this.view.setContent(content);
            }
            else if (content === "") {

            }
            else {
                this.view.setContent(core.Element.parse(content));
            }
        },

        /**
         * This method returns the text content of the TabContent widget
         *
         * @method getContent
         * @private
         */
        getContent: function() {
            this.view.getContent();
        }

    });
});

/*global define*/
define('widgets/Tabs/Tabs',[
    'widgets/main',
    './ext/ext.dom',
    './TabsView',
    './widgets/TabItem/TabItem',
    './widgets/TabContent/TabContent'
], function (core, domExt, View, TabItem, TabContent) {
    

    /**
     * The Tabs class uses the Ericsson brand assets.
     *
     * @class Tabs
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
         *       <li>enabled: boolean indicating whether Tabs should be enabled. Default is true.</li>
         *       <li>maxTabs: integer that determines the maximum number of tabs. Default is 15.</li>
         *       <li>limitTabWidth: integer that determines if the tab width is limited. If set to true, the tab width will be limited to 100px, if set to false the tab width will be unlimited. Default is false.</li>
         *       <li>tabs: an array containing objects that may be used to create tabs
         *          <br>e.g. tabs:[{title: 'Tab 1', content: '<p>Tab 1 Content</p>'}]
         *       </li>
         *   </ul>
         *
         * @method init
         * @param {Object} options
         */
        init: function (options) {
            this.options = options || {};
            if (this.options.tabs) {
                this.inputTabs = this.options.tabs;
            }
            else {
                this.inputTabs = [];
            }
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
            // set up the tabs widget
            if (this.options.enabled !== undefined) {
                this.enabled = this.options.enabled;
                this.view.getTopArea().setModifier('enabled', '' + this.enabled);
            }
            else {
                this.enabled = true;
                this.enable();
            }
            if (this.options.maxTabs !== undefined) {
                this.maxTabs = this.options.maxTabs;
            }
            else {
                this.maxTabs = 15;
            }
            if (this.options.limitTabWidth !== undefined) {
                this.limitTabWidth = this.options.limitTabWidth;
            }
            else {
                this.limitTabWidth = false;
            }
            showExpandBtn.call(this, false);
            showDropdown.call(this, false);
            addResizeHandler.call(this);
            addExpandHandler.call(this);
            addBlurHandler.call(this);
            this.pageReady = false;
            addOnLoadHandler.call(this); // quick fix for problem with clientWidth on page load
            this.tabArray = [];
            addTabs.call(this);
            if (this.tabArray.length > 0) {
                this.selectedTabIndex = 0;
                setSelect.call(this, this.tabArray[0]);
            }
        },

        /**
         * This method adds a tab to the Tabs widget
         *
         * @method addTab
         *
         * @param {Object} title
         * @param {Object} content
         */
        addTab: function (title, content) {
            if (this.tabArray.length < this.maxTabs) {
                var contentWidget;
                if (content instanceof TabContent) {
                    contentWidget = content;
                }
                else {
                    contentWidget = new TabContent({content: content});
                }
                this.tabArray.push({
                    titleWidget: new TabItem({title: title}),
                    contentWidget: contentWidget
                });
                this.tabArray[this.tabArray.length - 1].titleWidget.attachTo(this.view.getTabArea());
                this.tabArray[this.tabArray.length - 1].titleWidget.limitTabWidth(this.limitTabWidth);
                addClickHandler.call(this, this.tabArray[this.tabArray.length - 1]);
                if (this.pageReady) {
                    calcLayout.call(this);
                }
            }
        },

        /**
         * This method removes the tab stored at the specified index in the Tabs widget
         *
         * @method removeTab
         *
         * @param {int} tabIndex
         */
        removeTab: function (tabIndex) {
            if (this.tabArray.length !== 0) {
                if (this.selectedTabIndex === tabIndex) {
                    if (this.tabArray.length > 1) {
                        setSelect.call(this, this.tabArray[0]);
                    }
                    else {
                        clearSelect.call(this, tabIndex);
                    }
                }
                this.tabArray[tabIndex].titleWidget.detach();
                this.tabArray.splice(tabIndex, 1);
                if (this.selectedTabIndex > tabIndex) {
                    this.selectedTabIndex--;
                }
                calcLayout.call(this);
            }
        },

        /**
         * This method removes the last tab stored in the Tabs widget
         *
         * @method removeLastTab
         */
        removeLastTab: function () {
            if (this.tabArray.length !== 0) {
                if (this.selectedTabIndex === (this.tabArray.length - 1)) {
                    if (this.tabArray.length > 1) {
                        setSelect.call(this, this.tabArray[0]);
                    }
                    else {
                        clearSelect.call(this, this.tabArray.length - 1);
                    }
                }
                var toRemove = this.tabArray.pop();
                toRemove.titleWidget.detach();
            }
            calcLayout.call(this);
        },

        /**
         * This method clears all tabs stored in the Tabs widget
         *
         * @method clearTabs
         */
        clearTabs: function () {
            this.tabArray[this.selectedTabIndex].contentWidget.detach();
            for (var i = this.tabArray.length - 1; i > -1; i--) {
                this.tabArray[i].titleWidget.detach();
                this.tabArray.splice(i, 1);
                showExpandBtn.call(this, false);
            }
        },

        /**
         * This method sets the selected tab in the Tabs widget.
         *
         * @method setSelectedTab
         * @param {int} tabIndex
         */
        setSelectedTab: function (tabIndex) {
            if (tabIndex < this.tabArray.length && tabIndex >= 0) {
                setSelect.call(this, this.tabArray[tabIndex]);
            }
        },

        /**
         * This method enables the Tabs
         *
         * @method enable
         */
        enable: function () {
            this.enabled = true;
            this.view.getTopArea().setModifier('enabled', 'true');
        },

        /**
         * This method disables the Tabs
         *
         * @method disable
         */
        disable: function () {
            this.enabled = false;
            this.view.getTopArea().setModifier('enabled', 'false');
        }

    });

    /* ++++++++++++++++++++++++++++++++++++++++++ PRIVATE METHODS ++++++++++++++++++++++++++++++++++++++++++ */

    function addTabs() {
        if (this.inputTabs.length > 0) {
            for (var i = 0; i < this.inputTabs.length; i++) {
                this.addTab(this.inputTabs[i].title, this.inputTabs[i].content);
            }
        }
    }

    function addClickHandler(thisTabObj) {
        thisTabObj.titleWidget.addEventHandler('onClick', function () {
            setSelect.call(this, thisTabObj);
        }, this);
    }

    //TODO: Should be removed in the future, this timeout is required make sure the clientWidth property is determined correctly
    function addOnLoadHandler() {
        setTimeout(function () {
            this.pageReady = true;
            calcLayout.call(this);
        }.bind(this), 1); // set for 1/1000 second
    }

    function addBlurHandler() {
        this.view.getExpBtn().addEventHandler('blur', function () {
            if (!this.dropdownHasMouse) {
                showDropdown.call(this, false);
            } else {
                this.getElement().trigger('focus');
            }
        }.bind(this));
    }


//Alternative implementation, smoother but higher performance cost
//    function addResizeHandler() {
//         window.addEventListener('resize', function() {
//            calcLayout.call(this);
//        }.bind(this);
//    }

    function addResizeHandler() {
        var resizeTimeout;
        domExt.addWindowResizeHandler(function () {
            clearTimeout(resizeTimeout);
            resizeTimeout = setTimeout(function () {
                calcLayout.call(this);
            }.bind(this), 100);
        }.bind(this), false);
    }

    function addExpandHandler() {
        this.view.getExpBtn().addEventHandler('click', function (e) {
            domExt.stopPropagation(e);
            if (this.enabled) {
                if (this.dropdownShown) {
                    showDropdown.call(this, false);
                } else {
                    showDropdown.call(this, true);
                }
            }
        }.bind(this));

        this.view.getDropdown().addEventHandler('mouseenter', function () {
            this.dropdownHasMouse = true;
        }.bind(this));

        this.view.getDropdown().addEventHandler('mouseleave', function () {
            this.dropdownHasMouse = false;
        }.bind(this));
    }

    function calcLayout() {
        showExpandBtn.call(this, true);
        var tabAreaWidth = this.view.getWidth();

        this.itemsInDropdown = 0;
        if (tabAreaWidth === 0) {
            return;
        }

        var usedWidth = this.expandBtnShown ? 30 : 0;

        for (var i = 0; i < this.tabArray.length; i++) {
            if ((usedWidth + this.tabArray[i].titleWidget.getWidth()) > tabAreaWidth) {
                if (!this.expandBtnShown) {
                    showExpandBtn.call(this, true);
                }
                this.tabArray[i].titleWidget.detach();
                this.tabArray[i].titleWidget.attachTo(this.view.getDropdown());
                this.itemsInDropdown++;
            }
            else {
                usedWidth += this.tabArray[i].titleWidget.getWidth();
                this.tabArray[i].titleWidget.attachTo(this.view.getTabArea());
            }
        }
        if (this.itemsInDropdown < 1) {
            showExpandBtn.call(this, false);
        }
    }

    function setSelect(thisTabObj) {
        if (this.enabled) {
            if (this.selectedTabIndex !== undefined) {
                clearSelect.call(this, this.selectedTabIndex);
            }
            thisTabObj.titleWidget.setSelected(true);
            thisTabObj.contentWidget.attachTo(this.view.getContentEl());
            this.selectedTabIndex = this.tabArray.indexOf(thisTabObj);
        }
    }

    function clearSelect(index) {
        this.tabArray[index].titleWidget.setSelected(false);
        this.tabArray[index].contentWidget.detach();
    }

    function showDropdown(visible) {
        this.view.showDropdown(visible);
        this.dropdownShown = visible;
    }

    function showExpandBtn(visible) {
        this.view.showExpandBtn(visible);
        this.expandBtnShown = visible;
    }

});
define('widgets/Tabs', ['widgets/Tabs/Tabs'], function (main) { return main; });
