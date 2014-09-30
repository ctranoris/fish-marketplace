var app = angular.module('bakerapp', [   'ngCookies', 'ngResource', 'ngRoute' ]);

app.config(function($routeProvider, $locationProvider) {
	$routeProvider.when('/marketplace', {
		templateUrl : 'viewBunMarketplaceJS.html',
		controller : 'bunsCtrl'
	}).when('/installedbuns', {
		templateUrl : 'viewInstalledBunsJS.html',
		controller : 'bakerCtrl'
	}).when('/login', {
		templateUrl : 'SignInHomeSection.html',
		controller : 'LoginCtrl'
	}).when('/logout', {
		templateUrl : 'logout.html',
		controller : 'LogoutCtrl'
	}).when('/signup', {
		templateUrl : 'signup.html',
		controller : 'SignupCtrl'
	}).when('/users', {
		templateUrl : 'Users.html',
		controller : 'UserCtrl'
	}).otherwise({
		redirectTo : '/'
	});
	
});

app.run(function (api) {
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

app.controller('bakerCtrl', function($scope, BakerUser) {
	console.log('inside bakerCtrl controller');
	$scope.bakerusers = BakerUser.query();
});

app.controller('bunsCtrl', function($scope, Buns) {
	console.log('========== > inside bunsCtrl controller');
	$scope.buns = Buns.query();
	$scope.lalakis = 'Lalaks';
});

app.controller('SignupCtrl', function($scope) {

});

app.controller('UserCtrl', function($scope) {

});

// BakerUser Resource
app.factory('BakerUser', function($resource) {
	return $resource("/baker/services/api/repo/users/:Id", {
		Id : "@Id"
	}, {
		"update" : {
			method : "PUT"
		},
		"reviews" : {
			'method' : 'GET',
			'params' : {
				'reviews_only' : "true"
			},
			isArray : true
		}

	});
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


app.controller('LoginCtrl', function($scope, $log,  $rootScope, $location,
		SessionService) {
    
	$scope.user = {
		username : '',
		password : ''
	};

	$log.debug('========== > inside LoginCtrl controller');

	$scope.login = function() {

		$log.debug('========== > inside LoginCtrl:login function');
		$scope.user = SessionService.save($scope.user, function(success) {
			$rootScope.loggedIn = true;
			$location.path('/');
			$window.sessionStorage["loggedIn"] = JSON.stringify($rootScope.loggedIn);
			
		}, function(error) {
			$scope.loginError = true;
		});
	};
});



// The code below is heavily inspired by Witold Szczerba's plugin for AngularJS.
// // I have modified the code in order
// to reduce its complexity and make for easier explanation to novice JS
// developers. You can find his plugin here: // https://
// github.com/witoldsz/angular-http-auth

app.config(function($httpProvider) {
	$httpProvider.interceptors.push(function($rootScope, $location, $q, $log) {
		return {
			'request' : function(request) { // if we're not logged-in to the
				// AngularJS app, redirect to // login
				// page
				$rootScope.loggedIn = $rootScope.loggedIn || $rootScope.username;
				$log.debug('========== > inside httpProvider.interceptors');
				
				if (!$rootScope.loggedIn && $location.path() != '/login') {
					$location.path('/login');
				}
				return request;
			},
			'responseError' : function(rejection) { // if we're not logged-in to
													// the web service,
				// redirect to login page
				if (rejection.status === 401 && $location.path() != '/login') {
					$rootScope.loggedIn = false;
					$location.path('/login');
				}
				return $q.reject(rejection);
			}
		};
	});
});

/**
 * app.config(function($routeProvider) { $routeProvider.when('/', { templateUrl :
 * 'viewBunMarketplaceJS.html', controller : 'EventListCtrl' }).when('/login', {
 * templateUrl : 'login.html', controller : 'LoginCtrl' }).when('/logout', {
 * templateUrl : 'login.html', controller : 'LogoutCtrl' }).otherwise({
 * redirectTo : '/' }); });
 * 
 * 
 * 
 * app.controller('LoginCtrl', function($scope, $rootScope, $location,
 * SessionService) { $scope.user = { username : '', password : '' };
 * 
 * $scope.login = function() { $rootScope.loggedIn = true; $location.path('/'); //
 * $scope.user = SessionService.save($scope.user, function(success) { //
 * $rootScope.loggedIn = true; // $location.path('/'); // }, function(error) { //
 * $scope.loginError = true; // }); };
 * 
 * });
 * 
 * 
 * app.factory('SessionService', function($resource) { return
 * $resource('/api/sessions'); }); // The code below is heavily inspired by
 * Witold Szczerba's plugin for AngularJS. // I have modified the code in order //
 * to reduce its complexity and make for easier explanation to novice JS //
 * developers. You can find his plugin here: //
 * https://github.com/witoldsz/angular-http-auth
 * 
 * app.config(function($httpProvider) {
 * $httpProvider.interceptors.push(function($rootScope, $location, $q) { return {
 * 'request' : function(request) { // if we're not logged-in to the AngularJS
 * app, redirect to // login page $rootScope.loggedIn = $rootScope.loggedIn ||
 * $rootScope.username; if (!$rootScope.loggedIn && $location.path() !=
 * '/login') { $location.path('/login'); } return request; }, 'responseError' :
 * function(rejection) { // if we're not logged-in to the web service, redirect
 * to login // page if (rejection.status === 401 && $location.path() !=
 * '/login') { $rootScope.loggedIn = false; $location.path('/login'); } return
 * $q.reject(rejection); } }; }); });
 */

