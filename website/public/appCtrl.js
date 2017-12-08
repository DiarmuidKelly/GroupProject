angular.module('VCA_WebApp')
.controller('VCA_WebAppCtrl', ["$scope", '$rootScope','$location','$route', 'UserService', 
	function($scope, $rootScope,$location,$route, UserService){
		$scope.authenticated = UserService.getAuthenticated();
		console.log('VCA_WebAppCtrl:' + $scope.authenticated);

		// handle broadcast events
		$scope.$on('userLoggedOut', function(event, data) {
			$rootScope.$apply(function() {
				console.log("broadcast received appCtrl" + data);
				$location.path("/"); 
			    $route.reload();
			});	
		});
}]);