// Userful tutorial on factories: http://viralpatel.net/blogs/angularjs-service-factory-tutorial/
//   var meals = $firebaseArray(ref.child('courses')); <-- To access child of object

angular.module('VCA_WebApp')
.factory('UserService', ['$firebaseObject', '$q','$rootScope', function($firebaseObject, $q, $rootScope){
	// Felder: userId, first_name, groups[], last_name, role

    var firebaseDB = firebase.firestore();
    var firebaseRefString = 'application_data/users/';

    var _userId;
    var _userData;
    var _userAuthenticated = false;
    var _newUser = false;
    var _firstName, _lastName, _age, _role, _patientPIN;
    var _patients;

    var factory = this;

    firebase.auth().onAuthStateChanged(function(user) {
        if (user) {
            // User is signed in.
            console.log("user signed in:", user);
            var email = user.email;
            var emailVerified = user.emailVerified;
            _userId = user.uid;
            _userAuthenticated = true;
            $rootScope.loggedIn = true;

            if(_newUser){
              factory.addUserToDatabase(_firstName, _lastName, _age, _role);
            }
            // get user informatin from database after login
            factory.loadUserData(_userId).then(function(data){
                console.log("user data fetched from db");
                _role = data.role;
                _firstName = data.fName;
                _lastName = data.lName;
            }); 
            $rootScope.$broadcast('userLoggedIn', true);
        } else {
            // User is signed out.
            _userAuthenticated = false;
            $rootScope.loggedIn = false;
            console.log("user signed out:");
            $rootScope.$broadcast('userLoggedOut', true);

        }
    });

	factory = {
        isRegistered : false,
        registerNewUser : function(email,password, firstName, lastName, age, role, patientPIN){

            _newUser = true;
            _firstName = firstName;
            _lastName = lastName;
            _age = age;
            _role = role;
            _patientPIN = patientPIN;

            firebase.auth().createUserWithEmailAndPassword(email, password).catch(function(error) {
              // Handle Errors here.
              var errorCode = error.code;
              var errorMessage = error.message;
            });
        },
        authenticateUser : function(email, password){
            firebase.auth().signInWithEmailAndPassword(email, password).catch(function(error) {
                // Handle Errors here.
                var errorCode = error.code;
                var errorMessage = error.message;    
            });
            return _userAuthenticated;
        },
        logoutUser : function() {
            // Sign out user
            firebase.auth().signOut().catch(function(error) {
               var errorCode = error.code;
               var errorMessage = error.message;   
             });
        },
        getUserId : function(){
            return _userId;
        },
        getUserRole : function(){
            return _role;
        },
        loadUserData : function(userId){
            _userId = userId;
            var defer = $q.defer();

            firebaseDB.collection("users").doc(userId).get().then(function(doc) {
                if (doc.exists) {
                    console.log("Document data:", doc.data());
                    _role = doc.data().role;
                    $rootScope.roleDetermined = true;
                    _userData = doc.data();
                    defer.resolve(_userData);
                } else {
                    console.log("No such document!");
                }
            });

            return defer.promise;
        },
        getUserDataFromDB : function(userId){
            var defer = $q.defer();

            firebaseDB.collection("users").doc(userId).get().then(function(doc) {
                if (doc.exists) {
                    defer.resolve(doc.data());
                } else {
                    console.log("No such document!");
                }
            });

            return defer.promise;
        },
        loadPatients : function(){
            var result = [];
            var defer = $q.defer();

        
                firebaseDB.collection("users").doc(_userId).collection("patients").get().then(function(querySnapshot) {
                    querySnapshot.forEach(function(doc) {
                          console.log(doc.id, " => ", doc.data());
                          result.push(doc.id);
                    });
                    _patients = result;
                    defer.resolve(_patients);
                })
                 .catch(function(error) {
                        console.log("Error getting documents: ", error);
                });
            

            return defer.promise;
        },
        addUserToDatabase : function(firstName, lastName, age, role){
            firebaseDB.collection("users").doc(_userId).set({
                fName: firstName,
                lName: lastName,
                role: role, 
                age: age,
                userId: _userId
            })
            .then(function() {
                console.log("Document successfully written!");
                $rootScope.$broadcast('newUserRegistered', _newUser);
            })
            .catch(function(error) {
                console.error("Error writing document: ", error);
            });
        },
        getUserData : function(){
            return _userData;
        },
        getAuthenticated: function(){      
            if(_userAuthenticated==true)
                return true;
            else    
                return false;
        },
        getPatients : function(){
            return _patients;
        }, 
        getPatientPIN : function(){
            return _patientPIN;
        },
        linkToNewPatient : function(patientPIN){
            var foundPatient;
            var defer = $q.defer();
            var successful = false;

            firebaseDB.collection("codes").where("uniqueIdentifier", "==", patientPIN)
                .get()
                .then(function(querySnapshot) {
                    // loop through all query results
                    querySnapshot.forEach(function(doc) {
                            foundPatient = doc.data();

                            //Add caretaker to patient 
                            firebaseDB.collection("users").doc(_userId).collection("patients").doc(foundPatient.patientNo).set({
                                
                            })
                            .then(function() {
                                console.log("Document successfully written! Patient added");
                                successful = true;
                            })
                            .catch(function(error) {
                                console.error("Error writing document: ", error);
                                successful = false;
                            });

                            //and add caretaker to patient
                            firebaseDB.collection("users").doc(foundPatient.patientNo).collection("caretakers").doc(_userId).set({
                                
                            })
                            .then(function() {
                                console.log("Document successfully written! Caretaker added");
                                defer.resolve(foundPatient);
                                successful = true;
                            })
                            .catch(function(error) {
                                console.error("Error writing document: ", error);
                                successful = false;
                            });
                        
                     });
                 })
                .catch(function(error) {
                        console.log("Error getting documents: ", error);
                }); 

        return defer.promise;
        }
    }

    return factory;
}]);
