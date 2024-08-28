angular
    .module('app.dashboard')
    .controller('dashboardController', dashboardController);

function dashboardController($scope, DashboardService, $http) {
    var vm = this;
    vm.dashboardRequest = dashboardRequest;
    vm.overviewRequest = overviewRequest;
    vm.listTafClasses = listTafClasses;
    vm.listTeamMethods = listTeamMethods;
    vm.listTeamClasses = listTeamClasses;
    vm.countMethodsRequest = countMethodsRequest;

    vm.toggle = toggle;

    vm.checked = false;
    vm.size = '100px';

    vm.dataAvailable = false;
    vm.stable = "The stable annotation indicates that the code will not be changed in a backwards-incompatible way in the current major version";
    vm.stableData = {};
    vm.stableParam = {params: {apiValue: "stable"}}
    vm.deprecated = "This code should not be used and will be removed in an upcoming minor release. Details of any changes needed will be communicated before the minor version release.";
    vm.deprecatedData = {};
    vm.deprecatedParam = {params: {apiValue: "deprecated"}}
    vm.internal = "This is code designed to be used from within TAF and must not be used by any code except TAF itself. This is not guaranteed to be backward-compatible and can be changed without any communication.";
    vm.internalData = {};
    vm.internalParam = {params: {apiValue: "internal"}}
    vm.experimental = "New API which became available recently and is in evaluation stage. Might be promoted to Stable or rolled-back without prior notice.";
    vm.experimentalData = {};
    vm.experimentalParam = {params: {apiValue: "experimental"}}

    function toggle() {
        vm.checked = !vm.checked;
    }

    testGetMethod();

    function testGetMethod() {
        DashboardService.getTestData();
    }

    function dashboardRequest() {
        vm.stableData = {};
        vm.deprecatedData = {};
        vm.internalData = {};
        vm.experimentalData = {};
        vm.dataAvailable = false;
    }

    function overviewRequest() {
        vm.stableData = {};
        vm.deprecatedData = {};
        vm.internalData = {};
        vm.experimentalData = {};
        vm.dataAvailable = true;
    }

    function listTafClasses() {
        $http.get('/api/listTafClasses?apiValue=Stable')
                        .then(function(response){
                            vm.stableData=response.data;
                        })
                $http.get('/api/listTafClasses?apiValue=Deprecated')
                        .then(function(response){
                            vm.deprecatedData=response.data;
                        })
                $http.get('/api/listTafClasses?apiValue=Internal')
                        .then(function(response){
                            vm.internalData=response.data;
                        })
                $http.get('/api/listTafClasses?apiValue=Experimental')
                        .then(function(response){
                            vm.experimentalData=response.data;
                        })
                vm.dataAvailable = true;
    }

    function listTeamMethods() {
        $http.get('/api/listTeamMethods?apiValue=Stable')
                        .then(function(response){
                            vm.stableData=response.data;
                        })
                $http.get('/api/listTeamMethods?apiValue=Deprecated')
                        .then(function(response){
                            vm.deprecatedData=response.data;
                        })
                $http.get('/api/listTeamMethods?apiValue=Internal')
                        .then(function(response){
                            vm.internalData=response.data;
                        })
                $http.get('/api/listTeamMethods?apiValue=Experimental')
                        .then(function(response){
                            vm.experimentalData=response.data;
                        })
                vm.dataAvailable = true;
    }

    function listTeamClasses() {
        $http.get('/api/listTeamClasses?apiValue=Stable')
                        .then(function(response){
                            vm.stableData=response.data;
                        })
                $http.get('/api/listTeamClasses?apiValue=Deprecated')
                        .then(function(response){
                            vm.deprecatedData=response.data;
                        })
                $http.get('/api/listTeamClasses?apiValue=Internal')
                        .then(function(response){
                            vm.internalData=response.data;
                        })
                $http.get('/api/listTeamClasses?apiValue=Experimental')
                        .then(function(response){
                            vm.experimentalData=response.data;
                        })
                vm.dataAvailable = true;
    }

    function countMethodsRequest() {
        $http.get('/api/countannotatedmethodsbyapi?apiValue=Stable')
                .then(function(response){
                    vm.stableData=response.data;
                })
        $http.get('/api/countannotatedmethodsbyapi?apiValue=Deprecated')
                .then(function(response){
                    vm.deprecatedData=response.data;
                })
        $http.get('/api/countannotatedmethodsbyapi?apiValue=Internal')
                .then(function(response){
                    vm.internalData=response.data;
                })
        $http.get('/api/countannotatedmethodsbyapi?apiValue=Experimental')
                .then(function(response){
                    vm.experimentalData=response.data;
                })
        vm.dataAvailable = true;
    }
}
