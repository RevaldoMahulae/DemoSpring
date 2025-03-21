app.controller('UserEditController', function(UserService, $rootScope, $scope, $http) {
    var vm = this;
    vm.user = {};
	vm.availableRoles = [];
    vm.availableDivisions = [];

    vm.getRoles = function() {
        $http.get("http://localhost:9090/role").then(function(response) {
            vm.availableRoles = response.data;
        }).catch(function(error) {
            console.error('Error fetching roles:', error);
        });
    };

    vm.getDivisions = function() {
        $http.get("http://localhost:9090/division").then(function(response) {
            vm.availableDivisions = response.data;
        }).catch(function(error) {
            console.error('Error fetching divisions:', error);
        });
    };

	vm.getUserById = function(userId) {
	    UserService.getUserDetails(userId).then(function(response) {
	        vm.user = response.data;
	
	        if (vm.user.dob) {
	            vm.user.dob = new Date(vm.user.dob);
	        }
	
	        if (typeof vm.user.role === 'string') {
	            vm.user.roleIds = vm.user.role.split(',').map(Number);
	        } else if (Array.isArray(vm.user.role)) {
	            vm.user.roleIds = vm.user.role.map(role => role.id); // Jika objek, ambil ID
	        } else {
	            vm.user.roleIds = [];
	        }
	
	        if (typeof vm.user.division === 'string') {
	            vm.user.divisionIds = vm.user.division.split(',').map(Number);
	        } else if (Array.isArray(vm.user.division)) {
	            vm.user.divisionIds = vm.user.division.map(division => division.id);
	        } else {
	            vm.user.divisionIds = [];
	        }
	
	    }).catch(function(error) {
	        console.error('Error fetching user details:', error);
	    });
	};



	vm.updateUser = function() {
	    var userId = $rootScope.selectedUserId;
	    
	    if (!userId) {
	        alert("User ID tidak ditemukan!");
	        return;
	    }
	
	    var requestData = {
	        name: vm.user.name,
	        email: vm.user.email,
	        nik: parseInt(vm.user.nik, 10),
	        dob: vm.user.dob ? vm.user.dob.toISOString().split('T')[0] : null, 
	        roleIds: Array.isArray(vm.user.roleIds) ? vm.user.roleIds.map(Number) : [],
	        divisionIds: Array.isArray(vm.user.divisionIds) ? vm.user.divisionIds.map(Number) : []
	    };
	
	    UserService.updateUser(userId, requestData).then(function(response) {
	        alert("User berhasil diperbarui!");
	        $rootScope.$emit('userUpdated');
	        $rootScope.showEditModal = false;
	    }).catch(function(error) {
	        alert("Gagal memperbarui user!");
	        console.error('Error updating user:', error);
	    });
	};


    $scope.$watch(function() {
        return $rootScope.selectedUserId;
    }, function(newUserId) {
        if (newUserId) {
            vm.getUserById(newUserId);
        }
    });
    
    if ($rootScope.selectedUserId) {
	    vm.getUserById($rootScope.selectedUserId);
	}
    
    vm.getRoles();
    vm.getDivisions();
});
