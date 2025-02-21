var app = angular.module('userApp', []);

app.controller('MainController', function($rootScope) {
    var vm = this;
    vm.currentPage = 'userList';
    
    vm.showPage = function(page) {
        vm.currentPage = page;
    };
});
