window.addEventListener('load', function() {
    /* global _, d3 */

    var randomInRange = function(from, to) {
        return Math.floor(Math.random() * (to - from + 1)) + from;
    };
    var randomWord = function(fromCode, toCode, fromSize, toSize) {
        var letters = [];
        var size = randomInRange(fromSize, toSize);
        _.times(size, function() {
            letters.push(randomInRange(fromCode, toCode));
        });
        return String.fromCharCode.apply(null, letters).trim();
    };
    var randomNodes = function(from, to, depth) {
        if (depth < 1) {
            return [];
        }
        var amount = randomInRange(from, to);
        var nodes = [];
        _.times(amount, function() {
            nodes.push({
                name: randomWord(97, 122, 5, 15),
                color: '#' + randomWord(97, 102, 6, 6),
                children: randomNodes(from, to, depth - 1)
            });
        });
        return nodes;
    };
    var root = {
        name: '*',
        children: randomNodes(1, 3, 3),
        x0: 0,
        y0: 0
    };


    var width = 500;
    var height = 500;
    var barHeight = 20;
    var barWidth = width * 0.5;


    var vis = d3.select('#tree-chart').append('svg:svg')
        .attr('width', width)
        .attr('height', height)
        .append('svg:g')
        .attr('transform', 'translate(20,30)');

    var tree = d3.layout.tree()
        .size([height, 100]);

    var diagonal = d3.svg.diagonal()
        .projection(function(d) {
            return [d.y, d.x];
        });


    var counter = 0;
    var update = function(data) {

        // Compute the flattened node list.
        // TODO use d3.layout.hierarchy
        var nodes = tree.nodes(root);

        // Compute the layout.
        nodes.forEach(function(n, i) {
            n.x = i * barHeight;
        });

        // Update the node
        var node = vis.selectAll('g.node')
            .data(nodes, function(d) {
                return d.id || (d.id = ++counter);
            });

        var nodeEnter = node.enter().append('svg:g')
            .attr('class', 'node')
            .attr('transform', function() {
                return 'translate(' + data.y0 + ',' + data.x0 + ')';
            })
            .style('opacity', 1e-6);

        // Enter any new nodes at the parent's previous position.
        nodeEnter.append('svg:rect')
            .attr('y', -barHeight / 2)
            .attr('height', barHeight)
            .attr('width', barWidth)
            .attr('ry', 3)
            .attr('rx', 3)
            .style('fill', function(d) {
                return d.color;
            })
            .on('click', click);

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
        var link = vis.selectAll('path.link')
            .data(tree.links(nodes), function(d) {
                return d.target.id;
            });

        // Enter any new links at the parent's previous position.
        link.enter().insert('svg:path', 'g')
            .attr('class', 'link')
            .attr('d', function() {
                var o = {x: data.x0, y: data.y0};
                return diagonal({source: o, target: o});
            })
            .transition()
            .attr('d', diagonal);

        // Transition links to their new position.
        link.transition()
            .attr('d', diagonal);

        // Transition exiting nodes to the parent's new position.
        link.exit().transition()
            .attr('d', function() {
                var o = {x: data.x, y: data.y};
                return diagonal({source: o, target: o});
            })
            .remove();

        // Stash the old positions for transition.
        nodes.forEach(function(d) {
            d.x0 = d.x;
            d.y0 = d.y;
        });
    };

    // Toggle children on click.
    var click = function(d) {
        if (d.children) {
            d._children = d.children;
            d.children = null;
        } else {
            d.children = d._children;
            d._children = null;
        }
        update(d);
    };

    update(root);

});
