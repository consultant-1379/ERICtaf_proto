angular
    .module('app.dashboard')
    .service('DashboardService', DashboardService);

function DashboardService(Dashboard) {

    this.getTestData = getTestData;

    function getTestData() {
        return Dashboard.get().$promise;
    }
}
