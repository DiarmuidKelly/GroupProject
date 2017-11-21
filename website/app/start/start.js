
'use strict';
 
angular.module('VCA_WebApp.start', ['ngRoute'])
 
// Declared route 
.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/start', {
        templateUrl: 'start/start.html',
        controller: 'StartCtrl'
    }).when('/landingPage', {
    	templateUrl: 'landingPage/landingPage.html',
    	controller: 'DVECtrl'
    }).when('/detailViewElderly/:userId',{
    	templateUrl: 'detailViewElderly/detailViewElderly.html',
    	controller: 'DVECtrl'
    });
}])
 
// Start controller
.controller('StartCtrl', ['$scope', '$location', '$route' ,'UserService', 'PatientsService', 
	function($scope, $location,$route, UserService, PatientsService) {

	// handle broadcast events
	$scope.$on('userLoggedOut', function(event, data) {
		$rootScope.$apply(function() {
			console.log("broadcast received start.js" + data);
			$location.path("/landingPage"); 
		    $route.reload();
		});	
	});

	//$scope.elderly = Elderly.build({ elderlyName = "Martha", elderlyCVAId = "123"}).getElderlyName();
	$scope.authenticated = UserService.getAuthenticated();
	var userId = UserService.getUserId();
	if($scope.authenticated==true){
		
		console.log("start.js ",$scope.role);

		// fetch all the patients
		UserService.loadPatients().then(function(data){
			var patientsArr = UserService.getPatients();
			console.log("Patients: ", patientsArr);

			// add user role to scope
			$scope.role = UserService.getUserRole();

			var i;
			for(i=0;i<patientsArr.length;i++){
				console.log("Patient id: ): ", patientsArr[i]);
				PatientsService.getPatientFromFB(patientsArr[i]).then(function(data){
					console.log("Patient data: ): ", data);
					$scope.patients = [];
					$scope.patients.push(data);
				});
			}
		});	

	} else {
		$location.path("/landingPage");
	}

}]);