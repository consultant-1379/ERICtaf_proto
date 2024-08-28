/*global define, console, setInterval*/
define([
    'jscore/core',
    './ServersBlockView',
    './serversList/ServersList',
    './ServersCollection'
], function (core, View, ServersList, ServersCollection) {
    'use strict';

    return core.Widget.extend({
        /*jshint validthis:true*/

        View: View,

        onViewReady: function () {
            this.view.afterRender();

            this.serversList = new ServersList({servers: []});
            this.serversList.attachTo(this.view.getServersList());

            this.serversCollection = new ServersCollection();

            fetchCollection.call(this);
            setInterval(fetchCollection.bind(this), 15000);
        },

        setServers: function (serversCollection) {
            this.serversList.destroy();

            var servers = serversCollection.toJSON();
            this.serversList = new ServersList({
                servers: servers
            });
            this.serversList.attachTo(this.view.getServersList());
        }

    });

    function fetchCollection () {
        this.serversCollection.fetch({
            success: function () {
                this.setServers(this.serversCollection);
            }.bind(this)
        });
    }

});