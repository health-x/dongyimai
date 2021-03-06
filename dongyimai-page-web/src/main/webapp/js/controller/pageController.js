app.controller('pageController', function ($scope, $http) {

    //数量操作
    $scope.addNum = function (num) {
        $scope.num = $scope.num + num;
        if ($scope.num < 1) {
            $scope.num = 1;
        }
    }

    //记录用户选择的规则
    $scope.specificationItems = {};

    //用户选中规则
    $scope.selectSpecification = function (name, value) {
        $scope.specificationItems[name] = value;
        findSku();//读取sku
    }

    //判断某个规则是否被用户选中
    $scope.isSelected = function (name, value) {
        if ($scope.specificationItems[name] == value) {
            return true;
        } else {
            return false;
        }
    }

    //加载默认SKU
    $scope.loadSku = function () {
        $scope.sku = skuList[0];
        //深克隆
        $scope.specificationItems = JSON.parse(JSON.stringify($scope.sku.spec));
    }


    //匹配两个对象
    matchObject = function (map1, map2) {
        for (var k in map1) {
            if (map1[k] != map2[k]) {
                return false;
            }
        }
        for (var k in map2) {
            if (map2[k] != map1[k]) {
                return false;
            }
        }
        return true;
    }

    //查询SKU
    findSku = function () {
        for (var i = 0; i < skuList.length; i++) {
            if (matchObject(skuList[i].spec, $scope.specificationItems)) {
                $scope.sku = skuList[i];
                return;
            }
        }
        $scope.sku = {id: 0, title: '--------', price: 0};//如果没有匹配的
    }

    //添加商品到购物车
    $scope.addToCart = function () {
        $http.get('http://localhost:9108/cart/addGoodsToCartList.do?itemId='+$scope.sku.id + '&num=' + $scope.num,{'withCredentials':true}).success(function (response) {
            if (response.success) {
                location.href = 'http://localhost:9108/cart.html';//跳转到购物车页面
            } else {
                alert(response.message);
            }
        })
    }

})