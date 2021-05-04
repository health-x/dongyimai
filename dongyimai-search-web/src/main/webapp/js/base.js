/**
 * 声明模块
 * 	第一个参数是模块的名称
 * 	第二个参数是依赖列表（可以被注入到模块中的对象列表）
 */
var app = angular.module('dongyimai',[]);

/*$sce服务写成过滤器*/
app.filter('trustHtml', ['$sce',function($sce){
    return function(data){
        return $sce.trustAsHtml(data);
    }
}]);