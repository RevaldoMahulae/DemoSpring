app.controller('UserCreateController', function(UserService, $http, $timeout) {
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

    // Fetch roles and initialize Tom Select
    vm.getRoles = function() {
        $http.get("http://localhost:9090/role")
            .then(function(response) {
                vm.availableRoles = response.data;
                $timeout(initTomSelectRoles, 100); // Tunggu DOM selesai render
            })
            .catch(function(error) {
                console.error('Error fetching roles:', error);
            });
    };

    // Fetch divisions and initialize Tom Select
    vm.getDivisions = function() {
        $http.get("http://localhost:9090/division")
            .then(function(response) {
                vm.availableDivisions = response.data;
                $timeout(initTomSelectDivisions, 100); // Tunggu DOM selesai render
            })
            .catch(function(error) {
                console.error('Error fetching divisions:', error);
            });
    };

    function initTomSelectRoles() {
        new TomSelect("#roles", {
            plugins: ['remove_button'],
            persist: false,
            create: false
        });
    }

    function initTomSelectDivisions() {
        new TomSelect("#divisions", {
            plugins: ['remove_button'],
            persist: false,
            create: false
        });
    }

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
