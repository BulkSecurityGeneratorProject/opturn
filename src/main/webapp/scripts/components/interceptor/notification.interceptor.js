 'use strict';

angular.module('bmsapi2App')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-bmsapi2App-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-bmsapi2App-params')});
                }
                return response;
            },
        };
    });