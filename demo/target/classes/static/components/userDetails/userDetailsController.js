app.controller('UserDetailsController', function(UserService, $rootScope, $scope) {
    var vm = this;
    vm.user = null;
    vm.roles = [];
    vm.divisions = [];

    vm.getUserById = function(userId) {
        if (!userId) {
            console.error("User ID tidak tersedia!");
            return;
        }
        
        console.log("Fetching details for User ID:", userId);
        UserService.getUserByID(userId).then(function(response) {
            vm.user = response.data;
            console.log("User Details:", vm.user);

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
            console.log("Detected User ID change:", newUserId);
            vm.getUserById(newUserId);
        }
    });

    vm.closeDetails = function() {
        vm.user = null;
        vm.roles = [];
        vm.divisions = [];
        $rootScope.currentPage = 'userList';
    };
});
