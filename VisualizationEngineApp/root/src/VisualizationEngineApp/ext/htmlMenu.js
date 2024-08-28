define([
    'jscore/base/jquery',
    'jqueryui/menu'
], function($) {
    
    return {

        createMenu: function(element) {
            var self = this;
            var el = element.element;
            $(el).menu({
                select: function(event, ui) {
        
                    var subMenus = ui.item.find("ul");
                    if(subMenus.length !== 0) {
                       
                    }
                    else {
                        var clusterBaseSelection = "";
                        var activeParentItems = $(".ui-state-active");
                        activeParentItems.each(function(index, item) {
                            clusterBaseSelection += item.text + ".";
                        });
                        try {
                            var clickedItem = $(".ui-state-focus")[0];
                            clusterBaseSelection += clickedItem.text;
                            self.changeClusterBase(clusterBaseSelection);
                        }
                        catch(err) {
                            //console.log("No item in focus");
                        }
                    }
                }
            });
        }
    };
});
