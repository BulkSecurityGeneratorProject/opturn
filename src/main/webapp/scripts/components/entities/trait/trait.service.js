'use strict';

angular.module('bmsapi2App')
    .factory('Trait', function ($resource, DateUtils) {
        return $resource('api/traits/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
