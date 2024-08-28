angular.module('app.core', [
    /*
     * Angular modules
     */
    'ngAnimate', 'ngCookies', 'ngTouch',
    'ngSanitize', 'ngResource', 'ngMessages',

    /*
     * 3PP
     */
    'ui.router',
    'ui.bootstrap',
    'rt.debounce',
    'toastr',
    /*
     * Reusable modules
     */
    'blocks.logger',
    'blocks.popups'
]);
