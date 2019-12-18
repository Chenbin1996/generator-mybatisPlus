(function(window, angular) {
	'use strict';

	angular.module("app").controller(
			'regionManageController',
			[
					'$localStorage',
					'$scope',
					'$location',
					'$log',
					'$q',
					'$rootScope',
					'$window',
					'routeService',
					'$http',
					function regionManageController($localStorage, $scope,
							$location, $log, $q, $rootScope, $window,
							routeService, $http) {
						
						//初始化地图对象，加载地图
					    var district, map = new AMap.Map("container", {
					        resizeEnable: true,
					        center: [116.397428, 39.90923],//地图中心点
					        zoom: 10 //地图显示的缩放级别
					    });
					    function showCity(cityName) {
					        //加载行政区划插件
					        AMap.service('AMap.DistrictSearch', function() {
					            var opts = {
					                subdistrict: 1,   //返回下一级行政区
					                extensions: 'all',  //返回行政区边界坐标组等具体信息
					                level: 'city'  //查询行政级别为 市
					            };
					            //实例化DistrictSearch
					            district = new AMap.DistrictSearch(opts);
					            district.setLevel('district');
					            //行政区查询
					            district.search(cityName, function(status, result) {
					                var bounds = result.districtList[0].boundaries;
					                var polygons = [];
					                if (bounds) {
					                    for (var i = 0, l = bounds.length; i < l; i++) {
					                        //生成行政区划polygon
					                        var polygon = new AMap.Polygon({
					                        	 map: map,
					                             strokeWeight: 1,
					                             strokeColor: '#CC66CC',
					                             fillColor: '#CCF3FF',
					                             fillOpacity: 0.5,
					                             path: bounds[i]
					                        });
					                        polygons.push(polygon);
					                    }
//					                    map.setFitView();//地图自适应
					                    map.setCity(cityName);
					                }
					            });
					        });
					    }
						
						//初始化异步加载全国行政区域
						$scope.init = function(){
							var serviceUrl = $localStorage.platformRouterConfig['serviceUrl'];
							// zTree 参数配置
							 var setting = {
							  view: {
								   showIcon: true,//是否显示节点的图标
								   selectedMulti: false //设置是否允许同时选中多个节点。默认值: true。
							  },
							  data: {
								  simpleData: {
								    enable: true, //是否采用简单数据模式 (Array)。默认值：false
								    idKey: "id", //节点数据中保存唯一标识的属性名称。
								    pIdKey: "vicechairman", //节点数据中保存其父节点唯一标识的属性名称。
								    rootPid: "10000000000000" //用于修正根节点父节点数据，即 pIdKey 指定的属性值。
								  }
							  },
							  callback: {
								  // 用于捕获节点被点击的事件回调函数
								  onClick: function(event, treeId, treeNode, clickFlag) {
									  var treeObj = $.fn.zTree.getZTreeObj(treeId); //根据 treeId 获取 zTree 对象
								      // 这里判断节点被点击时，如果有已经加载下级节点，则不用请求服务器
								      if((treeNode.children == null || treeNode.children == "undefined")){
								      // 请求服务器，获得点击地区的下级地区
								      $.ajax({
									       type: "post",
									       async: false,
									       url : serviceUrl + "/rm/loadGlobalRegion",
									       data:{
									    	   vicechairman:treeNode.id
									       },
									       dataType:"json",
									       success: function(res){
										        if(res.data != null && res.data.length != 0){
										         //添加新节点
										         var newNodes = treeObj.addNodes(treeNode, res.data);
										       }
									       },
									       error:function(event, XMLHttpRequest, ajaxOptions, thrownError){
										        toastr.error("请求失败!");
									       }
									      });
								    }else{
								    	// 展开当前节点
								    	treeObj.expandNode(treeNode);
								    }
								      
								      map.clearMap();
								      showCity(treeNode.name);
								   }
								 }
							  	};
							 
								$.ajax({
		                            type : "post",
		                            url : serviceUrl + "/rm/loadGlobalRegion",
		                            async : false,
		                            data: {vicechairman:"0"},
		                        }).done(function(res){
		                			if(res.resCode == 1)
			    					{
		                				 var zNodes =[
		    						       {
		    						         name: "全国",
//		    						         icon:"vendor/zTree_v3/css/zTreeStyle/img/diy/10.png",
		    						         open:true,
		    						         children:res.data
		    						       }
		    						     ];
		    						    $.fn.zTree.init($("#regionTree"), setting, zNodes);
			    					}else
			    					{
			    						$.fn.zTree.init($("#regionTree"), setting, null);
			    					}
		                      	      
		                        }).error(function(errData){
		                        	
		                        });
	                        
	                        };
	                        
	                        
					}]);
})(window, angular);
