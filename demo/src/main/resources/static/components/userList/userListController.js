app.controller('UserListController', function(UserService, $rootScope) {
    var vm = this;
    vm.users = [];

    vm.getUsers = function() {
        UserService.getAllUsers().then(function(response) {
            vm.users = response.data;
        }).catch(function(error) {
            console.error('Error fetching user list:', error);
        });
    };
    
    vm.showUserDetails = function(userId) {
        $rootScope.selectedUserId = userId;
        $rootScope.currentPage = 'userDetails';
    };
   
    vm.getUsers();
});
