app.controller('UserListController', function(UserService, $rootScope) {
    var vm = this;
    vm.users = [];
    vm.showModal = false;
    vm.showEditModal = false;
    vm.selectedUser = {};

    vm.getUsers = function() {
        UserService.getAllUsers().then(function(response) {
            vm.users = response.data;
        }).catch(function(error) {
            console.error('Error fetching user list:', error);
        });
    };

    vm.showUserDetails = function(userId) {
        $rootScope.selectedUserId = userId;
        vm.showModal = true;
    };

    vm.closeModal = function() {
        vm.showModal = false;
        $rootScope.selectedUserId = null;
    };

	vm.editUser = function(userId) {
	    $rootScope.selectedUserId = userId;
	    vm.showEditModal = true;
	
	    UserService.getUserDetails(userId).then(function(response) {
	        $rootScope.selectedUser = response.data;
	        
	        if ($rootScope.selectedUser.dob) {
	            $rootScope.selectedUser.dob = new Date($rootScope.selectedUser.dob);
	        }
	
	        $rootScope.selectedUser.roleIds = $rootScope.selectedUser.role 
	            ? $rootScope.selectedUser.role.split(',').map(Number) 
	            : [];
	        $rootScope.selectedUser.divisionIds = $rootScope.selectedUser.division 
	            ? $rootScope.selectedUser.division.split(',').map(Number) 
	            : [];
	    }).catch(function(error) {
	        console.error('Error fetching user details for edit:', error);
	    });
	};


    vm.closeEditModal = function() {
        vm.showEditModal = false;
        $rootScope.selectedUserId = null;
    };

    vm.deleteUser = function(userId) {
        if (confirm("Apakah Anda yakin ingin menghapus pengguna ini?")) {
            UserService.deleteUser(userId)
                .then(function(response) {
                    alert("Pengguna berhasil dihapus!");
                    vm.closeModal();
                    vm.getUsers();
                })
                .catch(function(error) {
                    console.error('Error deleting user:', error);
                    alert("Gagal menghapus pengguna!");
                });
        }
    };

	$rootScope.$on('userUpdated', function() {
		window.location.reload();
    });


    vm.getUsers();
});
