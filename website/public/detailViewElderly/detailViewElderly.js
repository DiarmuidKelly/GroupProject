'use strict';
 
angular.module('VCA_WebApp.detailViewElderly', ['ngRoute'])
 
// Declared route 
.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/detailViewElderly', {
        templateUrl: 'detailViewElderly/detailViewElderly.html',
        controller: 'DVECtrl'
    });
}])
 
// detail view elderly controller
.controller('DVECtrl',['$scope','$routeParams','PatientsService', 'UserService', 
	function($scope, $routeParams, PatientsService, UserService) {	
		var patientId = $routeParams.userId;
		$scope.caretakers = [];	
		$scope.events = [];
		$scope.formData = {};
		$scope.datetimepicker = $('#datetimepicker1').datetimepicker();

		PatientsService.getPatientFromFB(patientId).then(function(patientData){
			$scope.patientData = patientData;
			$scope.insideGeofence = $scope.patientData.geoFence.insideGeofence;
			$scope.lights = $scope.patientData.lights;
			$scope.heating = $scope.patientData.heating;
			$scope.formData.geofenceRadius = $scope.patientData.geoFence.radius;
		});
		
		// function to search through events-array
		var checkIfEventExists = function (event){
			var result = $scope.events.filter(function( obj ) {
                    return obj.eventId == event;
                });
            return result[0];
		}

		// handle lights and heating switch
		$scope.$watch('[lights]', function(){
		    PatientsService.changeLightsStatus(patientId,$scope.lights);
		}, true );

		$scope.$watch('[heating]', function(){
		    PatientsService.changeHeatingStatus(patientId,$scope.heating);
		}, true );

		// handle calendar  events 
		$scope.$on('newEvent', function(event, data) {
				if(!checkIfEventExists(data.eventId)){
					$scope.events.push(data);
				}
				console.log("current events in scope", $scope.events);
		});

		// get geolocation
		PatientsService.getPatientGeoLocation(patientId).then(function(data){
			console.log("geolocation data: detailViewElderly: ", data);
			$scope.geodata = data;

			// creating the map 
			var mapOptions = {
		        zoom: 15,
		        center: new google.maps.LatLng(data.location.latitude, data.location.longitude),
		        mapTypeId: google.maps.MapTypeId.TERRAIN
		    }

		    $scope.map = new google.maps.Map(document.getElementById('map'), mapOptions);

		    $scope.markers = [];
		    
		    var infoWindow = new google.maps.InfoWindow();
		    
		    var createMarker = function (info){
		        
		        var marker = new google.maps.Marker({
		            map: $scope.map,
		            position: new google.maps.LatLng(info.location.latitude, info.location.longitude),
		            title: "Patient position"
		        });
		        marker.content = '<div class="infoWindowContent">' + "Last known position" + '</div>';
		        
		        google.maps.event.addListener(marker, 'click', function(){
		            infoWindow.setContent('<h3>' + marker.title + '</h3>' + marker.content);
		            infoWindow.open($scope.map, marker);
		        });
		        
		        $scope.markers.push(marker);
		        
		    }  
		    
		    // create marker for map
		    createMarker($scope.geodata);
		    
		    $scope.openInfoWindow = function(e, selectedMarker){
		        e.preventDefault();
		        google.maps.event.trigger(selectedMarker, 'click');
	    	}

		});

		//get associated caretaker names
		PatientsService.getCaretakers(patientId).then(function(data){
			var caretakersID = data;
		
			//get user data for each caretaker
            var i;
            for(i=0;i<caretakersID.length;i++){
            	UserService.getUserDataFromDB(caretakersID[i]).then(function(data){
            		$scope.caretakers.push(data);
            	});
            }
		});	

		// Geofence setting
		$scope.setGeofence = function(){
			PatientsService.setGeofence(patientId, $scope.formData.geofenceRadius);
		};

		// get patient events
		PatientsService.getEvents(patientId);

		//Add new event for patient
		$scope.addAppointment = function(){
			console.log("formdata", $scope.formData);
			PatientsService.addAppointment(patientId, $scope.formData.appointmentName,
				$scope.datetimepicker.data('DateTimePicker').date().format(), $scope.formData.appointmentLocation);
		};

}]);