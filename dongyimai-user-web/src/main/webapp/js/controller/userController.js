 //用户表控制层 
app.controller('userController',function($scope,$controller,userService){
	
	//注册
	$scope.register = function () {
		//判断用户输入的两次密码是否一致
		if($scope.entity.password!=$scope.password){
			alert("两次密码输入不一致，请重新输入！")
			return;
		}
		userService.add($scope.entity,$scope.code).success(function (response) {
			alert(response.message)
		})
	}

	//发送验证码
	$scope.sendCode = function () {
		if ($scope.entity.phone==null){
			alert("请输入手机号！");
			return;
		}
		userService.sendCode($scope.entity.phone).success(function (response) {
			alert(response.message)
		})
	}

	$scope.alert = function () {
		alert("alert")
	}

	$scope.findUsername = function () {
		userService.findUsername().success(function (response) {
			$scope.loginName = response.loginName;
		})
	}
});	