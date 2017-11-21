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
	'$rootScope',
	'$route',
	'UserService', 
	'$location', 
	'$firebase', 
	'$firebaseObject',
	function($scope, $rootScope,$route, UserService, $location, $firebase, $firebaseObject) {
		$scope.userData;
		var patients;

		// show current time in header
		$scope.getDatetime = Date.now();

		// handle broadcast events
		$scope.$on('userLoggedOut', function(event, data) {
			$rootScope.$apply(function() {
				console.log("logout broadcast received headerCtrl" + data);
			    $route.reload();	
			});	
		});

		$scope.logout = function(){
			UserService.logoutUser();
		};

		if(UserService.getAuthenticated()==true){
			console.log("HeaderCtrl: User signed in");
			$scope.userId = UserService.getUserId();
			console.log("User Id of logged in user: " + $scope.userId);

			UserService.loadUserData($scope.userId).then(function(data){
				console.log("User role = " + data.role);
				$scope.role = data.role;			
			});
		}		
}]);