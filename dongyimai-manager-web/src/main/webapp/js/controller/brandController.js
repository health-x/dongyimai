app.controller('brandController',function ($scope,$http,$controller,brandService){
    //控制器继承
    $controller('baseController',{$scope:$scope})


    $scope.findAll = function (){
        brandService.findAll().success(function (response){
            $scope.list = response;
        });
    };

    $scope.findPage = function (page,rows){
        brandService.findPage().success(function (response){
            $scope.selectedIds = [];
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        });
    };

    $scope.save = function (){
        var serviceObject;
        if ($scope.entity.id!=null) {
            serviceObject = brandService.update($scope.entity);
        }else {
            serviceObject = brandService.add($scope.entity)
        };
        serviceObject.success(function (response) {
            if(response.success){
                $scope.reloadList();
            }else {
                alert(response.message);
            };
        });
    }

    $scope.findOne = function (id){
        brandService.findOne(id).success(function (response){
            //response = tbbrand
            $scope.entity = response;
        });
    };

    $scope.delete = function (){
        brandService.delete($scope.selectedIds).success(function (response) {
            if(response.success){
                $scope.reloadList();
            }else {
                alert(response.message);
            };
        });
    };

    $scope.searchEntity = {};

    $scope.search = function (page,rows) {
        brandService.search(page,rows,$scope.searchEntity).success(function (response){
            $scope.list = response.rows;
            $scope.paginationConf.totalItems = response.total;
        });
    };
});