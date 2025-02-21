app.service('UserService', function($http) {
    var API_BASE_URL = "/users";

    this.getAllUsers = function() {
        return $http.get(API_BASE_URL);
    };
    
    this.getUserByID = function(userId) {
        return $http.get(API_BASE_URL + "/" + userId);
    };
});
