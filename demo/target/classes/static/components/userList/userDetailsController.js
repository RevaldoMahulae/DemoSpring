app.controller('UserDetailsController', function(UserService, $rootScope, $scope) {
    var vm = this;
    vm.user = {};
    vm.roles = [];
    vm.divisions = [];

    vm.getUserById = function(userId) {
        if (!userId) {
            console.error("User ID tidak tersedia!");
            return;
        }

        UserService.getUserDetails(userId).then(function(response) {
            vm.user = response.data;
            vm.roles = vm.user.role ? vm.user.role.split(',') : [];
            vm.divisions = vm.user.division ? vm.user.division.split(',') : [];
        })
        .catch(function(error) {
            console.error('Error fetching user details:', error);
        });
    };

    $scope.$watch(function() {
        return $rootScope.selectedUserId;
    }, function(newUserId) {
        if (newUserId) {
            vm.getUserById(newUserId);
        }
    });
});
