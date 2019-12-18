(function (window, angular) {
    'use strict';
    angular.module("app").controller(
        'dataBase_line', [
            'globalParam',
            '$scope',
            '$localStorage',
            '$location',
            '$log',
            '$q',
            '$rootScope',
            '$window',
            'routeService',
            '$http',
            function dataBase_line(globalParam, $scope, $localStorage, $location, $log, $q, $rootScope, $window, routeService, $http) {
                $rootScope.a = [1, 2, "3", {"aaa": 123}];
                $scope.init = function () {
                    $scope.status = localStorage.getItem("status") ? 1 : 0;
                    $scope.sqlList = ['', 'mysql'];
                    $scope.sqlPort = ['', '3306'];
                    $scope.sqlClass = ['', 'com.mysql.jdbc.Driver'];

                    $scope.dbType = localStorage.getItem("dbType");
                    $scope.url = localStorage.getItem("url");
                    $scope.port = localStorage.getItem("port");
                    $scope.user = localStorage.getItem("user");
                    $scope.passWord = localStorage.getItem("passWord");
                    $scope.dataBase = localStorage.getItem("dataBase");
                    $scope.driverClass = localStorage.getItem("driverClass");

                    if ($scope.status) {
                        console.log(1)
                        $scope.toConnect();
                    }

                    layui.use(['form'], function () {
                        var element = layui.element;
                        var form = layui.form
                            , layer = layui.layer
                            , table = layui.table;
                        element.render();
                        form.render();

                        $(".layui-anim dd").click(function () {
                            var index = $(this).index();
                            $scope.dbType = $scope.sqlList[index];
                            $scope.port = $scope.sqlPort[index];
                            $scope.driverClass = $scope.sqlClass[index];
                            $("#port").val($scope.port);
                        })
                    });
                }

                $scope.toConnect = function () {
                    if ($scope.dbType && $scope.url && $scope.port && $scope.user && $scope.passWord && $scope.dataBase && $scope.driverClass) {
                        $scope._load = layer.load(3, {shade: [0.3, '#000000']});
                        $http({
                            method: 'POST',
                            url: $localStorage.ipAdd + '/dbConfig/connection',
                            params: {
                                dbType: $scope.dbType,
                                url: $scope.url,
                                port: $scope.port,
                                user: $scope.user,
                                passWord: $scope.passWord,
                                dataBase: $scope.dataBase,
                                driverClass: $scope.driverClass
                            }
                        }).then(function successCallback(resp) {
                            // 请求成功执行代码
                            console.log(resp)
                            if (resp.data.resCode == 1) {
                                layer.close($scope._load);
                                localStorage.setItem("dbType", $scope.dbType);
                                localStorage.setItem("url", $scope.url);
                                localStorage.setItem("port", $scope.port);
                                localStorage.setItem("user", $scope.user);
                                localStorage.setItem("passWord", $scope.passWord);
                                localStorage.setItem("dataBase", $scope.dataBase);
                                localStorage.setItem("driverClass", $scope.driverClass);
                                localStorage.setItem("status", "1");

                                console.log(resp.data.data);

                                $scope.dataBaseList = resp.data.data;
                                $scope.status = 1;

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
                                                    $scope.detailData = resp.data;
                                                }, function errorCallback() {
                                                    // 请求失败执行代码
                                                });
                                            }
                                        })
                                    }, 0);
                                });

                            } else {
                                layer.msg(resp.data.resMsg, {time: 2000});
                                layer.close($scope._load);
                            }
                        }, function errorCallback(res) {
                            // 请求失败执行代码
                            console.log(res)
                            layer.msg(res.data.resMsg, {time: 2000});
                            layer.close($scope._load);
                        });
                    } else {
                        layer.msg("请填写完整");
                    }
                }

                //重新连接
                $scope.reLine = function () {
                    $scope.status = 0;

                }

            }
        ]);

})(window, angular);