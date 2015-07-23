<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="../../../js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="../../../js/webutils.js"></script>
<script src="../../../js/layer/layer.min.js"></script>
<script type="text/javascript" src="../../../js/Validform_v5.3.2_min.js"></script>
<!--[if IE 6]><script type="text/javascript" src="../../../js/pngFix-for-IE6-min.js"></script><![endif]-->
<!--关于IE6下PNG图片不透明问题的解决方案详见： http://blog.csdn.net/wangxiaohui6687/article/details/7360875 -->
<link type="text/css" rel="stylesheet" href="../../../css/layout.css" />
<link type="text/css" rel="stylesheet" href="../../../css/apply_nav.css">
<link type="text/css" rel="stylesheet" href="../../../css/Validform_style.css" media="all" />
<style type="text/css">
* {
	font-family: 微软雅黑, sans-serif, Microsoft YaHei, 宋体;
	font-size: 14px;
	color: #444;
}

.footer {
	text-align: center;
	font-size: 12px;
}

.header {
	background-image: url("../../../images/bg.gif");
	background-repeat: repeat-x;
}

.png1 {
	position: relative;
	top: 70px;
	left: 10%;
}

form li {
	padding-bottom: 10px;
	list-style: none;
}

form li input {
	width: 180px;
}
/* .Validform_checktip{margin-left:10px;} */
.label {
	display: inline-block;
	width: 120px;
	text-align: right;
	font-size: 16px;
	font-weight: bold;
}

.need {
	color: red;
	font-weight: bolder;
}
</style>
<title>查询</title>
</head>
<body>
	<div class="container">
		<div class="header" style="background-image: url('../../../images/bg_ui-cupertino.gif'); background-repeat: repeat-x;">
			<!-- 当前头部的高为140px -->
			<img src="../../../images/logo2.png" class="pngFix png1" onclick="window.location.href='../../../index.jsp'" style="cursor: pointer;" title="回首页">
			<!-- 362*39px -->
		</div>
		<div>
			<div class="left">&nbsp;</div>
			<!--style="width:65%;"style="width: 33%"-->
			<div class="content">
				<h3 style="margin-bottom: 0;">查询</h3>
				<hr>
				<div style=" text-align: center;padding:40px 0">
					<table style="width:100%;">
						<tr>
							<td class="td_side">&nbsp;&nbsp;&nbsp;&nbsp;</td>
							<td class="td_center" style="width: 500px;">
								<form id="query_form">
									<ul>
										<li><label class="label"><span class="need">*</span> 查询编号:</label> <input id="queryCode" type="text" value="${sessionScope.queryCode }" name="queryCode" datatype="n8-8" class="inputxt" placeholder="输入查询编号" nullmsg="请输入8位查询编号" errormsg="查询编号有误！" /></li>
									</ul>
								</form>
							</td>
							<td class="td_side">&nbsp;&nbsp;&nbsp;&nbsp;</td>
					</table>
				</div>
				<div style="text-align: center; padding-top:100px;">
					<a href="javascript:void(0);" class="nav-button" id="submitBtn"><img src="../../../js/easyui/themes/icons/search.png" style="vertical-align: middle;">&nbsp; 查&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;询</a>
				</div>
			</div>
		</div>
		<div class="right">&nbsp;</div>
		<!-- 有两个1%被margin_right占用了 -->
	</div>
	<div class="footer">
		<a href="../guanyu.jsp" target="_blank" style="text-decoration: none;font-size: 12px;">关于我们</a>&nbsp;|&nbsp; <a href="../xieyi.jsp" target="_blank" style="text-decoration: none;font-size: 12px;">服务条款</a> Copyright© 2014 湖北省标准化研究院缺陷产品管理中心. All Rights Reserved
	</div>
</body>
<script type="text/javascript">
	$(function() {
		var valid_form = $("#query_form").Validform({
			tiptype : 3,
			label : ".label",
			showAllError : true,
		}); //就这一行代码！;
		$.Tipmsg.r = "";
		$("#submitBtn").click(function() {
			var pass = valid_form.check();
			if (pass) {//如果通过
				var wait = layer.load(0, 1);
				var url = yp.bp() + '/user/applyAction!query.action';
				$.post(url, {
					"queryCode" : $("#queryCode").val()
				}, function(r) {
					layer.close(wait);
					if (r.ok) {
						//location.replace(yp.bp()+'/page/sys/'+result.object+'/'+result.object+'.jsp');
						window.location = yp.bp() + "/page/user/query/taskList.jsp";

					} else {
						layer.alert(r.msg, 8);
					}
				}, 'json');
			} else {
				return false;
			}
		});
	});
</script>
</html>
