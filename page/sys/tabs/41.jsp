<%@ page language="java" pageEncoding="UTF-8"%><%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath(); %>
<script src="<%=path %>/ui/js/datagrid_detailview.js"></script>
<style>
	.select_content{
		float: left;
		padding-top: 20px;
	}
	a[id$='_select_button']{
	width: 80px;margin:5px 10px;
	}
	select[id^='select_']{
		border: 1px solid #777;
		border-radius:4px;
		background: #efefef;
	}
</style>
<table id="rolemanage" data-options="fit:true">
       <thead>
            <tr>
                <th field="id" width="30">角色ID</th>
                <th field="name" width="50" sortable="true">角色名称</th>
                <th field="description" align="left" width="200">角色说明</th>
           </tr>
     </thead>
 </table>

<div id="tb_41" style="height:auto">
	<div  style="display: inline;margin-bottom:5px">
		<input id="searchbox_41" class="easyui-searchbox" style="width:150px;" data-options="searcher:searchRole,prompt:'按角色名称搜索'"></input>  
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-yp_searchclean',plain:true" onclick="$('#searchbox_41').searchbox('setValue','');grid_41.datagrid('load',{});">重置搜索</a>
	</div>
	<div style="margin-bottom:5px; display: inline;float: right">
		<div style="float: left;">
			<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-yp_addrole',plain:true" onclick=" method='add'; $('#addRole').panel('setTitle','新建角色').dialog('open');">添加新角色</a>
		</div>
		<div style="float: left;"><div style="float: left;" class="datagrid-btn-separator"></div></div>
		<div style="float: left;">
			<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-yp_key',plain:true" onclick="giveRightsDialog();">角色授权</a>
			<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-yp_editrole',plain:true" onclick="editRole();">编辑</a> 
			<a href="#" class="easyui-linkbutton"data-options="iconCls:'icon-yp_deleterole',plain:true" onclick="deleteRole();">删除</a> 
		</div>
	</div>
	<div style="padding:5px 0; background-color: #EEE2D1;"> 
				<img src="${pageContext.request.contextPath }/ui/easyui/themes/icons/tip.png">
		    	<span style="font-size: 12px;color:#444">单击左边的“+”号可以查看对应角色所拥有的权限信息！</span>
	</div>
</div>
<!-- ===========对话框============-->
<div id="addRole" class="easyui-dialog" title="增加角色" style="background-color: #EFF4FB; width: 400px;height: 200px;"
	data-options="iconCls:'icon-yp_addrole',resizable:true,modal:true,closed:true,buttons:[{text:'保存',
				iconCls:'icon-add',handler:function(){addRole();}
			},{text:'取消',	iconCls:'icon-cancel',handler:function(){$('#addRole').dialog('close');}}]">
		<div style="width: 80%;margin: 10px auto;">
			<table style="width: 100%;font-size: 12px;">
				<tr style="line-height:30px;"><td>角色名称：</td><td><input name="role.name" class="easyui-validatebox" id="roleName" placeholder="角色名称(必填)" style="width:220px;font-size: 12px;" data-options="required:true"></td></tr>
				<tr><td style="vertical-align:top; ">角色说明：</td><td><textarea name="role.description" placeholder="填写角色说明" class="easyui-validatebox" style="width:220px;resize:none;font-size: 12px;"></textarea></td></tr>
			</table>
		</div>
</div>
<!-- =========授权对话框=========== -->
<div id="giveRights" class="easyui-dialog" title="角色授权" style="background-color: #EFF4FB; width:600px;height: 460px;"
	data-options="onClose:clearSelect,iconCls:'icon-yp_keyadd',resizable:true,modal:true,closed:true,buttons:[{text:'确定',
				iconCls:'icon-ok',handler:function(){grant();}
			},{text:'取消',	iconCls:'icon-cancel',handler:function(){ $('#giveRights').dialog('close');}}]">
	<div id="currentRole" style="width: 95%;text-align: right;"></div>
	<div style="margin: 10px auto;padding-left:11%;">
	<div class="select_content">
		<b >未得权限</b><br>
		<select multiple="multiple" id="select_left" style="width:180px;height:300px;margin-top: 5px;"></select>
	</div>
		<div class="select_content" style="padding-top: 100px;">
				<a id="add2right_select_button" onclick="selectItem('select_left','select_right',false);" class="easyui-linkbutton" data-options="iconCls:'icon-yp_toright'">选择</a><br>
				<a id="add_all2right_select_button" onclick="selectItem('select_left','select_right',true);"  class="easyui-linkbutton"  data-options="iconCls:'icon-yp_all2right'">全选</a><br>
				<a id="remove_toleft_select_button"onclick="selectItem('select_right','select_left',false);" class="easyui-linkbutton"  data-options="iconCls:'icon-yp_toleft'">去选</a><br>
				<a id="remove_all2left_select_button" onclick="selectItem('select_right','select_left',true);" class="easyui-linkbutton"  data-options="iconCls:'icon-yp_all2left'">全不选</a>
		</div>
		<div class="select_content" >
		<b>当前拥有的权限</b><br>
			<select multiple="multiple" id="select_right" style="width: 180px;height:300px;margin-top: 5px;"></select>
		</div>
	</div>
</div>  
<script type="text/javascript">
$(function(){
	grid_41=$('#rolemanage').datagrid({
                title:'系统角色',
                toolbar:'#tb_41',
        		url:yp.bp()+'/sys/roleAction!listRoles.action',
                striped : true,//显示斑马线效果
        		rownumbers : true,
        		singleSelect : true,
        		sortName : 'name',
        		sortOrder : 'asc',
        		fitColumns:true,
        		selectOnCheck:false,
        		pagination : true,
        		pageSize : 20,
        		pageList : [ 10, 20, 30, 40, 50, 100 ],
                view: detailview,
                detailFormatter:function(index,row){return '<div style="padding:2px"><table class="detailTable"></table></div>';},
             	onExpandRow: function(index,row){
             		yp.print(row);
                   var detailTable = $(this).datagrid('getRowDetail',index).find('table.detailTable');
                    	detailTable.datagrid({
                    			title:'拥有的权限',
		                    	url:yp.bp()+'/sys/roleAction!getRights.action?role.id='+row.id,
		                     	fitColumns:true,
		                    	singleSelect:true,
		                     	loadMsg:'正在加载权限信息，请稍候...',
		                        height:'auto',
		                        width:820,
			                    onResize:function(){$('#rolemanage').datagrid('fixDetailRowHeight',index);},
		                        columns:[[
		                           	{field:'id',title:'权限ID',width:80,align:'center'},
		                         	{field:'name',title:'权限名称',width:100},
		                            {field:'description',title:'权限说明',width:200},
		                            {field:'url',title:'权限管理对象',width:400}
		                       	]],
		                       onLoadSuccess:function(){
		                            setTimeout(function(){
		                            $('#rolemanage').datagrid('fixDetailRowHeight',index);
		                          },0);
		                       }
                   });
               	$('#rolemanage').datagrid('fixDetailRowHeight',index);
               },
        	onLoadSuccess : function(data) {
        			//parent.$.messager.progress('close');
        			if (null!=data.ok&&!data.ok) {$.messager.alert('提示',data.msg,'info'); }
        	}
           });
		//双击选择权限
		$('#select_left').dblclick(function(){ //绑定双击事件
			selectItem('select_left','select_right',false);
			//$("option:selected",this).appendTo('#select_right'); //追加给对方
		});
		//双击去选权限
		$('#select_right').dblclick(function(){
			selectItem('select_right','select_left',false);
		  	//$("option:selected",this).appendTo('#select_left');
		}); 
});

/**===============数据表格已经加载完毕，且已经将页面中需要的各种对话框初始化完毕 ，以下是业务函数=================**/
 /**
 *角色搜索
 */
 function  searchRole(value,name){
	 	$('#rolemanage').datagrid('load',{
	 		loadtype:'conditional',
	 		searchname:value
	 	});
 }
 /**
 *增加（编辑）角色
 */
 var method='add';
 var roleid=undefined;
 function addRole(){
	 if($("#roleName").val()=='expert'){
		 $.messager.alert('提示','角色名称冲突！','info');return;
	 }
	 if($("#addRole input[name='role.name']").validatebox('isValid')){
			$.get(yp.bp()+'/sys/roleAction!addRole.action',{"method":method,"role.id":roleid,"role.name":$("#addRole input[name='role.name']").val(),"role.description":$("#addRole textarea[name='role.description']").val()},function(data){ $.messager.alert('提示',data.msg,'info');if(data.ok){$('#addRole').dialog('close');grid_41.datagrid('load',{});}},'json');
	}
}
 /**
 *打开角色授权对话框
 */
 function giveRightsDialog(){
	 var role=getItem();
	 if (role) {
		 if (role.superRole) {
			 $.messager.alert('提示','超级管理员的权限不容修改！','info');return;
		} 
		 $("#currentRole").html("当前角色："+role.name);
		 $.get(yp.bp()+'/sys/roleAction!getRights.action?loadtype=select&role.id='+role.id,function(data){
			 loadRights(data[0],'select_left');
			 loadRights(data[1],'select_right');
			 $('#giveRights').dialog('open');
			 },'json');
	} else {
		$.messager.alert('提示','未选择角色！','info');
	}
 }
 /**
 *角色授权
 */
 var hasChanged=false;
 function grant(){
	 if (hasChanged) {
	 $.messager.progress();
	 var role=getItem();
	 var selected=$('#select_right option');
	 var ids="";
	 for(var i=0;i<selected.length;i++){
		 ids=ids+"@"+selected[i].value;
	 }
	 ids=ids.substring(1, ids.length);
	$.get(yp.bp()+'/sys/roleAction!grant.action',{"ids":ids,"role.id":role.id},function(data){$.messager.progress('close'); $.messager.alert('提示',data.msg,'info');if(data.ok){ $('#giveRights').dialog('close');grid_41.datagrid('load',{});}},'json');
	} else {
		 $.messager.alert('提示',"未作修改！",'info');
	}
 }
 /**
 *角色编辑
 */
 function editRole(){
	 method='edit';
	 var role=getItem();
	 if (role) {
		 roleid=role.id;
		 if (role.superRole) {
			 $.messager.alert('提示','超级管理员不可编辑！','info');
			 return;
		}else if(role.name=='expert'){
			$("#roleName").attr("disabled","disabled");
		}else if(role.name!='expert'){
			$("#roleName").attr("disabled",false);
		} 
		 $("#addRole input[name='role.name']").val(role.name);
		 $("#addRole textarea[name='role.description']").val(role.description);
		 $('#addRole').panel('setTitle','编辑角色').dialog('open');
	} else {
		$.messager.alert('提示','未选择角色！','info');
	}
 }
 /**
 *角色删除
 */
 function deleteRole(){
	 var role=getItem();
	 if (role) {
		 if (role.superRole) {
			 $.messager.alert('提示','超级管理员不可删除！','info');return;
		}else if(role.name=='expert'){
			 $.messager.alert('提示','不可删除此角色！','info');return;
		} 
		 $.messager.confirm('确认删除', '警告：确认删除此角色吗？删除后可能导致普通管理员无法访问某些资源，您确定要继续吗？', function(r){
				if (r){	$.messager.progress();
					 roleid=role.id;
					 $.get(yp.bp()+'/sys/roleAction!deleteRole.action',{"role.id":roleid},function(data){ $.messager.alert('提示',data.msg,'info');if(data.ok){grid_41.datagrid('load',{});$.messager.progress('close');}},'json');
				};});
	} else {
		$.messager.alert('提示','未选择角色！','info');
	}
 }
 /**
 *获得选中项
 */
 function getItem(){
	 var row=grid_41.datagrid('getSelected');
	 return row;
 }
 /**
  *将权限数组加载到左右选择框中
  */
 function  loadRights(arr,selectid){
	 //1.根据arr创建options元素并添加到selectid对应的select元素下
	 var select=document.getElementById(selectid); 
	 for(var i=0;i<arr.length;i++){
		// select.options.add(new Option(arr[i].name,arr[i].id)); //这个兼容IE与firefox 
		//1.创建一个option
		var option=new Option(arr[i].name,arr[i].id);
		var group=document.getElementById(selectid+"-"+arr[i].type); //若存在则返回一个optgroup对象，否则返回undefined
				if(group){
					group.appendChild(option);
				}else{
					group=document.createElement('optgroup');  
					group.setAttribute("id", selectid+"-"+arr[i].type);
					group.setAttribute("label", arr[i].type);
					group.appendChild(option);
					select.appendChild(group);
				}
	 }
 }
 /**
 *将元素从id为fromid的select框中，移动至id为toid的select框中
 * @param fromid 源选择框的id
 *@param toid 目的选择框的id
 *@param isAll是否全部移动
 */
 function selectItem(fromid,toid,isAll){
	 if (isAll) {//全部移动
		 var item=mergeOption(fromid,toid);//将两者去重合并
		$("#"+toid).empty();//清空目标框;
		$("#"+fromid).empty();//清空目标框;
		item.appendTo('#'+toid);
		hasChanged=true;
	} else {//有选择的移动
		var opts=$('#'+fromid+' option:selected');
		if(opts.length>0){
			for(var n=0;n<opts.length;n++){
				var option=opts[n];
				var gp=$(option).parent()[0];
				var tgpid=toid+"-"+$(gp).attr("id").split("-")[1];
				var tgp=$("#"+tgpid)[0];
				if (tgp) {//在目标中已经存在此分组了
					$(tgp).append(option);
					hasChanged=true;
				} else {//在目标中不存在此分组
					var group=document.createElement('optgroup');  
					group.setAttribute("id", tgpid);
					group.setAttribute("label", $(gp).attr("label"));
					group.appendChild(option);
					document.getElementById(toid).appendChild(group); 
					hasChanged=true;
				}
				if($(gp).html()==''){//源分组下面已经无任务内容，则删除原分组
					$(gp).remove();
				}
			}
		}else{
			$.messager.alert('提示','未选项项目！','info');
			hasChanged=false;
		}
	}
 }
 /**
 *合并两个select中的optgroup到一个optgroup数组中
 */
 function mergeOption(fromid,toid){//合并到B中
	 var itemsA=$("#"+fromid+">*");
	 for(var i=0;i<itemsA.length;i++){
		 var id=toid+"-"+$(itemsA[i]).attr("id").split("-")[1];//得到的是一个格式为selectid-arr[i].type格式的id
		 var opt=$("#"+id)[0];//得到A中有，同时B中也有且在B中的的共同分组
		if (opt) {//B中存在此分组
			$(opt).append($(itemsA[i]).html());
		} else {//B中无此分组，opt=undefined
			//将整个A组改变id后追加到目标框中
			$(itemsA[i]).attr("id",id);
			$("#"+toid).append($(itemsA[i]));
		}
	 }
	 return $("#"+toid+">*");
 }
 /**
 *授权对话框关闭时应当删除select元素下的子元素
 */
 function clearSelect(){
	 $("#select_left").html("");
	 $("#select_right").html("");
	 hasChanged=false;
 }
 
 function isSuperRole(rowIndex){
	 
 }
</script>
