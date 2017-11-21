'use strict';
 
angular.module('VCA_WebApp.landingPage', ['ngRoute'])
 
// Declared route 
.config(['$routeProvider', function($routeProvider) {
	$routeProvider
		.when("/register", {
			templateUrl : '/register/register.html'
		})
		.when("/start", {
			templateUrl : '/start/start.html'
		});
}])
 
// Landing Page controller
.controller('LandingPageCtrl', ["$scope", "$rootScope", "$log", "$location", 'UserService',
	function($scope, $rootScope, $log, $location, UserService) {
		$scope.authenticated = UserService.getAuthenticated();
		
		$scope.$on('userLoggedIn', function(event, data) {
			$rootScope.$apply(function() {
				console.log("login in broadcast received landingPage" + data);
			    $location.path("/start"); 
			});	
		});

		$scope.$on('userLoggedOut', function(event, data) {
			$rootScope.$apply(function() {
				console.log("logout broadcast received landingPage" + data);
			    $location.path("/landingPage"); 
			});	
		});

		if($scope.authenticated){
			console.log(UserService.getAuthenticated());
			$rootScope.$apply(function() {
			    $location.path("/start"); 
			});
		} else {
			$scope.authenticate = function() {
				UserService.authenticateUser($scope.email,$scope.password);
			};
		}
}]);