'use strict';
 
angular.module('VCA_WebApp.messageBoard', ['ngRoute'])
 
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
 
// Message Board controller
.controller('MessageBoardCtrl', ['$scope','UserService', '$location', function($scope, UserService, $location) {
	if(UserService.getAuthenticated()==true){
		// here goes Action on message Board
	}
}]);