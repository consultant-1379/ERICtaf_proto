define([
    'jscore/core',
    './TreeMapView',
    'thirdparty/d3',
    'jscore/ext/utils/base/underscore',
    'jscore/base/jquery',
    'widgets/Button'
], function(core, View, d3, _, $, Button) {
    return core.Widget.extend({
        View: View,

        init: function() {
            $(window).resize(function() {
                this.resize();
            }.bind(this));

            this.reset();

            // Result colors
            this.borderGreen = '#a1c845';
            this.border = this._percentGradient('#e94d47', '#fbc933');
            this.backgroundGreen = '#d0e3a2';
            this.background = this._percentGradient('#f4a6a3', '#fde499');
        },

        reset: function() {
            this.root = null;
            this.results = {};
        },

        _percentGradient: function(from, to) {
            return d3.scale.linear()
                .range([from, to])
                .domain([0, 100]);
        },

        onAttach: function() {
            this.svgArea = this.getElement().find(".eaVEApp-wTreeMap-svgArea").element;
        },

        resize: _.debounce(function() {
            if (this.data) {
                this.buildTreeMap(this.data, this.getElement().element);
            }
        }, 100),

        handleEvents: function(event) {
            var results = this.results[event.test];
            if (results == null) {
                results = {};
            }
            results[event.id] = event.result;
            this.results[event.test] = results;
            this.redraw();
        },

        redraw: function() {
            if (this.root) {
                this.update(this.results);
            }
        },

        buildTreeMap: function(data, element) {
            var $el = $(element).empty();
            var width = $el.width();
            var height = $el.width() / 2;

            var treemap = d3.layout.treemap()
                .size([width, height])
                .sticky(true)
                .value(function(d) {
                    return d.storyPoints;
                });

            var div = d3.select(element).append('div')
                .style('position', 'relative')
                .style('width', width + 'px')
                .style('height', height + 'px');

            this.data = data;
            this.root = div.datum(data).selectAll('.node')
                .data(treemap.nodes);

            var nodeEnter = this.root.enter().append('div')
                .attr('class', function(d) {
                    return 'eaVEApp-wTreeMap-TreeMapArea-' + (d.children ? 'node' : 'leaf');
                })
                .style('left', function(d) {
                    return (d.x + 1) + 'px';
                })
                .style('top', function(d) {
                    return (d.y + 1) + 'px';
                })
                .style('width', function(d) {
                    return Math.max(0, d.dx - 2) + 'px';
                })
                .style('height', function(d) {
                    return Math.max(0, d.dy - 2) + 'px';
                });

            var leavesEnter = nodeEnter.filter(function(d) {
                return d.children == null;
            }).append('div')
                .attr('class', 'eaVEApp-wTreeMap-TreeMapArea-label');

            var keyEnter = leavesEnter.append('a')
                .attr("href", function(d) {
                    return 'http://jira-oss.lmera.ericsson.se/browse/' + d.key;
                })
                .attr('class', function() {
                    return 'eaVEApp-wTreeMap-TreeMapArea-label-title';
                })
                .text(function(d) {
                    return d.key;
                });

            keyEnter.filter(function(d) {
                return (d.dy < 25) || (d.dx * d.dy < 3000);
            }).style('font-size', '14px');

            var summaryEnter = leavesEnter.filter(function(d) {
                return (d.dy > 100) && (d.dx * d.dy > 16000);
            }).append('div')
                .attr('class', function() {
                    return 'eaVEApp-wTreeMap-TreeMapArea-label-summary';
                });
            summaryEnter.append('span')
                .text(function(d) {
                    return d.summary;
                });
            summaryEnter.append('span')
                .style('font-weight', 'bold')
                .text(function(d) {
                    return ' (' + d.storyPoints + ' SP)';
                });

            this.redraw();
        },

        update: function(results) {
            var self = this;
            this.root.filter(function(d) {
                return d.children == null;
            })
                .each(function(d) {
                    var ratio = _.countBy(results[d.key], function(result) {
                        return result;
                    });
                    var success = ratio.success != null ? ratio.success : 0;
                    var total = success + (ratio.failure != null ? ratio.failure : 0);
                    var percent = (total > 0) ? Math.round(success / total * 100) : null;
                    d3.select(this)
                        .style('border-color', function() {
                            if (percent != null) {
                                return percent < 100 ? self.border(percent) : self.borderGreen;
                            }
                        })
                        .style('background-color', function() {
                            if (percent != null) {
                                return percent < 100 ? self.background(percent) : self.backgroundGreen;
                            }
                        });
                });
        },

        getData: function() {
            return { id: _.uniqueId('tree-map-') };
        }

    });
});
