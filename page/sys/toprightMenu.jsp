<%@ page language="java" pageEncoding="UTF-8"%>
<style>
#updatePwdForm table tr td:first-child {
	width: 100px;
	text-align: right;
}
#updatePwdForm table tr td+td {
	width: 200px;
}
</style>
<div style="position: absolute; right: 40px;top:70px;">
	<div>
		 <a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#setup',iconCls:'icon-yp_sys'">设置</a> 
		 <a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#control',iconCls:'icon-yp_person'">个人信息</a> 
		 <a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-yp_out',plain:true" onclick="logout();">注销</a>
	</div>
	<div id="setup" >
		<div>系统设置</div>
		<div class="menu-sep"></div>
		<div  data-options="iconCls:'icon-yp_skin'">
			<span>更换皮肤</span>
			<div style="width:150px">
				<!-- <div onclick="yp.changeTheme('default');" title="default" data-options="iconCls:'icon-yp_skin_blue'">天蓝色</div> -->
				<div onclick="yp.changeTheme('gray');" title="gray" data-options="iconCls:'icon-yp_skin_gray'">银灰</div>
				<div onclick="yp.changeTheme('black');" title="black" data-options="iconCls:'icon-yp_skin_gray'">暗夜</div>
				<div onclick="yp.changeTheme('bootstrap');" title="bootstrap" data-options="iconCls:'icon-yp_skin_blue'">天蓝</div>
				<div onclick="yp.changeTheme('ui-cupertino');" title="ui-cupertino" data-options="iconCls:'icon-yp_skin_green'" >青绿色</div>
				<div onclick="yp.changeTheme('ui-dark-hive');" title="ui-dark-hive" data-options="iconCls:'icon-yp_skin_dark-black'">酷黑金属</div>
				<div onclick="yp.changeTheme('ui-pepper-grinder');" title="ui-pepper-grinder" data-options="iconCls:'icon-yp_skin_orange'">牛皮纸</div>
				<div onclick="yp.changeTheme('ui-sunny');" title="ui-sunny">午后阳光</div>
				<div class="menu-sep"></div>
				<div>
					<span>metro风格</span>
					<div style="width:150px;">
						<div onclick="yp.changeTheme('metro');" title="metro">metro-清新</div>
						<div onclick="yp.changeTheme('metro-blue');" title="metro-blue" data-options="iconCls:'icon-yp_skin_blue'">metro-蓝</div>
						<div onclick="yp.changeTheme('metro-gray');" title="metro-gray" data-options="iconCls:'icon-yp_skin_gray'">metro-灰</div>
						<div onclick="yp.changeTheme('metro-green');" title="metro-green" data-options="iconCls:'icon-yp_skin_green'">metro-绿</div>
						<div onclick="yp.changeTheme('metro-orange');" title="metro-orange" data-options="iconCls:'icon-yp_skin_orange'">metro-橙</div>
						<div onclick="yp.changeTheme('metro-red');" title="metro-red" data-options="iconCls:'icon-yp_skin_red'">metro-红</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="control">
		<div onclick="$('#updatePwd').dialog('open');" data-options="iconCls:'icon-yp_writing'">修改密码</div>
		<div class="menu-sep"></div>
		<div onclick="$('#userInfo').window('open')" data-options="iconCls:'icon-yp_person'">我的信息</div>
	</div>
</div>
<!---------------------------- 弹出式窗口 ------------------------>
<!---------------------------- 1.修改密码 ------------------------>
<div id="updatePwd" style="background-color: #EFF4FB" class="easyui-dialog" data-options="
					title: '修改密码',    
				    width: 350,    
				    height: 200,    
				    modal: true,
	    		    iconCls: 'icon-yp_keyedit',
				    closed:true,
					buttons:[
					{
					text:'确定',
					iconCls:'icon-ok',
					handler:function(){updatePwd();}
				},{
					text:'关闭',
					iconCls:'icon-cancel',
					handler:function(){$('#updatePwd').dialog('close');}
				}]
">
	<form>
		<table style="margin-left: 30px;margin-top: 30px;">
			<tr>
				<td><label for="password">原密码:</label></td>
				<td><input class="easyui-validatebox" type="text" name="password" data-options="required:true" /></td>
			</tr>
			<tr>
				<td><label for="newPwd">新密码:</label></td>
				<td><input id="newPwd" class="easyui-validatebox" type="password" name="newPwd" data-options="required:true,validType:['length[5,15]','password']" /></td>
			</tr>
			<tr>
				<td><label for="newPwd2">确认密码:</label></td>
				<td><input class="easyui-validatebox" type="password" data-options="validType:'eqPwd[\'#newPwd\']'" /></td>
			</tr>
		</table>
	</form>
</div>

<script>
/*******************注销********************/
 	function updatePwd(){
 		$.messager.progress();
		$('#updatePwd form').form('submit', {    
			url:yp.bp()+'/loginAction!updatePwd.action',
		    onSubmit: function(){  
		    	var isValid = $(this).form('validate');
		    	if (!isValid){
		    		$.messager.progress('close');
					return false;
				}
		    	return isValid;
		    },    
		    success:function(data){
		    	$.messager.progress('close');
		    	 var obj=yp.json2obj(data);
		    	 $.messager.alert('提示',obj.msg, 'info');
	             if(obj.ok){$('#updatePwd').dialog('close');
		    }}  
		});  
	}
	function logout(){
			$.get(
					yp.bp()+'/loginAction!logout.action',{},
					function(result) {
						if (result.ok) {
							location.replace(yp.bp()+'/index.jsp');
						}
				}, 'json');
		};
</script>