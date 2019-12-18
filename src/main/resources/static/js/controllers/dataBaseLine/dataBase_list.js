(function (window, angular) {
    'use strict';
    angular.module("app").controller(
        'dataBase_list', [
            '$scope',
            '$localStorage',
            '$location',
            '$log',
            '$q',
            '$rootScope',
            '$window',
            'routeService',
            '$http',
            'globalParam',
            function dataBase_list($scope, $localStorage, $location, $log, $q, $rootScope, $window, routeService, $http, globalParam) {

                $scope.dataBaseList = globalParam.getter();
                layui.use(['form'], function () {
                    var element = layui.element;
                    var form = layui.form;
                    setTimeout(function () {
                        element.render();
                        form.render();

                        $(".layui-anim dd").click(function () {
                            console.log($(this).attr('lay-value'));
                            if ($(this).attr('lay-value') >= 0) {

                                var tableName = $(this).html();

                                $http({
                                    method: 'GET',
                                    url: $localStorage.ipAdd + '/dbConfig/detail',

                                    params: {
                                        dbType: localStorage.getItem("dbType"),
                                        url: localStorage.getItem("url"),
                                        port: localStorage.getItem("port"),
                                        user: localStorage.getItem("user"),
                                        passWord: localStorage.getItem("passWord"),
                                        dataBase: localStorage.getItem("dataBase"),
                                        driverClass: localStorage.getItem("driverClass"),
                                        tableName: tableName
                                    }
                                }).then(function successCallback(resp) {
                                    // 请求成功执行代码
                                    console.log(resp)
                                    console.log(resp.data);
                                    $scope.detailData = resp.data;
                                }, function errorCallback() {
                                    // 请求失败执行代码
                                });
                            }

                        })
                    }, 0);
                });


                console.log(globalParam.getter());

            }
        ]);
})(window, angular);