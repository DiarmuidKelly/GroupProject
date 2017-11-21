'use strict';
 
angular.module('VCA_WebApp.detailViewElderly', ['ngRoute'])
 
// Declared route 
.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/detailViewElderly', {
        templateUrl: 'detailViewElderly/detailViewElderly.html',
        controller: 'DVECtrl'
    });
}])
 
// detail view elderly controller
.controller('DVECtrl',['$scope','$routeParams','PatientsService', 
	function($scope, $routeParams, PatientsService) {	
		var patientId = $routeParams.userId;
		$scope.patientData = PatientsService.getPatientById(patientId);
		$scope.insideGeofence = true;	

		// get geolocation
		PatientsService.getPatientGeoLocation(patientId).then(function(data){
			console.log("geolocation data: detailViewElderly: ", data);
			$scope.geodata = data;
			//$scope.map = { center: { latitude: $scope.geodata.location.latitude, longitude: $scope.geodata.location.longitude }, zoom: 8 };
		});

}]);