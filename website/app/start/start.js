'use strict';
 
angular.module('myApp.start', ['ngRoute'])
 
// Declared route 
.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/start', {
        templateUrl: 'start_screen/start.html',
        controller: 'StartCtrl'
    });
}])
 
// Home controller
.controller('StartCtrl', [function($scope) {
	
}]);