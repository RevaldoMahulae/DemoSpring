app.controller('UserCreateController', function(UserService) {
    var vm = this;

    vm.user = {
        name: '',
        email: '',
        nik: null,
        dob: '',
        roleIds: [],
        divisionIds: []
    };

    vm.availableRoles = [
        { id: 1, name: 'Admin' },
        { id: 2, name: 'User' },
        { id: 3, name: 'Manager' },
        { id: 4, name: 'Developer' },
        { id: 5, name: 'QA Tester' }
    ];

    vm.availableDivisions = [
        { id: 1, name: 'IT' },
        { id: 2, name: 'Finance' },
        { id: 3, name: 'HR' },
        { id: 4, name: 'Marketing' },
        { id: 5, name: 'Operations' }
    ];

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
            })
            .catch(function(error) {
                vm.errorMessage = 'Gagal membuat user: ' + (error.data.message || error.statusText);
                vm.successMessage = '';
                console.error('Error creating user:', error);
            });
    };
});
