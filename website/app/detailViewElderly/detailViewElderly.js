'use strict';
 
angular.module('VCA_WebApp.detailViewElderly', ['ngRoute'])
 
// Declared route 
.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/detailViewElderly', {
        templateUrl: 'detailViewElderly/detailViewElderly.html',
        controller: 'DVECtrl'
    });
}])
 
// Home controller
.controller('DVECtrl', function($scope) {	
	
});