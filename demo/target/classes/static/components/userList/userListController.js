app.controller('UserListController', function(UserService) {
    var vm = this;
    vm.users = [];
    vm.selectedUserId = null;

    vm.getUsers = function() {
        UserService.getAllUsers().then(function(response) {
            vm.users = response.data;
        }).catch(function(error) {
            console.error('Error fetching user list:', error);
        });
    };
   
    vm.getUsers();
});
