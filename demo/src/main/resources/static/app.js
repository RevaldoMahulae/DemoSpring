var app = angular.module('userApp', []);

app.controller('MainController', function($rootScope) {
    var vm = this;
    vm.currentPage = 'userList';
    $rootScope.selectedUserId = null;
      
    vm.showPage = function(page,userId = null) {
        vm.currentPage = page;
        if (userId !== null) {
            $rootScope.selectedUserId = userId;
        }
    };
});
