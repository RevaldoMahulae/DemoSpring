'use strict';

angular.module('skillSet').controller('SkillSetController', SkillSetController);

SkillSetController.$inject = [
    'restful',
    'toastr',
    'loadingService',
    '$location',
];

function SkillSetController(restful, toastr, loadingService, $location) {
    const vm = this;

    // Initial variables and declarations
    vm.menuName = 'Skill Set';

    vm.dialogs = {
        create: document.getElementById('dialog-create-skillset'),
        delete: document.getElementById('dialog-delete-skillset'),
    };
    vm.dialogTitle = '';
    vm.mode = '';
    vm.dialogMode = {
        CREATE: 'c',
        VIEW: 'v',
        EDIT: 'e',
    };

    vm.filters = {
        id: '',
        name: '',
        priority: 2,
    };

    vm.items = [];
    vm.selectedItem = {};

    vm.pagination = {
        currentPage: 1,
        itemsPerPage: 10,
        totalItems: 0,
    };

    vm.priorityOptions = [
        { value: 2, label: 'All' },
        { value: 1, label: 'Yes' },
        { value: 0, label: 'No' },
    ];

    ////////// Function Declarations //////////
    vm.showDialog = showDialog;
    vm.closeDialog = closeDialog;

    vm.onPageChange = handlePageChange;

    vm.fetchItems = fetchItems;
    vm.clearFilter = clearFilter;
    vm.search = search;

    vm.getChipStyle = getChipStyle;

    vm.save = save;
    vm.createItem = createItem;
    vm.editItem = editItem;
    vm.deleteItem = deleteItem;

    // Initial Call
    vm.fetchItems();

    ////////// Function Implementations //////////

    function handlePageChange(newPageNumber) {
        vm.pagination.currentPage = newPageNumber;
        vm.fetchItems();
    }

    function clearFilter() {
        vm.filters = {
            id: '',
            name: '',
            priority: 2,
        };
        vm.onPageChange(1);
    }

    function search(event) {
        event.preventDefault();
        vm.onPageChange(1);
    }

    function showDialog(dialog, mode, skillSet) {
        if (dialog) {
            if (dialog === vm.dialogs.create) {
                vm.selectedItem = angular.copy(skillSet);
                vm.mode = mode;
                switch (mode) {
                    case 'c':
                        vm.dialogTitle = `Create ${vm.menuName}`;
                        break;
                    case 'v':
                        vm.dialogTitle = `View ${vm.menuName}`;
                        break;
                    case 'e':
                        vm.dialogTitle = `Edit ${vm.menuName}`;
                        break;
                }
            } else {
                vm.selectedItem = skillSet;
            }

            dialog.showModal();
        }
    }

    function closeDialog(dialog) {
        if (dialog) {
            dialog.close();
        }
    }

    function save() {
        if (vm.mode == vm.dialogMode.CREATE) {
            vm.createItem();
        } else if (vm.mode == vm.dialogMode.EDIT) {
            vm.editItem();
        }
    }

    function fetchItems() {
        loadingService.setLoading(true);

        const endpointUrl = '/api/v1/skillSet/list';
        const params = {
            pageNumber: vm.pagination.currentPage,
            size: vm.pagination.itemsPerPage,
            id:
                vm.filters.id === null ||
                vm.filters.id === undefined ||
                vm.filters.id === ''
                    ? 0
                    : vm.filters.id,
            name: vm.filters.name || '',
            priority:
                vm.filters.priority === null ||
                vm.filters.priority === undefined ||
                vm.filters.priority === ''
                    ? 2
                    : vm.filters.priority,
        };

        vm.items = [];

        restful
            .httpGet(endpointUrl, params)
            .then((response) => {
                vm.items = response.items;
                vm.pagination.currentPage = response.currentPage;
                vm.pagination.totalItems = response.totalItems;
            })
            .catch((error) => {
                const errorMessage =
                    error.response?.data?.message ||
                    'An error occurred while fetching your data.';
                toastr.error(errorMessage);
            })
            .finally(() => {
                loadingService.setLoading(false);
            });
    }

    function createItem() {
        const endpointUrl = '/api/v1/skillSet/create';

        restful
            .httpPost(endpointUrl, vm.selectedItem)
            .then(function (response) {
                toastr.success(
                    `${vm.menuName} '${response.name}' has been created successfully!`
                );
                vm.closeDialog(vm.dialogs.create);
                vm.fetchItems();
            })
            .catch(function (error) {
                let errorMessage = 'An error occurred while saving your data.';
                if (
                    error.response &&
                    error.response.data &&
                    error.response.data.message
                ) {
                    errorMessage = error.response.data.message;
                }
                toastr.error(errorMessage);
            });
    }

    function editItem() {
        const endpointUrl = '/api/v1/skillSet/edit';

        restful
            .httpPost(endpointUrl, vm.selectedItem)
            .then(function (response) {
                toastr.success(
                    `${vm.menuName} '${response.name}' has been edited successfully!`
                );
                vm.closeDialog(vm.dialogs.create);
                vm.fetchItems();
            })
            .catch(function (error) {
                toastr.error('An error occurred while saving your data.');
            });
    }

    function deleteItem() {
        const endpointUrl = '/api/v1/skillSet/delete';

        restful
            .httpPost(endpointUrl, vm.selectedItem)
            .then(function (response) {
                toastr.success(
                    `${vm.menuName} '${response.name}' has been deleted successfully!`
                );

                vm.pagination.totalItems--;
                vm.closeDialog(vm.dialogs.delete);

                // Change page if the current page has no items left
                var totalPages = Math.ceil(
                    vm.pagination.totalItems / vm.pagination.itemsPerPage
                );

                if (vm.pagination.currentPage > totalPages) {
                    vm.pagination.currentPage = totalPages;
                }

                vm.fetchItems();
            })
            .catch(function (error) {
                toastr.error('An error occurred while deleting your data.');
            });
    }

    function getChipStyle(isPriority) {
        switch (isPriority) {
            case 1:
                return {
                    'background-color': '#E9F9F4',
                    color: '#0F9D58',
                };
            case 0:
                return {
                    'background-color': '#FFE8F0',
                    color: '#E91E63',
                };
        }
    }
}
