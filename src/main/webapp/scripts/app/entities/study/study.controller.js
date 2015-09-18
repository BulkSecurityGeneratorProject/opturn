'use strict';

angular.module('bmsapi2App')
    .controller('StudyController', function ($scope, Study, StudySearch, ParseLinks) {
        $scope.studys = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Study.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.studys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Study.get({id: id}, function(result) {
                $scope.study = result;
                $('#deleteStudyConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Study.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteStudyConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            StudySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.studys = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.study = {name: null, description: null, objective: null, type: null, startDate: null, endDate: null, id: null};
        };
    });
