'use strict';
 
angular.module('VCA_WebApp.messageBoard', ['ngRoute'])
 
// Declared route 
.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/', {
        templateUrl: 'templates/header.html',
        controller: 'HeaderCtrl'
    })
    .when("/landingPage", {
		templateUrl : 'landingPage/landingPage.html'
	});
}])
 
// Message Board controller
.controller('MessageBoardCtrl', ['$scope','UserService','PatientsService', 'MessageService', '$location', 
	function($scope, UserService, PatientsService, MessageService, $location) {
		if(UserService.getAuthenticated()==true){

			$scope.formData = {};
			$scope.messages = [];
			$scope.chatStatus = "Click on a patient name to open a chat."

			// here goes Action on message Board
			// load list of all patients
			$scope.patients = [];
			var patientsArr = UserService.getPatients();

			var i;
			for(i=0;i<patientsArr.length;i++){
				PatientsService.getPatientFromFB(patientsArr[i]).then(function(data){
					$scope.patients.push(data);
				});
			}

			$scope.$on('newMessage', function(event, data) {
				console.log("message on UI received", data)
				$scope.messages.push(data);
			});

			var currentChat;
			$scope.openChat = function(messageBoard){
				 $scope.chatStatus = "Fetching messages..."
				 $scope.chatArea = document.getElementById('chatArea');
				 $scope.members = [];
				 $scope.messages = [];
						 
				 currentChat = messageBoard;

				 MessageService.getMembersOfChat(messageBoard).then(function(memberData){
				 	var memberIDs = memberData;
				 	console.log("member ids: ", memberIDs);

				 	var j;
					for(j=0;j<memberIDs.length;j++){
						console.log("member: ", memberIDs[j]);
						UserService.getUserDataFromDB(memberIDs[j]).then(function(userData){
							 $scope.members.push(userData);
						});
					}
				 });
				 
				 MessageService.getMessagesOfChat(messageBoard);

			};

			$scope.addMessage = function(){
				MessageService.sendMessage(currentChat, $scope.formData.newMsg);
				
				var date = new Date();
				var newChatObject = {
					fromUserName : UserService.getUserData().lName,
					sent : date, 
					msgText : $scope.formData.newMsg
				}

				$scope.messages.push(newChatObject);
				$scope.formData.newMsg = "";

			};
		}
}]);