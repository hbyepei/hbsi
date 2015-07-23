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
#applydocinfo{border-left: 1px solid;border-right: 1px solid;border-top: 1px solid;width: 100%;border-collapse:collapse;}
#applydocinfo tr{line-height: 28px;}
#applydocinfo td{border-bottom: 1px solid #888;}
#applydocinfo td:nth-child(odd){width: 15%;}
#applydocinfo td:nth-child(even){min-width: 35%;}
.tittle_bold{font-weight: bold;text-align: left;line-height: 30px;}
</style>
<title>预览</title>
</head>
<body>
	<div class="container">
		<div class="header" style="background-image: url('../images/bg_ui-cupertino.gif'); background-repeat: repeat-x;"><!-- 当前头部的高为140px -->
			<img src="../images/logo2.png" class="pngFix png1" onclick="window.location.href='../index.jsp'" style="cursor: pointer;" title="回首页"><!-- 362*39px -->
		</div>
		<div>
			<div class="left">&nbsp;</div>  <!--style="width:65%;"style="width: 33%"-->
			<div class="content">
				<h3 style="margin-bottom: 0;text-align: center">家用汽车产品三包责任争议第三方调解申请书</h3><hr>
				<table id="applydocinfo">
					<tr><td class="tittle_bold">调解申请日期:</td><td id="applydocinfo_applytime"></td><td>编号:</td><td  id="applydocinfo_caseid"></td></tr>
					<tr><td colspan="4" class="tittle_bold">申请人信息</td></tr>
					<tr><td>姓名(组织名称/法人):</td><td  id="applydocinfo_name1"></td><td>身份证号(机构代码号):</td><td  id="applydocinfo_code1"></td></tr>
					<tr><td>联系人:</td><td  id="applydocinfo_agent1"></td><td>联系电话:</td><td  id="applydocinfo_phone1"></td></tr>
					<tr><td>通信地址:</td><td colspan="3"  id="applydocinfo_address1"></td></tr>
					<tr><td colspan="4" class="tittle_bold">被申请人信息</td></tr>
					<tr><td>姓名(组织名称/法人):</td><td  id="applydocinfo_name2"></td><td>身份证号(机构代码号):</td><td  id="applydocinfo_code2"></td></tr>
					<tr><td>联系人:</td><td  id="applydocinfo_agent2"></td><td>联系电话:</td><td  id="applydocinfo_phone2"></td></tr>
					<tr><td>通信地址:</td><td colspan="3"  id="applydocinfo_address2"></td></tr>
					<tr><td colspan="4" class="tittle_bold">三包责任争议简要情况</td></tr>
					<tr><td  colspan="4"  id="applydocinfo_description" style="height: 100px;vertical-align: top;"></td></tr>
					<tr><td colspan="4" class="tittle_bold">申请事项</td></tr>
					<tr><td  colspan="4"  id="applydocinfo_matter" style="height: 100px;vertical-align: top;"></td></tr>
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
	var datePattern="yyyy年M月d日-h:mm";
	if(!task){
		window.location=yp.bp()+"/page/error/404.jsp";
	}else{
		var name1,name2,code1,code2,agent1,agent2,phone1,phone2,address1,address2;
		var c=task.consumer;
		var s=task.saler;
		if(task.applytype=='Consumer'){
			name1=c.name;
			code1=c.idcard;
			agent1=c.agent;
			phone1=c.phone;
			address1=c.address;
			name2=s.name;
			code2=s.code;
			agent2=s.agent;
			phone2=s.phone;
			address2=s.address;
		}else{
			name1=s.name;
			code1=s.code;
			agent1=s.agent;
			phone1=s.phone;
			address1=s.address;
			name2=c.name;
			code2=c.idcard;
			agent2=c.agent;
			phone2=c.phone;
			address2=c.address;
		}
		$('#applydocinfo_applytime').html(getFormatDateByLong(task.applytime, datePattern));
		$('#applydocinfo_caseid').html(task.caseid);
		$('#applydocinfo_name1').html(name1);
		$('#applydocinfo_code1').html(code1);
		$('#applydocinfo_agent1').html(agent1);
		$('#applydocinfo_phone1').html(phone1);
		$('#applydocinfo_address1').html(address1);
		$('#applydocinfo_name2').html(name2);
		$('#applydocinfo_code2').html(code2);
		$('#applydocinfo_agent2').html(agent2);
		$('#applydocinfo_phone2').html(phone2);
		$('#applydocinfo_address2').html(address2);
		$('#applydocinfo_description').html(task.description);
		$('#applydocinfo_matter').html(task.matter);
	}
});
</script>
</html>
