 //商品类目控制层 
app.controller('itemCatController' ,function($scope,$controller,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			$scope.entity.parentId = $scope.parentId;
			serviceObject=itemCatService.add( $scope.entity  );//增加 
		}
		serviceObject.success(
			function(response){
				if(response.success){
					alert("添加成功")
					//重新查询
					$scope.reloadList();
		        	//$scope.findByParentId($scope.parentId);//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	

	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectedIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectedIds=[];
				}						
			}		
		);				
	}

	//记住上级id
	$scope.parentId = 0;

	$scope.search=function(page,rows){
		itemCatService.search($scope.parentId,page,rows).success(function (response) {
			$scope.list=response.rows;
			$scope.paginationConf.totalItems=response.total;//更新总记录数
		})
	}


	//默认等级为 1
	$scope.level = 1;
	//设置等级方法
	$scope.setLevel = function (value) {
		$scope.level = value;
	}

	//读取列表方法
	$scope.selectList = function (p_entity) {
		//如果等级为 1
		if($scope.level == 1){
			$scope.entity2 = null;
			$scope.entity3 = null;
		}
		//如果等级为 2
		if($scope.level == 2){
			$scope.entity2 = p_entity;
			$scope.entity3 = null;
			$scope.parentId = $scope.entity2.id;

		}
		//如果等级为 3
		if($scope.level == 3){
			$scope.entity3 = p_entity;
			$scope.parentId = $scope.entity3.id;
		}
		$scope.search(p_entity.id,$scope.paginationConf.itemsPerPage)
	}

	$scope.TypeList = {
		data:[]
	}
	$scope.findTypeTemplateList = function () {
		typeTemplateService.findTypeTemplateList().success(function (response) {
			$scope.TypeList = {data:response}
		})
	}
});	