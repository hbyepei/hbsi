<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="../../../js/jquery-1.10.2.min.js"></script>
<script src="../../../js/layer/layer.min.js"></script>
<script type="text/javascript" src="../../../js/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="../../../js/webutils.js"></script>
<!--[if IE 6]><script type="text/javascript" src="../../../js/pngFix-for-IE6-min.js"></script><![endif]--><!--关于IE6下PNG图片不透明问题的解决方案详见： http://blog.csdn.net/wangxiaohui6687/article/details/7360875 -->
<link type="text/css" rel="stylesheet" href="../../../css/layout.css" />
<link type="text/css" rel="stylesheet" href="../../../css/apply_nav.css">
<title>调解申请-主体选择</title>
</head>
<body>
	<div class="container">
		<div class="header" style="background-image: url('../../../images/bg_ui-cupertino.gif'); background-repeat: repeat-x;"><!-- 当前头部的高为140px -->
			<img src="../../../images/logo2.png" class="pngFix png" onclick="window.location.href='../../../index.jsp'" style="cursor: pointer;" title="回首页"><!-- 362*39px -->
		</div>
		<div>
			<div class="left">&nbsp;</div>  <!--style="width:65%;"style="width: 33%"-->
			<div class="content">
				<h3 style="margin-bottom: 0;">选择申请主体</h3><hr>
	    		<div id="navimg" style="margin-bottom: 40px;">
	  				<a id="step1"  class="active"  href="entityChoose.jsp"></a>
	  				<div class="split"></div><a id="step2"  href="applyer.jsp"></a>
	  				<div class="split split2"></div><a id="step3"  href="toApplyer.jsp"></a>
	  				<div class="split  split2" ></div><a id="step4"  href="description.jsp"></a>
	  			</div>
	  			<table style="width:100%;">
	  				<tr><td class="td_side">&nbsp;&nbsp;&nbsp;&nbsp;</td><td class="td_center">
			  			<form id="form_entityChoose">
			  				<input type="checkbox" id="xieyi" ><label for="xieyi">同意湖北省标准化研究院家用汽车三包责任争议调解平台的<a href="../xieyi.jsp" target="_blank" style="color: blue;">服务协议</a></label>
			  				<div style="text-align: center;">
			  				<br><br><label>我是：</label>
			  				<input type="radio" name="usertype" value="Consumer" id="consumer"><label for="consumer"><img src="../../../js/easyui/themes/icons/yp_user.png">&nbsp;消费者(家用车车主)</label>
			  				&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="usertype" value="Saler" id="saler"><label for="saler"><img src="../../../js/easyui/themes/icons/yp_person.png">&nbsp;修理商/销售商/制造商</label>
			  				</div>					
			  			</form>
			  			<div style="text-align: center; padding:40px 0;">
			  				<a href="javascript:void(0);" class="nav-button" onclick="next();"><img src="../../../js/easyui/themes/icons/forward.png"  style="vertical-align: middle;">&nbsp; 下一步</a>
			  			</div>
	  				</td><td class="td_side">&nbsp;&nbsp;&nbsp;&nbsp;</td>
	  			</table>

			</div>
			<div class="right">&nbsp;</div><!-- 有两个1%被margin_right占用了 -->
		</div>
	</div>
	<div class="footer"><a href="../guanyu.jsp" target="_blank" style="text-decoration: none;font-size: 12px;">关于我们</a>&nbsp;|&nbsp;
	<a href="../xieyi.jsp" target="_blank" style="text-decoration: none;font-size: 12px;">服务条款</a> 
	Copyright© 2014 湖北省标准化研究院缺陷产品管理中心. All Rights Reserved</div>
</body>
<script>
$(function(){
	var t=${!empty(sessionScope.usertype)};
	$("#xieyi").attr("checked",t?true:false);
	if(t){
		var radios=document.getElementsByName("usertype");
		var utp="${sessionScope.usertype}";
		for(var i=0;i<radios.length;i++){
			if(utp==radios[i].value){
				$(radios[i]).attr("checked",true);
				break;
			}
		}
	}
});
function next(){
    //form.Validform();//初始化验证组件
    var agree=document.getElementById("xieyi").checked;
    if(agree){
	   var ut=yp.serializeObject("#form_entityChoose");
	  	if(ut.usertype){
			var wait=layer.load(0, 1);
		    var url=yp.bp()+'/user/applyAction!setType.action';
		    $.post(url ,ut, function(result) {
					layer.close(wait);
				if (result.ok) {
					//location.replace(yp.bp()+'/page/sys/'+result.object+'/'+result.object+'.jsp');
					window.location=yp.bp()+"/page/user/apply/applyer.jsp"; 
				} else {
					layer.alert(result.msg, 8); 
				}
			}, 'json');
	  	}else{
	  		layer.alert('请选择申请主体！', 0,'提示'); 
	  	}
    }else{
    	layer.alert('使用本平台提供的服务，须同意相关服务条款！', 8,'提示'); 
    }
 }
</script>
</html>
