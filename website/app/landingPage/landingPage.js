'use strict';
 
angular.module('VCA_WebApp.landingPage', ['ngRoute'])
 
// Declared route 
.config(['$routeProvider', function($routeProvider) {
	$routeProvider
		.when("/detailViewElderly", {
			templateUrl : '/detailViewElderly/detailViewElderly.html'
		})
		.when("/start", {
			templateUrl : '/start/start.html'
		});
}])
 
// Landing Page controller
.controller('LandingPageCtrl', ["$scope", "$log", "$location", 'UserService',
	function($scope, $log, $location, UserService) {
		$scope.authenticated = UserService.getAuthenticated();
		if($scope.authenticated){
			console.log(UserService.getAuthenticated());
			$location.path("/start");
		} else {
			$scope.authenticate = function() {
				if(UserService.authenticateUser($scope.email,$scope.password)==true){
					// weiterleitung auf start-seite
					$location.path("/start");
				}
			};
		};
}]);