define([
    'thirdparty/d3'
], function (d3) {

    return {

        /* drawNodes
         * input:
         *      base - d3 object where node should be added
         *      nodeList - array list of objects that should be drawn should contain
         *          id          - unique id number, used for id of node
         *          title       - creates title
         *          class        - a class that
         *          textmatrix  - array of text to be displayed
         *      settings - json docs containing settings
         *          fontSize
         *          lineSpace
         *          boxHeight
         *          boxWidth
         */

        drawNodes: function (base, nodeList, settings) {
            var self = this;
            
            var fontSize = settings.fontSize ? settings.fontSize : 10;
            var lineSpace = settings.lineSpace ? settings.lineSpace : 1;
            var boxHeight = settings.boxHeight ? settings.boxHeight : 60;
            var boxWidth = settings.boxWidth ? settings.boxWidth : 180;
        
            // if first run
            if (base.selectAll(".node").empty() === true) {
                base.append("clipPath")
                    .attr("id", "clipRect")
                    .append("rect")
                        .attr("width", boxWidth).attr("height", boxHeight)
                        .attr("x", -boxWidth/2).attr("y", -boxHeight/2)
                        .attr("rx", 6).attr("ry", 6);
            }
            ///show each node with text
            var existingNodes = base.selectAll(".node").data(nodeList, function(d) { return d.id;});

            // select all the new items
            var newNodes = existingNodes.enter().append("g")
            .attr("clip-path", "url(#clipRect)");

            //add title for hover
            newNodes.append("svg:title");

            //draw rectangle for all new items
            newNodes
                .attr("class", "node")
                .attr("id", function (d) {return "node-" + d.id;})
                .append("rect")
                .attr("class", function (d) {return "nodebox " + d.class;})
                .attr("x", -boxWidth/2)                                         // move x and y-values
                .attr("y", -boxHeight/2)
                .attr("rx", 6)                                                  // round the corners
                .attr("ry", 6)
                .attr("width", boxWidth)                                        // set the width
                .attr("height", boxHeight);                                     // and height

            //node title constructor
            newNodes.append("text")
                .attr("class", "nodeTitle")
                .attr("x", -boxWidth/2)
                .attr("y", -boxHeight/2 + fontSize + 2*lineSpace);
        
            // allow dragging on the new objects
            newNodes.call(d3.behavior.drag().origin(Object).on("drag", function (d) {
                var coord = {x: d.y, y: d.x};
                d3.select(this)
                    .attr("transform", "translate(" + (coord.x + d3.event.dx)+ ", " + (coord.y + d3.event.dy) + ")");

                //update node's coord , then redraw affected edges
                d.x = d.x + d3.event.dy;
                d.y = d.y + d3.event.dx;

                //redraw arrow paths if they exist
                if (typeof drawPath === "function") {
                    self.drawPaths(base, self.links, true);
                }
            }));
            
            // add body text
            newNodes.append("g")
                .attr("class", "textmatrix")
                .selectAll("g").data(function (d) {return d.textmatrix?d.textmatrix:[];})
                .enter().append("g")
                .attr("class", "rows")
                .attr("transform", function (d, i) { return "translate(" + (-25) + ", " + (-boxHeight/2 + (i + 3)*fontSize+(i + 4)*lineSpace) + ")"; })
                .selectAll("g").data(function (d) {return d;})
                .enter().append("g")
                .attr("class", "columns")
                .attr("transform", function (d, i) { return "translate(" + i*50 + ", 0)"; })
                .append("text")
                .attr("text-anchor", "middle")
                .attr("class", "nodeText");
        
            newNodes.select(".textmatrix")
                .selectAll(".rows")
                .data(function (d) {return d.textmatrix?d.textmatrix:[];})
                .selectAll(".columns") //rows
                .data(function (d) {return d;})
                .select("text")
                .text(function (d) {return d;});
        
            ///show text
            newNodes.select(".nodeTitle").text(function (d) {
                return d.title;
            });
            newNodes.select("title").text(function (d) {
                return d.title;
            }).attr("id", function (d) {
                return d.title;
            });
            

        
            // set all existing nodes' position
            existingNodes
                .transition()
                .duration(500)
                .attr("transform", function (d) {
                    return "translate(" + d.y + ", " + d.x + ")"; });
        
            existingNodes.exit().remove();
            
            return existingNodes;
        }
    };
});
