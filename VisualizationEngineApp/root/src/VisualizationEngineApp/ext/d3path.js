define([
    'thirdparty/d3'
], function (d3) {

    return {
        
        /*
        * drawPaths input:
        *       base - d3 object where path should be added
        *       nodeList - array list of objects that should be linked
        *           target
        *           source
        *       instant - boolean to determine if the adding should be instant instead of transitioned
        */
        
        drawPaths: function(base, nodeList, settings) {
            var diagonal = d3.svg.diagonal().projection(function (d) { return [d.y, d.x]; });
            
            var stoke_width = settings.stoke_width ? settings.stoke_width : 3;
            var instant = settings.instant ? settings.instant : true;

            var path = base.selectAll(".link").data(nodeList);
            var new_path = path.enter();
            
            // when path changes
            if (instant === true) {
                path.attr("d", diagonal);
            }
            else {
                path.transition().duration(500).attr("d", diagonal);
            }
            
            // when new path arrives
            new_path
                .insert("path", "defs")
                .attr("id", function (d) {
                        return "link" + d.source.id+ "-" + d.target.id;
                })
                .attr("class", function (d) {
                        return "link" + " link-" + d.source.id + " link-" + d.target.id;
                })
                .style("stroke-width", stoke_width)
                .attr("d", diagonal);
        
            // when path's removed
            path.exit().remove();
            
            return path;
        }
    };
});
