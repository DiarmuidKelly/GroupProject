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
    var factory = this;

	factory = {
        authenticateUser : function(email, password){
            var defer = $q.defer();
            firebase.auth().signInWithEmailAndPassword(email, password).catch(function(error) {
                // Handle Errors here.
                // Reminder: mandy.reinhardt94@gmail.com test123
                var errorCode = error.code;
                var errorMessage = error.message;   
                var _userAuthenticated = false;
                return _userAuthenticated;   
            });
            _userAuthenticated = true;
            $rootScope.loggedIn = true;
            return _userAuthenticated;
        },
        getUserId : function(){
            _userId = firebase.auth().currentUser.uid
            return _userId;
        },
        loadUserData : function(userId){
            _userId = userId;
            var defer = $q.defer();

            firebaseDB.collection("users").doc(userId).get().then(function(doc) {
                if (doc.exists) {
                    console.log("Document data:", doc.data());
                    _userData = doc.data();
                    defer.resolve(_userData);
                } else {
                    console.log("No such document!");
                }
            });

            return defer.promise;

            //_userId = userId;
            //var defer = $q.defer();
            //var ref = firebase.database().ref(firebaseRefString + userId);
            //_userData = $firebaseObject(ref);

            //_userData.$loaded().then(function(){
            //    console.log("user data loaded");
            //    defer.resolve(_userData);
            //});

            //return defer.promise;
        },
        getUserData : function(){
            return _userData;
        },
        logout: function(){
            _userAuthenticated = false;
        },
        getAuthenticated: function(){      
            if(_userAuthenticated==true)
                return true;
            else    
                return false;
        },
        getTestValue: function(){
            return "test";
        } 
    }

    return factory;
}]);
