<%@ page language="java" pageEncoding="UTF-8"%>
<%@include file="include.jsp" %>
<!DOCTYPE HTML>
<html>
<style>
.docs li{
	line-height: 28px;
	font-size: 14px;
}
</style>
  <body>  
  <div style="padding:10px 10px;">
<span id="time"></span><span style="color:#3779C9">，${empty(user.name)?user.name:user.username}！</span>
<div style="margin-top:10px;">
	<img alt="任务消息" src="../../images/msg.png" style="vertical-align:bottom;margin-right:10px;">任务消息：
	<span   class="admin"><label id="toauditcount" style="color:red">0</label>个待审核任务，</span>
	<label  id="tohandlecount" style="color:red">0</label>个当前任务</div>
		<div style="padding:20px 15px;">
			<span  class="admin"><a href="#" onclick="parent.addTab('menubtn11')" style="text-decoration:none;color:#3779C9">待审核(<span id="toaudit_span">0</span>)</a>&nbsp;|&nbsp;</span>
			<a href="#" onclick="parent.addTab('menubtn12')" style="text-decoration:none;color:#3779C9">处理中(<span id="processing_span">0</span>)</a>&nbsp;|&nbsp;
			<a href="#" onclick="parent.addTab('menubtn13')" style="text-decoration:none;color:#3779C9">查看所有任务(<span id="alltask_span">0</span>)</a>
			<span  class="admin">&nbsp;|&nbsp;<a href="#" onclick="parent.addTab('menubtn14')" style="text-decoration:none;color:#3779C9">建立新任务</a></span>
		</div>
</div>

<div  class="easyui-panel" title="其它信息"   style="background:#fff;position: relative;" 
  data-options="iconCls:'icon-yp_msg',fit:true,border:false">   
  <div style="width: 100%;">
<!-- =========消息窗体============ -->
	<div>
		<fieldset style="border-radius:5px;border:1px solid #ccc;color:#555;margin: 5px 20px 0 20px; min-height:100px;padding:15px 10px;font-size:13px;">
			<legend>系统消息</legend>
			<ul>
				<li>新系统可能存在兼容性问题，推荐使用IE9+/Firefox(火狐)/Chrome(谷歌)/Opera(欧朋)等主流高版本浏览器！</li>
			</ul>
		</fieldset>
	</div>
	<div style="margin: 15px 0 0 20px;">
		文件模板：
		<ul class="docs">
			<li><a href="javascript:void(0);" onclick="dowloadDocs('applydoc');">家用汽车产品三包责任争议第三方调解申请书.doc</a></li>
			<li><a  href="javascript:void(0);" onclick="dowloadDocs('consultdoc');">家用汽车疑难问题技术咨询申请书.doc</a></li>
			<li><a  href="javascript:void(0);" onclick="dowloadDocs('consult_resultdoc');">家用汽车疑难问题技术咨询结果.doc</a></li>
			<li><a  href="javascript:void(0);" onclick="dowloadDocs('terminatedoc');">家用汽车产品三包责任争议第三方调解终结通知书.doc</a></li>
		</ul>
	</div>
</div>
</div>
<script>
$.ajaxSetup({cache:false}) ;
document.onkeydown = function() {
	    if(event.keyCode == 8) { // 如果按下的是退格键
	     // 如果是在textarea内不执行任何操作
	        if(event.srcElement.tagName.toLowerCase() == "input" && event.srcElement.tagName.toLowerCase()== "textarea"&& event.srcElement.tagName.toLowerCase() == "select" ){
		        	event.returnValue = true;
		     } else{ event.returnValue = false;  } 
	        // 如果是readOnly或者disable不执行任何操作
/* 	 		 if(event.srcElement.readOnly == true || event.srcElement.disabled == true){
	 			 event.returnValue = false;
	 		}     */                   
	     }
	}
$(function(){
	var h = new Date().getHours(); //获取当前小时数(0-23)
	var hello="";
	if (h<7) {hello="早上好";	}else if(h<12){hello="上午好";}else if(h<14){hello="中午好";}else if(h<19){hello="下午好";}else{hello="晚上好";}
	$("#time").html(hello);
	$.get(yp.bp()+'/sys/taskAction!taskCount.action',{loadtype:'toaudit,processing'},function(data){
	$("#toauditcount").html(data.object[0]);$("#tohandlecount").html(data.object[1]);	
	$("#toaudit_span").html(data.object[0]);$("#processing_span").html(data.object[1]);$("#alltask_span").html(data.object[2]);},'json');
});
function dowloadDocs(category){
 	 window.location=yp.bp()+'/sys/taskAction!download.action?doc='+category;
}

</script>
  </body>
</html>
