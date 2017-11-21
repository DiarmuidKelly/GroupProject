//  I used this guide to set up Data-Model

'use strict';

angular.module('VCA_WebApp')

.factory('PatientsService', ['UserService','$q', '$firebaseObject',function(UserService, $q, $firebaseObject) {
// Fields: user_id, vca_home, first_name, last_name, role

	var firebaseRefString = 'application_data/users/';
	var firebaseDB = firebase.firestore();

    var _userId = UserService.getUserId();
   	var _groupsData;
   	var _patients = [];

   	var factory = this;

	factory = {
		getPatientFromFB : function(userId){
            var defer = $q.defer();

            firebaseDB.collection("users").doc(userId).get().then(function(doc) {
                if (doc.exists) {
                    _patients.push(doc.data());
                    defer.resolve(doc.data());
                } else {
                    console.log("No such document!");
                }
            });

        return defer.promise;
        },
        getPatientById : function(userId){
        	var result = _patients.filter(function( obj ) {
			  return obj.userId == userId;
			});
			return result;
        },
        getPatientGeoLocation : function(userId){
            var defer = $q.defer();

            // get the most recent location from database
        	firebaseDB.collection("users").doc(userId).collection("geoPosition").orderBy("lastUpdateTime").limit(1)
        				.get().then(function(querySnapshot) {
                				querySnapshot.forEach(function(doc) {
                					console.log("geolocation data: ", doc.data());
                					defer.resolve(doc.data());
                		});                
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
