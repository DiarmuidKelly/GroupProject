'use strict';
 
angular.module('VCA_WebApp.header', ['ngRoute'])
 
// Declared route 
.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'templates/header.html',
        controller: 'HeaderCtrl'
    })
    .when("/landingPage", {
		templateUrl : 'landingPage/landingPage.html'
	});
}])
 
// Header controller
.controller('HeaderCtrl', [
	'$scope',
	'UserService', 
	'$location', 
	'$firebase', 
	'$firebaseObject',
	function($scope, UserService, $location, $firebase, $firebaseObject) {
		$scope.userData;
		if(UserService.getAuthenticated()==true){
			console.log("HeaderCtrl: User signed in");
			$scope.userId = UserService.getUserId();
			console.log("User Id of logged in user: " + $scope.userId);
			UserService.loadUserData($scope.userId).then(function(data){
				console.log("User role = " + data.role);
				UserService.getUserData().$bindTo($scope, 'userData').then(function() {
					$scope.first_name = $scope.userData.first_name;
					$scope.role = $scope.userData.role;
				});
				
			});
		}

		$scope.logout = function(){
			UserService.logout();
			$location.path("/landingPage");
		}
}]);