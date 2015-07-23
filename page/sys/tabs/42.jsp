<%@ page language="java" pageEncoding="UTF-8"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath(); %>
<script src="<%=path %>/ui/js/datagrid_groupview.js"></script>
<table  id="rightmanage"></table>
<div id="tb_42" style="height:auto">
	<div  style="display: inline;margin-bottom:5px">
		<input id="searchbox_42" class="easyui-searchbox" style="width:150px;" data-options="searcher:searchRight,prompt:'搜索权限名称'"></input>  
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-yp_searchclean',plain:true" onclick="$('#searchbox_42').searchbox('setValue','');grid_42.datagrid('load',{});">重置搜索</a>
	</div>
	<div style="margin-bottom:5px; display: inline;float: right">
		<div style="float: left;margin-right: 15px;">
			<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-yp_keyadd',plain:true" onclick="method='add'; $('#addRight').panel('setTitle','新建权限').dialog('open');">新建权限</a>
		</div>
	</div>
</div>

<!-- ===========对话框============-->
<div id="addRight" class="easyui-dialog" title="新建权限" style="background-color: #EFF4FB; width: 400px;height: 300px;"
	data-options="iconCls:'icon-yp_addrole',resizable:true,modal:true,closed:true,buttons:[{text:'保存',
				iconCls:'icon-add',handler:function(){addRight();}
			},{text:'取消',	iconCls:'icon-cancel',handler:function(){$('#addRight').dialog('close');}}]">
		<div style="width: 80%;margin: 10px auto;"><form id="addRightForm" method="post">
			<table style="width: 100%;font-size: 12px;">
				<tr style="line-height:30px;"><td>权限名称：</td><td><input name="right.name" class="easyui-validatebox" placeholder="权限名称(必填)" style="width:220px;font-size: 12px;" data-options="required:true"></td></tr>
				<tr><td style="vertical-align:top; ">权限说明：</td><td><textarea name="right.description" placeholder="填写权限说明" class="easyui-validatebox" style="width:220px;resize:none;font-size: 12px;"></textarea></td></tr>
				<tr style="line-height:30px;"><td>权限资源：</td><td><input name="right.url" class="easyui-validatebox" placeholder="权限资源(必填)" style="width:220px;font-size: 12px;" data-options="required:true"></td></tr>
				<tr><td>权限类型：</td><td><input id="rightTypeCombo"  class="easyui-combobox" name="right.rightType"style="width: 120px;" data-options="editable:false,valueField:'id',textField:'text', panelHeight:'auto',url: '${pageContext.request.contextPath }/sys/rightAction!getRightTypeList.action',required:true"></td></tr>
			</table></form>
		</div>
		<div style="padding:10px 0;text-align: center;margin-bottom: 10px;background-color: #EEE2D1;"> 
				<img src="${pageContext.request.contextPath }/ui/easyui/themes/icons/tip.png">
		    	<span style="font-size: 12px;color:#444">请确保权限资源的路径是正确的，否则权限无效！</span>
			</div>
</div>
<script type="text/javascript">
 $(function(){
	grid_42=$('#rightmanage').datagrid({
                title:'系统权限',
                toolbar:'#tb_42',
        		url:yp.bp()+'/sys/rightAction!listRights.action',
                striped : true,//显示斑马线效果
        		sortName : 'name',
        		sortOrder : 'desc',
                singleSelect:true,
                collapsible:true,
                rownumbers:false,
                fit:true,
                fitColumns:true,
                pagination : true,
        		pageSize : 50,
        		pageList : [ 20, 30,40, 50,60,70,80 ],
                view:groupview,
              	groupField:'type',
              	groupFormatter:function(value,rows){return value + ' - (' + rows.length+')'; },
	        	onLoadSuccess : function(data) {
        			//parent.$.messager.progress('close');
        			if (null!=data.ok&&!data.ok) {$.messager.alert('提示',data.msg,'info'); }
        		},
              	columns : [ 
             		      [  
             		         	{title : 'ID',field : 'id',width:50,align:'center' },
             		         	{title : '权限名称',field : 'name',width:100,sortable : true,align:'center'}, 
             		         	{title : '权限说明',field : 'description',width:300}, 
             		         	{title : '权限资源',field : 'url',width:150}, 
             		         	{title : '操作',field : 'operation',width:32,formatter:function(value,row,index){
             		         		return "<img alt='编辑' style='cursor: pointer;' onclick=editRight("+index+"); src='<%=path %>/ui/easyui/themes/icons/pencil.png'> <b>|</b> "
             		         		+"<img alt='删除' style='cursor: pointer;' onclick=deleteRight("+index+"); src='<%=path %>/ui/easyui/themes/icons/yp_delete.png'>";
             		         	}}
             				]
             		  ]
           });

}); 

/**===============数据表格已经加载完毕，且已经将页面中需要的各种对话框初始化完毕 ，以下是业务函数=================**/
 /**
 *角色搜索
 */
 function  searchRight(value,name){
	 	$('#rightmanage').datagrid('load',{
	 		loadtype:'conditional',
	 		searchname:value
	 	});
 }
 /**
 *增加(或修改)权限
 */
 var method;
 var rightid=undefined;
 function addRight(){
	 var f=$("#addRightForm");
	 f.form('submit', {    
		    url:yp.bp()+'/sys/rightAction!addRight.action?method='+method+'&right.id='+rightid,
		    onSubmit: function(){
		    	$.messager.progress();
		    	var isValid = $(this).form('validate');
				if (!isValid){$.messager.progress('close');	}return isValid;
		    },    
		    success:function(data){    
		    	var obj =yp.json2obj(data);
		    	$.messager.progress('close');
		    	 $.messager.alert('提示',obj.msg,'info');if(obj.ok){$('#addRight').dialog('close');grid_42.datagrid('load',{});}
		    }    
		});  
 }
 /**
 *编辑权限
 */
 function editRight(rowindex){
	 method='edit';
	 var row=grid_42.datagrid('selectRow',rowindex).datagrid('getSelected');
	 rightid=row.id;
	 
	 if (isSuperRight(rowindex)) {
		 $.messager.alert('提示',"超级管理员的专属权限，不容修改！",'info'); return;
	}
	var obj=yp.addPrefix(row, 'right.');//为对象的每个属性前面加上前缀
	 $('#addRight').panel('setTitle','编辑权限').dialog('open');
	 $("#rightTypeCombo").combobox('select',yp.getComboboxValue('rightTypeCombo', row.type));//需要将下拉列表框也选中
	 $('#addRightForm').form('load',obj);//表单填充
 }
 /**
 *删除权限
 */
 function deleteRight(rowindex){
	 if (isSuperRight(rowindex)) {
		 $.messager.alert('提示',"超级管理员的专属权限，不可删除！",'info'); return;
	}
	 $.messager.confirm('确认删除', '警告：确认删除此权限吗？删除后可能导致普通管理员无法访问该资源，您确定要继续吗？', function(r){
			if (r){
				 var id=grid_42.datagrid('selectRow',rowindex).datagrid('getSelected').id;
				$.messager.progress();
				$.post(yp.bp()+'/sys/rightAction!deleteRight.action',{'right.id':id},function(data){
				$.messager.alert('提示',data.msg,'info');
				if(data.ok){grid_42.datagrid('load',{});$.messager.progress('close');}},'json');
			}
 	});
 }
 function isSuperRight(rowIndex){
	 var row=grid_42.datagrid('selectRow',rowIndex).datagrid('getSelected');
	return row&&row.superRight;
 }
</script>
