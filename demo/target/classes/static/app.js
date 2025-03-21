var app = angular.module('userApp', []);

app.controller('MainController', function($rootScope) {
    var vm = this;
    vm.currentPage = 'userList';
    $rootScope.selectedUserId = null;
    $rootScope.currentPage = vm.currentPage;

    vm.showPage = function(page, userId = null) {
        vm.currentPage = page;
        $rootScope.currentPage = page;

        if (userId !== null) {
            $rootScope.selectedUserId = userId;
        }
    };
});
