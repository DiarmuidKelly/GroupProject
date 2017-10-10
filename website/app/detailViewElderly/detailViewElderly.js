'use strict';
 
angular.module('myApp.detailViewElderly', ['ngRoute'])
 
// Declared route 
.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/detailViewElderly', {
        templateUrl: 'detailViewElderly/detailViewElderly.html',
        controller: 'DVECtrl'
    });
}])
 
// Home controller
.controller('DVECtrl', function($scope) {	
	$scope.listElderly = [
		{
			"1": {
				"first_name": "Mandy",
				"last_name": "Reinhardt",
				"role": "elderly",
				"home": {
					"address": "96 Liberty Street",
					"city": "Cork",
					"county": "Cork",
					"long": 123,
					"lat": 456,
					"temperature": 20,
					"lights": true,
					"heating": false
				}
			}
	}];
});