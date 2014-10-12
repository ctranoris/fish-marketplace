var app = angular.module('bakerapp', [   'ngCookies', 'ngResource', 'ngRoute', 
                                         'trNgGrid', 'bakerapp.controllers', 'bakerapp.services', 'ngDialog',
                                         'angular-loading-bar', 'ngAnimate']);

app.config(function($routeProvider, $locationProvider, $anchorScrollProvider, cfpLoadingBarProvider) {
	
    $anchorScrollProvider.disableAutoScrolling();
    
	$routeProvider.when('/login', {
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
	}).when('/users_add', {
		templateUrl : 'UserAdd.html',
		controller : 'UserAddController'
	}).when('/edit_user/:id', {
		templateUrl : 'UserEdit.html',
		controller : 'UserEditController'
	}).when('/subscribed_machines', {
		templateUrl : 'SubscribedMachines.html',
		controller : 'SubscribedMachineListController'
	}).when('/add_subscribed_machine', {
		templateUrl : 'SubscribedMachineAdd.html',
		controller : 'SubscribedMachineAddController'
	}).when('/edit_subscribed_machine/:id', {
		templateUrl : 'SubscribedMachineEdit.html',
		controller : 'SubscribedMachineEditController'
	}).when('/apps', {
		templateUrl : 'Apps.html',
		controller : 'AppListController'
	}).when('/app_add', {
		templateUrl : 'AppAdd.html',
		controller : 'AppAddController'
	}).when('/app_edit/:id', {
		templateUrl : 'AppEdit.html',
		controller : 'AppEditController'
	}).when('/app_view/:id', {
		templateUrl : 'AppView.html',
		controller : 'AppViewController'
	}).when('/categories', {
		templateUrl : 'Categories.html',
		controller : 'CategoriesListController'
	}).when('/add_category', {
		templateUrl : 'CategoryAdd.html',
		controller : 'CategoryAddController'
	}).when('/edit_category/:id', {
		templateUrl : 'CategoryEdit.html',
		controller : 'CategoryEditController'
	}).when('/app_marketplace', {
		templateUrl : 'AppsMarketplace.html',
		controller : 'AppsMarketplaceController'
	}).when('/buns', {
		templateUrl : 'Buns.html',
		controller : 'BunListController'
	}).when('/bun_add', {
		templateUrl : 'BunAdd.html',
		controller : 'BunAddController'
	}).when('/bun_edit/:id', {
		templateUrl : 'BunEdit.html',
		controller : 'BunEditController'
	}).when('/bun_view/:id', {
		templateUrl : 'BunView.html',
		controller : 'BunViewController'
	}).when('/bun_marketplace', {
		templateUrl : 'BunsMarketplace.html',
		controller : 'BunsMarketplaceController'
	}).otherwise({
		redirectTo : '/'
	});

	cfpLoadingBarProvider.includeSpinner = true;
	cfpLoadingBarProvider.includeBar = true;
	
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


app.controller('NavCtrl', [ '$scope', '$location', '$rootScope', function($scope, $location, $rootScope) {
	
	//$scope.user = $rootScope.bakeruser;
	
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

app.controller('bakerCtrl', function($scope, BakerUser, $log) {
	$log.debug('inside bakerCtrl controller');
	//$scope.bakerusers = BakerUser.query();
});


app.controller('SignupCtrl', function($scope) {

});



app.controller("LoginCtrl", ["$scope", "$location", "$window", "authenticationSvc", "$log", "$rootScope",
                             function ($scope, $location, $window, authenticationSvc, $log, $rootScope) {
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
                $rootScope.loggedinbakeruser = $scope.userInfo.bakerUser;

        		$log.debug('========== > inside LoginCtrl controller $rootScope.bakeruser ='+ $rootScope.loggedinbakeruser);
        		$log.debug('========== > inside LoginCtrl controller $rootScope.bakeruser ='+ $rootScope.loggedinbakeruser.username);
                
                $location.path("/app_marketplace");
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
		            	$rootScope.loggedinbakeruser = userInfo.bakerUser;
		            	$log.debug('========== > $rootScope.loggedIn set to TRUE because userInfooo = '+userInfo);
		            	if (userInfo.bakerUser){
		            		$log.debug('========== > $rootScope.loggedIn set to TRUE because userInfo.bakerUser.username = '+userInfo.bakerUser.username);
		            		$log.debug('========== > $rootScope.loggedIn set to TRUE because user $rootScope.bakeruser='+$rootScope.loggedinbakeruser.username);
		            	}
		            	
		            }
		        }
				
				if (!$rootScope.loggedIn && $location.path() != '/login' && $location.path() != '/app_marketplace') {
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
        $log.debug('========== > authenticationSvc Login');
        $http.post("/baker/services/api/repo/sessions/", { username: userName, password: password })
            .then(function (result) {
                userInfo = {
                    accesstoken: "NOTIMPLEMENTED",//result.data.access_token,
                    username: result.data.username,
                    bakerUser: result.data.bakerUser,
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
            	$rootScope.loggedinbakeruser = userInfo.bakerUser;
            	$log.debug('========== > $rootScope.loggedIn set to TRUE because userInfo ='+userInfo);
            	$log.debug('========== > $rootScope.loggedIn set to TRUE because userInfo.bakerUser ='+userInfo.bakerUser);
            	if (userInfo.bakerUser){
            		$log.debug('========== > $rootScope.loggedIn set to TRUE because user $rootScope.bakeruser.name ='+$rootScope.loggedinbakeruser.name);
            		$log.debug('========== > $rootScope.loggedIn set to TRUE because user $rootScope.bakeruser.id ='+$rootScope.loggedinbakeruser.id);
            	}
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











