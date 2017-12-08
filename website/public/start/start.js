
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

	$scope.formData = {};
	$scope.patients = [];

	// handle broadcast events
	$scope.$on('userLoggedOut', function(event, data) {
		$rootScope.$apply(function() {
			console.log("broadcast received start.js" + data);
			$location.path("/landingPage"); 
		    $route.reload();
		});	
	});

	$scope.addPatient = function(){
		// link caretaker to patient
		var patientPIN = $scope.formData.patientPIN;
		console.log("patient pin: " + patientPIN);
		UserService.linkToNewPatient(patientPIN).then(function(data){
			console.log("result from linking, patient: ", data );

			PatientsService.getPatientFromFB(data.patientNo).then(function(patientData){
				$scope.patients.push(patientData);
				$scope.formData.patientPIN = " ";
			});
	
		}); 
	};

	$scope.authenticated = UserService.getAuthenticated();
	var userId = UserService.getUserId();
	if($scope.authenticated==true){
		
		$scope.role = UserService.getUserRole();

		// ROLE EMPLOYEE 
		// ----------------------------------------------------------------------
		if($scope.role=="employee"){
			PatientsService.getAllPatientsFromDatabase().then(function(data){
				$scope.patients = [];
				console.log("all patients ", data);

				// add patients to map
				// creating the map 
				var mapOptions = {
			        zoom: 12,
			        center: new google.maps.LatLng(51.8959843,-8.5330895),
			        mapTypeId: google.maps.MapTypeId.TERRAIN
			    }

			    $scope.map = new google.maps.Map(document.getElementById('map'), mapOptions);

			    $scope.markers = [];
			    
			    var infoWindow = new google.maps.InfoWindow();
			    
			    var createMarker = function(patientData, info){
			        console.log("createMarker patientData: ", patientData);
			        var marker = new google.maps.Marker({
			            map: $scope.map,
			            position: new google.maps.LatLng(info.location.latitude, info.location.longitude),
			            title: patientData.fName + " " + patientData.lName
			        });
			        marker.content = '<div class="infoWindowContent">' + "Last known position" + '</div>';
			        
			        google.maps.event.addListener(marker, 'click', function(){
			            infoWindow.setContent('<h3>' + marker.title + '</h3>' + marker.content);
			            infoWindow.open($scope.map, marker);
			        });
			        
			        $scope.markers.push(marker);
			        
			    }  
			    			    
			    // check if patients are outside of geofence
				var i;
				for(i=0;i<data.length;i++){
					if(data[i].geoFence.insideGeofence==false){
						var patientData = data[i];
						$scope.patients.push(patientData);
						
						// get current geofence
						PatientsService.getPatientGeoLocation(patientData.userId).then(function(geoData){
							console.log("scope patientdata", patientData);
							createMarker(patientData,geoData);
						});
					}
				}
			    
			    $scope.openInfoWindow = function(e, selectedMarker){
			        e.preventDefault();
			        google.maps.event.trigger(selectedMarker, 'click');
		    	}
			});

		// ----------------------------------------------------------------------
		// ROLE CARETAKER
		// ----------------------------------------------------------------------
		} else {
			// load patients from memory
			UserService.loadPatients().then(function(data){
				var patientsArr = UserService.getPatients();
				console.log("Patients: ", patientsArr);

				var i;
				for(i=0;i<patientsArr.length;i++){
					console.log("Patient id: ): ", patientsArr[i]);
					PatientsService.getPatientFromFB(patientsArr[i]).then(function(data){
						
						$scope.patients.push(data);
					});
				}

				console.log("Patient data: ): ", $scope.patients);
			});	
		}
		

	} else {
		$location.path("/landingPage");
	}

}]);