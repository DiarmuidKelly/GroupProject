'use strict';
 
angular.module('VCA_WebApp.header', ['ngRoute'])
 
// Declared route 
.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'templates/header.html',
        controller: 'HeaderCtrl'
    })
    .when("/start", {
		templateUrl : 'start/start.html'
	})
	.when("/searchResults/:lastName",{
		templateUrl : 'searchResults/searchResults.html'
	})
	.when("/messageBoard", {
		templateUrl : 'messageBoard/messageBoard.html'
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
		$scope.formData = {};
		
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

		$scope.searchName = function(){
			$location.path("/searchResults/" + $scope.formData.nameSearch);
			$scope.formData.nameSearch = "";
		};

		if(UserService.getAuthenticated()==true){
			$scope.userId = UserService.getUserId();
			$scope.role = UserService.getUserRole();
			$scope.firstName = UserService.getUserData().fName;
		}		
}]);