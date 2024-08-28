define([
    'jscore/core',
    './TreeChartView',
    'thirdparty/d3',
    'jscore/ext/utils/base/underscore'
], function(core, View, d3, _) {
    'use strict';

    return core.Widget.extend({
        View: View,

        init: function() {
            this.idCounter = 0;

            this.barHeight = 20;
            this.barWidth = 250;
            this.paddingX = 20;
            this.paddingY = 30;

            this.root = {
                name: 'Tests',
                children: [],
                x0: 0,
                y0: 0
            };
        },

        onAttach: function() {
            var svgArea = this.getElement().find('.eaVEApp-wTreeChart-svgArea');
            this.svg = d3.select(svgArea.element).append('svg:svg');
            this.vis = this.svg.append('svg:g')
                .attr('transform', 'translate(' + this.paddingX + ',' + this.paddingY + ')');

            this.tree = d3.layout.tree()
                .size([this.height, 100]);

            this.diagonal = d3.svg.diagonal()
                .projection(function(d) {
                    return [d.y, d.x];
                });

            this.redraw();
        },

        /**
         Method to draw the tree chart and the connecting links
         @param data  - this is the array of menus and sub menus required to be drawn.
         */
        update: function(data) {
            // Compute the flattened node list.
            // TODO use d3.layout.hierarchy
            var nodes = this.tree.nodes(this.root);

            // Compute the layout.
            nodes.forEach(function(n, i) {
                n.x = i * (this.barHeight + 1);
            }.bind(this));

            // Update the node
            var width = 0;
            var height = 0;
            var node = this.vis.selectAll('g.node')
                .data(nodes, function(d) {
                    width = Math.max(width, d.y + this.barWidth);
                    height = Math.max(height, d.x + this.barHeight);
                    return d.id || (d.id = ++this.idCounter);
                }.bind(this));
            this.svg
                .attr('width', width + this.paddingX * 2)
                .attr('height', height + this.paddingY * 2);

            var nodeEnter = node.enter().append('svg:g')
                .attr('class', 'node')
                .attr('data-result', function(d) {
                    return d.result;
                })
                .attr('transform', function() {
                    return 'translate(' + data.y0 + ',' + data.x0 + ')';
                })
                .style('opacity', 1e-6);

            node.attr('data-result', function(d) {
                return d.result;
            });

            // Enter any new nodes at the parent's previous position.
            nodeEnter.append('svg:rect')
                .attr('y', -this.barHeight / 2)
                .attr('height', this.barHeight)
                .attr('width', this.barWidth)
                .attr('ry', 3)
                .attr('rx', 3)
                .attr('data-type', function(d) {
                    return d.type;
                })
                .attr('data-parent', function(d) {
                    if (d.children != null && d.children.length > 0) {
                        return true;
                    }
                })
                .on('click', function(d) {
                    if (d.children) {
                        d._children = d.children;
                        d.children = null;
                    } else {
                        d.children = d._children;
                        d._children = null;
                    }
                    this.update(d);
                }.bind(this));

            nodeEnter.append('svg:text')
                .attr('dy', 3.5)
                .attr('dx', 5.5)
                .text(function(d) {
                    return d.name;
                });

            // Transition nodes to their new position.
            nodeEnter.transition()
                .attr('transform', function(d) {
                    return 'translate(' + d.y + ',' + d.x + ')';
                })
                .style('opacity', 1);

            node.transition()
                .attr('transform', function(d) {
                    return 'translate(' + d.y + ',' + d.x + ')';
                })
                .style('opacity', 1);

            // Transition exiting nodes to the parent's new position.
            node.exit().transition()
                .attr('transform', function() {
                    return 'translate(' + data.y + ',' + data.x + ')';
                })
                .style('opacity', 1e-6)
                .remove();

            // Update the links
            var link = this.vis.selectAll('path.link')
                .data(this.tree.links(nodes), function(d) {
                    return d.target.id;
                });

            // Enter any new links at the parent's previous position.
            link.enter().insert('svg:path', 'g')
                .attr('class', 'link')
                .attr('d', function() {
                    var o = {x: data.x0, y: data.y0};
                    return this.diagonal({source: o, target: o});
                }.bind(this))
                .transition()
                .attr('d', this.diagonal);

            // Transition links to their new position.
            link.transition()
                .attr('d', this.diagonal);

            // Transition exiting nodes to the parent's new position.
            link.exit().transition()
                .attr('d', function() {
                    var o = {x: data.x, y: data.y};
                    return this.diagonal({source: o, target: o});
                }.bind(this))
                .remove();

            // Stash the old positions for transition.
            nodes.forEach(function(d) {
                d.x0 = d.x;
                d.y0 = d.y;
            });
        },

        redraw: function() {
            this.update(this.root);
        },

        getData: function() {
            return { id: _.uniqueId('tree-chart-') };
        },

        updateTreeData: function(event) {
            this.handleEvent(event);
        },

        /**
         Method to handle events and group them together to be shown on the tree chart
         @param event - incoming event that was not processed before
         */
        handleEvent: function(event) {
            var suiteNode = this.updateChild(this.root, this.createTreeNode('suite', event.suite, null));
            var testNode = this.updateChild(suiteNode, this.createTreeNode('test', event.test, null));
            this.updateChild(testNode, this.createTreeNode('execution', event.execution, event.result));
            this.update(this.root);
        },

        updateChild: function(parent, newChild) {
            var foundChild = _.find(parent.children, function(child) {
                return child.name === newChild.name;
            });
            if (foundChild) {
                foundChild.result = newChild.result;
                return foundChild;
            } else {
                if (parent._children != null) {
                    parent._children.push(newChild);
                } else {
                    parent.children.push(newChild);
                }
                return newChild;
            }
        },

        /**
         Create a new sub node
         @return treeNode
         */
        createTreeNode: function(type, name, result) {
            var treeNode = {
                type: type,
                name: name,
                result: result,
                children: []
            };
            return treeNode;
        }

    });
});
