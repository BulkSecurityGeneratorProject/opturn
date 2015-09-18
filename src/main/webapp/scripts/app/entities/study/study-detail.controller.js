'use strict';

angular.module('bmsapi2App')
    .controller('StudyDetailController', function ($scope, $rootScope, $stateParams, entity, Study, Trait) {
        $scope.study = entity;
        $scope.load = function (id) {
            Study.get({id: id}, function(result) {
                $scope.study = result;
            });
        };
        $rootScope.$on('bmsapi2App:studyUpdate', function(event, result) {
            $scope.study = result;
        });
    });
