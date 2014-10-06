var appServices = angular.module('bakerapp.services',[]);

//BakerUser Resource
appServices.factory('BakerUser', function($resource) {
	return $resource("/baker/services/api/repo/users/:id", 
			{ id: '@id' }, {
	    update: {
	        method: 'PUT' // this method issues a PUT request
        	
	      }
	});
});


appServices.service('popupService',function($window){
    this.showPopup=function(message){
        return $window.confirm(message);
    }
});



//SubscribedMachine Resource
appServices.factory('SubscribedMachine', function($resource) {
	return $resource("/baker/services/api/repo/subscribedmachines/:id", 
			{ id: '@id' }, {
	    update: {
	        method: 'PUT' // this method issues a PUT request
        	
	      }
	});
});
