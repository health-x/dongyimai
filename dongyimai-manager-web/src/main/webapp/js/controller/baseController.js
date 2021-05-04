app.controller('baseController',function ($scope) {

    //定义一个选中的id的集合
    $scope.selectedIds = [];

    //更新复选框，$event：监控对象，主要用于监控复选框的选中与被选中的值
    $scope.updateSelection = function ($event,id){
        //如果是选中状态，增加到自定义数组中
        if($event.target.checked){
            $scope.selectedIds.push(id)
        }else {
            var idx = $scope.selectedIds.indexOf(id);   //找到选中的数据的值
            //splice(要删除的数据,一次删除几个)
            $scope.selectedIds.splice(idx,1)    //把选中的那个从selectedIds数组中删除
        };
    };

    $scope.reloadList = function (){
        $scope.selectedIds = [];
        //切换页码要重新发起查询
        $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
    };

    $scope.paginationConf = {
        currentPage : 1,
        totalItems:10,	//如果没有总记录数，默认显示10条
        itemsPerPage:10,	//每页显示10条数据
        perPageOptions:[5,10,15,20],	//每页显示x条，用户可选
        onChange:function (){
            $scope.reloadList();
        }
    };

    //提取json中的某个属性值
    $scope.getJsonValue = function (jsonString,key) {
        var json = JSON.parse(jsonString);
        var value = "";
        for(var i = 0 ; i<json.length ; i++){
            if(i>0){
                value += ",";
            }
            value += json[i][key];
        }
        return value;
    }
})