//  I used this guide to set up Data-Model

'use strict';

angular.module('VCA_WebApp')

.factory('PatientsService', ['UserService','$q', '$firebaseObject',function(UserService, $q, $firebaseObject) {
// Fields: user_id, vca_home, first_name, last_name, role

	var firebaseRefString = 'application_data/groups/';
    var _userId = UserService.getUserId();
   	var _groupsData;
   	var _patients = [];

   	var factory = this;

	factory = {
		loadPatients : function(){
            var defer = $q.defer();
            var ref = firebase.database().ref(firebaseRefString).orderByKey();
            _groupsData = $firebaseObject(ref);

            _groupsData.$loaded().then(function(){
                console.log("groupsData loaded");

                // retrieves list of patients for all groups
                var listOfGroups = [];
			    angular.forEach(_groupsData, function(value,key){
			       	listOfGroups.push({ groupId: key, patientsList: value.patients });

			       	angular.forEach(value.patients, function(value,key){
			    		_patients.push({patientId: key , patientData: value });
			    	});
			    });
			    
			    console.log(_patients);
                defer.resolve(_patients);
            });

            return defer.promise;
        }
	}

	return factory;

	// Constructor with a class name
	/*
	function Elderly(elderlyName, elderlyVCAId) {
		this.elderlyName = elderlyName;
		this.elderlyVCAId = elderlyVCAId;
	}

	// Public method assigned to prototype
	Elderly.prototype.getElderlyName = function {
		return this.elderlyName;
	};

	// Static method assigned to class
	// Instance ("this") is not available in static context
	Elderly.build = function (data) {
		return new Elderly(data.elderlyName,elderlyVCAId);
	};

	return Elderly;
	*/ 
}]); 
