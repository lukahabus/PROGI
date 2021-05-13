var app = angular.module('main', ['ngRoute', 'dndLists']);

var REST_base = "http://worldpiis.duckdns.org:8080"

app.config(function ($routeProvider) {
	$routeProvider.when('/', {
		templateUrl: './views/login.html',
		controller: 'loginController'
	}).when('/kanban', {
		resolve: {
			"check": function ($location, $window) {
				if ($window.localStorage.getItem("loggedIn")===null || $window.localStorage.getItem("loggedIn")===undefined) {
					$location.path('/');
				}
			}
		},
		templateUrl: './views/kanban.html',
		controller: 'kanbanController'
	}).when('/homeC', {
		resolve: {
			"check": function ($location, $window) {
				if ($window.localStorage.getItem("loggedIn")===null || $window.localStorage.getItem("loggedIn")===undefined) {
					$location.path('/');
				}
			}
		},
		templateUrl: './views/home.html',
		controller: 'coorController'
	}).when('/homeB', {
		resolve: {
			"check": function ($location, $window) {
				if ($window.localStorage.getItem("loggedIn")===null || $window.localStorage.getItem("loggedIn")===undefined) {
					$location.path('/');
				}
			}
		},
		templateUrl: './views/home.html',
		controller: 'boardController'
	}).when('/profile', {
		resolve: {
			"check": function ($location, $window) {
				if ($window.localStorage.getItem("loggedIn")===null || $window.localStorage.getItem("loggedIn")===undefined) {
					$location.path('/');
				}
			}
		},
		templateUrl: './views/profile.html',
		controller: 'ProfileCtrl'
	}).when('/edit-profile', {
		resolve: {
			"check": function ($location, $window) {
				if ($window.localStorage.getItem("loggedIn")===null || $window.localStorage.getItem("loggedIn")===undefined) {
					$location.path('/');
				}
			}
		},
		templateUrl: './views/edit-profile.html',
		controller: 'EditCtrl'
	}).when('/addTask',{
		resolve:{
			"check": function ($location, $window) {
				if ($window.localStorage.getItem("loggedIn")===null || $window.localStorage.getItem("loggedIn")===undefined) {
					$location.path('/');
				}
			}
		},
		templateUrl: './views/addTask.html',
		controller: 'AddTask'
	}).when('/editTask',{
		resolve:{
			"check": function ($location, $window) {
				if ($window.localStorage.getItem("loggedIn")===null || $window.localStorage.getItem("loggedIn")===undefined) {
					$location.path('/');
				}
			}
		},
		templateUrl: './views/editTask.html',
		controller: 'EditTask'
	}).otherwise({
			template: '404'
		})

});

app.factory('userData',function(){
	var userInfo,addUserData,getUserData;

	addUserData = function(user){
		userInfo=user;
	};

	getUserData = function(){
		return userInfo;
	};

	return{
		addUserData: addUserData,
		getUserData: getUserData,
	};
});

app.factory('teamData',function(){
	var teamInfo,addTeamData,getTeamData;

	addTeamData = function(team){
		teamInfo=team;
	};

	getTeamData = function(){
		return teamInfo;
	};

	return{
		addTeamData: addTeamData,
		getTeamData: getTeamData,
	};
})


app.controller('loginController', function ($scope, $location, $http, userData, $window) {

	$scope.login = function () {

		$scope.employee=$http.get(REST_base + '/employees/'+$scope.username).then(function (response){
			$scope.employee=response.data;

			if($scope.password==$scope.employee.password){
				if ($window.localStorage.getItem("loggedIn")!==null || $window.localStorage.getItem("loggedIn")!==undefined)
					$window.localStorage.removeItem("loggedIn");
				userData.addUserData($scope.employee);
				$window.localStorage.setItem("loggedIn",$scope.username);
			    $window.localStorage.setItem("data",JSON.stringify($scope.employee));
				if($scope.employee.clearanceLevel=="COORDINATOR") $location.path('/homeC');
				else if($scope.employee.clearanceLevel=="BOARD") $location.path('/homeB');
				else $location.path('/kanban');
			}
			else $scope.error = "Netočna lozinka!";

		},function (response){
			$scope.error = "Korisnik ne postoji!";
		});
	}
});

app.controller('kanbanController', function ($scope,userData,teamData,$http,$location,$window,$route) {
	userData.addUserData(JSON.parse($window.localStorage.getItem("data")));
	
	$scope.firstName=userData.getUserData().name;
	$scope.surname=userData.getUserData().surname;       
	$scope.userCLevel=userData.getUserData().clearanceLevel;
	if(userData.getUserData().clearanceLevel == "COORDINATOR" || userData.getUserData().clearanceLevel == "BOARD") $scope.team=(JSON.parse($window.localStorage.getItem("team"))).teamId;
	else $scope.team=userData.getUserData().team.teamId;

	$scope.lists = [ {id: 1, name: "BACKLOG", cards: []},
    {id: 2, name: "RAZVIJANJE",  cards: []},
    {id: 3, name: "TESTIRANJE", cards: []},
	{id: 4, name: "ZAVRŠENO", cards: []}]
	    
	$scope.dropCallbackItems = function(index, item, external, ind){
		console.log(index, item, external, ind)
	  }
	  
	$scope.dropCallback = function(index, item, external, ind) {
		$http.defaults.headers.post["Content-Type"] = "application/json;charset=UTF-8";
		pom=$scope.lists[index].name 
		if(pom=="RAZVIJANJE") pom="DEVELOP";
		if(pom=="TESTIRANJE") pom="TESTING";
		if(pom=="ZAVRŠENO") pom="DONE";

		$http.put(REST_base + '/tasks/'+item.id,{
			"id": item.id,
			"name": item.name,
			"description": item.description,
			"priority": item.priority,
			"deadline": item.deadline,
			"status": pom
		  })
		 return item;
	   };
	   
	   
	$scope.dragStart = function(ind){
		 console.log(ind)
		 $scope.draggedFrom = ind.toString()
	   };
	

	$scope.employees=$http.get(REST_base + '/employees').then(function (response){
		$scope.employees=response.data;
		for(var i=0;i<$scope.employees.length;i++) {
            if($scope.employees[i].team != null && $scope.employees[i].clearanceLevel=="TEAM_LEAD" && $scope.employees[i].team.teamId==$scope.team){
                for(j = 0;j<$scope.employees[i].team.tasks.length;j++){
					$scope.assig="Slobodan";
						for(var h=0;h<$scope.employees.length;h++){
							if($scope.employees[h].team != null){
								if ($scope.employees[h].team.teamId==$scope.team || $scope.employees[h].team==$scope.team){
									if($scope.employees[h].tasks.indexOf($scope.employees[i].team.tasks[j].id)!=-1){
										$scope.assig=$scope.employees[h].name+" "+$scope.employees[h].surname;
									}
								}
							}
						}

					var task={
						id: $scope.employees[i].team.tasks[j].id,
                		name: $scope.employees[i].team.tasks[j].name,
                		description: $scope.employees[i].team.tasks[j].description ,
                		priority: $scope.employees[i].team.tasks[j].priority,
                		deadline: $scope.employees[i].team.tasks[j].deadline,
						status:$scope.employees[i].team.tasks[j].status,
						assigned: $scope.assig
					}

					if($scope.employees[i].team.tasks[j].status=="BACKLOG")
						$scope.lists[0].cards.push(task);
            		if($scope.employees[i].team.tasks[j].status=="DEVELOP")
                		$scope.lists[1].cards.push(task);
            		if($scope.employees[i].team.tasks[j].status=="TESTING")
                		$scope.lists[2].cards.push(task);
            		if($scope.employees[i].team.tasks[j].status=="DONE")
						$scope.lists[3].cards.push(task);
					
				}
			}
		}
		$scope.calendar=false;
		
	});

	$scope.logout=function(){
		$window.localStorage.clear();
		$location.path('/');
	};

 	$scope.addToList = function () {
		$location.path('/addTask')
	};

	$scope.openProfile = function () {
		$location.path('/profile');
	};

	$scope.takeTaskFromList=function(task){
		
		$http.defaults.headers.post["Content-Type"] = "application/json;charset=UTF-8";

		if($scope.userCLevel != "BOARD" && $scope.userCLevel != "COORDINATOR"){
		$http.put(REST_base + '/employees/'+userData.getUserData().username+'/tasks/'+task.id)
			.then(function(response){
				userData.addUserData(response.data);
				$route.reload();
			},function(response){
				alert('Nešto je pošlo po krivu, podatci nisu promijenjeni.');
				$location.path('/kanban');
			});
		}
	}
	$scope.editTask = function(card){
		$window.localStorage.setItem("task",JSON.stringify(card));
		$location.path('/editTask');
	};

});
app.controller('AddTask',function($window,$scope,$location,$http,userData){
	userData.addUserData(JSON.parse($window.localStorage.getItem("data")));
	
	$scope.add=function(){
		var Task={
            name: $scope.name,
            description: $scope.description ,
            priority: $scope.priority,
            deadline: $scope.deadline
		};
		$http.defaults.headers.post["Content-Type"] = "application/json;charset=UTF-8";

		$http.post(REST_base + '/tasks/'+userData.getUserData().team.teamId,JSON.stringify(Task))
			.then(function(response){
				$scope.task=response.data;
				$location.path('/kanban');
			},function(response){
				alert('Nešto je pošlo po krivu, zadatak nije dodan');
				$location.path('/kanban');
			});
	}
});

app.controller('EditTask',function($window,$scope,$location,$http,userData){
	var task=JSON.parse($window.localStorage.getItem("task"));
	$scope.name=task.name;
	$scope.description=task.description;
	$scope.priority=task.priority;
	$scope.saveTask=function(){
		var newtask={
			name: $scope.name,
            description: $scope.description ,
            priority: $scope.priority,
			deadline: task.deadline,
			status: task.status
		}

		$http.defaults.headers.post["Content-Type"] = "application/json;charset=UTF-8";

		$http.put(REST_base + '/tasks/'+task.id,JSON.stringify(newtask))
		.then(function(response){
			$scope.task=response.data;
			$window.localStorage.removeItem("task");
			$location.path('/kanban');
		},function(response){
			alert('Nešto je pošlo po krivu, zadatak nije dodan');
			$window.localStorage.removeItem("task");
		$location.path('/kanban');
		});
	};
	$scope.cancelTask=function(){
		$window.localStorage.removeItem("task");
		$location.path('/kanban');
	}

});

app.controller('ProfileCtrl',function($scope,$location,userData,$window){

	userData.addUserData(JSON.parse($window.localStorage.getItem("data")));
	$scope.ime=userData.getUserData().name;
	$scope.prezime=userData.getUserData().surname;
	$scope.korisnickoIme=userData.getUserData().username;
	$scope.lozinka=userData.getUserData().password;
	$scope.brMob=userData.getUserData().phoneNumber;
	$scope.email=userData.getUserData().email;
	$scope.zatvori=function(){
		if(userData.getUserData().clearanceLevel=="COORDINATOR") $location.path('/homeC');
		else if(userData.getUserData().clearanceLevel=="BOARD") $location.path('/homeB');
		else $location.path('/kanban');
	}
	$scope.uredi=function(){
		$location.path('/edit-profile');
	}

});

app.controller('EditCtrl',function($scope,$location,userData,$http,$window){
		
	userData.addUserData(JSON.parse($window.localStorage.getItem("data")));
	$scope.ime=userData.getUserData().name;
	$scope.prezime=userData.getUserData().surname;
	$scope.korisnickoIme=userData.getUserData().username;
	$scope.lozinka=userData.getUserData().password;
	$scope.brMob=userData.getUserData().phoneNumber;
	$scope.email=userData.getUserData().email;
	$scope.spremi=function(){
		var User={
			id: userData.getUserData().id,
			username : $scope.korisnickoIme,
			password: $scope.lozinka,
			name: $scope.ime,
			surname: $scope.prezime,
			phoneNumber: $scope.brMob,
			email: $scope.email,
			clearanceLevel: userData.getUserData().clearanceLevel,
			team: userData.getUserData().team,
			tasks:userData.getUserData().tasks

		};
		$http.defaults.headers.post["Content-Type"] = "application/json;charset=UTF-8";

		$http.put(REST_base + '/employees/'+$scope.korisnickoIme,JSON.stringify(User))
			.then(function(response){
				userData.addUserData(response.data);
				$window.localStorage.setItem("data",JSON.stringify(response.data));
				$location.path('/profile');
			},function(response){
				alert('Nešto je pošlo po krivu, podatci nisu promijenjeni.');
				$location.path('/profile');
			});
	}
});
app.controller('coorController', function($scope,$location,userData,teamData,$http,$window){
	userData.addUserData(JSON.parse($window.localStorage.getItem("data")));
	$scope.openProfile = function () {
		$location.path('/profile');
	}
	$scope.logout=function(){
		$window.localStorage.clear();
		$location.path('/');
	};
	
	$scope.teams = [];
	var tim = [];

	$scope.id=userData.getUserData().id;
	
	$http.get(REST_base + '/workgroups/'+$scope.id+'/teams/').then(function (response){
		$scope.getteams=response.data;

		for(i=0;i<$scope.getteams.length;i++){
			$scope.teams.push($scope.getteams[i].teamId);
			tim.push($scope.getteams[i]);
		}
	},function (response){});

	$scope.openKanban=function(team){
		var n;
		for(i = 0; i < tim.length; i++){
			if(tim[i].teamId == team) 
				n = i;
		}
		$window.localStorage.setItem("team",JSON.stringify(tim[n]));
		teamData.addTeamData(tim[n]);
		$location.path('/kanban');
	}
});
app.controller('boardController', function($scope,$location,teamData,$http,$window,userData){
	userData.addUserData(JSON.parse($window.localStorage.getItem("data")));
	$scope.openProfile = function () {
		$location.path('/profile');
	}
	$scope.logout=function(){
		$window.localStorage.clear();
		$location.path('/');
	};
	$scope.teams = [];
	var tim = [];

	$http.get(REST_base + '/teams/').then(function (response){
		$scope.getteams=response.data;

		for(i=0;i<$scope.getteams.length;i++){
			$scope.teams.push($scope.getteams[i].teamId);
			tim.push($scope.getteams[i]);
		}
	},function (response){});

	$scope.openKanban=function(team){
		var n;
		for(i = 0; i < tim.length; i++){
			if(tim[i].teamId == team) 
				n = i;
		}
		$window.localStorage.setItem("team",JSON.stringify(tim[n]));
		teamData.addTeamData(tim[n]);
		$location.path('/kanban');
	}
});

app.controller('homeController',function($scope){
	$scope.calendar=false;
});