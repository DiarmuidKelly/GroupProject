// https://github.com/angular/angular-seed <-- Sceleton of project taken from this source

'use strict';

angular.module('VCA_WebApp', [
	'ngRoute',
	'VCA_WebApp.start',
	'VCA_WebApp.detailViewElderly',
	'VCA_WebApp.messageBoard',
	'VCA_WebApp.header', 
	'VCA_WebApp.landingPage',
	'VCA_WebApp.register',
	'VCA_WebApp.searchResults',
	'firebase'
	])
.config(['$routeProvider', function($routeProvider) {
		$routeProvider
		.when("/", {
			templateUrl : 'landingPage/landingPage.html'
		})
	//	.otherwise({
	//		redirectTo: '/start'
	//	});
}]);
