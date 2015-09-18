'use strict';

angular.module('bmsapi2App').controller('TraitDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Trait', 'Study',
        function($scope, $stateParams, $modalInstance, entity, Trait, Study) {

        $scope.trait = entity;
        $scope.studys = Study.query();
        $scope.load = function(id) {
            Trait.get({id : id}, function(result) {
                $scope.trait = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('bmsapi2App:traitUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.trait.id != null) {
                Trait.update($scope.trait, onSaveFinished);
            } else {
                Trait.save($scope.trait, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
