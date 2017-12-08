'use strict';
 
angular.module('VCA_WebApp.login', ['ngRoute'])
 
// Declared route 
.config(['$routeProvider', function($routeProvider) {
	$routeProvider
		.when("/detailViewElderly", {
			templateUrl : '/detailViewElderly/detailViewElderly.html'
		})
		.when("/", {
			templateUrl : '/detailViewElderly/detailViewElderly.html'
			//templateUrl : '/start/start.html'
		});
}])
 
// Login controller
.controller('LoginCtrl', ["$scope", "$log", 'UserService',
	function($scope, $log, UserService) {
		console.log("does this print to console work??");
		$scope.user_id = UserService.getTestValue();

		$scope.authenticate = function() {
			firebase.auth().signInWithEmailAndPassword('mandy.reinhardt94@gmail.com', 'test123').catch(function(error) {
			          // Handle Errors here.
			          var errorCode = error.code;
			          var errorMessage = error.message;      
	        });

	        var db = firebase.database();
	        var userId = firebase.auth().currentUser.uid;

			$scope.user_id = userId;
				
			UserService.loadUserData(userId).then(function(data){
				$scope.userData = data;
				$scope.name_field = data.first_name;
			});			
		}; 	
}]);