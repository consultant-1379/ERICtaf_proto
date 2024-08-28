angular
    .module('app.core')
    .config(function(toastrConfig) {
        angular.extend(toastrConfig, {
            timeOut: 1500,
            preventOpenDuplicates: true
        });
    })
    .config(function($httpProvider) {
        $httpProvider.interceptors.push([
            '$injector',
            function($injector) {
                return $injector.get('AuthInterceptor');
            }
        ]);
    });
