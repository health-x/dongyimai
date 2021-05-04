app.controller('searchController',function ($scope,$location,searchService){

    $scope.search = function () {
        $scope.searchMap.pageNo = JSON.parse($scope.searchMap.pageNo);
        searchService.search($scope.searchMap).success(function (response){
            $scope.resultMap = response;
            buildPageTag();
        })
    }
    $scope.searchMap = {
        'keywords':'',
        'category':'',
        'brand':'',
        'price':'',
        'pageNo':1,
        'pageSize':10,
        'sortField':'',
        'sort':'',
        'spec':{}
    }

    //添加搜索项
    $scope.addSearchMap = function (key,value){
        $scope.searchMap.pageNo = 1;
        if (key == 'category' || key == 'brand' || key == 'price'){
            $scope.searchMap[key] = value;
        } else {
            $scope.searchMap.spec[key] = value;
        }
        console.log($scope.searchMap);
        $scope.search();
    }
    //移除搜索向
    $scope.deleteSearch = function (key){
        $scope.searchMap.pageNo = 1;
        if (key =='category' || key == 'brand' || key == 'price'){
            $scope.searchMap[key] = '';
        } else {
            delete $scope.searchMap.spec[key];
        }
        $scope.search();
    }
    $scope.keywordIsBrand = function () {
        for (var i =0; i<$scope.resultMap.brandList.length;i++){
            if ($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text)>=0){
                return true;
            }
        }
        return false;
    }

    //构建分页标签
    buildPageTag = function () {
        //分页栏数组
        $scope.pageTag = [];
        //最大页码
        var maxPageNo = $scope.resultMap.totalPages;
        //开始页码
        var firstPage = 1;
        //截止页码
        var lastPage = maxPageNo;

        // 默认前后都有点
        $scope.firstPoint = true;
        $scope.lastPoint = true;

        //总页数大于5页的情况
        if($scope.resultMap.totalPages>5){
            //当前页小于等于 3
            if($scope.searchMap.pageNo<=3){
                lastPage = 5;
                //前面没点
                $scope.firstPoint = false;
            } else if ($scope.searchMap.pageNo>=lastPage-2) {//当前页大于等于最大页码数减二
                firstPage = maxPageNo - 4;
                //后面没点
                $scope.lastPoint = false;
            }else {
                firstPage = $scope.searchMap.pageNo - 2;
                lastPage = $scope.searchMap.pageNo + 2;
            }
        }else {
            $scope.firstPoint = false;
            $scope.lastPoint = false;
        }
        //循环产生页签
        for (var i=firstPage;i<=lastPage;i++){
            $scope.pageTag.push(i)
        }

    }

    //根据页码查询
    $scope.queryByPage = function (pageNo) {
        //页码验证
        if(pageNo<1||pageNo>$scope.resultMap.totalPages){
            return;
        }
        $scope.searchMap.pageNo = pageNo;
        $scope.search();
    }
    //如果当前页为第一页
    $scope.isFirstPage = function () {
        if($scope.searchMap.pageNo==1){
            return true;
        }else{
            return false;
        }
    }

    $scope.isCurrentPage = function (page) {
        if (parseInt(page)==parseInt($scope.searchMap.pageNo)){
            return true;
        }else{
            return false;
        }
    }

    //设置排序规则
    $scope.sortSearch = function (sortField,sort) {
        $scope.searchMap.sortField = sortField;
        $scope.searchMap.sort = sort;
        $scope.search();
    }

    $scope.loadKeywords = function () {
        $scope.searchMap.keywords = $location.search()['keywords'];
        $scope.search();
    }

})












