define([
    'jscore/core',
    './FlowChartView',
    'thirdparty/d3',
    'app/ext/d3tooltip',
    'app/ext/d3path',
    'app/ext/d3node',
    'jscore/ext/utils/base/underscore',
    'jscore/base/jquery',
    'widgets/Tabs'
], function (core, View, d3, tip, path, node, _, $, Tabs) {
    return core.Widget.extend({
        View: View,

        init: function () {
            this.active_field = 0;
            this.data = [[]];
            this.dataByEventId = [];
            this.settings = this.getSettings();
            this.handledEvents = [];
            
			this.firstLoad = true;
            this.tabs = new Tabs({
                enabled: true
            });
            _.extend(this, tip);
            _.extend(this, path);
            _.extend(this, node);
            
            this.cluster = d3.layout.cluster();
            this.firstRun = true;
            
            this.nodeSettings = {
                boxHeight : 60,
                boxWidth : 180
            };
            this.pathSettings = {
                stoke_width : 3,
                instant : true
            };
            this.eventCollection = this.options.eventCollection;
			this.aspectRatio = this.options.aspectRatio;
			
			this.defaultAspectRatio = this.aspectRatio;
            
        },
        
        // Function for stringification of Eiffel MB - data
        showTooltip: function (name) {
            var event = this.eventCollection._collection.get(name).attributes;

            if (!this.tooltip || this.tooltip.__data__.name !== event.id) {
                return;
            }
            var circular = [], outputTip = "";
            outputTip = JSON.stringify(event, function (key, value) {
                if (typeof value === 'object' && value !== null) {
                    if (circular.indexOf(value) !== -1) {
                        return "self"; // Circular reference found, discard key
                    }
                    if (key === "parent") {
                        return {"id": value.id};
                    }
                    if (key === "children") {
                        var objectList = [];
                        _.each(value, function (v) {
                            objectList.push({"id": v.id});
                        });
                        return objectList;
                    }
                    circular.push(value); // Store value in our collection
                }
                return value;
            }, "\t");
            circular = null; // clear the cache (garbage collection)

            var exp = /(\b(https?|ftp|file):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/ig;
            // make the text readable;
            var message = outputTip.replace(/(\]\,|\}\,)\n\t+(\[|\{)/g, "$1$2")         //place "}," on same line as "{" to save space
                .replace(/\n/g, '<br />')                           //break rows
                .replace(/\t/g, '&nbsp;&nbsp;&nbsp;&nbsp;')        //visualize spaces
                .replace(exp, "<a href='$1' target='_blank'>$1</a>");                //create the links

            this.tooltipText = message;
        },
    
        /// final step , bind some data
        bind: function (data) {
            var conv2tree = function (data) {
                var root = this.getRoot(data);
                var hasParentFlag = [];
                hasParentFlag[root.id] = true;
                this.traverseEdge(data, function (source, target) {
                    if (!hasParentFlag[target.id] && source.id !== target.id) {
                        if (!source.children) {
                            source.children = [];
                        }
                        source.children.push(target);
                        hasParentFlag[target.id] = true;
                    }
                });
                return root;
            }.bind(this);

            var buildNodes = function (tree) {
                return this.cluster.nodes(tree);
            }.bind(this);

            var buildLinks = function (data) {
                var result = [];
                this.traverseEdge(data, function (source, target) {
                    result.push({
                        'source': source,
                        'target': target
                    });
                });
                return result;
            }.bind(this);

            var merge = function (nodes, links) {
                this.nodes = nodes;
                this.links = links;
            }.bind(this);

            //1)temporarily convert a connectivity to a tree
            var tree = conv2tree(data);
            //2)caculate for nodes' coords with <code>this.cluster.nodes(tree);</code>
            var nodes = buildNodes(tree);
            //3)fill in all the edges(links) of the connectivity
            var links = buildLinks(data);
            //4)do merge to keep info like node's position
            merge(nodes, links);
            //5)redraw
            var firstPosX = null;
            nodes.forEach(function (d) {
                firstPosX = firstPosX ? firstPosX : d.x;
                d.x = (d.x - firstPosX) * 400;
                d.y = d.depth * 150 + d.position * 100;
            });
            
            this.redraw();
        },
        
        clear: function () {
            if (this.firstRun === true) {
                this.trigger('onLoad');
                this.svgArea = d3.select(this._parent.element).select(".eaVEApp-wFlowChart-svgArea");
                this.svgArea.append("svg").append("g").append("svg:defs");
                this.svg = this.svgArea.select("svg");
                this.svg_g = this.svg.select("g");
                this.firstRun = false;
            }
            else {
                this.svgArea.selectAll(".d3-tooltip-node").remove();
            }
            var minWidth = _.max(this.nodes, function (d) { return d.y; }).y + 300;
            var minHeight = _.max(this.nodes, function (d) { return d.x; }).x + 500;
            minWidth = _.max([this.nodeSettings.boxWidth*4, minWidth]);
            minHeight = _.max([this.nodeSettings.boxHeight*4, minHeight]);
            
            this.svg
                .attr("class", "eaVEApp-wFlowchart-svg")
                .attr("width", 100*this.aspectRatio+"%")
                .attr("height","100%")
                .attr("viewBox", "0 0 " + minWidth + " " + minHeight);
        
            this.svg
                .select("g")
                .attr("transform", "translate(150, " + minHeight / 2 + ")");
        
            this.svg_g.select("defs").selectAll("marker")
                .data(["suit"])
                .enter()
                .append("svg:marker")
                .attr("id", "idArrow")
                .attr("viewBox", "0 -5 10 10")
                .attr("refX", 15)
                .attr("refY", -1.5)
                .attr("markerWidth", 8)
                .attr("markerHeight", 8)
                .attr("orient", "auto")
                .append("svg:path")
                .attr("d", "M 0,-5 L 10,0 L 0,5");
        },

        /// call redraw() if necessary (reconfig, recostom the actions, etc.)
        redraw: function () {
            var self = this;
            this.clear();
            this.drawPaths(this.svg_g, this.links, this.pathSettings);
            var nodes = this.drawNodes(this.svg_g, this.nodes, this.nodeSettings);
            
            if(typeof this.addTooltip === 'function') {
                nodes.each(function () {
                    var node = this;
                    self.addTooltip(this, {
                        content: function() {
                            self.tooltip = node;
                            self.showTooltip(node.__data__.name);
                            return self.tooltipText;
                        },
                        offset: [45, 15],
                        direction: "e"
                    });
                });
            }
			this.firstLoad = false;
        },
        
        getRoot: function (data) {
            return data[0];
        },
        
        traverse: function (data, callback) {
            if (!data) console.error('data is null');

            function _init() {
                var i;
                for(i in data) {
                    data[i].visited = false;
                }
            }

            function _traverse(pt, callback) {
                if (!pt) {return;}
                pt.visited = true;
                callback(pt);
                if (pt.ref) {
                    pt.ref.forEach(function (ref) {
                        var childNode = _.find(data, function (d) { return d.id === ref.to; });

                        if (childNode && !childNode.visited) {
                            _traverse(childNode, callback);
                        }
                    });
                }
            }

            _init();
            _traverse(this.getRoot(data), callback);
        },

        traverseEdge: function (data, callback) {
            if (!data) console.error('data is null');

            this.traverse(data, function (node) {
                if (node.ref) {
                    node.ref.forEach(function (ref) {
                        var childNode = _.find(data, function (d) { return d.id === ref.to; });
                        if (childNode) {
                            callback(node, childNode, ref);
                        }
                    });
                }
            });
        },

        refresh: function () {
            //request data here
            var myData = this.settings.data;
            
            var data = [];
            for (var i = 0; i < myData.length; i++) {
                data[i] = {
                    id: myData[i].id,
                    name: myData[i].name,
                    title: myData[i].type,
                    textmatrix: myData[i].textmatrix,
                    class: myData[i].rate,
                    ref: myData[i].ref,
                    position: myData[i].position
                };
            }
            if (data.length > 0) {
                this.bind(data);
            }
        },

        setEventFinder: function(eventFinder) {
            this.eventFinder = eventFinder;
        },

        getData: function () {
            return { id: _.uniqueId('flow-chart-') };
        },

        getSettings: function () {
            return {
                edge_show_arrow: true,
                node_draggable: true,
                data: this.data[this.active_field]
            };
            
        },

        findActiveTab: function(scope) {
            scope.active_field = $(this).find(".ebTabItem_selected_true").index();     // change the marker for active data set
            scope.settings.data = scope.data[scope.active_field];                               // change the active data set
            scope.refresh();                                                                    // Finally update the chart
        },

        onAttach: function () {
            // set the tab at the tab area
            this.tabs.attachTo(this.getElement().find(".eaVEApp-wFlowChart-tabField"));
            
            var update = _.debounce(                            // debounce prevents multible firings
                function () {
                    this.tabs.addTab("");                         // do whatever floats your boat
                    this.tabs.removeLastTab();                         // do whatever floats your boat
                }.bind(this), 300);                             // ms of silence required before firing the function
            $(this.element.element).closest(".eaVisualizationEngineApp-rFlowChartArea").resize(update);
        },

        addTab: function (number) {
            var scope = this;
            this.tabs.addTab(this.data[number][0].type, " ");

            d3.select(this.tabs.element.find(".ebTabs-tabArea").element)
                .on("click", function () {
                    scope.findActiveTab.call(this, scope);
                });
        },
        
        updateFlowData: function (event) {
            if (this.handledEvents.indexOf(event.eventId) === -1) {
                this.handledEvents.push(event.eventId);
                this.handleEvent(event);
            }
        },
		/*
		Method Sets values back to the default value
		*/
		setDefaultValues: function() {
			if(!this.firstLoad) {
				this.aspectRatio = this.defaultAspectRatio;
				this.redraw();
            }
        },
		/*
		Changes the aspect ratio to the event value 
		*/
		setAspectRatio: function(aspectRatio) {
			if(!this.firstLoad) {
				this.aspectRatio = (aspectRatio/10);
				this.redraw();
			}
   
        },
        
        handleEvent: function (event) {
            // This function converts the incoming event to the correct input format data and redraws the flow chart
            var count = 0;
            
            _.each(this.data, function (d) { count += d.length; });

            var parentDataLocation = [];

            var rating = event.eventType==="EiffelJobFinishedEvent"?event.eventData.resultCode:"SUCCESS";

            var item = {
                    id: count,
                    name: event.eventId,
                    type: event.eventType,
                    textmatrix: [["Location:", event.domainId], ["Status:", rating]],
                    rate: rating,
                    ref: [],
                    position: 0
                };
            this.dataByEventId[event.eventId] = item;

            if (count > 0) {
                //var self = this;
                _.each(event.inputEventIds, function(parentEventId) {
                    var smallCounter = 0;
                    var parent = this.dataByEventId[parentEventId];
                    if (parent) {
                        parent.ref = parent.ref.concat({
                            from:parent.id,
                            to:count
                        });

                        _.each(this.data, function(d) {
                            var nameList = _.pluck(d, 'name');
                            
                            if (nameList.indexOf(item.name) !== -1) { }  // if event already exists (duplicate)
                            else if (nameList.indexOf(parentEventId) !== -1) {  // if event does not exist
                                parentDataLocation.push(smallCounter);
                            }
                            smallCounter++;
                        });
                    }

                }.bind(this));

                parentDataLocation = _.uniq(parentDataLocation, false);
                if (parentDataLocation.length > 0) { // if we found at least one parent in the list we can add this new item to that data set
                    _.each(parentDataLocation, function (p) {
                        item.position = this.data[p].length;
                        this.data[p].push(item);
                        if (p === this.active_field) {
                            this.refresh();
                        }
                    }.bind(this));
                }
                else { // else we need to create a new data set for the new item
                    this.data.push([item]);
                    // update the field droplist to allow for selecting this new group here
                    this.addTab(this.data.length-1);
                }
            }
            else { //this is the first data, so we don't care about parents
                this.data[this.active_field].push(item);
                 // update the field droplist to allow for selecting this new group here
                this.addTab(0);
                this.tabs.selectedTabIndex = 0;
                this.tabs.setSelectedTab(0);
                this.refresh();
            }
        }
    });
});