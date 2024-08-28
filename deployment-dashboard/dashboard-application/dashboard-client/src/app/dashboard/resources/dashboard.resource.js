angular
    .module('app.dashboard')
    .factory('Dashboard', Dashboard);

function Dashboard($resource, API_ROOT) {
    return $resource(API_ROOT + '/greeting', {}, {
        get: {method: 'get', timeout: 10000}
    });
}
