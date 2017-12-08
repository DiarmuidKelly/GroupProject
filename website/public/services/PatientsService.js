//  I used this guide to set up Data-Model

'use strict';

angular.module('VCA_WebApp')

.factory('PatientsService', ['UserService','$q', '$firebaseObject', '$rootScope',
    function(UserService, $q, $firebaseObject, $rootScope) {

	var firebaseRefString = 'application_data/users/';
	var firebaseDB = firebase.firestore();

    var _userId = UserService.getUserId();
   	var _groupsData;
   	var _patients = [];
    var _allPatients = [];

   	var factory = this;

	factory = {
        getAllPatientsFromDatabaseByName : function(lastName){
            var defer = $q.defer();
            var result = [];

            firebaseDB.collection("users").where("role", "==", "patient")
                .get()
                .then(function(querySnapshot) {
                    // loop through all query results
                    querySnapshot.forEach(function(doc) {
                        if(doc.data().lName.toLowerCase().includes(lastName.toLowerCase())){
                            result.push(doc.data());
                        } 
                     });
                    defer.resolve(result);
                 })
                .catch(function(error) {
                        console.log("Error getting documents: ", error);
                }); 
        return defer.promise;
        },
        getAllPatientsFromDatabase : function() {
            var defer = $q.defer();
            var result = [];

            firebaseDB.collection("users").where("role", "==", "patient")
                .get()
                .then(function(querySnapshot) {
                    // loop through all query results
                    querySnapshot.forEach(function(doc) {
                        console.log("patient found: ", doc.data());
                        result.push(doc.data()); 
                        _allPatients.push(doc.data());
                     });
                    defer.resolve(result);
                 })
                .catch(function(error) {
                        console.log("Error getting documents: ", error);
                }); 

            return defer.promise;
        },
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
            // if employee --> use _allPatients
            var role = UserService.getUserRole();
            if(role=="employee"){
                var result = _allPatients.filter(function( obj ) {
                    return obj.userId == userId;
                });
                return result[0];
            } else {
                var result = _patients.filter(function( obj ) {
                    return obj.userId == userId;
                });
                return result[0];
            }
        	
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
        }, 
        getCaretakers : function(patientId) {
            var defer = $q.defer();
            var caretakersID = [];
            
            // get all caretakers from the database associated with a patient
            firebaseDB.collection("users").doc(patientId).collection("caretakers")
                .get().then(function(querySnapshot) {
                    querySnapshot.forEach(function(doc) {
                        caretakersID.push(doc.id);
                    });

                    // defer promise to UI
                    defer.resolve(caretakersID);                    
            });

            return defer.promise;
        },
        getEvents : function(patientId){
            firebaseDB.collection("users").doc(patientId).collection("calendar")
                .onSnapshot(function(querySnapshot) {
                    // loop through all query results
                    querySnapshot.forEach(function(doc) {
                        $rootScope.$broadcast('newEvent', doc.data());
                });
            });    
        },
        changeLightsStatus : function(patientId, status){
            firebaseDB.collection("users").doc(patientId).set({
                lights: status
            }, {merge :true})
            .then(function() {
                console.log("Lights successfully written!");
            })
            .catch(function(error) {
                console.error("Error writing document: ", error);
            });
        },
        changeHeatingStatus : function(patientId, status){
            firebaseDB.collection("users").doc(patientId).set({
                heating: status
            }, {merge :true})
            .then(function() {
                console.log("Heating successfully written!");
            })
            .catch(function(error) {
                console.error("Error writing document: ", error);
            });
        },
        setGeofence : function(patientId, radius){
            firebaseDB.collection("users").doc(patientId).set({
                geofenceRadius: Number(radius)
            }, {merge :true})
            .then(function() {
                console.log("Radius successfully written!");
            })
            .catch(function(error) {
                console.error("Error writing document: ", error);
            });
        },
        addAppointment : function(patientId, appointmentName, appointmentTime, appointmentLocation){
            firebaseDB.collection("users").doc(patientId).collection("calendar").add({
                eventLocation : appointmentLocation,
                eventName : appointmentName,
                eventTime : new Date(appointmentTime)
            })
            .then(function(docRef) {

                firebaseDB.collection("users").doc(patientId).collection("calendar").doc(docRef.id).set({
                    eventId: docRef.id
                }, {merge :true})
                .then(function() {
                    console.log("Event successfully written!");
                })
                .catch(function(error) {
                    console.error("Error writing document: ", error);
                });
            })
            .catch(function(error) {
                console.error("Error writing document: ", error);
            });
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
