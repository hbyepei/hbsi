<%@ page language="java" pageEncoding="UTF-8"%>
<style>
#consultant_12_form ul li{line-height: 28px;list-style: none;}
#consultant_12_form ul li span{width:90px; text-align: right;vertical-align: top;display: inline-block;margin-right: 10px;}
</style>
<table id="taskList_12" data-options="fit:true,border:false"></table>
<div id="tb_12" style="padding:5px;height:auto">
	<div id="searchbox_12" style="display: inline;margin-bottom:5px">
		从: <input id="starttime_12"  class="easyui-datebox" name="starttime_12" style="width:90px" data-options="editable:false"> 
		至: <input   id="endtime_12" class="easyui-datebox" name="endtime_12" style="width:90px"data-options="editable:false"> 
		<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-yp_clean'" onclick="$('#starttime_12, #endtime_12').datebox('setValue','');$('#searchbox_12 a:eq(1)').click()">重置</a> 
		 <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="simpleSearch('12','#taskList_12','#searchbox_12');">搜索</a> 
		 <a href="javascript:void(0);" class="easyui-linkbutton"  data-options="iconCls: 'icon-yp_detailsearch'" onclick="openAdvancedSearchDialog('processing_advanced');">高级查找</a>
	</div>
	<div style="margin-bottom:5px; display: inline;float: right">
		<div style="float: left;">
			<a href="javascript:void(0);"  class="easyui-linkbutton admin" data-options="iconCls:'icon-ok',plain:true"  onclick="finishOrTerminate('finish');" >完成调解</a>
			<a href="javascript:void(0);" class="easyui-linkbutton  admin" data-options="iconCls:'icon-yp_stop',plain:true"  onclick="finishOrTerminate('terminate');" >终止调解</a>
			<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true"  onclick="consultant('over');" >填写咨询结果</a>
			<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-yp_writing',plain:true"  onclick="consultant('modify');" >修改咨询意见</a>
		</div>
		<div style="float: left;"><div style="float: left;" class="datagrid-btn-separator"></div></div>
		<div style="float: left;">
			<a href="javascript:void(0);" class="easyui-linkbutton  admin" data-options="iconCls:'icon-edit',plain:true"  onclick="addEditTab(this,'edit','taskList_12');">编辑</a>
			<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-yp_detailsearch',plain:true"  onclick="showdetail('#taskList_12');">查看详情</a>
		</div>
	</div>
</div>
<div id="finish_12" class="easyui-dialog" title="填写备注信息..." style="width:400px;height:180px;"   
        data-options="iconCls:'icon-edit',resizable:true,modal:true,closed:true,buttons:[{
				text:'确定',
				iconCls:'icon-ok',
				handler:function(){var sub=new finishOrTerminate();sub.doSubmit();}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				handler:function(){$('#finish_12').dialog('close');}
			}]">
			<div style="background-color: #EEE2D1;padding:2px 0; " >
					<div style="margin:2px auto;width:95%;height: 20px;">&nbsp;&nbsp;
						<img src="../../js/easyui/themes/icons/tip.png">
						<span style="font-size: 14px;color:#444"><label></label></span>
					</div>
			</div>
			<textarea id="finish_12_textarea"  style="width: 100%;height: 60px; border: none; resize:none;"placeholder="填写备注信息...(选填)">双方已达成一致</textarea>
</div> 
<div id="consultant_12" class="easyui-dialog" title="修改咨询意见" style="width:500px;height:330px;"   
        data-options="iconCls:'icon-edit',cache:false,resizable:true,modal:true,closed:true,buttons:[{
				text:'确定',
				iconCls:'icon-ok',
				handler:function(){var sub=new consultant();sub.doSubmit();}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				handler:function(){$('#consultant_12').dialog('close');}
			}]">
			<div style="background-color: #EEE2D1;padding:2px 0; " >
					<div style="margin:2px auto;width:100%;height: 20px;">&nbsp;&nbsp;
						<span style="font-size: 14px;color:#444">当前处理的任务编号：<label></label></span>
					</div>
			</div>
				<form id="consultant_12_form"  method="post" enctype="multipart/form-data">
					<ul>
						<li id="consultant_12_form_li_firstRow">
								<span>结果:</span>
								<input name="tm.info" type="radio" value="调解正常完成" id="consultant_12_info_a" checked="checked"><label for="consultant_12_info_a">调解正常完成</label>
								<input name="tm.info" type="radio" value="达不成一致" id="consultant_12_info_b"><label for="consultant_12_info_b">达不成一致</label>
						</li>
						<li><span>咨询问题:	</span><textarea name="tm.question" class="easyui-validatebox" data-options="required:true" id="consultant_12_form_li_question" style="resize:none;height: 60px;width:300px;" placeholder="咨询问题...(必填)"></textarea></li>
						<li><span>专家咨询意见:</span><textarea name="tm.consultation"   class="easyui-validatebox" data-options="required:true"  id="consultant_12_form_li_consultation"  style="resize:none;height: 60px;width: 300px;" placeholder="填写咨询意见...(必填)"></textarea></li>
						<li  id="consultant_12_form_li_lastRow"><span>咨询结果文件:</span><input name="upfile" type="file" multiple="multiple"></li>
					</ul>
				</form>
</div> 
<script>
/**===============业务函数=================**/
  var mthd=undefined;
 /**
 * 修改专家咨询意见
 */
 function consultant(mtd){
	 var rows=$('#taskList_12').datagrid('getChecked');
	 if (rows.length>1) {$.messager.alert('提示','一次只能处理一条任务！','info');	} else if(rows.length<1){$.messager.alert('提示','请选择一个任务进行处理！','info');
	 }else {
		checkedRow=rows[0];
		var status=checkedRow.status;
		var dlg=$("#consultant_12");
		$("#consultant_12 div span label").html(checkedRow.caseid);
		$("#consultant_12_form_li_question").val(checkedRow.question);
		var li_1=$("#consultant_12_form_li_firstRow");
		var li_2=$("#consultant_12_form_li_lastRow");
		if(mtd=='modify'){//修改操作
			if(status=='已出咨询意见'){//方可修改（加载历史咨询意见后弹出结论编写对话框）
				/**===============修改并弹出对话框===================**/
				dlg.dialog('setTitle','修改咨询意见');
				li_1.css("display","none");
				li_2.css("display","none");
				$("#consultant_12_form_li_consultation").val(checkedRow.consultation);
				mthd=mtd;
				dlg.dialog('open');
				/**==============修改并弹出对话框====================**/
			} else if(status=='处理中'){//当前状态下无法修改咨询意见
				$.messager.alert('提示','当前状态下无法修改咨询意见！','info');	
			}
		}else if(mtd=='over'){//结束操作
			if(status=='已出咨询意见'){//该任务已经结束，您可以修改填写过的咨询意见
				$.messager.alert('提示','该任务已经结束，您可以修改填写过的咨询意见！','info');	
			} else if(status=='处理中'){//方可结束（弹出结论编写对话框）
				/**===============修改并弹出对话框===================**/
				dlg.dialog('setTitle','填写咨询意见');
				li_1.css("display","inherit");
				li_2.css("display","inherit");
				$("#consultant_12_form_li_consultation").val('');
				mthd=mtd;
				dlg.dialog('open');
				/**===============修改并弹出对话框===================**/
			}
		}
	 }
	 this.doSubmit =function(){
		 var f=$("#consultant_12_form");
		 var url="";
			 if(mthd=='over'){
				url=yp.bp()+'/sys/taskAction!over.action?tm.id='+checkedRow.id;
			 }else if(mthd=='modify'){
				 url=yp.bp()+'/sys/taskAction!modify.action?tm.id='+checkedRow.id;
			 }
			 sbmt(f,url);
	 };
}
 function sbmt(form,url){
	 form.form('submit', {
		    	'url':url,    
		    	onSubmit: function(){
		    	$.messager.progress();
		    	var isValid = $(this).form('validate');
		    	if (!isValid){
					$.messager.progress('close');	
		    		$.messager.alert('提示','内容不能为空!','info');
					return false;
				}
		    	return isValid;
		    },    
		    success:function(data){//成功后，关闭编辑框
		    	var j=yp.json2obj(data);
		    	$.messager.progress('close');
		    	$.messager.alert('提示',j.msg,'info');
		    	if(j.ok){//编辑成功后关闭窗口
		    		$("#consultant_12").dialog('close');
					 $('#taskList_12').datagrid('reload');
					 checkedRow=undefined;
					 mthd=undefined;
		    	}
		    }    
		}); 
 }
 
 /**
 *完成或终止调解
 */
function finishOrTerminate(mtd){
	 var rows=$('#taskList_12').datagrid('getChecked');
	 if (rows.length>1) {$.messager.alert('提示','一次只能处理一条任务！','info');	} else if(rows.length<1){$.messager.alert('提示','请选择一个任务进行处理！','info');
	 }else {
		checkedRow=rows[0];
		var status=checkedRow.status;
		var neede=checkedRow.needexpert;
		var dlg=$("#finish_12");
		var textarea=$("#finish_12_textarea");
		var label=$("#finish_12 div span label");
		if(mtd=='finish'){
			if((status=='处理中'&&neede==false)||status=='已出咨询意见'){//无需专家且在处理中
				dlg.dialog('setTitle','填写备注信息...');
				label.html('完成调解');
				textarea.val('双方已达成一致');
				mthd=mtd;
				dlg.dialog('open');
			}else {
				$.messager.confirm('确认','此任务需要专家协助处理，专家尚未完成操作，您确定要直接完成此次调解任务吗?',function(r){    
				    if (r){
						consultant('over');
				    }
				  });
			}
		}else if(mtd=='terminate'){
			if(status=='处理中'&&neede==true){
				$.messager.confirm('确认','此任务需要专家协助处理，专家尚未完成操作，您确定要直接终止此次调解任务吗?',function(r){    
				    if (r){
				    	consultant('over');
				    }
				  });
			}else{
				dlg.dialog('setTitle','填写终止理由');
				label.html('终止调解');
				textarea.val('');
				textarea.attr('placeholder','填写终止理由...(必填)');
				mthd=mtd;
				dlg.dialog('open');
			}
		}
	 }
	 this.doSubmit =function(){
		 var content=$('#finish_12_textarea').val();
		 if(content.length<4){
			 $.messager.alert('提示','亲，内容太少的感觉啊！！','info');	
		 }else{
			 var msg='';
			 var url='';
			 if(mthd=='finish'){
				 msg='您确认完成此次调解任务吗？';
				 url=yp.bp()+'/sys/taskAction!complete.action';
			 }else if(mthd=='terminate'){
				 msg='您确认终止此次调解任务吗？';
				 url=yp.bp()+'/sys/taskAction!terminate.action';
			 }
			 $.messager.confirm('确认',msg,function(r){    
				    if (r){
				    	 $.messager.progress();
						 $.post(url,	{"tm.id":checkedRow.id,"tm.info":content},
							function(r){
									 $.messager.progress('close');
								if(r.ok){
									 $('#finish_12').dialog('close');
									 var index=$('#taskList_12').datagrid('getRowIndex',checkedRow);
									 $('#taskList_12').datagrid('deleteRow',index);
									 checkedRow=undefined;
									 mthd=undefined;
								}
									$.messager.alert('提示',r.msg,'info');
							},	'json');
				    }    
				});  
		 }
	 };
}
/**
 * 加载数据表格并初始化页面中所需要的各种对话框
 */
$(function() {
	var grid = $('#taskList_12').datagrid({
		title : '处理中的任务',
		url :yp.bp()+"/sys/taskAction!loadTasks.action?loadtype=processing",
		striped : true,//显示斑马线效果
		rownumbers : true,
		selectOnCheck:false,
		pagination : true,
		singleSelect : true,
		idField : 'id',//id域
		remoteSort:false,
		sortName : 'applytime',
		sortOrder : 'desc',
		pagePosition:'both',
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		onDblClickRow:function(rowIndex, rowData){showdetail('#taskList_12');	},
		frozenColumns : [ 
		      [ 
			        {title : 'ID',field : 'id',rowspan:2,checkbox:true}, 
			        {field : 'info',hidden:true}, 
			        {field : 'question',hidden:true}, 
			        {field : 'consultation',hidden:true}, 
			        {title : '编号',field : 'caseid',rowspan:2,sortable : true,align:'center'}, 
			        {title : '状态',field : 'status',rowspan:2,width:90,align:'center',sortable : true}, 
			        {title : '申请事项',field : 'matter',rowspan:2,width:200,sortable : true},
			        {title : '申请日期',field : 'applytime',	rowspan:2,width:75,sortable : true,align:'center',
			        	formatter : function(value, row, index) {
							return value.substring(0,value.indexOf(' ')); }}
		      ] 
		   ],
		columns : [ 
		      [  
		         	{title : '申请类型',field : 'applytype',	rowspan:2,width:60,sortable : true,align:'center'}, 
					{title : '需专家介入',field : 'needexpert',rowspan:2,width:70,sortable : true,align:'center',
											formatter : function(value, row, index) {switch (value) {case true:	return '是';case false:	return '否';}	}	},
					{	title : '凭证文件',field : 'upfile',rowspan:2,width:60,align:'center',
											formatter : function(value, row, index) {//文件数据
												if(value){
													return '<a href="javascript:void(0);" onclick="showFiles(\''+value.join("#")+'\');">单击查看</a>';
												}else{return '无';}
																				}}, 
					{title : '申请方信息',	colspan:4,align:'center'},
					{title : '责任方信息',colspan:4,align:'center'},
					{title : '汽车信息',colspan:4,align:'center'},
					{title : '问题描述',field : 'description',	rowspan:2	,align:'center'}, 
				], 
				[
					{field:'name_1',title:'用户(单位名称)',width:110,align:'center',sortable : true},
					{field:'phone_1',title:'联系方式',width:90,align:'center',sortable : true},
					{field:'code_1',title:'身份证(机构代码)',width:120,sortable : true},
					{field:'id_1',title:'详细信息',align:'center',width:58,formatter : function(value, row, index) {
						if(!value){return '无';
						}else{	return "<a id='rowlink12_"+index+"1' href='javascript:void(0);'  title='"+value+"#"+row.applytype+"' style='text-decoration:none;'>更多</a>"; }}},
					{field:'name_2',title:'单位名称(用户)',width:110,align:'center',sortable : true},
					{field:'phone_2',title:'联系方式',width:90,align:'center',sortable : true},
					{field:'code_2',title:'机构代码(身份证)',width:120,sortable : true},
					{field:'id_2',title:'详细信息',width:58,align:'center',formatter : function(value, row, index) {
						if(!value){return '无';
						}else{
							//在责任方栏中，applytype应当于申请方的相反
							var type=row.applytype;
							if(type=='消费者'){
								type="单位用户";
							}
							return "<a id='rowlink12_"+index+"2' href='javascript:void(0);'title='"+value+"#"+type+"' style='text-decoration:none;'>更多</a>"; }}},
					{field:'brand',title:'汽车品牌',align:'center',sortable : true},
					{field:'model',title:'型号',align:'center',sortable : true},
					{field:'carriage',title:'车架号',align:'center',sortable : true},
					{field:'dealtime',title:'购车时间',sortable : true,align:'center',formatter : function(value, row, index) {
						if (value) {return value.substring(0,value.indexOf(' ')); 	}else return '';}}
		     	], 
		  ],
		toolbar : '#tb_12',
		onLoadSuccess : function(data) {
			if (null!=data.ok&&!data.ok) {
				$.messager.alert('提示',data.msg,'info');
			}
			bindClick('12');//数据表格加载完毕后，为其它信息的超链接绑定点击事件
		}
	});
});
</script>
