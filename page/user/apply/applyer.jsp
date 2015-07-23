<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="../../../js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="../../../js/webutils.js"></script>
<script src="../../../js/layer/layer.min.js"></script>
<script type="text/javascript" src="../../../js/Validform_v5.3.2_min.js"></script>
<!--[if IE 6]><script type="text/javascript" src="../../../js/pngFix-for-IE6-min.js"></script><![endif]--><!--关于IE6下PNG图片不透明问题的解决方案详见： http://blog.csdn.net/wangxiaohui6687/article/details/7360875 -->
<link type="text/css" rel="stylesheet" href="../../../css/Validform_style.css" media="all"/>
<link type="text/css" rel="stylesheet" href="../../../css/layout.css" />
<link type="text/css" rel="stylesheet" href="../../../css/apply_nav.css">
<style>
form li{padding-bottom:10px;list-style: none;}
form li input{width:180px;}
/* .Validform_checktip{margin-left:10px;} */
form .label{display:inline-block; width:120px;text-align: right;}
.need{color:red;}
.gray{color:gray;}
</style>
<title>调解申请-申请方信息</title>
</head>
<body>
	<div class="container">
		<div class="header" style="background-image: url('../../../images/bg_ui-cupertino.gif'); background-repeat: repeat-x;"><!-- 当前头部的高为140px -->
			<img src="../../../images/logo2.png" class="pngFix png" onclick="window.location.href='../../../index.jsp'" style="cursor: pointer;" title="回首页"><!-- 362*39px -->
		</div>
		<div>
			<div class="left">&nbsp;</div>  <!--style="width:65%;"style="width: 33%"-->
			<div class="content">
				<h3 style="margin-bottom: 0;">填写申请方信息</h3><hr>
	    		<div id="navimg" style="margin-bottom: 40px;">
	  				<a id="step1"   class="active"  href="entityChoose.jsp"></a>
	  				<div class="split"></div><a id="step2" class="active"  href="applyer.jsp"></a>
					<div class="split split2"></div><a id="step3"  href="toApplyer.jsp"></a>
	  				<div class="split  split2" ></div><a id="step4"  href="description.jsp"></a>
	  			</div>
	  			<table style="width:100%;">
	  				<tr><td class="td_side">&nbsp;&nbsp;&nbsp;&nbsp;</td><td class="td_center" style="width: 500px;">
	  					<c:choose>
    						<c:when test="${!empty(sessionScope.usertype) }">
	    						<c:if test="${sessionScope.usertype=='Consumer' }">
    								<form id="applyer_form_c">
		    							<ul>
							                <li><label class="label"><span class="need">*</span> 姓名：</label>	
							                	<input type="text" value="${!empty(sessionScope.c)?sessionScope.c.name:'' }" name="c.name" class="inputxt"  placeholder="输入姓名" /></li>
							                <li><label class="label"><span class="need">*</span> 身份证号：</label>
							                	<input type="text" value="${!empty(sessionScope.c)?sessionScope.c.idcard:'' }" name="c.idcard" class="inputxt" placeholder="输入身份证号码" /></li>
							                <li><label class="label">车辆所有人：</label>
							                	<input type="text" value="${!empty(sessionScope.c)?sessionScope.c.agent:'' }" name="c.agent" class="inputxt" placeholder="见机动车行驶证"/></li>
							                <li><label class="label"><span class="need">*</span> 联系电话：</label>
							                	<input type="text" value="${!empty(sessionScope.c)?sessionScope.c.phone:'' }" name="c.phone" class="inputxt" placeholder="输入联系电话"/></li>
							                <li><label class="label">E-mail：</label>
							                	<input type="text" value="${!empty(sessionScope.c)?sessionScope.c.email:'' }" name="c.email" class="inputxt" placeholder="输入电子邮件地址"  ignore="ignore" datatype="e" errormsg="电子邮件地址错误！"/></li>
							                <li><label class="label" style="vertical-align: top;">通信地址：</label>
							                	<textarea rows="3" name="c.address"  placeholder="输入通讯地址" style="min-width: 300px;">${!empty(sessionScope.c)?sessionScope.c.address:'' }</textarea>
							            </ul>
							          </form>
	    							</c:if>
	    							<c:if test="${sessionScope.usertype=='Saler' }">
	    								<form id="applyer_form_s">
		    								<ul>
								                <li><label class="label"><span class="need">*</span>修理商/销售商/制造商：</label>	
								                	<input type="text" value="${!empty(sessionScope.s)?sessionScope.s.name:'' }" name="s.name" datatype="*" nullmsg="请输入单位名称" class="inputxt" placeholder="单位名称" /></li>
								                <li><label class="label"><span class="need">*</span> 机构代码：</label>
								                	<input type="text" value="${!empty(sessionScope.s)?sessionScope.s.code:'' }" name="s.code" datatype="s9" nullmsg="请输入组织机构代码" errormsg="组织机构代码有误" class="inputxt" placeholder="输入组织机构代码" /></li>
								                <li><label class="label"><span class="need">*</span>负责人：</label>
								                	<input type="text" value="${!empty(sessionScope.s)?sessionScope.s.agent:'' }" name="s.agent"datatype="*" nullmsg="请输入负责人姓名" class="inputxt" placeholder="负责人（代理人）姓名"/></li>
								                <li><label class="label"><span class="need">*</span> 联系电话：</label>
								                	<input type="text" value="${!empty(sessionScope.s)?sessionScope.s.phone:'' }" name="s.phone" class="inputxt" placeholder="输入联系电话"/></li>
								               <li><label class="label">E-mail：</label>
							                		<input type="text" value="${!empty(sessionScope.s)?sessionScope.s.email:'' }" name="s.email" class="inputxt" placeholder="输入电子邮件地址"  ignore="ignore" datatype="e" errormsg="电子邮件地址错误！"/></li>
								                <li><label class="label" style="vertical-align: top;">通信地址：</label>
								                	<textarea rows="3" name="s.address"  placeholder="输入通讯地址" style="min-width: 300px;">${!empty(sessionScope.s)?sessionScope.s.address:'' }</textarea>
								            </ul>
		    							</form>
	    							</c:if>
    							<div style="text-align: center; padding:40px 0;">
					  				<a href="entityChoose.jsp" class="nav-button"><img src="../../../js/easyui/themes/icons/back.png"  style="vertical-align: middle;">&nbsp; 上一步</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					  				<a href="javascript:void(0);" class="nav-button" id="submitBtn"><img src="../../../js/easyui/themes/icons/forward.png"  style="vertical-align: middle;">&nbsp; 下一步</a>
					  			</div>
					  		</c:when>
					  		<c:otherwise>
				  				<div style="text-align: center; padding:40px 0;">
		  							<b style="color:red;">对不起，不知道您的用户类型，请后退选择您的用户类型！</b><br><br><br><br>
					  				<a href="entityChoose.jsp" class="nav-button"><img src="../../../js/easyui/themes/icons/back.png"  style="vertical-align: middle;">&nbsp; 返&nbsp; &nbsp; 回</a>
					  			</div>
    						</c:otherwise>
					  	</c:choose>
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
<script type="text/javascript">
$(function(){
	//$(".registerform").Validform();  //就这一行代码！;
	var t=${!empty(sessionScope.usertype)};
		if(t){
			var utp="${sessionScope.usertype}";
			var formSelector=undefined;
			var valid_form=undefined;
			if(utp=='Consumer'){
				formSelector="#applyer_form_c";
				valid_form=$(formSelector).Validform({
					tiptype:3,
					label:".label",
					showAllError:true,
					datatype:{
						"zh1-4":/^[\u4E00-\u9FA5\uf900-\ufa2d]{1,4}$/,
						"tel":/(^(\d{3,4}-)?\d{7,8})$|(1[3,4,5,8][0-9]{9}$)/,
						"idcard":function(gets,obj,curform,datatype){return yp.isIDCard(gets);}//对身份证号进行严格验证，该方法由佚名网友提供;//"idcard":/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/,
					},
				});
				valid_form.addRule([
				                    {ele:".inputxt:eq(0)",datatype:"zh2-4",nullmsg:"请输入姓名",errormsg:"2-4位中文名"},
				                    {ele:".inputxt:eq(1)",datatype:"idcard",nullmsg:"请输入身份证号",errormsg:"身份证号有误"},
				                    {ele:".inputxt:eq(3)",datatype:"tel",nullmsg:"请输入联系电话",errormsg:"电话号码有误"},
				                    ]);
			}else if(utp=='Saler'){
				formSelector="#applyer_form_s";
				valid_form=$(formSelector).Validform({
					tiptype:3,
					label:".label",
					showAllError:true,
					datatype:{"tel":/(^(\d{3,4}-)?\d{7,8})$|(1[3,4,5,8][0-9]{9}$)/},
				});
				valid_form.addRule([{ele:".inputxt:eq(3)",datatype:"tel",nullmsg:"请输入联系电话",errormsg:"电话号码有误"}]);
			}
			if(valid_form){
				$.Tipmsg.r="";
			$("#submitBtn").click(function(){
				var pass=valid_form.check();
				if(pass){//如果难通过
					var wait=layer.load(0, 1);
				    var url=yp.bp()+'/user/applyAction!setApplyerInfo.action';
				    var params=yp.serializeObject(formSelector);
				    $.post(url,params , function(r) {
							layer.close(wait);
						if (r.ok) {
							//location.replace(yp.bp()+'/page/sys/'+result.object+'/'+result.object+'.jsp');
							window.location=yp.bp()+"/page/user/apply/toApplyer.jsp"; 
						} else {
							layer.alert(r.msg, 8); 
						}
					}, 'json');
				}else{
					return false;
				}
			});
			}
		}
});
</script>
</html>
