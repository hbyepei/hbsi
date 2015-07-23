<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script src="js/jquery-1.8.3.min.js"></script>
<script src="js/layer/layer.min.js"></script>
<script type="text/javascript" src="js/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="js/webutils.js"></script>
<!--[if IE 6]><script type="text/javascript" src="js/pngFix-for-IE6-min.js"></script><![endif]-->
<!--关于IE6下PNG图片不透明问题的解决方案详见： http://blog.csdn.net/wangxiaohui6687/article/details/7360875 -->
<link type="text/css" rel="stylesheet" href="css/layout.css" />
<style type="text/css">
* {font-family: 微软雅黑, sans-serif, Microsoft YaHei, 宋体;	font-size: 14px;	color: #444;}
.footer {	text-align: center;	font-size: 12px;}
.header {background-image: url("images/bg_ui-cupertino.gif");background-repeat: repeat-x;}
.png1 {position: relative;top: 70px;	left: 10%;}
#login{display: block;float: right;text-decoration: none;padding: 5px;vertical-align: middle;}
#login img{vertical-align: middle;}
#login:hover {font-weight: bold;border-bottom: 1px solid #CBE0F4; border-right:  1px solid #CBE0F4;background:#65B7FF;color:white;}
.btn{display: inline-block;text-decoration: none;padding: 5px;vertical-align: middle;font-size: 24px;}
.btn img{vertical-align: middle;}
.btn:hover {font-weight: bold;border-bottom: 1px solid #CBE0F4; border-right:  1px solid #CBE0F4;background:#DBF5FF;color:#888}
</style>
<title>湖北省标准化研究院-缺陷产品管理中心</title>
</head>
<body>
	<div class="container">
		<div class="header">
			<!-- 当前头部的高为140px -->
			<img src="images/logo2.png" class="pngFix png1">
			<!-- 362*39px -->
		</div>
		<div>
			<!--<div class="left">左侧导航</div>  -->
			<div class="content" style="width:65%;">
				<table id="contentarea" style="zoom:1; padding-top:200px;padding-left: 10%;">
					<tr>
						<td><img src="images/main_area.png" class="pngFix"></td>
					</tr>
				</table>
	</div>
			<div class="right" style="width: 33%;">
				<!-- 有两个1%被margin_right占用了 -->
				<!-- 子元素有浮动元素时，父元素高度塌陷了，为0了，为了防止这种情况，可以在父元素中加上“overflow:hidden;” -->
				<div style="overflow:hidden;">
					<a href="javascript:void(0);"  id="login" >
					<img src="images/icons/yp_sysuser.png" class="pngFix">&nbsp; 管理员/专家登录</a>
				</div>
				<div id="rightcontainer" style="border-left: 1px solid #D4E2F3;padding-left:10px;">
					<div>
						<span style="font-size: 18px;">选择服务：</span>
					</div>
					<div id="rightarea" style="zoom:1;padding-left: 50px;">
						<a id="applybtn" class="pngFix btn" href="page/user/apply/entityChoose.jsp" style="margin-right:50px; "><img src="images/icons/apply.png" class="pngFix">&nbsp; 调解申请</a>
						<a id="querybtn" class="pngFix btn" href="page/user/query/query.jsp"><img src="images/icons/query.png" class="pngFix">&nbsp; 调解查询</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="footer">关于我们&nbsp;|&nbsp;服务条款 Copyright© 2014 湖北省标准化研究院缺陷产品管理中心. All Rights Reserved</div>
	<div id="login_frame" style="display: none;">
		<form method="post" style="margin: 10px 0 0 20px;" >
			<table cellpadding="5" style="font-size: 14px;">
				<tr>
					<td>用户帐号：</td>
					<td><label class="label"></label><input type="text" id="username" name="name" 
					datatype="s4-15" 
					errormsg="非法用户名！"
					nullmsg="用户名不能为空！"
					sucmsg=" "
					tip="填写用户名"
					placeholder="填写用户名"
					 /></td>
				</tr>
				<tr>
					<td>登录密码：</td>
					<td><label class="label"></label><input type="password" id="password" name="password" 
					datatype="*" 
					sucmsg=" "
					nullmsg="密码不能为空！"
					placeholder="填写密码"
					/></td>
				</tr>
				<tr>
					<td>用户类型：</td>
					<td><input type="radio" id="adminRadio" name="usertype" value="Admin" checked="checked"/><label for="adminRadio">管理员</label>&nbsp;&nbsp;&nbsp;
					<input type="radio" id="expertRadio" name="usertype" value="Expert" /><label for="expertRadio">专家</label>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
<script type="text/javascript">
	window.onload = function() {
		var bro=yp.browser();
		if(bro&&bro.isIE){
			var version=bro.innerCode;
			if(version<10){
				alert("您当前使用的是IE内核的浏览器，但版本过低，建议你升级你的IE版本或者改换其它浏览器(推荐使用“谷歌浏览器”)。\n当前浏览器版本信息:\n名称 :"+bro.appName+";\n版本号:"+bro.version+";\n内核:"+bro.inner+";\n内核版本:"+bro.innerCode+"\n建议的内核版本:10.0+");
			}
		}
		//alert(");
		zoom("contentarea");
		zoom("rightarea");
	};
	$(function(){
		$("#rightcontainer").height(screen.height*0.5);
		var h=$("#rightcontainer").height();
		$("#rightarea").css({"position":"relative","top":h/3});
		$('#login').on('click', function(){
			var form=$("#login_frame form");
			var validform=form.Validform({
				tiptype:3,
				label:".label",
				showAllError:true,
				postonce:true//防止重复提交 
			});//初始化验证组件
			//显示弹出层
			var loginFrame = $.layer({
			    type : 1,
			    title :  ['用户登录', 'background:#1eee;'],
			    fix : false,
			    shade: [0.2, '#000'],
			    area : ['400px','200px'],
			    page : {dom : '#login_frame'},
			    btns: 2,
			    btn: ['登录', '取消'],
			    shift: 'top',
			    bgcolor: '#fff',
			    yes: function(index){
			    	if(validform.check()){
			    		var layerindex=layer.load(3, 1);
			  			$.post(yp.bp()+'/loginAction!login.action' , form.serialize(), function(result) {
			  				layer.close(layerindex);
		    				if (result.ok) {
		    					//location.replace(yp.bp()+'/page/sys/'+result.object+'/'+result.object+'.jsp');
		    					location.replace(yp.bp() + '/page/sys/admin.jsp');
		    				} else {
			    				layer.alert(result.msg, 9); 
		    				}
		    			}, 'json');
			    	}
			    },
			});
		});
	});
	/**
	 * 检测浏览器的分辨率，自动将给定id的组件进行缩放
	 * 
	 * @param id
	 */
	function zoom(id) {
		var w = screen.width;
		var h = screen.height;
		var d = w * h;
		var zoom = 1;
		var e = document.getElementById(id);
		if (d <= 860 * 720) {
			zoom = 0.7;
		} else if (d <= 1024 * 768) {
			zoom = 0.70;
		} else if (d <= 1280 * 768) {
			zoom = 0.85;
		} else if (d <= 1400 * 900) {
			zoom = 0.9;
		} else if (d <= 1600 * 900) {
			zoom = 0.95;
		} else if (d <= 1920 * 1080) {
			zoom = 1;
		} else {
			zoom = 1.2;
		}
		// e.style.zoom=zoom;
		$(e).css({
			"overflow" : "hidden",
			"zoom" : zoom,
			"-moz-transform" : "scale(" + zoom + ")",
			"-moz-transform-origin" : "top left",
			"-o-transform" : "scale(" + zoom + ")",
			"-o-transform-origin" : "top left"
		});
	}
</script>
</html>
