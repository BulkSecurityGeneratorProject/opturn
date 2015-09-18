'use strict';

angular.module('bmsapi2App')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
