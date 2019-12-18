'use strict';

/**
 * Config for the router
 */
angular.module('app')
  .run(
    [          '$rootScope', '$state', '$stateParams',
      function ($rootScope,   $state,   $stateParams) {
          $rootScope.$state = $state;
          $rootScope.$stateParams = $stateParams;        
      }
    ]
  )
  .config(
    [          '$stateProvider', '$urlRouterProvider',
      function ($stateProvider,   $urlRouterProvider) {
          
          $urlRouterProvider
              .otherwise('/app/dashboard-v1');
          $stateProvider
              .state('app', {
                  abstract: true,
                  url: '/app',
                  templateUrl: 'tpl/app.html'
              })
              .state('app.dashboard-v1', {
                  url: '/dashboard-v1',
                  templateUrl: 'tpl/app_dashboard_v1.html',
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['vendor/chart.js']);
                    }]
                  }
              })
              .state('app.dashboard-v2', {
                  url: '/dashboard-v2',
                  templateUrl: 'tpl/app_dashboard_v2.html',
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['vendor/chart.js']);
                    }]
                  }
              }).state('app.index', {
                    url: '/index/:param',
                    //params : {"param" : null},
                    templateProvider:['$stateParams','$templateRequest', '$rootScope','menuConfig','$localStorage',
                    function($stateParams,$templateRequest, $rootScope,menuConfig, $localStorage)
                    {
                    	//拆分路由标识
                        var args = $stateParams.param.split('_');
                        if(args.length>=2)
                        {
                            if($localStorage.platformRouterConfig){
                                $localStorage.platformRouterConfig['type'] = args[0];
                            }else {
                                $localStorage.platformRouterConfig = {};
                                $localStorage.platformRouterConfig['type'] = args[0];
                            }
                        }
                        $localStorage.platformRouterConfig['serviceUrl'] = menuConfig.getServiceURL();

                      return $templateRequest(menuConfig.targetHTML($stateParams.param));
                    }],
                    resolve: {
                        deps: ['$stateParams','$ocLazyLoad', 'uiLoad', 'menuConfig', '$rootScope','$localStorage',
                          function($stateParams, $ocLazyLoad, uiLoad, menuConfig,$rootScope,$localStorage){
                        	
                        	$rootScope.targetHTML = menuConfig.targetHTML($stateParams.param);

                              return $ocLazyLoad.load('ui.select').then(
                                  function(){
                                      return $ocLazyLoad.load('vendor/select.js').then(
                                          function(){
                                              return $ocLazyLoad.load(menuConfig.targetJS($stateParams.param));
                                          }
                                      )
                                  }
                              )

                        }]
                    }
                });
      }
    ]
  );