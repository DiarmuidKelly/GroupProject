'use strict';
 
angular.module('VCA_WebApp.register', ['ngRoute'])
 
// Declared route 
.config(['$routeProvider', function($routeProvider) {
	$routeProvider
		.when("/start", {
			templateUrl : '/start/start.html'
		})
		.when("/landingPage", {
			templateUrl : '/landingPage/landingPage.html'
		});
}])
 
// Landing Page controller
.controller('RegisterCtrl', ["$scope", "$rootScope", "$log", "$location", 'UserService',
	function($scope, $rootScope, $log, $location, UserService) {
		
		$scope.$on('newUserRegistered', function(event, data) {
			$rootScope.$apply(function() {
				console.log("broadcast received " + data);
		        $location.path("/start"); 
		      });	
		 });

		$scope.register = function() {
			var email = $scope.email;
			var password = $scope.password;

			var firstName = $scope.firstName;
			var lastName = $scope.lastName;
			var age = $scope.age;
			var patientId = $scope.patientId;
			var patientPIN = $scope.patientPIN;

			UserService.registerNewUser(email,password, firstName, lastName, age, "caretaker");
		};
}]);