angular.module('VCA_WebApp')
.controller('VCA_WebAppCtrl', ["$scope", 'UserService', function($scope, UserService){
	$scope.authenticated = UserService.getAuthenticated();
	console.log('VCA_WebAppCtrl:' + $scope.authenticated);
}]);