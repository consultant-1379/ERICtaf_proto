angular
    .module('app.footer')
    .directive('footer', function() {
        return {
            templateUrl: 'app/footer/footer.html',
            scope: {},
            controller: Footer,
            controllerAs: 'vm',
            replace: true
        };
    });

function Footer() {
    //var vm = this;
}
