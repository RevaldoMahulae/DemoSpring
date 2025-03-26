app.controller('UserCreateController', function(UserService, $http) {
    var vm = this;
    vm.availableRoles = [];
    vm.availableDivisions = [];
    
    vm.user = {
        name: '',
        email: '',
        nik: null,
        dob: '',
        roleIds: [],
        divisionIds: []
    };

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

    vm.createUser = function() {
        var requestData = {
            name: vm.user.name,
            email: vm.user.email,
            nik: vm.user.nik,
            dob: vm.user.dob,
            roleIds: vm.user.roleIds,
            divisionIds: vm.user.divisionIds
        };

        UserService.createUser(requestData)
            .then(function(response) {
                vm.successMessage = 'User berhasil dibuat!';
                vm.errorMessage = '';
                vm.user = { name: '', email: '', nik: null, dob: '', roleIds: [], divisionIds: [] };
                vm.getRoles();
                vm.getDivisions();
                window.location.reload();
            })
            .catch(function(error) {
                vm.errorMessage = 'Gagal membuat user: ' + (error.data.message || error.statusText);
                vm.successMessage = '';
                console.error('Error creating user:', error);
            });
    };

    vm.getRoles();
    vm.getDivisions();
});
