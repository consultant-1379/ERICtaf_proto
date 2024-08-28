angular
    .module('app.dashboard')
    .directive('rdWidget', rdWidget);

function rdWidget() {
    var directive = {
        transclude: true,
        template: '<div class="widget" ' +
        'ng-style="statusOfClusterDeployments===\'ERROR\' && ' +
        '{\'background-color\': \'crimson\'}||statusOfClusterDeployments===\'WARNING\' && ' +
        '{\'background-color\': \'orange\'}||statusOfClusterDeployments===\'ONLINE\' && ' +
        '{\'background-color\': \'green\'}" ng-transclude></div>',
        restrict: 'EA'
    };
    return directive;
}
