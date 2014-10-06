var app = angular.module('bakerapp', [   'ngCookies', 'ngResource', 'ngRoute', 
                                         'trNgGrid', 'bakerapp.controllers', 'bakerapp.services' ]);

app.config(function($routeProvider, $locationProvider, $anchorScrollProvider) {
	
    $anchorScrollProvider.disableAutoScrolling();
    
	$routeProvider.when('/marketplace', {
		templateUrl : 'viewBunMarketplaceJS.html',
		controller : 'bunsCtrl'
	}).when('/installedbuns', {
		templateUrl : 'viewInstalledBunsJS.html',
		controller : 'bakerCtrl'
	}).when('/login', {
		controller : 'LoginCtrl'
	}).when('/logout', {
		templateUrl : 'logout.html',
		controller : 'LogoutCtrl'
	}).when('/signup', {
		templateUrl : 'signup.html',
		controller : 'SignupCtrl'
	}).when('/users', {
		templateUrl : 'Users.html',
		controller : 'UserListController'
	}).when('/view_user/:id', {
		templateUrl : 'UserView.html',
		controller : 'UserViewController'
	}).when('/users_add', {
		templateUrl : 'UserAdd.html',
		controller : 'UserAddController'
	}).when('/edit_user/:id', {
		templateUrl : 'UserEdit.html',
		controller : 'UserEditController'
	}).when('/subscribed_machines', {
		templateUrl : 'SubscribedMachines.html',
		controller : 'SubscribedMachineListController'
	}).when('/view_subscribed_machine/:id', {
		templateUrl : 'SubscribedMachineView.html',
		controller : 'SubscribedMachineViewController'
	}).when('/add_subscribed_machine', {
		templateUrl : 'SubscribedMachineAdd.html',
		controller : 'SubscribedMachineAddController'
	}).when('/edit_subscribed_machine/:id', {
		templateUrl : 'SubscribedMachineEdit.html',
		controller : 'SubscribedMachineEditController'
	}).otherwise({
		redirectTo : '/'
	});
	
});


//app.value('$anchorScroll', angular.noop);

app.run(function ( api) {
	  api.init();
});

app.factory('api', function ($http, $cookies) {
	  return {
	      init: function (token) {
	          $http.defaults.headers.common['X-Access-Token'] = token || $cookies.token;
	      }
	  };
	});


app.controller('NavCtrl', [ '$scope', '$location', function($scope, $location) {
	$scope.navClass = function(page) {
		var currentRoute = $location.path().substring(1) || 'home';
		return page === currentRoute ? 'active' : '';
	};
	
    
} ]);

app.controller('LogoutCtrl', [ '$scope', '$location', 'authenticationSvc', '$log',function($scope, $location, authenticationSvc, $log) {
	
	$log.debug('In LogoutCtrl');
	authenticationSvc.logout().then(function(result) {
				$scope.userInfo = null;
				$location.path("/login");
			}, function(error) {
				$log.debug(error);
			});
   
    
    
} ]);

app.controller('bakerCtrl', function($scope, BakerUser) {
	console.log('inside bakerCtrl controller');
	//$scope.bakerusers = BakerUser.query();
});

app.controller('bunsCtrl', function($scope, Buns, $log) {
	$log.debug('========== > inside bunsCtrl controller');
	$scope.buns = Buns.query();
	$scope.lalakis = 'Lalaks';
});

app.controller('SignupCtrl', function($scope) {

});





// BakerUser Resource
app.factory('Buns', function($resource) {
	return $resource("/baker/services/api/repo/buns/:Id", {
		Id : "@Id"
	}, {
		"update" : {
			method : "PUT"
		}

	});
});

app.factory('SessionService', function($resource) {
	return $resource('/baker/services/api/repo/sessions/');
});


app.controller("LoginCtrl", ["$scope", "$location", "$window", "authenticationSvc", "$log", "$rootScope",function ($scope, $location, $window, authenticationSvc, $log, $rootScope) {
	$log.debug('========== > inside LoginCtrl controller');
    $scope.userInfo = null;
    $scope.user = {
    		username : '',
    		password : ''
    	};
    $scope.login = function () {
        authenticationSvc.login($scope.user.username, $scope.user.password)
            .then(function (result) {
    			$rootScope.loggedIn = true;
                $scope.userInfo = result;
                $location.path("/");
            }, function (error) {
                //$window.alert("Invalid credentials");
    			$scope.loginError = true;
    			$log.debug(error);
            });
    };

    $scope.cancel = function () {
        $scope.user.userName = "";
        $scope.user.password = "";
    };
}]);



// The code below is heavily inspired by Witold Szczerba's plugin for AngularJS.
// // I have modified the code in order
// to reduce its complexity and make for easier explanation to novice JS
// developers. You can find his plugin here: // https://
// github.com/witoldsz/angular-http-auth

app.config(function($httpProvider) {
	$httpProvider.interceptors.push(function($rootScope, $location, $q, $log,$window) {
		return {
			'request' : function(request) { // if we're not logged-in to the
				// AngularJS app, redirect to // login
				// page
				$rootScope.loggedIn = $rootScope.loggedIn || $rootScope.username;
				$log.debug('========== > inside httpProvider.interceptors');
				
				if ($window.sessionStorage["userInfo"]!=null) {
		            userInfo = JSON.parse($window.sessionStorage["userInfo"]);
		            if (userInfo){
		            	$rootScope.loggedIn = true;
		            	$log.debug('========== > $rootScope.loggedIn set to TRUE because userInfooo = '+userInfo);
		            }
		        }
				
				if (!$rootScope.loggedIn && $location.path() != '/login' && $location.path() != '/marketplace') {
					$log.debug('========== > $rootScope.loggedIn IS FALSE');
					$location.path('/login');
				}
				return request;
			},
			'responseError' : function(rejection) { // if we're not logged-in to
													// the web service,
				// redirect to login page
				if (rejection.status === 401 && $location.path() != '/login') {
					$rootScope.loggedIn = false;
		            $window.sessionStorage["userInfo"] = null;
					$location.path('/login');
				}
				return $q.reject(rejection);
			}
		};
	});
});




app.factory("authenticationSvc", ["$http","$q","$window","$rootScope", "$log", function ($http, $q, $window,$rootScope, $log) {
    var userInfo;

	$log.debug('========== > authenticationSvc');
	
    function login(userName, password) {
        var deferred = $q.defer();

        $http.post("/baker/services/api/repo/sessions/", { username: userName, password: password })
            .then(function (result) {
                userInfo = {
                    accesstoken: "NOTIMPLEMENTED",//result.data.access_token,
                    username: result.data.username
                };
                $window.sessionStorage["userInfo"] = JSON.stringify(userInfo);
                deferred.resolve(userInfo);
            }, function (error) {
                deferred.reject(error);
            });

        return deferred.promise;
    }

    function logout() {
    	$log.debug('========== > authenticationSvc logout' );
        var deferred = $q.defer();

        $http({
            method: "GET",
            url: "/baker/admin/logout",
            headers: {
                "access_token": "NOT_IMPLEMENTED"//userInfo.accessToken
            }
        }).then(function (result) {
        	$log.debug('========== > authenticationSvc logout RESET everything' );
            userInfo = null;
			$rootScope.loggedIn = false;
            $window.sessionStorage["userInfo"] = null;
            deferred.resolve(result);
        }, function (error) {
            deferred.reject(error);
        });

        return deferred.promise;
    }

    function getUserInfo() {
        return userInfo;
    }

    function init() {
		$log.debug('========== > authenticationSvc inside init');
        if ($window.sessionStorage["userInfo"]) {
            userInfo = JSON.parse($window.sessionStorage["userInfo"]);
            if (userInfo){
            	$rootScope.loggedIn = true;
            	$log.debug('========== > $rootScope.loggedIn set to TRUE because userInfo ='+userInfo);
            }
        }
    }
    init();

    return {
        login: login,
        logout: logout,
        getUserInfo: getUserInfo
    };
}]);











