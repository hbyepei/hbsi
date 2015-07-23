<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="../../../js/jquery-1.10.2.min.js"></script>
<script src="../../../js/layer/layer.min.js"></script>
<script type="text/javascript" src="../../../js/Validform_v5.3.2_min.js"></script>
<script type="text/javascript" src="../../../js/webutils.js"></script>
<script type="text/javascript" src="../../../js/laydate/laydate.js"></script>
<script type="text/javascript" src="../../../js/jquery-form.js"></script>
<!--[if IE 6]><script type="text/javascript" src="../../../js/pngFix-for-IE6-min.js"></script><![endif]--><!--关于IE6下PNG图片不透明问题的解决方案详见： http://blog.csdn.net/wangxiaohui6687/article/details/7360875 -->
<link type="text/css" rel="stylesheet" href="../../../css/Validform_style.css" media="all"/>
<link type="text/css" rel="stylesheet" href="../../../css/layout.css" />
<link type="text/css" rel="stylesheet" href="../../../css/apply_nav.css">
<style>
form li{padding-bottom:10px;list-style: none;}
form li input{width:180px;}
form li .ckbx{width: auto;}
/* .Validform_checktip{margin-left:10px;} */
form .label{display:inline-block; width:100px;text-align: right;}
.need{color:red;}
</style>
<title>调解申请-争议描述</title>
</head>
<body>
	<div class="container">
		<div class="header" style="background-image: url('../../../images/bg_ui-cupertino.gif'); background-repeat: repeat-x;"><!-- 当前头部的高为140px -->
			<img src="../../../images/logo2.png" class="pngFix png" onclick="window.location.href='../../../index.jsp'" style="cursor: pointer;" title="回首页"><!-- 362*39px -->
		</div>
		<div>
			<div class="left">&nbsp;</div>  <!--style="width:65%;"style="width: 33%"-->
			<div class="content">
				<h3 style="margin-bottom: 0;">填写争议描述信息</h3><hr>
	    		<div id="navimg" style="margin-bottom: 40px;">
	  				<a id="step1"   class="active"  href="entityChoose.jsp"></a>
	  				<div class="split"></div><a id="step2" class="active"  href="applyer.jsp"></a>
					<div class="split split2"></div><a id="step3"class="active"  href="toApplyer.jsp"></a>
	  				<div class="split  split2" ></div><a id="step4" class="active"  href="description.jsp"></a>
	  			</div>
	  			<table style="width:100%;">
	  				<tr><td class="td_side">&nbsp;&nbsp;&nbsp;&nbsp;</td><td class="td_center" style="width: 500px;">
	  					<c:choose>
    						<c:when test="${!empty(sessionScope.usertype) }">
    								<form id="description_form" method="post" enctype="multipart/form-data">
		    							<ul>
							                <li><label class="label" ><span class="need">*</span> 申请事项：</label>
							                	<input type="text" value="${!empty(sessionScope.t)?sessionScope.t.matter:'' }" name="t.matter" datatype="*" class="inputxt" placeholder="填写申请事项" style="width: 340px;"/></li>
							                <li><label class="label"><span class="need">*</span> 车辆品牌：</label>
							                	<input type="text" value="${!empty(sessionScope.t.car)?sessionScope.t.car.brand:'' }" name="t.car.brand"  class="inputxt" datatype="*" placeholder="输入车辆品牌"/></li>
							                <li><label class="label"><span class="need">*</span> 车辆型号：</label>
							                	<input type="text" value="${!empty(sessionScope.t.car)?sessionScope.t.car.model:'' }" name="t.car.model" class="inputxt" datatype="*" placeholder="输入车辆型号"/></li>
							                <li><label class="label">车架号：</label>
							                	<input type="text" value="${!empty(sessionScope.t.car)?sessionScope.t.car.carriage:'' }" name="t.car.carriage" class="inputxt" placeholder="输入车架号" /></li>
							               	<li><label class="label" style="vertical-align: top;">购车时间：</label>
							               		<input class="laydate-icon" value="${!empty(sessionScope.dealtime)?sessionScope.dealtime:'' }" name="t.car.dealtime"   id="dealtime"  onclick="laydate({format: 'YYYY-MM-DD',max: laydate.now(0)})" placeholder="输入购车时间" readonly="readonly"/></li>
							                <li><label class="label" style="width: 478px;text-align: left;margin-bottom: 5px;"><span class="need">*</span> 三包责任争议简易情况描述：</label><br>
							                	<textarea rows="8" style="width: 450px;" name="t.description"  datatype="*"  class="inputxt" placeholder="填写争议情况描述">${!empty(sessionScope.t)?sessionScope.t.description:'' }</textarea></li>
							                <li><label class="label"><span class="need">*</span>事故发生部位:</label>&nbsp;&nbsp;
							                	<span><input id="acdtpt_0" class="ckbx" type="checkbox" name="cap.chesheng" /><label for="acdtpt_0">车身</label></span>&nbsp;&nbsp;
							                	<span><input id="acdtpt_1" class="ckbx"  type="checkbox" name="cap.chuandong"/><label for="acdtpt_1">传动系</label></span>&nbsp;&nbsp;
							                	<span><input id="acdtpt_2"  class="ckbx" type="checkbox" name="cap.dianqishebei"/><label for="acdtpt_2">电气设备</label></span>&nbsp;&nbsp;
							                	<span><input  id="acdtpt_3" class="ckbx" type="checkbox" name="cap.fadongji"/><label for="acdtpt_3">发动机</label></span>&nbsp;&nbsp;
							                	<span><input id="acdtpt_4"  class="ckbx" type="checkbox" name="cap.chelun"/><label for="acdtpt_4">车轮和轮胎</label></span>&nbsp;&nbsp;
							                	<span><input id="acdtpt_5" class="ckbx"  type="checkbox" name="cap.qinang"/><label for="acdtpt_5">气囊和安全带</label></span>&nbsp;&nbsp;
							                	<span><input  id="acdtpt_6"class="ckbx"  type="checkbox" name="cap.xuanjiaxi"/><label for="acdtpt_6">悬架系</label></span>&nbsp;&nbsp;
							                	<span><input id="acdtpt_7" class="ckbx"  type="checkbox" name="cap.zhidongxi"/><label for="acdtpt_7">制动系</label></span>&nbsp;&nbsp;
							                	<span><input id="acdtpt_8" class="ckbx"  type="checkbox" name="cap.zhuanxiangxi"/><label for="acdtpt_8">转向系</label></span>&nbsp;&nbsp;
							                	<span><input  id="acdtpt_9" class="ckbx" type="checkbox" name="cap.fujiashebei"/><label for="acdtpt_9">附加设备</label></span>
							                </li>
							                <li><label class="label">凭证文件：</label>
							                	<input type="file" id="upfile" name="upfile" multiple="multiple" style="text-overflow : ellipsis;white-space:nowrap;border: none;"/></li>
							                <li><label class="label">需专家介入：</label>
							                	<input type="radio" value="true" id="yes" name="t.needexpert" style="width: 30px;"/><label for="yes">是</label>
							                	<input type="radio" value="false" id="no" name="t.needexpert" checked="checked" style="width: 30px;"/><label for="no">否</label></li>
							            </ul>
							          </form>
    							<div style="text-align: center; padding:40px 0;">
					  				<a href="toApplyer.jsp" class="nav-button"><img src="../../../js/easyui/themes/icons/back.png"  style="vertical-align: middle;">&nbsp; 上一步</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					  				<a href="javascript:void(0);" class="nav-button" id="submitBtn"><img src="../../../js/easyui/themes/icons/forward.png"  style="vertical-align: middle;">&nbsp; 提&nbsp;&nbsp;&nbsp;&nbsp;交</a>
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
 <% Object c1=(null!=session.getAttribute("c"))?session.getAttribute("c"):"";
   		Object s1=(null!=session.getAttribute("s"))?session.getAttribute("s"):"";
 %>
<input type="hidden" id="c" value="<%=c1.toString() %>">
<input type="hidden" id="s" value="<%=s1.toString() %>">
	<div class="footer"><a href="../guanyu.jsp" target="_blank" style="text-decoration: none;font-size: 12px;">关于我们</a>&nbsp;|&nbsp;
	<a href="../xieyi.jsp" target="_blank" style="text-decoration: none;font-size: 12px;">服务条款</a> 
	Copyright© 2014 湖北省标准化研究院缺陷产品管理中心. All Rights Reserved</div>
</body>
<script type="text/javascript">
$(function(){
	laydate.skin('yalan');//设置日期选择框的皮肤
	var nd=${!empty(sessionScope.t)};
	if(nd){
		var utp="${sessionScope.t.needexpert}";
			if(utp=='true'){
				$("#yes").attr("checked",true);
			}else{
				$("#no").attr("checked",true);
			}
		}
	var valid_form=$("#description_form").Validform({
		tiptype:3,
		label:".label",
		showAllError:true,
	});
	$.Tipmsg.r="";
	//提示信息
	var tipsDesc=new Array(
			'车驾、车梁、风窗、挂车钩、后视镜、拉手、门锁、座椅', //车身
			'AMT/EMT、DSG/DCT车桥、传动轴、多个车桥、手动变速器、自动变速器、车轮驱动、排气管',//传动系
			'辐射模块、充电系、除雾/除霜、导航、防盗控制、刮水器、集成式拖车制动控制、空调、喇叭、外部灯、线束、仪表、音响',//电气设备
			'柴油机、汽油机、其它燃料机、新能源系统、点火系、进气系统、起动系、水箱',//发动机
			'车轮与轮胎',//车轮和轮胎
			'气囊和安全带',//气囊和安全带
			'前悬架、后悬架、主动悬架控制（钢板、弹簧、控制臂、连杆）',//悬架系
			'ASR/ESP、气压行车制动、液圧行车制动、驻车制动',//制动系
			'齿轮齿条式、电动和电液转向助力、方向盘、循环球式、液压转向助力、转向传动装置、转向管柱',//转向系
			'标识、机械、使用说明书');//附加设备
	var tipParams={
	        guide: 0,//guide: 指引方向（0：上，1：右，2：下，3：左）
	        time: 2,//0表示不自动关闭，若3秒后自动关闭，time: 3即可，默认为0
	        style: ['background-color:#3BAAE3; color:#fff', '#3BAAE3'],
	        maxWidth:240
	    };
	for(var i=0;i<tipsDesc.length;i++){
		var ele=$("#acdtpt_"+i).parent();
			ele.attr('tip',tipsDesc[i]);
			ele.on('mouseover', function(){
			layer.tips($(this).attr("tip"), this,tipParams );	
		});
	}
	
	$("#submitBtn").click(function(){
				layer.load(0, 1);
				var c=$("#c").val(); var s=$("#s").val();//先检查申请方和被申请方信息是否存在
				 if(!valid_form.check()){
					 layer.closeAll();
					 return false;
				 }else if(!checkBoxCheck()){//事故部位未填写
					 layer.alert("请勾选“汽车事故的发生部位！”", 8,function(){layer.closeAll();} ); return false;
				 }else if (c==""&&s=="") {
					 layer.alert("信息填写不完整，请填写完整后再提交！", 8,function(){layer.closeAll();} ); return false;
				 }else{
					 layer.closeAll();
					 layer.confirm('您确定要提交吗？', function(){
						 layer.load(0, 1);
						 $("#description_form").ajaxSubmit({
			                 type:'post',
			                 url:yp.bp()+'/user/applyAction!finish.action',
			                 success:function(data){
			                     layer.closeAll();
			                     var obj=yp.json2obj(data);
			                     if(obj.ok){
			                    	 window.location=yp.bp()+"/page/user/apply/success.jsp"; 
			                     }else{
			                    	 layer.alert(obj.msg, 8);
			                     }
			                 },
			                 error:function(XmlHttpRequest,textStatus,errorThrown){
			                     console.log(XmlHttpRequest);
			                     console.log(textStatus);
			                     console.log(errorThrown);
			                     layer.closeAll();
			                 }
						 });
					},function(){
						layer.closeAll();
					});
				 }
	});
});
 function checkBoxCheck(){
	 var flag=false;
	 for(var i=0;i<10;i++){
		 var ckd=document.getElementById("acdtpt_"+i).checked;
		 flag=flag||ckd;
	 }
	return flag;
}
</script>
</html>
