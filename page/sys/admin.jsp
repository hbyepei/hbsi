<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="include.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>后台管理</title>
</head>
<body id="mainpage" class="easyui-layout" data-options="fit:'true'" >
	<div id="headerbg" data-options="region:'north'" 
		style="height:100px; overflow:'hidden';background-image: url('../../images/<%=headerbg%>.gif'); background-repeat: repeat-x;">
		<!-- ==========================问候语============================= -->
		<div id="headerWelcome" style="float: right;margin-right:100px;margin-top:10px;color:#<%=color_white%>;font-weight: bold;font-size:14px;vertical-align:middle;">
		<img src="<%=path %>${user.info}" width="25px" height="25px" alt="" style="vertical-align:middle;">&nbsp;欢迎您，<a title="个人信息" href="javascript:void(0);" onclick="$('#userInfo').window('open')" 
		style="text-decoration: none;color: #fff;font-size: 15px;font-weight: bold;">
		${empty(user.username)?user.name:user.username}</a>!</div>
		<!-- ==========================logo============================= -->
		<div style="margin-left: 50px; margin-top: 10px;">
			<img id="logo" src="../../images/<%=headerLogo%>.png" style="height: 80px">
			<table style="display: inline; height: 70px; font-size: 30px;">
				<tr><td id="headerTitle" style="padding-top:20px;color:#<%=color_black%> ">缺陷产品管理中心<span style="font-size: 16px;">——家用汽车三包责任争议第三方调解平台</span></td>	</tr>
			</table>
		</div>
		<!-- ==========================右上角菜单============================= -->
		
			<jsp:include page="toprightMenu.jsp"/>
	</div>
	<!-- ==========================左侧导航============================= -->
	<div data-options="region:'west',split:true,title:'管理菜单',border:true,iconCls:'icon-yp_menu'" style="width:240px;overflow:hidden; ">
		<jsp:include page="leftMenu.jsp"/>
	</div>
	<!-- ==========================管理中心============================= -->
	<div data-options="region:'center',split:true" style="overflow: hidden;">
		<div  id="mainTabs">
			<div title="首页" data-options="iconCls:'icon-yp_home'" >
				<iframe id="welcomePage" src="../../page/sys/welcome.jsp"  style="border: 0; width: 100%; height: 99%;" ></iframe>
			</div>
		</div>
	</div>
	<!-- ==========================右侧通知============================= -->
	<div data-options="region:'east',split:true,border:false,collapsed:true,href:'east.jsp',title:'信息'" style="width:200px;overflow: hidden;"></div>
	<!-- ==========================底部权限============================= -->
	<div data-options="region:'south'" style="height:40px;">
		<div style="text-align: center;padding-top: 15px">Copyright &copy; 2014 湖北省标准化研究院缺陷产品管理中心. All Rights Reserved.</div>
	</div>
<!-- ==========================对话框窗体============================= -->
	<div id="passwordDialog" title="修改密码" style="display: none;background-color: #EFF4FB">
		<form method="post" class="form" onsubmit="return false;">
			<table class="table">
				<tr>
					<th style="font-size: 13px;width: 70px;" >新密码:</th>
					<td><input id="pwd" name="data.pwd" type="password" class="easyui-validatebox" data-options="required:true" /></td>
				</tr>
				<tr>
					<th style="font-size: 13px;width: 70px;">重复密码:</th>
					<td><input type="password" class="easyui-validatebox" data-options="required:true,validType:'eqPwd[\'#pwd\']'" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>