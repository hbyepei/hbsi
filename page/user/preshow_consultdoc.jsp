<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<!DOCTYPE HTML">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="../js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="../js/webutils.js"></script>
<!--[if IE 6]><script type="text/javascript" src="../js/pngFix-for-IE6-min.js"></script><![endif]--><!--关于IE6下PNG图片不透明问题的解决方案详见： http://blog.csdn.net/wangxiaohui6687/article/details/7360875 -->
<link type="text/css" rel="stylesheet" href="../css/layout.css" />
<style type="text/css">
* {font-family: 微软雅黑, sans-serif, Microsoft YaHei, 宋体;font-size: 14px;color: #444;}
.footer {text-align: center;	font-size: 12px;}
.header {background-image: url("../images/bg.gif");background-repeat: repeat-x;}
.png1{position: relative;top: 70px;left:10%;}
#consultdocinfo{border-left: 1px solid;border-right: 1px solid;border-top: 1px solid;width: 100%;border-collapse:collapse;}
#consultdocinfo tr{line-height: 28px;}
#consultdocinfo td{border-bottom: 1px solid #888;}
#consultdocinfo td:nth-child(odd){width: 10%;}
#consultdocinfo td:nth-child(even){min-width: 35%;}
.tittle_bold{font-weight: bold;text-align: left;line-height: 30px;}
</style>
<title>技术咨询书</title>
</head>
<body>
	<div class="container">
		<div class="header" style="background-image: url('../images/bg_ui-cupertino.gif'); background-repeat: repeat-x;"><!-- 当前头部的高为140px -->
			<img src="../images/logo2.png" class="pngFix png1" onclick="window.location.href='../index.jsp'" style="cursor: pointer;" title="回首页"><!-- 362*39px -->
		</div>
		<div>
			<div class="left">&nbsp;</div>  <!--style="width:65%;"style="width: 33%"-->
			<div class="content">
				<h3 style="margin-bottom: 0;text-align: center">家用汽车疑难问题技术咨询申请书</h3><hr>
				<table id="consultdocinfo">
					<tr><td colspan="4" class="tittle_bold">问题描述</td></tr>
					<tr><td  colspan="4"  id="consultdocinfo_description" style="height: 100px;vertical-align: top;"></td></tr>
					<tr><td colspan="4" class="tittle_bold">车辆信息</td></tr>
					<tr><td>车架号:</td><td colspan="3"  id="consultdocinfo_car_carriage"></td></tr>
					<tr><td>购车时间:</td><td colspan="3"  id="consultdocinfo_car_dealtime"></td></tr>
					<tr><td colspan="4" class="tittle_bold">专家信息</td></tr>
					<tr><td>专家姓名:</td><td  id="consultdocinfo_expert_name_1"></td><td>证书编号:</td><td  id="consultdocinfo_expert_letterid_1"></td></tr>
					<tr><td>专家姓名:</td><td  id="consultdocinfo_expert_name_2"></td><td>证书编号:</td><td  id="consultdocinfo_expert_letterid_2"></td></tr>
					<tr><td>专家姓名:</td><td  id="consultdocinfo_expert_name_3"></td><td>证书编号:</td><td  id="consultdocinfo_expert_letterid_3"></td></tr>
					<tr><td>专家姓名:</td><td  id="consultdocinfo_expert_name_4"></td><td>证书编号:</td><td  id="consultdocinfo_expert_letterid_4"></td></tr>
					<tr><td>专家姓名:</td><td  id="consultdocinfo_expert_name_5"></td><td>证书编号:</td><td  id="consultdocinfo_expert_letterid_5"></td></tr>
					<tr><td colspan="4">咨询人愿意支付专家技术咨询费，交通、食宿等相关费用按实际发生数额支付。</td></tr>
					<tr><td colspan="4">咨询人理解此次专家技术咨询不具备法律上的强制力，也不具备其他的证明力。</td></tr>
					<tr><td colspan="4">特申请专家技术咨询。</td></tr>
					<tr><td class="tittle_bold">咨询人（签/章）:</td><td colspan="3"></td></tr>
					<tr><td class="tittle_bold">咨询时间:</td><td colspan="3"  id="consultdocinfo_time"></td></tr>
				</table>
			</div>
			<div class="right">&nbsp;</div><!-- 有两个1%被margin_right占用了 -->
		</div>
	</div>
	<div class="footer"><a href="../page/user/guanyu.jsp" target="_blank" style="text-decoration: none;font-size: 12px;">关于我们</a>&nbsp;|&nbsp;
	<a href="../page/user/xieyi.jsp" target="_blank" style="text-decoration: none;font-size: 12px;">服务条款</a> 
	Copyright© 2014 湖北省标准化研究院缺陷产品管理中心. All Rights Reserved</div>
</body>
<script type="text/javascript">
$(function(){
	var task=<%=null==session.getAttribute("task")?null:(String)session.getAttribute("task")%>
	//var datePattern="yyyy年M月d日-h:mm";
	if(!task){
		window.location=yp.bp()+"/page/error/404.jsp";
	}else{
		$('#consultdocinfo_description').html(task.description);
		$('#consultdocinfo_car_carriage').html(task.car.carriage);
		$('#consultdocinfo_car_dealtime').html(task.car.dealtime);
		$('#consultdocinfo_time').html('');
		var name="#consultdocinfo_expert_name_";
		var letterid="#consultdocinfo_expert_letterid_";
		var experts=task.experts;
		if(experts&&experts.length>0){
			for(var i=0;i<experts.length;i++){
				var e=experts[i];
				$(name+(i+1)).html(e.name);
				$(letterid+(i+1)).html(e.letterid);
			}
		}
	}
});
</script>
</html>
