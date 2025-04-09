app.controller('UserEditController', function(UserService, $rootScope, $scope, $http, $timeout) {
    var vm = this;
    vm.user = { roleIds: [], divisionIds: [] };
    vm.availableRoles = [];
    vm.availableDivisions = [];

    vm.getRoles = function() {
        $http.get("http://localhost:9090/role")
            .then(function(response) {
                vm.availableRoles = response.data;
                vm.initTomSelect("#role-select", vm.user.roleIds, vm.availableRoles);
            })
            .catch(function(error) {
                console.error('Error fetching roles:', error);
            });
    };

    vm.getDivisions = function() {
        $http.get("http://localhost:9090/division")
            .then(function(response) {
                vm.availableDivisions = response.data;
                vm.initTomSelect("#division-select", vm.user.divisionIds, vm.availableDivisions);
            })
            .catch(function(error) {
                console.error('Error fetching divisions:', error);
            });
    };

    vm.getUserById = function(userId) {
        UserService.getUserDetails(userId).then(function(response) {
            vm.user = response.data;

            if (vm.user.dob) {
                vm.user.dob = new Date(vm.user.dob);
            }

            // Konversi role dan division dari string ke array ID
            vm.user.roleIds = vm.convertNamesToIds(vm.user.role, vm.availableRoles, 'roleName');
            vm.user.divisionIds = vm.convertNamesToIds(vm.user.division, vm.availableDivisions, 'divisionName');

            // Inisialisasi Tom Select setelah data user dimuat
            $timeout(function() {
                vm.initTomSelect("#role-select", vm.user.roleIds, vm.availableRoles);
                vm.initTomSelect("#division-select", vm.user.divisionIds, vm.availableDivisions);
            });

        }).catch(function(error) {
            console.error('Error fetching user details:', error);
        });
    };

    vm.convertNamesToIds = function(nameString, availableList, key) {
        if (!nameString) return [];
        return nameString.split(',').map(name => {
            let item = availableList.find(item => item[key] === name.trim());
            return item ? item.id : null;
        }).filter(id => id !== null);
    };

    vm.initTomSelect = function(selector, model, availableOptions) {
        var element = document.querySelector(selector);
        if (!element) return;

        if (element.tomselect) {
            element.tomselect.destroy();
        }

        var tomSelectInstance = new TomSelect(element, {
            plugins: ['remove_button'],
            persist: false,
            create: false,
            options: availableOptions.map(option => ({ value: String(option.id), text: option.roleName || option.divisionName })),
            onChange: function(value) {
                $scope.$apply(function() {
                    model.length = 0;
                    model.push(...value.map(Number));
                });
            }
        });

        tomSelectInstance.setValue(model.map(String));
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
            roleIds: vm.user.roleIds.map(Number),
            divisionIds: vm.user.divisionIds.map(Number)
        };

        UserService.updateUser(userId, requestData).then(function(response) {
            alert("User berhasil diperbarui!");
            $rootScope.$emit('userUpdated');
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
