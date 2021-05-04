 //InnoDB free: 5120 kB控制层 
app.controller('contentController' ,function($scope,$controller,contentService){
	
	$controller('baseController',{$scope:$scope});//继承

	$scope.contentList = [];
    $scope.findContentById = function (categoryId) {
		contentService.findContentById(categoryId).success(function (response) {
			$scope.contentList[categoryId] = response;
		})
	}

	//跳转搜索
	$scope.search = function () {
		location.href = "http://localhost:9104/search.html#?keywords="+$scope.keywords;
	}
});	