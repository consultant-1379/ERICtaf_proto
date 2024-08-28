angular
    .module('app.routing')
    .config(RoutingConfiguration);

function RoutingConfiguration($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('login', {
            url: '/login',
            templateUrl: 'app/login/login.html',
            controller: 'loginController',
            controllerAs: 'vm'
        });

    $urlRouterProvider.otherwise('/login');
}
