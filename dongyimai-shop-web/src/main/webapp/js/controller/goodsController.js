 //InnoDB free: 5120 kB控制层 
app.controller('goodsController' ,function($scope,$controller,$location,goodsService,typeTemplateService,uploadService,itemCatService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(){
		var id = $location.search()['id'];
		if (id==null){
			return ;
		}
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;
				//向富文本编辑器中添加值
				editor.html($scope.entity.goodsDesc.introduction)
				$scope.entity.goodsDesc.itemImages = JSON.parse($scope.entity.goodsDesc.itemImages)
				$scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.entity.goodsDesc.customAttributeItems);
				$scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);
				//SKU列表转换
				for (var i =0;i<$scope.entity.itemList.length;i++){
					$scope.entity.itemList[i].spec = JSON.parse($scope.entity.itemList[i].spec);
				}
			}
		);				
	};

	//根据规格和选项名称返回是否被选中
	//http://localhost:9102/admin/goods_edit.html#?id=149187842868051
	$scope.checkAttr = function(specName,optionName){
		var items = $scope.entity.goodsDesc.specificationItems;
		var object = $scope.searchByKey(items,'attributeName',specName)
		if (object==null){
			return false
		}else {
			if (object.attributeValue.indexOf(optionName)>=0){
				return true;
			}else {
				return false;
			}
		}
	}

	
	//保存 
	$scope.save=function(){
		//提取富文本编辑器的值
		$scope.entity.goodsDesc.introduction = editor.html();
		var serviceObject;//服务层对象  				
		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					alert(response.message)
		        	$scope.entity={};
					editor.html("");
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	$scope.add = function () {

		$scope.entity.goodsDesc.introduction = editor.html();
		goodsService.add($scope.entity).success(function (response) {
			if(response.success){
				alert("保存成功！");
				//$scope.entity = {};
				$scope.entity = {
					goodsDesc:{
						itemImages:[],
						specificationItems:[],
						customAttributeItems:[]
					}
				}
				editor.html("");
			}else{
				alert(response.message);
			}
		})
	}

	//上传图片
	$scope.upload = function () {
		uploadService.upload().success(function (response) {
			if(response.success) {
				$scope.imageEntity.url = response.message;
			}else{
				alert(response.message)
			}
		}).error(function () {
			alert("上传出错！")
		})
	}

	$scope.entity = {
		goods:{},
		goodsDesc:{
			//都是goodsDesc表中的字段
			itemImages:[],
			specificationItems:[]
		}
	}
	//添加图片列表
	$scope.addImages = function () {
		$scope.entity.goodsDesc.itemImages.push($scope.imageEntity)
	}
	//添加图片列表
	$scope.deleImages = function (index) {
		$scope.entity.goodsDesc.itemImages.splice(index,1)
	}
	//读取一级分类
	$scope.selectItemCat1List = function () {
		itemCatService.findParentId(0).success(function (response) {
			$scope.itemcat1List = response;
		})
	}
	//读取二级分类(oldValue:选中的一级分类的id，newValue：选中的二级分类的id)
	$scope.$watch('entity.goods.category1Id',function (newValue) {
		console.log("一级："+newValue)
		// console.log(oldValue+"======"+newValue)
		//判断一级分类如果有选择具体的值，再去获取二级分类
		if(newValue){
			itemCatService.findParentId(newValue).success(function (response) {
				$scope.itemcat2List = response;
			})
		}
	})

	//读取三级分类
	$scope.$watch('entity.goods.category2Id',function (newValue) {
		console.log("二级："+newValue)
		// console.log(oldValue+"======"+newValue)
		//判断一级分类如果有选择具体的值，再去获取二级分类
		if(newValue){
			itemCatService.findParentId(newValue).success(function (response) {
				$scope.itemcat3List = response;
			})
		}
	})

	//读取模板id
	$scope.$watch('entity.goods.category3Id',function (newValue) {
		console.log("三级："+newValue)
		if(newValue){
			itemCatService.findOne(newValue).success(function (response) {
				$scope.entity.goods.typeTemplateId = response.typeId;
			})
		}
	})

	$scope.$watch('entity.goods.typeTemplateId',function (newValue) {
		console.log("模板ID："+newValue)
		if(newValue){
			typeTemplateService.findOne(newValue).success(function (response) {
				$scope.typeTemplate = response;
				$scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);
				if ($location.search()['id']==null){
					$scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems)
				}
			})
			//查询规格列表
			goodsService.findSpecList(newValue).success(function (response) {
				$scope.specList = response;
			})
		}
	})

	//
	$scope.checkSpec = function ($event,name,value) {
		/*console.log($scope.entity.goodsDesc.specificationItems)
		console.log("name:"+name)
		console.log("value:"+value)*/
		var object = $scope.searchByKey($scope.entity.goodsDesc.specificationItems,'attributeName',name);
		if (object!=null) {
			if($event.target.checked){
				object.attributeValue.push(value);
			}else {
				//取消勾选
				object.attributeValue.splice(object.attributeValue.indexOf(value),1);
				//勾选的选项都取消
				if(object.attributeValue.length == 0){
					$scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(object),1);
				}
			}
		}else {
			$scope.entity.goodsDesc.specificationItems.push({'attributeName':name,'attributeValue':[value]})
		}
	}
	//创建SKU列表(对象)
	$scope.createItemList = function () {
		$scope.entity.itemList = [{
			spec:{},
			price:0,
			num:999,
			status:'0',
			isDefault:'0'
		}];
		var items = $scope.entity.goodsDesc.specificationItems;
		for (var i = 0; i < items.length; i++) {
			$scope.entity.itemList = addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
		}
	}

	addColumn = function (list,columnName,columnValues) {
		//创建新集合
		var newList = [];
		for (var i = 0; i < list.length; i++) {
			var oldRow = list[i];
			for (var j = 0; j < columnValues.length; j++) {
				//前端深克隆
				var newRow = JSON.parse(JSON.stringify(oldRow));
				newRow.spec[columnName] = columnValues[j];
				newList.push(newRow);
			}
		}
		return newList;
	}

	$scope.categorys = [];
	$scope.status = ['未审核','已审核','审核未通过','关闭'];
	$scope.findItemCatList = function () {
		itemCatService.findAll().success(function (response) {
			for (var i = 0; i < response.length; i++) {
				$scope.categorys[response[i].id] = response[i].name;
			}
		})
	}
});	