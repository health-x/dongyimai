 //InnoDB free: 5120 kB控制层 
app.controller('contentController' ,function($scope,$controller,uploadService,contentCategoryService,contentService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		contentService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		contentService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		contentService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=contentService.update( $scope.entity ); //修改  
		}else{
			serviceObject=contentService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		contentService.dele($scope.selectedIds).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectedIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		contentService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	$scope.uploadFile = function () {
		uploadService.upload().success(function (response) {
			if (response.success){
				$scope.entity.pic = response.message;
			}else {
				alert(response.message)
			}
		})
	}

	$scope.state = ['无效','有效']

	$scope.findContentCategoryList = function () {
		contentCategoryService.findAll().success(function (response) {
			$scope.categoryList = response;
		})
	}

	$scope.updateStatus = function (status) {
		contentService.updateStatus($scope.selectedIds,status).success(function (response) {
			if(response.success){
				$scope.reloadList();//刷新列表
				$scope.selectedIds=[];
			}else {
				alert(response.message);
			}
		})
	}
});	