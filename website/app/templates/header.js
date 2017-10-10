'use strict';
 
angular.module('myApp.header', ['ngRoute'])
 
// Declared route 
.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'start/start.html',
        controller: 'HeaderCtrl'
    });
}])
 
// Home controller
.controller('HeaderCtrl', [function($scope) {
	
}]);