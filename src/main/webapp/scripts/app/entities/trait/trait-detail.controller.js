'use strict';

angular.module('bmsapi2App')
    .controller('TraitDetailController', function ($scope, $rootScope, $stateParams, entity, Trait, Study) {
        $scope.trait = entity;
        $scope.load = function (id) {
            Trait.get({id: id}, function(result) {
                $scope.trait = result;
            });
        };
        $rootScope.$on('bmsapi2App:traitUpdate', function(event, result) {
            $scope.trait = result;
        });
    });
