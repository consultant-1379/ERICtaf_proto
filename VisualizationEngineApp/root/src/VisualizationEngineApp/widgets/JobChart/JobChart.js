define([
    'jscore/core',
    './JobChartView',
    'thirdparty/d3',
    'app/ext/d3tooltip',
    'app/ext/d3node',
    'jscore/ext/utils/base/underscore',
	'app/widgets/Popup/Popup',
], function (core, View, d3, tip, node, _, Popup) {
    return core.Widget.extend({
        View: View,

        init: function () {
            this.allData = [];
            this.activeData = {};
            this.clusterBase = "eventData.jobInstance"; //.optionalParameters.eventSource";
            this.handledEvents = [];
			this.aspectRatio = 1;
            this.atStart = true;
	
			
            _.extend(this, tip);
            _.extend(this, node);
            
            this.firstRun = true;
            
            this.settings = {
                edge_show_arrow: true,
                node_draggable: true,
                data: this.activeData,
                cols: 2
				
            },
            this.nodeSettings = {
                boxHeight : 60,
                boxWidth : 180
            };
            this.eventCollection = this.options.eventCollection;
			this.defaultAspectRatio = this.aspectRatio;
        },
        
        changeColumns: function (n) {
            this.settings.cols = n;
            if (this.nodes) {
                this.arrangeNodes();
                this.redraw();
            }
        },
                
        setFadeout: function (timeInSeconds) {
            clearInterval(this.fadeInterval);
            if(timeInSeconds !== 0) {
                this.fadeInterval = setInterval(function() {
                    this.fadeout();
                }.bind(this), timeInSeconds/6 * 1000);
            }
        },
		
		/*
		Method Sets values back to the default value
		*/
		setDefaultValues: function() {
			if(!this.firstRun) {
				this.aspectRatio = this.defaultAspectRatio;
				this.redraw();
		   }
        },
		/*
		Changes the aspect ratio to the event value 
		*/
		setAspectRatio: function(aspectRatio) {
			if(!this.firstRun) {
				this.aspectRatio = (aspectRatio/10);
				this.redraw();
			}
   
        },
        
        // Function for stringification of Eiffel MB - data
        showTooltip: function (name) {
            var event = this.eventCollection._collection.get(name).attributes;
            
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

            return message;
			
        },
		/**
		Method to display the node history if available
		*/
		historicalToolTip: function () {
				var nodeHistory = [];
				var self = this;
	
				this.el = this.getElement().find('.eaVEApp-wJobChart-svgArea');
				
				var node = this.svg_g.selectAll('.node');	
				
				node.on("click", function(d) { 				
						
						if (self.popup !== undefined) {
						self.popup.destroy();
					    }
					
						self.popup = new Popup({content: self.getNodeHistory(self.getItemsJobName(d3.select(this).attr("id").replace(/[^0-9.]/g,""))), position: {x: d3.event.pageX, y: d3.event.pageY + 40}});
							self.popup.attachTo(self.el);
							self.popup.addEventHandler('close', function() {
								self.popup.destroy();
							});	
												
						});
						
					
		},

        /// final step , bind some data
        arrangeNodes: function () {
            this.nodes = [];
            
            var position = 0;
            _.each(this.activeData, function (d) {
                d.y = (10 + this.nodeSettings.boxWidth) * (position % this.settings.cols);
                d.x = (10 + this.nodeSettings.boxHeight) * Math.floor(position / this.settings.cols);
                this.nodes.push(d);
                position++;
            }.bind(this));
        },
        
        clear: function () {
            if (this.firstRun === true) {
                this.trigger('onLoad');
                this.svgArea = d3.select(this._parent.element).select(".eaVEApp-wJobChart-svgArea");
                this.svgArea.append("svg").append("g").append("svg:defs");
                this.svg = this.svgArea.select("svg");
                this.svg_g = this.svg.select("g");
                this.firstRun = false;
                this.setFadeout(0);//48*60*60); // standard interval 48 hours
            }
            else {
                this.svgArea.selectAll(".d3-tooltip-node").remove();
            }

            var maxWidth = _.max(this.nodes, function (d) { return d.y; }).y + this.nodeSettings.boxWidth;
            var maxHeight = _.max(this.nodes, function (d) { return d.x; }).x + this.nodeSettings.boxHeight;
            maxWidth = isNaN(maxWidth) ? 0 : maxWidth;
            maxHeight = isNaN(maxHeight) ? 0 : maxHeight;
            
            var height = this.element.element.clientWidth / maxWidth * maxHeight;
            height = isNaN(height) ? 0 : height;
            
            this.svg
                .attr("class", "eaVEApp-wJobChart-svg")
                .attr("width", 100*this.aspectRatio+"%")
                .attr("height", height)
                .attr("viewBox", (-this.nodeSettings.boxWidth/2 - 10) + " " + -this.nodeSettings.boxHeight/2 + " " + (maxWidth + 20) + " " + maxHeight );
            
            this.svg
                .select("g");
        },
                
        fadeout: function () {
            var self = this;
            var d3Nodes = this.svg_g.selectAll('.node');
            d3Nodes.each(function () {
                var r = d3.select(this).select("rect");
                if (r.style("opacity") <= 0.2) {
                    //this.remove();  // remove the node
                    delete self.activeData[this.firstChild.id];
                    self.arrangeNodes();
                    self.redraw();
                }
                else if (r.style("opacity").length >= 1) {
                    r.attr("opacity", Math.round(10*r.style("opacity")-2)/10);
                } else {
                    r.style("opacity", 1);
                }
            });
        },

        // call redraw() if necessary (reconfig, recostom the actions, etc.)
        redraw: function () {
            
			if(this.nodes){
			this.clear();
            var d3Nodes = this.drawNodes(this.svg_g, this.nodes, this.nodeSettings);
			this.historicalToolTip();
			}
						

        },
        
        setEventFinder: function(eventFinder) {
            this.eventFinder = eventFinder;
        },

        getData: function () {
            return { id: _.uniqueId('job-chart-') };
        },
        
        setClusterBase: function(clusterBase) {
            this.clusterBase = clusterBase;
        },
        
        clearActiveData: function() {
            this.activeData = {};
        },
        
        createItem: function(event) {
            var date = (new Date()).toLocaleTimeString();
			
			var eventSource;
			if(typeof event.eventData.optionalParameters === 'undefined') { 
				eventSource = 'undetected';		
				}
			else { 
			eventSource = event.eventData.optionalParameters.eventSource
			}
			
            var item = {
                id: this.allData.length,
                name: event.eventId,
				time: event.eventTime,
				resultCode: event.eventData.resultCode,
                title: event.eventType,
				source: eventSource,
				jobInstance: event.eventData.jobInstance,
                textmatrix: [["Job ID:", event.eventData.jobExecutionId], ["Updated:", date]],
                class: event.eventData.resultCode?event.eventData.resultCode:"SUCCESS"
            };
            return item;
        },
        
        update: function (event) {
            var redraw = this.handleNewEvent(event);
            if (redraw === true) {
                this.arrangeNodes();
                this.redraw();
            }
        },
        
        getItemIfAlreadyExists: function(eventId) {
            for(var i = 0; i < this.allData.length; i++) {
                if(this.allData[i].name == eventId) {
                    return this.allData[i];
                }
            }
        },
		/**
		Method to return all history as a list object in html by searching for the eventSource or JobInstance
		@param indentifier
		@return converted list
		*/
		getNodeHistory : function(identifier) {
		var self = this;
		    var nodeHistory = new Array();
			
			for(var i = 0; i < this.allData.length; i++) {
                if(this.allData[i].source === identifier || this.allData[i].jobInstance === identifier) {
                    nodeHistory.push(this.allData[i]);
                }
            }			
			

			var convertedList = '<ol><dl>';		
			for(var key in nodeHistory.reverse()) {
			  var date = new Date(nodeHistory[key].time);
			  var title = '<dt><b>'+nodeHistory[key].title+'</b> </dt>';
			  var result_code ='<dd><b>Status: <div id=\"eaVisualization-jobChartStatus-ToolTip\" class=\"'+nodeHistory[key].resultCode+'\">'+nodeHistory[key].resultCode +'</div></b></dd>';
			  var time = '<dd><b>Time:</b> ['+date.toString().substring(0,24)+'] </dd>';
			  var JSONText = '<dd><div class="eaVisualization-jobChartEvent-JSON">'+this.showTooltip(nodeHistory[key].name)+'</div></dd>';
			  
			  convertedList += '<li>'+ title+ time + result_code + JSONText+'</li><br>' 
			  		   
			}
			convertedList += '</dl></ol>'
						
			return convertedList
						
		},
		/**
		Method to return the jobInstance or eventSource depending on whether the event has either one or the other 
		@return jobInstance or eventSource
		*/
		getItemsJobName: function(itemIndex) { 
			if(this.allData[itemIndex].source == "undetected") {
				return this.allData[itemIndex].jobInstance;

			}
			else {
				return this.allData[itemIndex].source; 
			}					
        },	
        
        getJobInstance: function(event) {
            var inputEventIds = event.inputEventIds;
            var parentEvent = this.eventCollection.getModel(inputEventIds[0])._model.attributes;
            return parentEvent.eventData.jobInstance;
        },
        
        sortObject: function (o) {
            var sorted = {},
                key, a = [];

            for (key in o) {
            	if (o.hasOwnProperty(key)) {
            		a.push(key);
            	}
            }
        
            a.sort();
        
            for (key = 0; key < a.length; key++) {
            	sorted[a[key]] = o[a[key]];
            }
            return sorted;
        },
        
        handleNewEvent: function(event) {
        	
            if(event.eventType == "EiffelConfidenceLevelModifiedEvent") {
                event.eventData.jobInstance = this.getJobInstance(event);
        	}
        	
            var eventId = event.eventId;
            var item = this.getItemIfAlreadyExists(eventId);
            if(item == undefined) {
                item = this.createItem(event);
                this.allData.push(item);
            }
        
            var cBArray = this.clusterBase.split(".");
            var e = event;
            _.each(cBArray, function (d) {
                try {
                    item.position = _.keys(e).indexOf(d);
                    e = e[d];
                }
                catch (err) { return; }
            }.bind(this));
            
            //override old active event if applicable
            if (typeof e === "string") {
                if (!this.activeData[e]) {  // if the post is new
                    this.activeData[e] = []; //insert token
                    this.activeData = this.sortObject(this.activeData); //resort the array
                }    
                item.title = e;
                this.activeData[e] = item;
            }
            else {
                return false;
            }
            return true;
        },
        
        selectFromEventCollection: function() {
        	var self = this;
        	this.eventCollection.each(function(event) {
        		var data = event._model.attributes;
        		self.update(data);
        	});
        }
    });
});