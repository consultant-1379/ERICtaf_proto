angular
    .module('app.dashboard')
    .controller('dashboardController', dashboardController);

function dashboardController($scope, $http, sse, usSpinnerService, $q) {

    $scope.version = '';
    $scope.nodeServices = null;
    $scope.hostServices = null;
    $scope.uid = "test-123";
    $scope.statusOfClusterDeployments = 'NONE';
    $scope.spinnerName = 'dashboard-spinner';
    var showSpinner = false;
    var countMessages = 0;

    sse.addEventListener('message', function(e) {
        var result = JSON.parse(e.data);
        console.log(countMessages++);

        if (result.type === 'version') {
            $scope.version = result.version;
        } else if (result.type === 'nodeServices') {
            $scope.nodeServices = result.services;
            var numberOfNodesOffline = 0;
            var groups = [];
            var error = false;
            var warning = false;

            result.services.forEach(function(entry) {
                if (groups.indexOf(entry.name) === -1) {
                    groups.push(entry.name);
                }
            });

            //split services up into groups
            var servicesByGroup = [];

            groups.forEach(function(group) {
                var serviceGroup = [];
                result.services.forEach(function(service) {
                    if (service.name === group) {
                        serviceGroup.push(service);
                    }
                });
                servicesByGroup.push(serviceGroup);
            });

            servicesByGroup.forEach(function(services) {
                services.forEach(function(service) {
                    if (service.status === 'OFFLINE') {
                        numberOfNodesOffline++;
                    }
                    if (numberOfNodesOffline === 1) {
                        warning = true;
                    } else if (numberOfNodesOffline > 1) {
                        error = true;
                    }
                });
                numberOfNodesOffline = 0;
            });

            if (error) {
                $scope.statusOfClusterDeployments = 'ERROR';
            } else if (warning) {
                $scope.statusOfClusterDeployments = 'WARNING';
            } else {
                $scope.statusOfClusterDeployments = 'ONLINE';
            }
        } else if (result.type === 'hostServices') {
            $scope.hostServices = result.services;
        }
        $scope.stopSpinner();
    });

    $scope.alerts = [{
        type: 'success',
        msg: 'Please enter a cluster Id to see live status of the ENM deployment'
    }];

    $scope.clusterId = null;

    $scope.submitClusterId = function() {
        $scope.startSpinner();
        var host = {};
        host.clusterId = $scope.clusterId;
        host.uid = $scope.uid;

        return $http.post('/api/register', host).then(
            function(result) {
                return result.data;
            },
            function(result) {
                return $q.reject(result.data);
            }
        );
    };

    $scope.stopClusterId = function() {
        var host = {};
        host.uid = $scope.uid;
        return $http.post('/api/unregister/' + $scope.uid);
    };

    $scope.startSpinner = function() {
        showSpinner = true;
        usSpinnerService.spin($scope.spinnerName);
    };

    $scope.stopSpinner = function() {
        showSpinner = false;
        usSpinnerService.stop($scope.spinnerName);
    };

    $scope.showSpinner = function() {
        return showSpinner;
    };
}
