define([
	'jscore/ext/utils/base/underscore',
	// Comment out the line below and comment in the next next line to have a random event generator in the browser
    'app/ext/socket',
    // 'app/lib/randomEventGeneratorModule',
    //'app/lib/debugEventGeneratorModule',
	//'app/lib/debugTestEventGeneratorModule',
    'app/models/EventModel',
    'app/models/JiraModel',
    'app/models/ErrorModel'
], function (_, SocketExt, EventModel, JiraModel, ErrorModel) {

	var CommunicationHandler = function(config) {
		this.watchedSubscriptions = {};

		// Connect to the server
        this.socket = SocketExt;
        this.socket.connect(config.url, config.resource);
        this.eventCollection = config.collections.eventCollection;
        this.jiraCollection = config.collections.jiraCollection;
        this.errorCollection = config.collections.errorCollection;
        this.jiraRequests = {};
	};

	CommunicationHandler.prototype.listen = function() {
        this.socket.on('message', this._handleIncomingMessage.bind(this));
	};

	CommunicationHandler.prototype.subscribeToServer = function(region, topic) {
        if (!this._isWatched(topic)) {
            this.socket.emit('subscribe', {
                topic: topic
            });
        }
        this._addToWatchedSubscriptions(region, topic);
	};

    CommunicationHandler.prototype.unsubscribeToServer = function(region, topic) {
        if (topic !== undefined) {
            this._removeFromWatchedSubscriptions(region, topic);
            if (!this._isWatched(topic)) {
                this.socket.emit('unsubscribe', {
                    topic: topic
                });
            }
        } else {    // If no topic is given unsubscribe to all the regions topics
            _.map(this.watchedSubscriptions[region], function(topic) {
                this._removeFromWatchedSubscriptions(region, topic);
                if (!this._isWatched(topic)) {
                    this.socket.emit('unsubscribe', {
                        topic: topic
                    });
                }
            }, this);
        }
    };

    CommunicationHandler.prototype.retrieveJiraData = function(region, epicId) {
        var regions = this.jiraRequests[epicId];
        if (regions == null) {
            regions = [];
        }
        regions.push(region);
        this.jiraRequests[epicId] = regions;
        this.socket.emit('jiraEpic', {
            epicId: epicId
        });
    };

	CommunicationHandler.prototype._handleIncomingMessage = function(data) {
        switch (data.sender) {
            case "router":
                // Add or merge the received event data to the collection.
                var model = new EventModel(this._setDestination.call(this, data));
                this.eventCollection.addModel(model, {
                    merge: true
                });
                this._setDestination(data);
                break;
            case "jira":
                // FIXME
                _.each(this.jiraRequests[data.serverData.epicId], function(region) {
                    data.message = _.extend({destination: region}, data.message);
                    if (_.has(data.message, "error")) {
                        this.errorCollection.addModel(new ErrorModel(data));
                    } else {
                        this.jiraCollection.addModel(new JiraModel(data));
                    }
                }, this);
                delete this.jiraRequests[data.serverData.epicId];
                break;
            default:
                console.log('Unhandled socket message:', data);
        }
	};

	CommunicationHandler.prototype._addToWatchedSubscriptions = function(region, topic) {
		if (region !== undefined) {
			if (this.watchedSubscriptions[region] === undefined) {
				this.watchedSubscriptions[region] = [];
			}
			if (!_.contains(this.watchedSubscriptions[region], topic)) {
				this.watchedSubscriptions[region].push(topic);
			}
		}
	};

    CommunicationHandler.prototype._removeFromWatchedSubscriptions = function(region, topic) {
        if (region !== undefined) {
            if (_.has(this.watchedSubscriptions, region)) {
                this.watchedSubscriptions[region] = _.without(this.watchedSubscriptions[region], topic);
            }
        }
    };

    CommunicationHandler.prototype._isWatched = function(topic) {
        return _.any(this.watchedSubscriptions, function(region) {
             return _.contains(region, topic);
        });
    };

    CommunicationHandler.prototype._setDestination = function(data) {
        var topic = data.serverData.topic,
            eiffel = data.message;

        eiffel.destination = [];

        _.map(_.keys(this.watchedSubscriptions), function (region) {
            if (_.contains(this.watchedSubscriptions[region], topic)) {
                eiffel.destination.push(region);
            }
        }, this);

        return eiffel;
    };

	return CommunicationHandler;
});
