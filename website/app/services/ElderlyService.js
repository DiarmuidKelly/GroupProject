//  I used this guide to set up Data-Model

'use strict';

angular.module('VCA_WebApp')

.factory('ElderlyService', function() {
// Fields: user_id, vca_home, first_name, last_name, role

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
}); 
