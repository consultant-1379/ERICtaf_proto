module.exports = function (logger) {
	var getNextWorker = function(workers, currentWorker) {
		for (var id in workers) {
			if (id >= currentWorker)
				return workers[id];
		}
		return workers[id];
	};
	
	var workerOnline = function() {
		logger.info('workerUtils.js - Worker online');
	};
	
	return {
		getNextWorker: getNextWorker,
		workerOnline: workerOnline
	};
};