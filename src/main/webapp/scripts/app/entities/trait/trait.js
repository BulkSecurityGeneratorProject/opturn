'use strict';

angular.module('bmsapi2App')
    .config(function ($stateProvider) {
        $stateProvider
            .state('trait', {
                parent: 'entity',
                url: '/traits',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'bmsapi2App.trait.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/trait/traits.html',
                        controller: 'TraitController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('trait');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('trait.detail', {
                parent: 'entity',
                url: '/trait/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'bmsapi2App.trait.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/trait/trait-detail.html',
                        controller: 'TraitDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('trait');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Trait', function($stateParams, Trait) {
                        return Trait.get({id : $stateParams.id});
                    }]
                }
            })
            .state('trait.new', {
                parent: 'trait',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/trait/trait-dialog.html',
                        controller: 'TraitDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, description: null, property: null, measurementMethod: null, scale: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('trait', null, { reload: true });
                    }, function() {
                        $state.go('trait');
                    })
                }]
            })
            .state('trait.edit', {
                parent: 'trait',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/trait/trait-dialog.html',
                        controller: 'TraitDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Trait', function(Trait) {
                                return Trait.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('trait', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
