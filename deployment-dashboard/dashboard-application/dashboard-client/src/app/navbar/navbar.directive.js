angular
    .module('app.navbar')
    .directive('navbar', function() {
        return {
            templateUrl: 'app/navbar/navbar.html',
            scope: {},
            controller: NavigationBar,
            controllerAs: 'vm',
            replace: true
        };
    });

function NavigationBar($state) {
    var vm = this;
    vm.goToMain = goToMain;

    function goToMain() {
        $state.go('dashboard');
    }
}
