app.service('UserService', function($http) {
    var API_URL = "http://localhost:9090/users";

    this.getAllUsers = function() {
        return $http.get(API_URL);
    };

    this.getUserDetails = function(userId) {
        return $http.get(API_URL + "/" + userId);
    };

    this.createUser = function(userData) {
        return $http.post(API_URL + "/create", userData);
    };
});