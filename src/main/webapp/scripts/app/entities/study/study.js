'use strict';

angular.module('bmsapi2App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('study', {
                parent: 'entity',
                url: '/studys',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'bmsapi2App.study.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/study/studys.html',
                        controller: 'StudyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('study');
                        $translatePartialLoader.addPart('studyType');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('study.detail', {
                parent: 'entity',
                url: '/study/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'bmsapi2App.study.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/study/study-detail.html',
                        controller: 'StudyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('study');
                        $translatePartialLoader.addPart('studyType');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Study', function($stateParams, Study) {
                        return Study.get({id : $stateParams.id});
                    }]
                }
            })
            .state('study.new', {
                parent: 'study',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/study/study-dialog.html',
                        controller: 'StudyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, description: null, objective: null, type: null, startDate: null, endDate: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('study', null, { reload: true });
                    }, function() {
                        $state.go('study');
                    })
                }]
            })
            .state('study.edit', {
                parent: 'study',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/study/study-dialog.html',
                        controller: 'StudyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Study', function(Study) {
                                return Study.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('study', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
