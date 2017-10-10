'use strict';
 
angular.module('myApp.login', ['ngRoute'])
 
// Declared route 
.config(['$routeProvider', function($routeProvider) {
	$routeProvider
		.when("/login", {
			templateUrl : '/login/login.html'
		})
		.when("/detailViewElderly", {
			templateUrl : '/detailViewElderly/detailViewElderly.html'
		})
		.when("/", {
			templateUrl : '/start/start.html'
		});
}])
 
// Home controller
.controller('LoginCtrl', [function() {
 
}]);