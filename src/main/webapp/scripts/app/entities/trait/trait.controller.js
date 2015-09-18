'use strict';

angular.module('bmsapi2App')
    .controller('TraitController', function ($scope, Trait, TraitSearch, ParseLinks) {
        $scope.traits = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Trait.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.traits = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Trait.get({id: id}, function(result) {
                $scope.trait = result;
                $('#deleteTraitConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Trait.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTraitConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            TraitSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.traits = result;
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
            $scope.trait = {name: null, description: null, property: null, measurementMethod: null, scale: null, id: null};
        };
    });
