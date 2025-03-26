app.controller('UserEditController', function(UserService, $rootScope, $scope, $http) {
    var vm = this;
    vm.user = { roleIds: [], divisionIds: [] };
    vm.availableRoles = [];
    vm.availableDivisions = [];

    vm.showRoles = false;
    vm.showDivisions = false;

    vm.getRoles = function() {
        $http.get("http://localhost:9090/role")
            .then(function(response) {
                vm.availableRoles = response.data;
            })
            .catch(function(error) {
                console.error('Error fetching roles:', error);
            });
    };

    vm.getDivisions = function() {
        $http.get("http://localhost:9090/division")
            .then(function(response) {
                vm.availableDivisions = response.data;
            })
            .catch(function(error) {
                console.error('Error fetching divisions:', error);
            });
    };

    vm.toggleSelection = function(id, type) {
        var index = vm.user[type].indexOf(id);
        if (index > -1) {
            vm.user[type].splice(index, 1);
        } else {
            vm.user[type].push(id);
        }
    };


    vm.getUserById = function(userId) {
        UserService.getUserDetails(userId).then(function(response) {
            vm.user = response.data;

            if (vm.user.dob) {
                vm.user.dob = new Date(vm.user.dob);
            }


	            vm.user.roleIds = vm.user.role
	                ? vm.user.role.split(',').map(roleName => {
	                    let role = vm.availableRoles.find(r => r.roleName === roleName.trim());
	                    return role ? role.id : null;
	                }).filter(id => id !== null)
	                : [];
	
	            vm.user.divisionIds = vm.user.division
	                ? vm.user.division.split(',').map(divisionName => {
	                    let division = vm.availableDivisions.find(d => d.divisionName === divisionName.trim());
	                    return division ? division.id : null;
	                }).filter(id => id !== null)
	                : [];	            
	        
            vm.mapSelectedRoles();
            vm.mapSelectedDivisions();
            


        }).catch(function(error) {
            console.error('Error fetching user details:', error);
        });
    };

    vm.mapSelectedRoles = function() {
        if (vm.availableRoles.length > 0 && vm.user.roleIds.length > 0) {
            vm.user.roleIds = vm.availableRoles
                .filter(role => vm.user.roleIds.includes(role.id))
                .map(role => role.id);
        }
    };

    vm.mapSelectedDivisions = function() {
        if (vm.availableDivisions.length > 0 && vm.user.divisionIds.length > 0) {
            vm.user.divisionIds = vm.availableDivisions
                .filter(division => vm.user.divisionIds.includes(division.id))
                .map(division => division.id);
        }
    };
    
    vm.getSelectedRoles = function() {
        return vm.availableRoles
            .filter(role => vm.user.roleIds.includes(role.id))
            .map(role => role.roleName)
            .join(', ');
    };

    vm.getSelectedDivisions = function() {
        return vm.availableDivisions
            .filter(division => vm.user.divisionIds.includes(division.id))
            .map(division => division.divisionName)
            .join(', ');
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
            vm.showEditModal = false;
        }).catch(function(error) {
            alert("Gagal memperbarui user!");
            console.error('Error updating user:', error);
        });
    };

    // Update user ID yang dipilih
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
