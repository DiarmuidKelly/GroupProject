angular.module('VCA_WebApp')
.factory('MessageService', ['UserService','$q', '$rootScope',
	function(UserService, $q, $rootScope) {

		var firebaseRefString = 'application_data/users/';
		var firebaseDB = firebase.firestore();

    	var _userId = UserService.getUserId();

    	factory = {
        	getMembersOfChat : function(chatId){
        		var defer = $q.defer();
	            var result = [];

	            firebaseDB.collection("messageBoards").doc(chatId).collection("members")
	                .get()
	                .then(function(querySnapshot) {
	                    // loop through all query results
	                    querySnapshot.forEach(function(doc) {
	                        result.push(doc.id);
	                     });
	                    defer.resolve(result);
	                 })
	                .catch(function(error) {
	                        console.log("Error getting documents: ", error);
	                }); 

	        	return defer.promise;
        	},
        	getMessagesOfChat : function(chatId){

	            firebaseDB.collection("messageBoards").doc(chatId).collection("messages").orderBy("sent")
	                .onSnapshot(function(querySnapshot) {
	                    // loop through all query results
	                    querySnapshot.forEach(function(doc) {
							$rootScope.$broadcast('newMessage', doc.data());
	                     });
	                 });    
        	},
        	sendMessage : function(messageBoard, messageText){

        		var userName = UserService.getUserData().lName;
        		var date = new Date();

        		firebaseDB.collection("messageBoards").doc(messageBoard).collection("messages").add({
	                fromUser: _userId,
	                fromUserName : userName,
	                sent : date,
	                msgText : messageText
	            })
	            .then(function() {
	                console.log("Document successfully written!");
	            })
	            .catch(function(error) {
	                console.error("Error writing document: ", error);
	            });
        	}
    	}

        return factory;

}]);