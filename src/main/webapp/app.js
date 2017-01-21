angular.module('app', ['ui.router', 'ui.bootstrap']);

angular.element(document).ready(function () {
    angular.bootstrap(document.getElementById('app'), ['app']);
});

angular.module('app')
.directive('convertToNumber', function() {
    return {
        require: 'ngModel',
        link: function(scope, element, attrs, ngModel) {
             ngModel.$parsers.push(function(val) {
                return parseInt(val, 10);
             });
             ngModel.$formatters.push(function(val) {
                 return '' + val;
             });
        }
    };
})
.constant('CRITERIA', [
    {title: 'All Aplicants', path: '/'},
    {title: 'Ukrainian Enrolled', path: '/enrolled/ukrainian'},
    {title: 'Foreign Enrolled', path: '/enrolled/foreign'}
])
.constant('HEADERS', [{
        title: 'ID',
        field: 'identity'
    }, {
        title: 'First Name',
        field: 'firstName'
    }, {
        title: 'Last Name',
        field: 'lastName'
    }, {
        title: 'Middle Name',
        field: 'middleName'
    }, {
        title: 'Mark',
        field: 'score'
    }, {
        title: 'Phone number',
        field: 'phone'
    }, {
        title: 'Gender',
        field: 'gender'
    }, {
        title: 'Country',
        field: 'country'
    }
])
.component('sortingCriteria', {
    templateUrl: 'view/sorting-criteria.html',
    controller: 'SortingCriteriaController'
})
.filter('toLowerCase', function toLowerCaseFilterProvider() {
    return function toLowerCaseFilter(value) {
        return ('' + value).toLowerCase();
    }
})
.controller('SortingCriteriaController', function SortingCriteriaController(CRITERIA, $rootScope) {
    this.criteria = CRITERIA;
    [this.selected] = CRITERIA;

    this.onCriteriaChange = function onCriteriaChange(selected) {
        $rootScope.$broadcast('criteria-selected', {path: selected.path});
    }
})
.controller('HomeController', function HomeController(HEADERS, $http, $scope, $uibModal) {
    var vm = this;

    vm.path = '/';
    vm.applicant = [];
    vm.headers = HEADERS;

    vm.addApplicant = addApplicant;
    vm.deleteApplicant = deleteApplicant;
    vm.changeScore = changeScore;

    $scope.$on('criteria-selected', onCriteriaChange)

    getApplicants();

    function addApplicant(applicant) {
        var modal = $uibModal.open({
            templateUrl: 'view/applicant.html',
            controller: function($scope, $uibModalInstance, applicant, countries, $http) {
                $scope.applicant = applicant;
                $scope.applicant.gender = $scope.applicant.gender || 0;
                $scope.applicant.country = $scope.applicant.country || countries[0];
                $scope.countries = countries;
                $scope.type = applicant ? 'Edit' : 'Add';

                $scope.submit = submit;
                $scope.close = close;

                function submit() {
                    $http.post('/applicants/', $scope.applicant)
                        .then(function(response) {
                            $uibModalInstance.close(response.data);
                        });
                }

                function close() {
                    $uibModalInstance.dismiss();
                }
            },
            resolve: {
                applicant: function() {
                    return applicant || {};
                },
                countries: function($http) {
                    return $http.get('/countries/').then(function(response) {
                        return response.data;
                    });
                }
            }
        });

        modal.result.then(function() {
            getApplicants();
        });
    }

    function changeScore(applicant) {
        var modal = $uibModal.open({
            templateUrl: 'view/scores.html',
            controller: function($scope, $uibModalInstance, applicant, subjects, $http) {
                $scope.applicant = applicant;
                $scope.subjects = subjects;

                $scope.mark = {
                    applicant: applicant
                };
                $scope.marks = applicant.marks;
                $scope.create = create;
                $scope.close = close;
                $scope.removeScore = removeScore;

                function create() {
                    $http.post('/marks/', $scope.mark)
                        .then(function(response) {
                            $scope.mark.subject = null;
                            $scope.mark.mark = undefined;
                            return $http.get('/applicants/' + applicant.id)
                        })
                        .then((response) => {
                            $scope.marks = response.data.marks;
                    });
                }

                function removeScore(mark) {
                    $http.delete('/marks/' + mark.id)
                        .then((response) => {
                            const index = $scope.marks.indexOf(mark);
                            if (index !== -1) {
                                $scope.marks.splice(index, 1);
                            }
                        });
                }

                function close() {
                    $uibModalInstance.dismiss();
                }
            },
            resolve: {
                applicant: function() {
                    return applicant || {};
                },
                subjects: function($http) {
                    return $http.get('/subjects/').then(function(response) {
                        return response.data;
                    });
                }
            }
        });

        modal.result.then(getApplicants, getApplicants);
    }



    function deleteApplicant(applicant) {
        $http.delete('/applicants/' + applicant.id)
            .then(applicantDeleted)
            .catch(serverError);

        function applicantDeleted(response) {
            getApplicants();
        }
    }

    function getApplicants() {
        $http.get('/applicants' + vm.path)
            .then(gotApplicants)
            .catch(serverError);

        function gotApplicants(response) {
            vm.applicants = response.data;
        }
    }

    function serverError(reason) {
        console.error(reason);
    }

    function onCriteriaChange(event, data) {
        vm.path = data.path;
        getApplicants();
    }
})
.config(function($stateProvider, $locationProvider, $urlRouterProvider) {
    $stateProvider
        .state('home', {
            url: '/',
            views: {
                "": {
                    templateUrl: 'view/home.html',
                    controller: 'HomeController as home'
                }
            }
        });

    $urlRouterProvider.otherwise('/');

    $locationProvider.html5Mode(true);
});
