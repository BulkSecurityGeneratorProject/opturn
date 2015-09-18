'use strict';

angular.module('bmsapi2App')
    .factory('TraitSearch', function ($resource) {
        return $resource('api/_search/traits/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
