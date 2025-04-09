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
  
    this.updateUser = function(userId, userData) {
        return $http.put(API_URL + "/update/" + userId, userData, {
            headers: { 'Content-Type': 'application/json' }
        });
    };
    
    this.deleteUser = function(userId) {
        return $http.delete(API_URL + "/delete/" + userId);
    };
    
    this.searchUsers = function(keyword) {
        return $http.get('http://localhost:9090/users/search', { params: { keyword: keyword } });
    };
});