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
		
		var pin = $scope.patientPIN;

		$scope.$on('newUserRegistered', function(event, data) {
			$rootScope.$apply(function() {
				console.log("broadcast received " + data);
				// run this code as soon as user was successfully registered
				// link caretaker to patient
				var patientPIN = UserService.getPatientPIN();
				console.log("patient pin: " + patientPIN);
				UserService.linkToNewPatient(patientPIN).then(function(data){
					console.log("result from linking: " + data );
			 		$location.path("/start"); 
				}); 
		      });	
		 });

		$scope.register = function() {
		
			var email = $scope.email;
			var password = $scope.password;
			var password2 = $scope.password2;
			var firstName = $scope.firstName;
			var lastName = $scope.lastName;
			var age = $scope.age;
			var patientPIN = $scope.patientPIN;

			var registerApproved = false;

			// check if passwords are identical
			if(password!=password2){
				console.log("register not approved " + registerApproved);
				alert('Passwords must be identical!');
			} else {
				registerApproved = true;
				console.log("register approved " + registerApproved);
			}

			if(registerApproved){
				UserService.registerNewUser(email,password, firstName, lastName, age, "caretaker", patientPIN);
			}
		};
}]);