'use strict';

angular.module('bmsapi2App')
    .factory('StudySearch', function ($resource) {
        return $resource('api/_search/studys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
