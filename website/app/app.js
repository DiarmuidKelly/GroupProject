'use strict';

angular.module('myApp', [
	'ngRoute',
	'myApp.login',
	'myApp.start',
	'myApp.detailViewElderly',
	'myApp.header'
	]).config(['$routeProvider', function($routeProvider) {
		$routeProvider
		.when("/login", {
			templateUrl : '/login/login.html'
		})
		.when("/detailViewElderly", {
			templateUrl : '/detailViewElderly/detailViewElderly.html'
		})
		.when("/", {
			templateUrl : '/start/start.html'
		})
	//	.otherwise({
	//		redirectTo: '/start'
	//	});
	}]);
