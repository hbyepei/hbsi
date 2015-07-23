<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="../../../js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="../../../js/webutils.js"></script>
<!--[if IE 6]><script type="text/javascript" src="../../../js/pngFix-for-IE6-min.js"></script><![endif]--><!--关于IE6下PNG图片不透明问题的解决方案详见： http://blog.csdn.net/wangxiaohui6687/article/details/7360875 -->
<link type="text/css" rel="stylesheet" href="../../../css/layout.css" />
<link type="text/css" rel="stylesheet" href="../../../css/apply_nav.css">
<style type="text/css">
* {font-family: 微软雅黑, sans-serif, Microsoft YaHei, 宋体;font-size: 14px;color: #444;}
.footer {text-align: center;	font-size: 12px;}
.header {background-image: url("../../../images/bg.gif");background-repeat: repeat-x;}
.png1{position: relative;top: 70px;left:10%;}
#spindle div{height: 60px; float: left; background-image: url('../../../images/progress_on.png');}
#showTime div{height: 40px;float: left; color:#444;vertical-align: bottom;}
#tiptext div{height: 20px;float: left; color:#444;}
div[id^='status']{cursor: pointer;}
div[id^='time'],div[id^='text'] {text-align: center;}
.active{display: inherit;}
.inactive{display: none;}
</style>
<title>进度信息</title>
</head>
<body>
	<div class="container">
		<div class="header" style="background-image: url('../../../images/bg_ui-cupertino.gif'); background-repeat: repeat-x;"><!-- 当前头部的高为140px -->
			<img src="../../../images/logo2.png" class="pngFix png1" onclick="window.location.href='../../../index.jsp'" style="cursor: pointer;" title="回首页"><!-- 362*39px -->
		</div>
		<div>
			<div class="left">&nbsp;</div>  <!--style="width:65%;"style="width: 33%"-->
			<div class="content">
				<h3 style="margin-bottom: 0;">调解进度</h3><hr>
				    <!-- 存在usertype时，显示以下信息，否则，显示提示 -->
			    		<div  style=" padding:5px ;text-align:left;">
	    					<div id="toptip" style="background-color: #EEE2D1;padding:5px 0; " >
			    					<div style="margin:5px auto;width:95%;height: auto;">&nbsp;&nbsp;
			    						<img src="../../../js/easyui/themes/icons/tip.png">
			    						<span id="tipspan" style="font-size: 12px;color:#444"><label>
			    							尊敬的用户，您好，您的调解申请的处理进度如下，调解进度的快慢视具体情况而定，请耐心等待。如有什么问题，欢迎电话咨询！
			    						</label></span>
			    					</div>
	    					</div>
			  				<div style=" text-align: center;" id="paceInfo">
			  					<div id="showTime"  style="width: 556px;height: 40px;padding:0 0;margin:20px auto 5px auto;">
						    	 		<div id="time0" style="width: 120px;text-align: left;">　</div>
						    	 	 	<div style="width: 31px; ">　</div>
						    	 		<div id="time1" style="width: 120px;">　</div>
						    	 	 	<div style="width: 45px; ">　</div>
						    	 		<div id="time2" style="width: 120px; " >　</div>
						    	 		<div style="width: 45px; ">　</div>
						    	 		<div id="time3" style="width: 75px; ">　</div>
					    	 	</div>
					    	 	<div id="spindle" style="width: 556px;height: 60px;padding:0 0;margin:0 auto;background-image: url('../../../images/progress_off.png');">
					    	 		<div id="status0" class="active" style="width:60px;background-position: 0 0;"></div>
					    	 		<div id="status1" class="inactive" style="width:165px;background-position:-60px 0;"></div>
					    	 		<div id="status2" class="inactive" style="width:165px;background-position: 166px 0;"></div>
					    	 		<div id="status3" class="inactive" style="width:166px;background-position: 331px 0;"></div>
					    	 	</div>
					    	 	<div id="tiptext"  style="width: 556px;height: 20px;padding:0 0;margin:0 auto;">
						    	 		<div style="width:151px;text-align:left ;cursor: pointer;" title="提交">　提交</div>
						    	 		<div id="text1" style="width: 120px;">待审核</div>
						    	 	 	<div style="width: 45px; "></div>
						    	 		<div id="text2" style="width: 120px; " ></div>
						    	 		<div style="width: 45px; "></div>
						    	 		<div id="text3" style="width: 75px;text-align: right;"></div>
					    	 	</div>
					    	 	<div  style="width: 556px;padding:0 0;margin:0 auto;">
					    	 	<table  id="docDownload" style="display: none;margin-top:100px;width: 100%;">
					    	 		<tr id="doc1" >
						    	 		<td>
						    	 			<a  id="doc1_download" href="../../../user/applyAction!downloadUserDoc.action?docname=applydoc&taskid=">
						    	 			下载：家用汽车产品三包责任争议第三方调解申请书.doc</a>
						    	 		</td>
						    	 		<td style="padding-left:20px;">
						    	 		<a id="doc1_look" href="../../../user/applyAction!onlinePrelook.action?looker=user&docname=applydoc&taskid="  target="_blank">查看</a></td>
					    	 		</tr>
					    	 		<tr id="doc2" style="display: none;">
						    	 			<td>
						    	 				<a  id="doc2_download" href="../../../user/applyAction!downloadUserDoc.action?docname=consultdoc&taskid=">
						    	 				下载：家用汽车疑难问题技术咨询申请书.doc</a>
						    	 			</td>
						    	 			<td  style="padding-left:20px;">
						    	 			<a id="doc2_look" href="../../../user/applyAction!onlinePrelook.action?looker=user&docname=consultdoc&taskid="  target="_blank">查看</a></td>
					    	 		</tr>
					    	 		<tr id="doc3" style="display: none;">
						    	 			<td>
						    	 				<a  id="doc3_download" href="../../../user/applyAction!downloadUserDoc.action?docname=terminatedoc&taskid=">
						    	 				下载：家用汽车产品三包责任争议第三方调解终结通知书.doc</a>
						    	 			</td>
					    	 		</tr>
					    	 		<tr id="doc4" style="display: none;">
						    	 			<td>
						    	 				<a  id="doc4_download" href="../../../user/applyAction!downloadUserDoc.action?docname=consult_resultdoc&taskid=">
						    	 				下载：家用汽车疑难问题技术咨询结果.doc</a>
						    	 			</td>
					    	 		</tr>
					    	 	</table>
					    	 	<div style="margin-top: 20px;text-align: left;">
						    	 			<a id="task_detail" href="../../../user/applyAction!onlinePrelook.action?looker=user&docname=applydoc_server&taskid=">
						    	 			查看详细情况</a>
					    	 		</div>
					    	 	<div id="tip_tr"  style="margin-top:10px; text-align: left;">注意，此任务还没有审核！</div>
					    	 </div>
	    					</div>
					    <div id="nosessiontip" style="display: none;width:70%;height: auto;margin:20px auto; text-align: center;font-size: 16px;">
				  			<p style="color: red">对不起，没有结果！</p>
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
	var task=<%=null==session.getAttribute("task")?null:(String)session.getAttribute("task")%>
	var statusTime=undefined;
	var applytime=undefined;
	var datePattern="yyyy年M月d日-h:mm";
	var taskid=undefined;
	if (task!=null) {
		taskid=task.id;
		var task_detailUrl=$("#task_detail").attr('href')+taskid;
		$('#task_detail').attr('href',task_detailUrl);
		var status=task.status;
		statusTime=task.statustime;
		applytime=task.applytime;
		var to="toaudit",re="refused",pr="processing",te="terminate",cs="consulted",co="complete";
		switch (status) {
		case to: toaudit();break;
		case re:refused();break;
		case pr:processing();break;
		case te: terminate();break;
		case cs:consulted();break;
		case co:complete();break;
		}
		//显示进度信息，且，凡当状态经历过“处于中”时，请显示文件下载链接
   	}else{
   		window.location=yp.bp()+"/page/error/404.jsp";
   	}
	function toaudit(){
		//1.增加status0的onhover属性
		$("#time1").html(getFormatDateByLong(applytime, datePattern));
		$("#status0").attr("title","提交任务");
		$("#status1").attr("title","等待审核中！");
		$("#status1").attr("class","active");
	}
	function refused(){
		toaudit();
		//将text2文字改为已拒绝，text3、text4的字隐藏
		$("#text2").html("不予受理");
		$("#text3").html("");
		$("#time2").html(getFormatDateByLong(statusTime.refused, datePattern));
		$("#status2").attr("class","active");
		$("#tip_tr").css("display","none");
		//增加hover属性显示拒绝理由
		$("#status1").attr("title","不予受理此申请！");
	}
	function processing(){
		toaudit();
		$("#status2").attr("class","active");
		$("#time2").html(getFormatDateByLong(statusTime.processing, datePattern));
		$("#text2").html("处理中");
		$("#tip_tr").css("display","none");
		$("#docDownload").css("display","inherit");
		var doc1downUrl=$('#doc1_download').attr('href')+taskid;//生成文档下载链接
		var doc1lookUrl=$('#doc1_look').attr('href')+taskid;//生成文档在线预览链接
		$('#doc1_download').attr('href',doc1downUrl);
		$('#doc1_look').attr('href',doc1lookUrl);
		$("#status2").attr("title","正在处理中...");
		if(task.needexpert){
			$("#doc2").css("display","inherit");
			var doc2downUrl=$('#doc2_download').attr('href')+taskid;//生成文档下载链接
			var doc2lookUrl=$('#doc2_look').attr('href')+taskid;//生成文档在线预览链接
			$('#doc2_download').attr('href',doc2downUrl);
			$('#doc2_look').attr('href',doc2lookUrl);
		}
	}
	function terminate(){
		var cancelInProcessing=statusTime.processing;
		if(cancelInProcessing){
			processing();
			$("#status3").attr("class","active");
			$("#time3").html(getFormatDateByLong(statusTime.terminate, datePattern));
			$("#text3").html("调解终止");
			$("#status3").attr("title","调解已经终止！");
		}else{
			toaudit();
			//将text2文字改为已拒绝，text3、text4的字隐藏
			$("#text2").html("调解终止");
			$("#text3").html("");
			$("#time2").html(getFormatDateByLong(statusTime.terminate, datePattern));
			$("#status2").attr("class","active");
			$("#tip_tr").css("display","inherit");
			$("#tip_tr").html("用户已经在处理之前撤销了此申请！");
			//增加hover属性显示拒绝理由
			$("#status2").attr("title","用户已经撤销此申请！");
		}
		//显示调解终止的下载链接，并隐藏其它下载链接
		var doc3downUrl=$('#doc3_download').attr('href')+taskid;//生成文档下载链接
		$('#doc3_download').attr('href',doc3downUrl);
		$("#doc3").css("display","inherit");
		$("#doc1").css("display","none");
		$("#doc2").css("display","none");
		$("#doc4").css("display","none");
	}
	function consulted(){
		processing();
		$("#text3").html("");
		$("#status2").attr("title","专家已经出具咨询意见！");
	}
	function complete(){
		processing();
		$("#status3").attr("class","active");
		$("#time3").html(getFormatDateByLong(statusTime.complete, datePattern));
		$("#text3").html("完成");
		$("#status3").attr("title","调解已经完成！");
		//显示调解完成后咨询结果通知书的下载链接，并隐藏其它下载链接
		var doc4downUrl=$('#doc4_download').attr('href')+taskid;//生成文档下载链接
		$('#doc4_download').attr('href',doc4downUrl);
		$("#doc4").css("display","inherit");
		$("#doc1").css("display","none");
		$("#doc2").css("display","none");
		$("#doc3").css("display","none");
	}
});

</script>
</html>
