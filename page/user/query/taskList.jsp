<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="../../../js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="../../../js/webutils.js"></script>
<script src="../../../js/layer/layer.min.js"></script>
<!--[if IE 6]><script type="text/javascript" src="../../../js/pngFix-for-IE6-min.js"></script><![endif]--><!--关于IE6下PNG图片不透明问题的解决方案详见： http://blog.csdn.net/wangxiaohui6687/article/details/7360875 -->
<link type="text/css" rel="stylesheet" href="../../../css/Validform_style.css" media="all"/>
<link type="text/css" rel="stylesheet" href="../../../css/layout.css" />
<link type="text/css" rel="stylesheet" href="../../../css/apply_nav.css">
<style type="text/css">
* {font-family: 微软雅黑, sans-serif, Microsoft YaHei, 宋体;font-size: 14px;color: #444;}
.footer {text-align: center;	font-size: 12px;}
.header {background-image: url("../../../images/bg.gif");background-repeat: repeat-x;}
.png1{position: relative;top: 70px;left:10%;}
#taskList_table{background-color: #F9F9F9}
#taskList_table tr {border-bottom: 1px solid #888;line-height: 28px;}
#taskList_table tr td{border-right: 1px dashed #aaa;}
#taskList_table .thead {border-bottom: 2px solid #555;font-weight: bold;}
.toolong{overflow: hidden;white-space: nowrap;text-overflow: ellipsis;}
.matter{max-width: 15%;}
.description{max-width: 25%;}
.bodyRow:hover {
	background-color: #D6F4D8
}
</style>
<title>列表</title>
</head>
<body>
	<div class="container">
		<div class="header" style="background-image: url('../../../images/bg_ui-cupertino.gif'); background-repeat: repeat-x;"><!-- 当前头部的高为140px -->
			<img src="../../../images/logo2.png" class="pngFix png1" onclick="window.location.href='../../../index.jsp'" style="cursor: pointer;" title="回首页"><!-- 362*39px -->
		</div>
		<div>
			<div class="left">&nbsp;</div>  <!--style="width:65%;"style="width: 33%"-->
			<div class="content">
				<h3 style="margin-bottom: 0;">任务列表</h3><hr>
				    <!-- 存在usertype时，显示以下信息，否则，显示提示 -->
			    		<div  style=" padding:5px ;text-align:left;">
	    					<div id="tittlediv" style="background-color: #EEE2D1;padding:5px 0; " >
			    					<div style="margin:5px auto;width:95%;height: auto;">&nbsp;&nbsp;
			    						<img src="../../../js/easyui/themes/icons/tip.png">
			    						<span id="tipspan" style="font-size: 12px;color:#444"><label>所有任务</label><label id="tip"></label></span>
			    					</div>
	    					</div>
			  				<div style=" text-align: center;">
				  				<table style="width:100%; border-collapse:collapse;" id="taskList_table">
					  				<tr class="thead">
							  				<td style="text-align: center;">案件编号</td>
							  				<td >状态</td>
							  				<td>申请事项</td>
							  				<td >问题描述</td>
							  				<td >申请时间</td>
							  				<td style="text-align: center;">备注</td>
							  				<td style="text-align: center;border-right: none">操作</td>
					  				</tr>
					  					<c:choose>
					  						<c:when test="${!empty(sessionScope.tasks) }">
				  								<c:forEach items="${sessionScope.tasks}" var="t"> 
				  									<tr  id="${t.id}"  class="bodyRow"><td>${t.caseid }</td><td>${t.status }</td>
				  									<td id="toolong_td_1" title="${t.matter }"><p class="toolong" >${t.matter }</p></td>
				  									<td  id="toolong_td_2" title="${t.description }" ><p class="toolong">${t.description }</p></td>
				  									<td>${t.time }</td><td  style="text-align: center;">${t.selfapplyed?'申请方':'被申请方' }</td><td style="text-align: center;border-right: none">
				  									<c:if test="${t.selfapplyed}">
					  									<c:if test="${t.status=='待审核'||t.status=='处理中' }">
					  										<a href="javascript:void(0);" onclick="cancelTask(${t.id });" >撤销申请</a>&nbsp;|&nbsp;
					  									</c:if>
					  									<a  href="javascript:void(0);" onclick="deleteTask(${t.id});">删除</a>&nbsp;|&nbsp;
					  									<a href="javascript:void(0);" onclick="showPace(${t.id });" >进度</a>
				  									</c:if>
					  								</td></tr>
												</c:forEach>
					  						</c:when>
					  						<c:otherwise>
					  							<jsp:forward page="../../../page/error/404.jsp"></jsp:forward>
					  						</c:otherwise>
					  					</c:choose>
						  		</table>
	    					</div>
	    					<div style="text-align: center; padding-top:100px;">
					  				<a href="query.jsp" class="nav-button" ><img src="../../../js/easyui/themes/icons/back.png"  style="vertical-align: middle;">&nbsp; 返   回</a>
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
var w=$("#tittlediv").width()-560;
var t1=w/3;
var t2=2*w/3;
$("#toolong_td_1").width(t1);
$("#toolong_td_2").width(t2);
$(".toolong:nth-child(1)").css("width",t1+"px");
$(".toolong:nth-child(2)").css("width",t2+"px");
});

function showPace(id){
	if(id&&id!=""){
		var wait=layer.load(0, 1);
		var url=yp.bp()+"/user/applyAction!showPace.action";
		//j.object中保存的是TaskDetailModel对象
	    $.post(url,{"taskid":id} , function(r) {
			layer.close(wait);
		if (r.ok) {
			window.open(yp.bp() + "/page/user/query/taskPace.jsp");
		} else {
			layer.alert(r.msg, 8); 
		}
	}, 'json');
	}else{
		layer.alert("id为空！",0); 
	}
}
	function deleteTask(id){
		if(id&&id!=""){
			layer.confirm('确定删除吗？', function(){
				layer.load(0, 1);
				var url=yp.bp()+"/user/applyAction!deleteTask.action";
				//j.object中保存的是TaskDetailModel对象
			    $.post(url,{"taskid":id} , function(r) {
					layer.closeAll();
				if (r.ok) {
					//如果删除成功，则应该隐藏那一行
					$("#taskList_table #"+id).hide(500);
				} else {
					layer.alert(r.msg, 8); 
				}
			}, 'json');
			});
		}else{
			layer.alert("id为空！",0); 
		}
	}
function cancelTask(id){
	if(id&&id!=""){
		layer.confirm('确定要撤销此申请吗？', function(){
			layer.load(0, 1);
			var url=yp.bp()+"/user/applyAction!cancelTask.action";
			//j.object中保存的是TaskDetailModel对象
		    $.post(url,{"taskid":id} , function(r) {
				layer.closeAll();
			if (r.ok) {
				//如果删除成功，则应该隐藏那一行
				//$("#taskList_table #"+id).hide(500);
				$("#"+id+" td:nth-child(2)").html("调解终止");
			} else {
				layer.alert(r.msg, 8); 
			}
		}, 'json');
		});
	}else{
		layer.alert("id为空！",0); 
	}
}
</script>
</html>
