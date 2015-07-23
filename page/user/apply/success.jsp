<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="../../../js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="../../../js/webutils.js"></script>
<script src="../../../js/layer/layer.min.js"></script>
<!--[if IE 6]><script type="text/javascript" src="../../../js/pngFix-for-IE6-min.js"></script><![endif]--><!--关于IE6下PNG图片不透明问题的解决方案详见： http://blog.csdn.net/wangxiaohui6687/article/details/7360875 -->
<link type="text/css" rel="stylesheet" href="../../../css/layout.css" />
<link type="text/css" rel="stylesheet" href="../../../css/apply_nav.css">
<style type="text/css">
* {font-family: 微软雅黑, sans-serif, Microsoft YaHei, 宋体;font-size: 14px;color: #444;}
.footer {text-align: center;	font-size: 12px;}
.header {background-image: url("../../../images/bg.gif");background-repeat: repeat-x;}
.png1{position: relative;top: 70px;left:10%;}
</style>
<title>成功</title>
</head>
<body>
	<div class="container">
		<div class="header" style="background-image: url('../../../images/bg_ui-cupertino.gif'); background-repeat: repeat-x;"><!-- 当前头部的高为140px -->
			<img src="../../../images/logo2.png" class="pngFix png1" onclick="window.location.href='../../../index.jsp'" style="cursor: pointer;" title="回首页"><!-- 362*39px -->
		</div>
		<div>
			<div class="left">&nbsp;</div>  <!--style="width:65%;"style="width: 33%"-->
			<div class="content">
				<h3 style="margin-bottom: 0;">申请成功</h3><hr>
				    <!-- 存在usertype时，显示以下信息，否则，显示提示 -->
			    		<div id="showQueryCode" style=" padding:5px ;text-align:left;">
	    					<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;    尊敬的用户您好，您的申请已经成功提交，请耐心等等处理结果。若有问题，我们会及时与您沟通，您也可以到此网站上<a href="../query/query.jsp" target="_blank">查询处理进度</a>。</p>
			  				<br>
	    					<b>下面是您的查询编号：您可以凭此编号查询您的申请处理进度和结果，请牢记。</b><br>
			  				<div style=" text-align: center;padding:40px 0">
				  				<table style="width:100%;">
					  				<tr><td class="td_side">&nbsp;&nbsp;&nbsp;&nbsp;</td><td class="td_center" style="width: 500px;">
					  					<div style="font-weight: bolder;font-size: 28px; vertical-align: middle;"><img src="../../../images/ok-big.png" style="vertical-align: bottom;">&nbsp;&nbsp;查询编号:<b style="color:red;font-size: 28px;">${sessionScope.queryCode}</b></div>
					  				</td><td class="td_side">&nbsp;&nbsp;&nbsp;&nbsp;</td>
						  		</table>
	    					</div>
	    					<div style="background-color: #EEE2D1;padding:5px 0; " >
			    					<div style="margin:10px auto;width:95%;height: auto;">&nbsp;&nbsp;
			    						<img src="../../../js/easyui/themes/icons/tip.png">
			    						<span id="tipspan" style="font-size: 12px;color:#444"><label>提示：查询编号是您查询调解处理情况的依据，请牢记！</label><label id="tip"></label></span>
			    					</div>
	    					</div>
	    					<div style="text-align: center; padding-top:100px;">
					  				<a href="../query/query.jsp" class="nav-button"><img src="../../../js/easyui/themes/icons/search.png"  style="vertical-align: middle;">&nbsp; 查看申请</a>
					  		</div>
			  		</div>
			</div>
			<div class="right">&nbsp;</div><!-- 有两个1%被margin_right占用了 -->
		</div>
	</div>
	<div class="footer"><a href="../guanyu.jsp" target="_blank" style="text-decoration: none;font-size: 12px;">关于我们</a>&nbsp;|&nbsp;
	<a href="../xieyi.jsp" target="_blank" style="text-decoration: none;font-size: 12px;">服务条款</a> 
	Copyright© 2014 湖北省标准化研究院缺陷产品管理中心. All Rights Reserved</div>
</body>
<script type="text/javascript">
$(function(){
	var queryCode="${sessionScope.queryCode}";
	if(queryCode.trim()==""){
		window.location=yp.bp()+"/page/error/404.jsp";
		return;
	}
	var hasEmail="${sessionScope.hasEmail}";
	if(hasEmail!="true"){
		$("#tip").html("为了防止您忘记查询编号，我们建议您<b><a id='setEamil' href='javascript:void(0);'>填写一个电子邮箱地址</a></b>，以供找回查询编号！");
		$("#setEamil").on('click',function(){
			layer.use(yp.bp()+'/js/layer/extend/layer.ext.js',function(){
				layer.prompt({title: '您的常用邮件地址：'}, function(email){
				   var pattern=/^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
				   if (!pattern.exec(email)){
					   layer.alert("地址不合法！",8);
				   } else{
					   var url=yp.bp()+'/user/applyAction!setEmail.action';
					    $.post(url,{"userEmail":email} , function(r) {	layer.alert(r.msg, 0); },'json');
				   }
				});
			});
		});
	}
});
</script>
</html>
