define([
    'thirdparty/d3'
], function (d3) {
    // d3.tip
    // Copyright (c) 2013 Justin Palmer
    //
    // Tooltips for d3.js SVG visualizations

    // Public - contructs a new tooltip
    //
    // Returns a tip
    d3.tip = function() {
        var direction = d3_tip_direction,
            offset    = d3_tip_offset,
            html      = d3_tip_html,
            node      = initNode(),
            svg       = null,
            point     = null,
            enabled   = false;

        function tip() {
            svg = getSVGNode(this);
            point = svg.createSVGPoint();
            svg.parentElement.appendChild(node);
            d3.select(svg.parentElement).on('remove', function () {
                d3.select(svg).selectAll(".d3-tooltip-node").remove();
            });
        }

        // Public - show the tooltip on the screen
        //
        // Returns a tip
        tip.show = function() {
            var content = html.apply(this, arguments),
                poffset = offset.apply(this, arguments),
                dir     = direction.apply(this, arguments),
                nodel   = d3.select(node), i = 0,
                coords;

            nodel.html(content).style('display', 'block');
            while(i--) nodel.classed(directions[i], false);
            coords = direction_callbacks.get(dir).apply(this);
            nodel.classed(dir, true).style({
                top: (Math.max(coords.top, 0) +  poffset[0]) + 'px',
                left: (Math.max(coords.left, 0) + poffset[1]) + 'px'
            });
            enabled = true;
            return tip;
        };

        // Public - hide the tooltip
        //
        // Returns a tip
        tip.hide = function() {
            node.style.display = 'none';
            node.innerHTML = '';
            enabled = false;
            return tip;
        };

        tip.shown = function () {
            return enabled;
        };

        // Public: Proxy attr calls to the d3 tip container.  Sets or gets attribute value.
        //
        // n - name of the attribute
        // v - value of the attribute
        //
        // Returns tip or attribute value
        tip.attr = function(n, v) {
            if (arguments.length < 2 && typeof n === 'string') {
                  return d3.select(node).attr(n);
            } else {
                var args =  Array.prototype.slice.call(arguments);
                d3.selection.prototype.attr.apply(d3.select(node), args);
            }
            return tip;
        };

        // Public: Proxy style calls to the d3 tip container.  Sets or gets a style value.
        //
        // n - name of the property
        // v - value of the property
        //
        // Returns tip or style property value
        tip.style = function(n, v) {
            if (arguments.length < 2 && typeof n === 'string') {
                return d3.select(node).style(n);
            } else {
                var args =  Array.prototype.slice.call(arguments);
                d3.selection.prototype.style.apply(d3.select(node), args);
            }
            return tip;
        };

        // Public: Set or get the direction of the tooltip
        //
        // v - One of n(north), s(south), e(east), or w(west), nw(northwest),
        //     sw(southwest), ne(northeast) or se(southeast)
        //
        // Returns tip or direction
        tip.direction = function(v) {
            if (!arguments.length) return direction;
            direction = v === null ? v : d3.functor(v);
            return tip;
        };

        // Public: Sets or gets the offset of the tip
        //
        // v - Array of [x, y] offset
        //
        // Returns offset or
        tip.offset = function(v) {
            if (!arguments.length) return offset;
            offset = v === null ? v : d3.functor(v);
            return tip;
        };

        // Public: sets or gets the html value of the tooltip
        //
        // v - String value of the tip
        //
        // Returns html value or tip
        tip.html = function(v) {
            if (!arguments.length) return html;
            html = v === null ? v : d3.functor(v);
            return tip;
        };

        function d3_tip_direction() { return 'n'; }
        function d3_tip_offset() { return [0, 0]; }
        function d3_tip_html() { return ' '; }

        var direction_callbacks = d3.map({
            n:  direction_n,
            s:  direction_s,
            e:  direction_e,
            w:  direction_w,
            nw: direction_nw,
            ne: direction_ne,
            sw: direction_sw,
            se: direction_se
        }),

        directions = direction_callbacks.keys();

        function direction_n() {
            var bbox = getScreenBBox();
            return {
                top:  bbox.n.y - node.offsetHeight,
                left: bbox.n.x - node.offsetWidth / 2
            };
        }

        function direction_s() {
            var bbox = getScreenBBox();
            return {
                top: bbox.s.y,
                left: bbox.s.x - node.offsetWidth / 2
            };
        }

        function direction_e() {
            var bbox = getScreenBBox();
            return {
                  top:  bbox.e.y - node.offsetHeight / 2,
                  left: bbox.e.x
            };
        }

        function direction_w() {
            var bbox = getScreenBBox();
            return {
                  top:  bbox.w.y - node.offsetHeight / 2,
                  left: bbox.w.x - node.offsetWidth
            };
        }

        function direction_nw() {
            var bbox = getScreenBBox();
            return {
                  top:  bbox.nw.y - node.offsetHeight,
                  left: bbox.nw.x - node.offsetWidth
            };
        }

        function direction_ne() {
            var bbox = getScreenBBox();
            return {
                top:  bbox.ne.y - node.offsetHeight,
                left: bbox.ne.x
            };
        }

        function direction_sw() {
            var bbox = getScreenBBox();
            return {
                top:  bbox.sw.y,
                left: bbox.sw.x - node.offsetWidth
            };
        }

        function direction_se() {
            var bbox = getScreenBBox();
            return {
                top:  bbox.se.y,
                left: bbox.e.x
            };
        }

        function initNode() {
            var node = document.createElement('div');
            node.className += 'd3-tooltip-node ebBgColor_white ebColor_black';
            node.style.position = 'absolute';
            node.style.display = 'none';
            node.style.boxSizing = 'border-box';
            node.style.border = 'solid 1px #333';
            
            return node;
        }

        function getSVGNode(el) {
            el = el.node();
            if(el.tagName.toLowerCase() === 'svg')
                return el;

            return el.ownerSVGElement;
        }

        // Private - gets the screen coordinates of a shape
        //
        // Given a shape on the screen, will return an SVGPoint for the directions
        // n(north), s(south), e(east), w(west), ne(northeast), se(southeast), nw(northwest),
        // sw(southwest).
        //
        //    +-+-+
        //    |   |
        //    +   +
        //    |   |
        //    +-+-+
        //
        // Returns an Object {n, s, e, w, nw, sw, ne, se}
        function getScreenBBox() {
            var target     = d3.event.target,
                bbox       = {},
                matrix     = target.getCTM(),
                tbbox      = target.getBBox(),
                width      = tbbox.width,
                height     = tbbox.height,
                x          = tbbox.x,
                y          = tbbox.y;

            point.x = x;
            point.y = y;
            bbox.nw = point.matrixTransform(matrix);
            point.x += width;
            bbox.ne = point.matrixTransform(matrix);
            point.y += height;
            bbox.se = point.matrixTransform(matrix);
            point.x -= width;
            bbox.sw = point.matrixTransform(matrix);
            point.y -= height / 2;
            bbox.w  = point.matrixTransform(matrix);
            point.x += width;
            bbox.e = point.matrixTransform(matrix);
            point.x -= width / 2;
            point.y -= height / 2;
            bbox.n = point.matrixTransform(matrix);
            point.y += height;
            bbox.s = point.matrixTransform(matrix);

            return bbox;
        }
        return tip;
    };
});
