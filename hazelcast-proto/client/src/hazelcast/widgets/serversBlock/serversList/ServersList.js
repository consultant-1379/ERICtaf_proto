/*global define*/
define([
    'jscore/core',
    './ServersListView'
], function (core, View) {
    'use strict';

    return core.Widget.extend({

        view: function () {
            return new View({
                template: {
                    servers: this.options.servers
                }
            });
        },

        onViewReady: function () {

        },

        setServers: function (servers) {
            this.options.servers = servers;
//            this.view = new View({
//                template: {
//                    servers: this.options.servers
//                }
//            });
            this.view.render();
        }

    });

});