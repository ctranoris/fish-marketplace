var appControllers = angular.module('bakerapp.controllers',[]) 

appControllers.controller('UserListController', ['$scope','$window','$log', 'BakerUser', 'popupService', 'ngDialog',
                            	function($scope, $window, $log, BakerUser, popupService, ngDialog) {
	
	

	$scope.bakerusers = BakerUser.query(function() {
		    console.log($scope.bakerusers);
		  }); //query() returns all the bakerUsers
		 
	
	
	 $scope.deleteBakerUser = function(gridItem, useridx, username, name){

		 	console.log("Selected to DELETE User with userID = "+ useridx);
		 	

		 	var bakeruser=BakerUser.get({id:useridx}, function() {
			    $log.debug("WILL DELETE User with ID "+ bakeruser.id);
			    
		        if(popupService.showPopup('Really delete user '+name+' with username "'+username+'" ?')){
		        	console.log("WILL DELETE User with $scope.bakeruser.id = "+ bakeruser.id);
				 	
		        	bakeruser.$delete(function(){
		    			$scope.bakerusers.splice($scope.bakerusers.indexOf(gridItem),1)
		            });
		        
		        }
		 	});
	    }
	 
	 $scope.clickToOpen = function (gridItem) {
	        ngDialog.open({ 
	        	template: 'UserView.html',
	        	controller : ['$scope', 'BakerUser', function( $scope,  BakerUser){
	        	    $scope.bakeruser=BakerUser.get({id:gridItem});
	        	    console.log("WILL GET User with ID "+gridItem);   
	    			}],
	    		className: 'ngdialog-theme-default'
	    		
	        	});
	    };
	    
}]);

appControllers.controller('UserViewController', ['$scope', '$route', '$routeParams', '$location', 'BakerUser', '$anchorScroll',
                                                 function( $scope, $route, $routeParams, $location, BakerUser, $anchorScroll){
    $scope.bakeruser=BakerUser.get({id:$routeParams.id});
    console.log("WILL GET User with ID "+$routeParams.id);
    
	$scope.name = "UserViewController";
	$scope.params = $routeParams;
	
	

}]);

appControllers.controller('UserAddController',function($scope, $location, BakerUser){

    $scope.bakeruser=new BakerUser();

    $scope.addBakerUser=function(){
        $scope.bakeruser.$save(function(){
			$location.path("/users");
        });
    }

});

appControllers.controller('UserEditController', ['$scope', '$route', '$routeParams', '$location', 'BakerUser', '$anchorScroll',
        function( $scope, $route, $routeParams, $location, BakerUser, $anchorScroll){


    console.log("WILL EDIT User with ID "+$routeParams.id);
	
    $scope.updateUser=function(){

        console.log("$scope.password = "+$scope.password);
        console.log("$scope.retypepassword = "+$scope.retypepassword);
    	if ( ($scope.password) && ($scope.password === $scope.retypepassword))
    		$scope.bakeruser.password= $scope.password;
    	else {
            console.log("Will send to server empty password to keep old one ");
    		$scope.bakeruser.password= ''; //send empty to server, so not to change!
    	}
    	
        $scope.bakeruser.$update(function(){
			$location.path("/users");
        });
    };

    $scope.loadUser=function(){
        $scope.bakeruser=BakerUser.get({id:$routeParams.id});
    };

    $scope.loadUser();
}]);

appControllers.directive('equals', function() {
	  return {
	    restrict: 'A', // only activate on element attribute
	    require: 'ngModel', // get a hold of NgModelController
	    link: function(scope, elem, attrs, ngModel) {
	        console.log("IN LINK! ");
	      if(!ngModel) return; // do nothing if no ng-model

	        console.log("PASS IN LINK! ");
	      // watch own value and re-validate on change
	        
	      scope.$watch(attrs.ngModel, function() {
	        validate();
	      });

	      // observe the other value and re-validate on change
	      attrs.$observe('equals', function (val) {
	        validate();
	      });

	      var validate = function() {
	        // values
	        var val1 = ngModel.$viewValue;
	        var val2 = attrs.equals;

	        console.log("val1= "+val1);
	        console.log("val2= "+val2);
	        // set validity
	        ngModel.$setValidity('passwordVerify', ! val1 || ! val2 || val1 === val2);
	      };
	    }
	  }
	});




appControllers.controller('SubscribedMachineListController', ['$scope','$window','$log', 'SubscribedMachine', 'popupService','ngDialog',
                                             	function($scope, $window, $log, SubscribedMachine, popupService, ngDialog ) {
                 	
                 	

 	$scope.subscribedmachines = SubscribedMachine.query(function() {
 		    console.log($scope.subscribedmachines);
 		  }); //query() returns all the subscribedmachines
 		 
 	
 	
 	 $scope.deleteSubscribedMachine = function(gridItem, useridx, url){

 		 	console.log("Selected to DELETE SubscribedMachine with id = "+ useridx);
 		 	

 		 	var subscribedmachine=SubscribedMachine.get({id:useridx}, function() {
 			    $log.debug("WILL DELETE SubscribedMachine with ID "+ subscribedmachine.id);
 			    
 		        if(popupService.showPopup('Really delete SubscribedMachine '+subscribedmachine.id+'" ?')){
 				 	
 		        	subscribedmachine.$delete(function(){
 		    			$scope.subscribedmachines.splice($scope.subscribedmachines.indexOf(gridItem),1)
 		            });
 		        
 		        }
 		 	});
 	    }
 	 
 	 $scope.clickToOpen = function (gridItem, useridx, url) {
        ngDialog.open({ 
        	template: 'SubscribedMachineView.html',
        	controller : ['$scope', 'SubscribedMachine', function( $scope,  SubscribedMachine){
        	    $scope.subscribedmachine=SubscribedMachine.get({id:useridx});
        	    var i =SubscribedMachine.get({id:useridx});
        	    console.log("WILL GET SubscribedMachine with ID "+useridx);
        	    console.log("WILL GET SubscribedMachine with i "+i.id);	        	    
    			}],
    		className: 'ngdialog-theme-default'
    		
        	});
    };

              	
                 	 
}]);

appControllers.controller('SubscribedMachineViewController', ['$scope', '$route', '$routeParams', '$location', 'SubscribedMachine', '$anchorScroll', 
                                                 function( $scope, $route, $routeParams, $location, SubscribedMachine, $anchorScroll){
    $scope.subscribedmachine=SubscribedMachine.get({id:$routeParams.id});
    var i =SubscribedMachine.get({id:$routeParams.id});
    console.log("WILL GET SubscribedMachine with ID "+$routeParams.id);
    console.log("WILL GET SubscribedMachine with i "+i.id);
    
	$scope.name = "SubscribedMachineViewController";
	$scope.params = $routeParams;
	
	  

}]);

appControllers.controller('SubscribedMachineAddController',function($scope, $location, SubscribedMachine){

    $scope.subscribedmachine=new SubscribedMachine();

    $scope.addSubscribedMachine=function(){
        $scope.subscribedmachine.$save(function(){
			$location.path("/subscribed_machines");
        });
    }

});

appControllers.controller('SubscribedMachineEditController', ['$scope', '$route', '$routeParams', '$location', 'SubscribedMachine', '$anchorScroll',
        function( $scope, $route, $routeParams, $location, SubscribedMachine, $anchorScroll){


    console.log("WILL EDIT SubscribedMachine with ID "+$routeParams.id);
	
    $scope.updateSubscribedMachine=function(){
        $scope.subscribedmachine.$update(function(){
			$location.path("/subscribed_machines");
        });
    };

    $scope.loadSubscribedMachine=function(){
        $scope.subscribedmachine=SubscribedMachine.get({id:$routeParams.id});
    };

    $scope.loadSubscribedMachine();
}]);
