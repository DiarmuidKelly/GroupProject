'use strict';

angular.module('VCA_WebApp.searchResults', ['ngRoute'])
 
// Declared route 
.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'templates/header.html',
        controller: 'HeaderCtrl'
    })
    .when("/landingPage", {
		templateUrl : 'landingPage/landingPage.html'
	})
	.when("/search/:lastName",{
		templateUrl : 'seachResults/searchResults.html'
	});
}])
 
// Header controller
.controller('SearchResultCtrl', ['$scope', 'UserService', '$routeParams','PatientsService',
	function($scope, UserService, $routeParams, PatientsService) {
		if(UserService.getAuthenticated()==true){
			// get all users searched for
			$scope.searchTerm = $routeParams.lastName;
			console.log("Search term:", $scope.searchTerm);

			if($scope.searchTerm==null){
				PatientsService.getAllPatientsFromDatabase().then(function(data){
					console.log("PatientSearch patients found:", data);
					$scope.patients = [];
					$scope.patients = data;
				});
			} else {
				PatientsService.getAllPatientsFromDatabaseByName($scope.searchTerm).then(function(data){
					console.log("PatientSearch patients found:", data);
					$scope.patients = [];
					$scope.patients = data;
				});
			}

			
		}
}]);
