//InnoDB free: 5120 kB服务层
app.service('contentService',function($http){

	this.findContentById = function (categoryId) {
		return $http.get('../content/findContentById.do?categoryId='+categoryId)
	}
});