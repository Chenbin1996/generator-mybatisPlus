(function (window, angular) {
    'use strict';
    angular.module("app").controller(
        'codeDownload', [
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
            function codeDownload(globalParam,$scope, $localStorage, $location, $log, $q, $rootScope, $window, routeService, $http) {
                console.log($rootScope.a)
                $scope.init=function(){
                    $scope.status=localStorage.getItem("status") ? 1 : 0;
                    if($scope.status === 0){
                        return;
                    }
                    $scope.url=localStorage.getItem("url");
                    $scope.port=localStorage.getItem("port");
                    $scope.user=localStorage.getItem("user");
                    $scope.passWord=localStorage.getItem("passWord");
                    $scope.dataBase=localStorage.getItem("dataBase");
                    $scope.dbType=localStorage.getItem("dbType");
                    $scope.driverClass=localStorage.getItem("driverClass");

                    $scope.creater=""
 					$scope.getValue();
                    $scope.arrNames=[];
                    $scope.typeList=["完整项目","片段代码"];
                    
                    
                }

				
				$scope.checkCreater = function(){
					if($scope.creater == null || $scope.creater === ''){
					}
				}
				
				

                $scope.getTableList=function(){
                    $http({
                        method: 'post',
                        url: $localStorage.ipAdd + '/dbConfig/connection',
                        params: {
                            dbType:$scope.dbType,
                            url:$scope.url,
                            port:$scope.port,
                            user:$scope.user,
                            passWord:$scope.passWord,
                            dataBase:$scope.dataBase,
                            driverClass:$scope.driverClass
                        }
                    }).then(function successCallback(resp) {
                        // 请求成功执行代码

                        console.log(resp.data.data);
                        $scope.tableList=resp.data.data;

                    }, function errorCallback(res) {
                        // 请求失败执行代码
                      
                    });
                };

                $scope.getValue = function () {
                    $scope._load = layer.load(3, {shade: [0.3, '#000000']});
                    $http({
                        method: 'get',
                        url: $localStorage.ipAdd + '/dbConfig/tableComment',
                        params: {
                            url: localStorage.getItem("url"),
                            port: localStorage.getItem("port"),
                            user: localStorage.getItem("user"),
                            passWord: localStorage.getItem("passWord"),
                            dataBase: localStorage.getItem("dataBase"),
                            dbType: localStorage.getItem("dbType"),
                            driverClass: localStorage.getItem("driverClass")
                        }
                    }).success(
                        function (res) {
                            layer.close($scope._load);
                            console.log(res);
                            $scope.tableList = res.data;
                        }
                    )
                };

                $scope.valueChange = function (val, obj) {
                    obj.description = val.comment;
                    obj.tableName = val.tableName;
                    obj.pkDataType = val.jdbcType;
                    obj.domainName = val.domainName;
                };
				
                $scope.add=function(){
                    var obj={};
                    $scope.arrNames.push(obj);
                };

                $scope.delete=function(index){
                    $scope.arrNames.splice(index,1);
                };
                $scope.save=function(){
                	
                	$('#checkfrom').click();
                	
                    if($scope.creater && $scope.fileName && $scope.creatName && $scope.type){
                        if($scope.arrNames.length>0){
                            for(var i=0;i<$scope.arrNames.length;i++){
                            	console.log($scope.arrNames[i]);
                            	if($scope.arrNames[i].description == null || $scope.arrNames[i].description === ''){
                            		layer.msg("请为每张表添加描述",{time:2000});
                            		return;
                            	}
                            	if ($scope.arrNames[i].tableName == null || $scope.arrNames[i].tableName ===''){
                                    layer.msg("请为每张表添加表名",{time:2000});
                                    return;
                                }
                                if ($scope.arrNames[i].pkDataType == null || $scope.arrNames[i].pkDataType === ''){
                                    layer.msg("请为每张表添加主键类型",{time:2000});
                                    return;
                                }
                                $scope.arrNames[i].pkDataType = $(".tableList tr").eq(i).find("select").eq(1).val();
                                $scope.arrNames[i].description = $(".tableList tr").eq(i).find("input").eq(1).val();
                                $scope.arrNames[i].domainName = $(".tableList tr").eq(i).find("input").eq(0).val();
                            }
                            if($scope.type=='完整项目'){
                                $scope.typeNum='1';
                            }else {
                                $scope.typeNum = '2';
                            }
                            console.log($scope.arrNames);

                            $("#downloadform").remove();
                            var form = $("<form>");// 定义一个form表单
                            form.attr("id", "downloadform");
                            form.attr("style", "display:none");
                            form.attr("target", "");
                            form.attr("method", "get");
                            form.attr("action", $localStorage.ipAdd + '/springBoot/create');
                            var input1 = "<input type='hidden' name='jsonData' value='"+JSON.stringify($scope.arrNames)+"' />";
                            form.append(input1);
                            var input2 = "<input type='hidden' name='author' value='"+$scope.creater+"' />";
                            form.append(input2);
                            var input3 = "<input type='hidden' name='fileName' value='"+$scope.fileName+"' />";
                            form.append(input3);
                            var input4 = "<input type='hidden' name='packAge' value='"+$scope.creatName+"' />";
                            form.append(input4);
                            var input5 = "<input type='hidden' name='dbType' value='"+$scope.dbType+"' />";
                            form.append(input5);
                            var input6 = "<input type='hidden' name='url' value='"+$scope.url+"' />";
                            form.append(input6);
                            var input7 = "<input type='hidden' name='port' value='"+$scope.port+"' />";
                            form.append(input7);
                            var input8 = "<input type='hidden' name='user' value='"+$scope.user+"' />";
                            form.append(input8);
                            var input9 = "<input type='hidden' name='passWord' value='"+$scope.passWord+"' />";
                            form.append(input9);
                            var input10 = "<input type='hidden' name='dataBase' value='"+$scope.dataBase+"' />";
                            form.append(input10);
                            var input11 = "<input type='hidden' name='driverClass' value='"+$scope.driverClass+"' />";
                            form.append(input11);
                            var input12 = "<input type='hidden' name='type' value='"+$scope.typeNum+"' />";
                            form.append(input12);
                            $("body").append(form);// 将表单放置在web中
                            form.submit();// 表单提交
                        }else{
                            layer.msg("请先添加表");
                        }
                    }
                }

                //模态框打开
                $scope.tableShow = function () {
                    $scope.tableName = null;
                    $scope.checkedTable = $scope.userId;
                    $("#table_ztree").modal('show');
                    $scope.arrNames=[];
                };

                //模态框关闭
                $scope.table_modalOk = function () {
                    var length=$("#tableForm input").length;
                    for(var i=0;i<length;i++){
                        if($("#tableForm input").eq(i).is(":checked")){
                            $scope.arrNames.push(changeObj($scope.tableList[i]));
                        }
                    }
                    console.log($scope.arrNames);
                    $("#table_ztree").modal('hide');
                };

                //创建用于显示列表的对象
                function changeObj(obj){
                    var newObj = {};
                    newObj.description = obj.comment;
                    newObj.ms = obj;
                    newObj.tableName = obj.tableName;
                    newObj.pkDataType = obj.jdbcType;
                    newObj.domainName = obj.domainName;
                    return newObj;
                }

                //模态框选中
                $scope.selectedTable = function (table) {
                };

                //取消选择
                $scope.cancel = function () {
                    document.getElementById("tableForm").reset();
                    $scope.checkedTable = null;
                    $scope.tableName = null;
                    $scope.table = null;
                    $scope.userId = null;

                };

                //搜索表
                $scope.select_table = function () {

                    $http({
                        method: 'get',
                        url: $localStorage.ipAdd + '/dbConfig/tableComment',
                        params: {
                            url: localStorage.getItem("url"),
                            port: localStorage.getItem("port"),
                            user: localStorage.getItem("user"),
                            passWord: localStorage.getItem("passWord"),
                            dataBase: localStorage.getItem("dataBase"),
                            dbType: localStorage.getItem("dbType"),
                            driverClass: localStorage.getItem("driverClass"),
                            tableName: $scope.tableName
                        }
                    }).success(
                        function (res) {
                            layer.close($scope._load);
                            console.log(res);
                            $scope.tableList = res.data;
                        }
                    )
                }
            }
        ]);
})(window, angular)