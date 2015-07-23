<%@ page language="java" pageEncoding="UTF-8"%>
<style>
#taskAudit table td:nth-child(odd){text-align:right;font-weight: bold; }
</style>
<table id="taskList_11" data-options="fit:true,border:false"></table>
<div id="tb_11" style="padding:5px;height:auto">
	<div id="searchbox_11" style="display: inline;margin-bottom:5px">
		从: <input id="starttime_11" class="easyui-datebox" name="starttime" style="width:90px" data-options="editable:false"> 
		至: <input id="endtime_11" class="easyui-datebox" name="endtime" style="width:90px"data-options="editable:false"> 
		 <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-yp_clean'" onclick="$('#starttime_11, #endtime_11').datebox('setValue','');$('#searchbox_11 a:eq(1)').click()">重置</a> 
		 <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="simpleSearch('11','#taskList_11','#searchbox_11');">搜索</a> 
		 <a href="javascript:void(0);" class="easyui-linkbutton"  data-options="iconCls: 'icon-yp_detailsearch'" onclick="openAdvancedSearchDialog('toaudit_advanced');">高级查找</a>
	</div>
	<div style="margin-bottom:5px; display: inline;float: right">
		<div style="float: left;">
			<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-yp_docbook',plain:true"  onclick="audit();" >任务审核</a>
		</div>
		<div style="float: left;"><div style="float: left;" class="datagrid-btn-separator"></div></div>
		<div style="float: left;">
			<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true"  onclick="addEditTab(this,'edit','taskList_11');">编辑</a>
			<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-yp_detailsearch',plain:true"  onclick="showdetail('#taskList_11');">查看详情</a>
		</div>
	</div>
</div>
<!-- ==================任务审核窗体===========================-->
<div id="taskAudit" style="background-color: #EFF4FB" class="easyui-dialog" data-options="
	title: '任务审核',    
    width: 500,    
    height: 350,    
    cache: true,
    modal: true,
    maximizable:true,
    resizable:true,
 	iconCls: 'icon-yp_task',
    closed:true,
	buttons:[{
	text:'通过审核',
	iconCls:'icon-yp_ok2',
	handler:function(){passAudit();}},
	{
	text:'不予受理',
	iconCls:'icon-yp_stop',
	handler:function(){$('#refuseReason_11').dialog('open');}},
	{
	text:'关闭',
	iconCls:'icon-cancel',
	handler:function(){$('#taskAudit').dialog('close');}
}]
">
<!-- 通过审核或者拒绝受理后，应当刷新表格 -->
	<div style="text-align: right;padding-right: 10px; margin-top: 5px;">
	编号：<label id="taskAudit_caseid"></label>
	<label id="taskAudit_status" style="margin-left: 10px;color:red;"></label></div>
		<fieldset style="border-radius:5px;margin-top: 5px;">
			<legend><b style="color: #2779AA">任务信息</b></legend>
					<table style="font-size: 12px;width: 100%;">
						<tr>
							<td>申请日期：</td><td id="taskAudit_applytime"></td>
							<td  style="margin-left: 20px;">需要专家：</td><td  id="taskAudit_needexpert"></td>
							<td>申请主体：</td><td  id="taskAudit_applytype"></td>
						</tr>
						<tr>
							<td>申请事项：</td><td colspan="5"  id="taskAudit_matter" ></td>
						</tr>
						<tr>
							<td style="vertical-align: top;">问题描述：</td><td colspan="5" >
								<textarea id="taskAudit_description" rows="5" style="width: 100%;overflow: auto; resize:none; font-size: 12px;border: none; background-color:#F0F5FB " readonly="readonly"  ></textarea>
							</td>
						</tr>
				</table>
		</fieldset>
		<div style="padding-left: 10px;margin-top: 5px;">
			<input type="checkbox" id="setexpert"><label for="setexpert">为此任务分配专家</label>
			<!-- ==================专家选择窗体============================= -->
		        <select id="chooseExperts" class="easyui-combogrid" style="width:250px" data-options="
			            panelWidth: 600,
			            panelHeight: 300,
			            panelMinWidth: '50%',
			            top:100,
			            idField: 'id',
			            textField: 'username',
			            url: '../../sys/expertAction!getExperts.action?loadtype=combogrid',
			            method: 'get',
			            multiple: true,
			            striped:true,
			            disabled:true,
			            remoteSort:false,
			            fit: true,
			            editable:false,
			            selectOnNavigation:false,
			            columns: [[
			                {field:'id',checkbox:true},
			                {field:'image',title:'头像',width:40,formatter: function(value,row,index){
									return '&lt;a href=\'javascript:void(0);\' title=\''+value+'\' onclick=\'yp.showPic(this.title);\' &gt;&lt;img src=\''+yp.bp()+value+'\' alt=\''+row.name+'\'  style=\'width:20px;height:20px;\'&gt;&lt;a/&gt;';
								}
			                },
			                {field:'username',title:'姓名',width:50,sortable:true},
			                {field:'letterid',title:'聘书编号',width:100,sortable:true},
			              	{field:'gender',title:'性别',width:30,sortable:true},
			              	{field:'currenttasks',title:'正处理的任务数',width:100,align:'center',sortable:true},
			                {field:'technology',title:'技术组',width:80,sortable:true},
			                {field:'brand',title:'现服品牌',width:150,sortable:true},
			                {field:'area',title:'来源地区',width:120}
			          ]],
			        pagination:true,
					pageSize:20, 
					sortName : 'username',
					sortOrder : 'desc',
			        pageList : [ 10, 20, 30, 40, 50, 100, 200],
			        toolbar:'#toolbar_expertcombogrid_11',
			        onChange:function(newValue,oldValue){
			        	if(newValue.length>5){
			        		var arr=oldValue.slice(0,5)
			        		$('#chooseExperts').combogrid('setValues',arr);
			        		$.messager.alert('提示','不能选择超过五个专家，系统将只取用前选择的前五个专家！','info');
			        	}
			        }
			       ">
			  </select>
		</div>
</div> 
<div id="toolbar_expertcombogrid_11">
			&nbsp;<img src="../../js/easyui/themes/icons/yp_user.png" style="vertical-align: middle;"/>
			<label style="color: #279FD2">显示组: </label><input class="easyui-combobox"  data-options="
		         url:'../../sys/expertAction!getExpertsGroupComboInfo.action',
		        method:'get',
		        valueField:'id',
      			textField:'text',
      			panelHeight:'auto',
      			formatter:function(row){return '&lt;span style=\'color:#279FD2;cursor:pointer;\'&gt;' + row.text + '&lt;/span&gt;';},
      			onSelect:function(record){$('#chooseExperts').combogrid('grid').datagrid('load',{groupID:record.id}); }
		"></input>
</div> 
<div id="refuseReason_11" class="easyui-dialog" title="填写不予受理的理由..." style="width:400px;height:200px;"   
        data-options="iconCls:'icon-edit',resizable:true,modal:true,closed:true,buttons:[{
				text:'确定',
				iconCls:'icon-ok',
				handler:function(){refuse();}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				handler:function(){$('#refuseReason_11').dialog('close');}
			}]">
			<textarea style="width: 100%;height: 96%;border: none;resize:none;"placeholder="填写不予受理的理由...(必填)"></textarea>
</div> 
<script>
/**
 * 加载数据表格并初始化页面中所需要的各种对话框
 */
$(function() {
	var grid = $('#taskList_11').datagrid({
		title : '待审核任务列表',
		url :yp.bp()+"/sys/taskAction!loadTasks.action?loadtype=toaudit",
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
		onDblClickRow:function(rowIndex, rowData){//双击一行时打开查看详情对话框
			showdetail('#taskList_11');
		},
		frozenColumns : [ 
		      [ 
			        {title : 'ID',field : 'id',rowspan:2,checkbox:true}, 
			        {title : '编号',field : 'caseid',rowspan:2,sortable : true,align:'center'}, 
			        {title : '状态',field : 'status',rowspan:2,width:45,align:'center'}, 
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
					{field:'name_1',title:'用户(单位名称)',width:110,sortable : true,align:'center'},
					{field:'phone_1',title:'联系方式',width:90,sortable : true,align:'center'},
					{field:'code_1',title:'身份证(机构代码)',sortable : true,width:120},
					{field:'id_1',title:'详细信息',align:'center',width:58,formatter : function(value, row, index) {
						if(!value){return '无';
						}else{	return "<a id='rowlink11_"+index+"1' href='javascript:void(0);'  title='"+value+"#"+row.applytype+"' style='text-decoration:none;'>更多</a>"; }}},
					{field:'name_2',title:'单位名称(用户)',width:110,sortable : true,align:'center'},
					{field:'phone_2',title:'联系方式',width:90,sortable : true,align:'center'},
					{field:'code_2',title:'机构代码(身份证)',sortable : true,width:120},
					{field:'id_2',title:'详细信息',width:58,align:'center',formatter : function(value, row, index) {
						if(!value){return '无';
						}else{
							//在责任方栏中，applytype应当于申请方的相反
							var type=row.applytype;
							if(type=='消费者'){
								type="单位用户";
							}
							return "<a id='rowlink11_"+index+"2' href='javascript:void(0);'title='"+value+"#"+type+"' style='text-decoration:none;'>更多</a>"; }}},
					{field:'brand',title:'汽车品牌',sortable : true,align:'center'},
					{field:'model',title:'型号',sortable : true,align:'center'},
					{field:'carriage',title:'车架号',sortable : true,align:'center'},
					{field:'dealtime',title:'购车时间',sortable : true,align:'center',formatter : function(value, row, index) {
						if (value) {return value.substring(0,value.indexOf(' ')); 	}else return '';}}
		     	], 
		  ],
		toolbar : '#tb_11',
		onLoadSuccess : function(data) {
			if (null!=data.ok&&!data.ok) {
				$.messager.alert('提示',data.msg,'info');
			}
			bindClick('11');//数据表格加载完毕后，为其它信息的超链接绑定点击事件
		}
	});
});

/**===============数据表格已经加载完毕，且已经将页面中需要的各种对话框初始化完毕 ，以下是业务函数=================**/

 /**
 *通过审核
 */
 function passAudit(){
	 if(checkedRow){
		 var params;
		 var setneede= document.getElementById('setexpert').checked;//勾选了分配专家选项
		 var neede=checkedRow.needexpert;//此任务本来是否需要专家
		 var expertids=$('#chooseExperts').combogrid('getValues');
		 if(neede){//本来需要专家
			 if(setneede){//勾选了分配专家按钮
				 if(expertids.length<1){
					 $.messager.alert('错误','若需要分配专家，那么请选择要分配的专家后再通过审核！','error');	
				 }else{
					 //通过审核，并上传专家id，分配专家
					var ids=expertids.join(",");
					 params={"id":checkedRow.id,"tm.info":ids,"needexpert":true};
					 handleAudit(params);
				 }
			 }else{
				 $.messager.confirm('确认','本任务需要分配专家，您确认不分配专家吗?（若不分配专家，将视为其不需要专家，不过通过审核之后，您还可能通过修改此任务属性来为其再行分配专家。）',function(r){    
					    if (r){
					        //通过审核，将需专家介入属性置为false
					    	params={"id":checkedRow.id,"needexpert":false};
							 handleAudit(params);
					    }
					}); 
			 }
		 }else{
 			if(setneede){//勾选了分配专家按钮，提示
 				$.messager.confirm('确认','本任务中，用户未要求分配专家，您确认要为其分配专家协助调解吗?（若分配了专家，系统将会通过E-mail通知相关专家。）',function(r){    
				    if (r){
				    	if(expertids.length<1){
							 $.messager.alert('错误','若需要分配专家，那么请选择要分配的专家后再通过审核！','error');	
						 }else{
				        //通过审核，将需专家介入属性置为true，并分配专家
				        	var ids=expertids.join(",");
							 params={"id":checkedRow.id,"tm.info":ids,"needexpert":true};
							 handleAudit(params);
						 }   
				    }
				}); 
			 }else{
				 //直接通过审核，不分配专家
				 params={"id":checkedRow.id,"needexpert":false};
				 handleAudit(params);
			 }
		 }
	 }else{
		 $.messager.alert('错误','未选中数据！','error');	
	 }
 }
 function handleAudit(params){
	 var url=yp.bp()+'/sys/taskAction!audit.action';
	 $.get(url,params,function(r){
					$.messager.alert('提示',r.msg,'info');
					if(r.ok){
						 $('#taskAudit').dialog('close');
						 var index=$('#taskList_11').datagrid('getRowIndex',checkedRow);
						 $('#taskList_11').datagrid('deleteRow',index);
						 checkedRow=undefined;
		}},	'json');
 }
 
 /**
 *拒绝受理
 */
 function refuse(){
	 if(checkedRow){
		 var content=$('#refuseReason_11 textarea').val();
		 if(content.length<5){
			 $.messager.alert('提示','亲，内容太少的感觉啊！！','info');	
		 }else{
			 //拒绝，并将content做为拒绝理由提交。再关闭审核对话框，若操作成功，则将handleSucc置为true
			 $.post(yp.bp()+'/sys/taskAction!refuse.action',
				{id:checkedRow.id,reason:content},
				function(r){
					if(r.ok){
						 $('#refuseReason_11').dialog('close');
						 var index=$('#taskList_11').datagrid('getRowIndex',checkedRow);
						 $('#taskList_11').datagrid('deleteRow',index);
						 checkedRow=undefined;
						 $('#taskAudit').dialog('close');
					}else{
						$.messager.alert('提示',r.msg,'info');
					}
				},	'json');
		 }
	 } else{
		 $.messager.alert('错误','未选中数据！','error');	
	 }
 }
 /**
  * 任务审核，若通过审核，则根据需要分配专家。否则给出理由！
  */
 function audit(){
 	 var rows=$('#taskList_11').datagrid('getChecked');
 	 if (rows.length>1) {$.messager.alert('提示','一次只能处理一条任务！','info');	} else if(rows.length<1){$.messager.alert('提示','请选择一个任务进行处理！','info');
 	 }else {
 		checkedRow=rows[0];
 		$("#taskAudit_caseid").html(checkedRow.caseid);
 		$("#taskAudit_status").html(checkedRow.status);
 		$("#taskAudit_applytime").html(checkedRow.applytime);
 		$("#taskAudit_needexpert").html(checkedRow.needexpert?'是':'否');
 		$("#taskAudit_applytype").html(checkedRow.applytype);
 		$("#taskAudit_matter").html(checkedRow.matter);
 		$("#taskAudit_description").html(checkedRow.description);
 		$("#setexpert").on('change',function(){
 			var f= document.getElementById('setexpert').checked;
 			if(f){$("#chooseExperts").combogrid("enable");}else{$("#chooseExperts").combogrid("disable").combogrid('clear');}
 		});
 		if(checkedRow.needexpert){
 			$("#setexpert").attr('checked',true);
 		}
 		$("#taskAudit").dialog('open');
 	 }	
 }
</script>
