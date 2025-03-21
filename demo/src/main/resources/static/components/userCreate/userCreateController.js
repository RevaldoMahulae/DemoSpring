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

    vm.createUser = function() {
        var requestData = {
            name: vm.user.name,
            email: vm.user.email,
            nik: vm.user.nik,
            dob: vm.user.dob,
            roleIds: vm.user.roleIds.map(Number), 
            divisionIds: vm.user.divisionIds.map(Number)
        };

        UserService.createUser(requestData)
            .then(function(response) {
                vm.successMessage = 'User berhasil dibuat!';
                vm.errorMessage = '';
                vm.newUser = {};
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
