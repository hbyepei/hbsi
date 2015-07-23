<%@ page language="java" pageEncoding="UTF-8"%>
<style>
#consultant_13_form ul li{line-height: 28px;list-style: none;}
#consultant_13_form ul li span{width:90px; text-align: right;vertical-align: top;display: inline-block;margin-right: 10px;}
</style>
<table id="taskList_13" data-options="fit:true,border:false"></table>
<div id="tb_13" style="padding:5px;height:auto">
	<div id="searchbox_13" style="display: inline;margin-bottom:5px">
		从: <input id="starttime_13"  class="easyui-datebox" name="starttime_13" style="width:90px" data-options="editable:false"> 
		至: <input   id="endtime_13" class="easyui-datebox" name="endtime_13" style="width:90px"data-options="editable:false"> 
		<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-yp_clean'" onclick="$('#starttime_13, #endtime_13').datebox('setValue','');$('#searchbox_13 a:eq(1)').click()">重置</a> 
		 <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="simpleSearch('13','#taskList_13','#searchbox_13');">搜索</a> 
		 <a href="javascript:void(0);" class="easyui-linkbutton"  data-options="iconCls: 'icon-yp_detailsearch'" onclick="openAdvancedSearchDialog('all_advanced');">高级查找</a>
	</div>
	<div style="margin-bottom:5px; display: inline;float: right">
		<div style="float: left;">
			<a href="javascript:void(0);" title="全部导出到Excel文件" class="easyui-linkbutton" data-options="iconCls:'icon-yp_excel',plain:true"  onclick="exportAll();" >全部导出</a>
			<a href="javascript:void(0);" title="将选定的任务信息生成Word文件并导出"  class="easyui-linkbutton" data-options="iconCls:'icon-yp_word',plain:true"  onclick="save2Word();" >存档</a>
			<a href="javascript:void(0);" title="查看与任务相关的文件"  class="easyui-linkbutton" data-options="iconCls:'icon-yp_doc',plain:true"  onclick="openFileDialog();" >相关文件</a>
			<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-yp_detailsearch',plain:true"  onclick="showdetail('#taskList_13');">查看详情</a>
		</div>
	</div>
</div>
<div id="filesList_13" class="easyui-dialog" data-options="
	    title: '相关文件列表',    
	    width: 400,    
	    closed: true,    
	    cache: false,  
	    iconCls:'icon-yp_file',
	    modal: true,
	    onClose:function(){
	    	$('#filesList_13 ul').html('');
	    },
	    buttons:[{text:'关闭',
			handler:function(){$('#filesList_13').dialog('close');}}]
"><ul></ul></div>  
<script>
/**===============业务函数=================**/
 function exportAll(){
	 $.messager.confirm('确认对话框', '是否将<b style=\'color:red;\'>正在进行中的任务</b>也一并导出到一个Excel文件中？', function(r){
		 $.messager.progress({
			 title:'处理数据',
			 msg:'请稍候',
			 text:'正在合并数据...',
			 interval:500
		 });
		 var url=yp.bp()+"/sys/taskAction!generateExportInputStream.action?doc=excel&exportAll="+r;
		 $.get(url,function(data){
			 $.messager.progress('close');
			 if(data.ok){
				 window.location = yp.bp() + "/sys/taskAction!export2File.action?doc=excel";
			 }else{
				 $.messager.alert('提示',data.msg,'info');
			 }
		 },'json');
	});
} 
 function save2Word(){
	 var rows=$('#taskList_13').datagrid('getChecked');
	 if (rows.length<1) {
		 $.messager.alert('提示','未选择数据！','info'); 
	 }else {
		 $.messager.progress({
			 title:'处理数据',
			 msg:'请稍候',
			 text:'正在生成数据文件...',
			 interval:500
		 });
		 var docids="";
		 for(var i=0;i<rows.length;i++){
			 docids+=rows[i].id+",";
		 }
		 var url=yp.bp()+"/sys/taskAction!generateExportInputStream.action?doc=word&docids="+docids.substring(0, docids.length-1);
		 $.get(url,function(data){
			 $.messager.progress('close');
			 if(data.ok){
				 window.location = yp.bp() + "/sys/taskAction!export2File.action?doc=word";
			 }else{
				 $.messager.alert('提示',data.msg,'info');
			 }
		 },'json');
	 }
 }
 function openFileDialog(){
	 var rows=$('#taskList_13').datagrid('getChecked');
	 if (rows.length<1) {
		 $.messager.alert('提示','未选择数据！','info'); 
	 }else if(rows.length>1){
		 $.messager.alert('提示','选择的条目过多！','info'); 
	 }else{
		 	var files=rows[0].upfile;
		 	var count=0;
		 	if(files&&files.length>0){//显示凭证文件
		 		$("#filesList_13 ul").append("<li style='list-style: none; font-weight:bold;margin-left:-30px;'>凭证文件("+files.length+"):</li>");
				for ( var i in files) {
					var f = files[i];
					$("#filesList_13 ul").append("<li><a href='javascript:void(0);' onclick='showFile(\"" + f[0] + "\",\""+f[1]+ "\")'>" + f[1] + "</a></li>");
				}
				count++;
		 	}
			var status=rows[0].status;
			if(status&&status=='调解终止'){
				$("#filesList_13 ul").append("<li  style='list-style: none; font-weight:bold;margin-left:-30px;'>调解终结通知书:</li>");
				$("#filesList_13 ul").append("<li><a href='../../user/applyAction!downloadUserDoc.action?docname=terminatedoc&taskid="+rows[0].id+"'>家用汽车产品三包责任争议第三方调解终结通知书.doc</a></li>");
				count++;
			}else if(status&&rows[0].needexpert&&(status=='已出咨询意见'||status=='调解完成')){
				$("#filesList_13 ul").append("<li  style='list-style: none; font-weight:bold;margin-left:-30px;'>技术咨询结果:</li>");
				$("#filesList_13 ul").append("<li><a href='../../user/applyAction!downloadUserDoc.action?docname=consult_resultdoc&taskid="+rows[0].id+"'>家用汽车疑难问题技术咨询结果.doc</a></li>");
				count++;
			}
			if(count<1){
				$("#filesList_13 ul").append("<li  style='list-style: none; font-weight:bold;margin-left:-30px;'>无任何文件</li>");
			}
		$('#filesList_13').dialog('open');
	 }
 }
 
$(function() {
	var grid = $('#taskList_13').datagrid({
		title : '所有任务列表',
		url :yp.bp()+"/sys/taskAction!loadTasks.action?loadtype=all",
		striped : true,//显示斑马线效果
		rownumbers : true,
		selectOnCheck:false,
		checkOnSelect:false,
		pagination : true,
		idField : 'id',//id域
		remoteSort:false,
		sortName : 'applytime',
		sortOrder : 'desc',
		pageSize : 20,
		pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
		onDblClickRow:function(rowIndex, rowData){showdetail('#taskList_13');	},
		frozenColumns : [ 
		      [ 
			        {title : 'ID',field : 'id',checkbox:true}, 
			        {title : '编号',field : 'caseid',sortable : true,align:'center'}, 
			        {title : '状态',field : 'status',width:90,align:'center',sortable : true}, 
			        {title : '申请事项',field : 'matter',width:200,sortable : true},
			        {title : '申请日期',field : 'applytime',width:75,sortable : true,align:'center',formatter : function(value, row, index) {return value.substring(0,value.indexOf(' ')); }}
		      ] 
		   ],
		columns : [ 
		      [  
		         	{title : '申请类型',field : 'applytype',width:60,sortable : true,align:'center'}, 
					{title : '需专家介入',field : 'needexpert',width:70,sortable : true,align:'center',formatter : function(value, row, index) {switch (value) {case true:	return '是';case false:	return '否';}	}	},
					{title : '申请方信息',field:'id_1',width:70,align:'center',formatter : function(value, row, index) {
						if(!value){return '无';}else{	return "<a id='rowlink13_"+index+"1' href='javascript:void(0);'  title='"+value+"#"+row.applytype+"' style='text-decoration:none;'>查看</a>"; }}},
					{title : '责任方信息',field:'id_2',width:70,align:'center',formatter : function(value, row, index) {
						if(!value){return '无';}else{var type=row.applytype;if(type=='消费者'){type="单位用户";}return "<a id='rowlink13_"+index+"2' href='javascript:void(0);'title='"+value+"#"+type+"' style='text-decoration:none;'>查看</a>"; }}},
					{title : '咨询问题',field : 'question',width:180},
					{title : '咨询意见',field : 'consultation',width:180,formatter:function(value,row,index){var sts=row.status;var ne=row.needexpert;if(!ne){return '无';}else if(ne&&(sts!='调解完成'||sts!='已出咨询意见')){return '专家咨询尚未完成';}else{return value;}}},
					{title : '备注',field : 'info',width:150}
				]
		  ],
		toolbar : '#tb_13',
		onLoadSuccess : function(data) {
			if (null!=data.ok&&!data.ok) {
				$.messager.alert('提示',data.msg,'info');
			}
			bindClick('13');//数据表格加载完毕后，为其它信息的超链接绑定点击事件
		}
	});
});
</script>
