'use strict';
 
angular.module('VCA_WebApp.start', ['ngRoute'])
 
// Declared route 
.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/start', {
        templateUrl: 'start/start.html',
        controller: 'StartCtrl'
    }).when('/landingPage', {
    	templateUrl: '/landingPage'
    });
}])
 
// Start controller
.controller('StartCtrl', ['$scope', 'UserService', 'PatientsService', function($scope, UserService, PatientsService) {
	//$scope.elderly = Elderly.build({ elderlyName = "Martha", elderlyCVAId = "123"}).getElderlyName();
	$scope.authenticated = UserService.getAuthenticated();
	if($scope.authenticated==true){
		// do something while signed in
		PatientsService.loadPatients().then(function(data){
			$scope.patientsData = data;
			var first_name = $scope.patientsData[0].patientData.first_name;
			var last_name = $scope.patientsData[0].patientData.last_name;
			$scope.elderly_name = first_name + " " + last_name;
		});
	} else {
		$location.path("/landingPage");
	}
}]);