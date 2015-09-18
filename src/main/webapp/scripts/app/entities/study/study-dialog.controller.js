'use strict';

angular.module('bmsapi2App').controller('StudyDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Study', 'Trait',
        function($scope, $stateParams, $modalInstance, entity, Study, Trait) {

        $scope.study = entity;
        $scope.traits = Trait.query();
        $scope.load = function(id) {
            Study.get({id : id}, function(result) {
                $scope.study = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('bmsapi2App:studyUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.study.id != null) {
                Study.update($scope.study, onSaveFinished);
            } else {
                Study.save($scope.study, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
