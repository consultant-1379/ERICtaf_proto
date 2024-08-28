angular
    .module('app.routing')
    .config(RoutingConfiguration);

function RoutingConfiguration($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('dashboard', {
            url: '/dashboard',
            templateUrl: 'app/dashboard/dashboard.html',
            controller: 'dashboardController',
            controllerAs: 'vm'
        });

    $urlRouterProvider.otherwise('/dashboard');
}
