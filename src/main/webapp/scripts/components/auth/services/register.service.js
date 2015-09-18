'use strict';

angular.module('bmsapi2App')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


