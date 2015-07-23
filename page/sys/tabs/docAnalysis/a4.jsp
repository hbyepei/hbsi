<%@ page language="java" pageEncoding="UTF-8"%>

<div id="toolbar_a4" style="float: right;">
年份选择：<select  id="applytime_year_a4" name="year" style="width: 80px;"></select>
</div>
<table id="a4_table" ></table> 
<script>
$('#a4_table').datagrid({ 
    url:yp.bp()+"/sys/docAnalysisAction!getCarAccidentPartsInfo.action",
    striped : true,//显示斑马线效果
    fitColumns:true,
    singleSelect:true,
    fit:true,
    toolbar: '#toolbar_a4',
    pagination:false,
    sortName : 'month',
	sortOrder : 'asc',
	//idFiled:'formid',
    columns:[[
        {field:'month',title:'月份',align:'center',width:100,formatter: function(value,row,index){if(value>0){return value+"月";}else{return "汇总"; }}},    
        {field:'chesheng',title:'车身',align:'center',width:100},    
        {field:'chuandong',title:'传动系',align:'center',width:120},
        {field:'dianqishebei',title:'电气设备',align:'center',width:130},    
        {field:'fadongji',title:'发动机',align:'center',width:100},    
        {field:'chelun',title:'车轮和轮胎',align:'center',width:140},    
        {field:'qinang',title:'气囊和安全带',align:'center',width:150},    
        {field:'xuanjiaxi',title:'悬架系',align:'center',width:120},    
        {field:'zhidongxi',title:'制动系',align:'center',width:120},    
        {field:'zhuanxiangxi',title:'转向系',align:'center',width:120},    
        {field:'fujiashebei',title:'附加设备',align:'center',width:130},    
        {field:'all',title:'合计',align:'center',width:100}    
    ]]    
});
$('#applytime_year_a4').combobox({
	editable:false,
	valueField:'id',
	textField:'text',
	panelHeight:'auto',
	url:yp.bp()+"/sys/docAnalysisAction!getMonthDataByYear2.action",
	onSelect:function(record){$('#a4_table').datagrid('load',{	year:record.id});},//选择某一项后，重新加载当前页面
	onLoadSuccess:function(){
		var data=$(this).combobox('getData');//获得加载的数据集合，然后将最近的一年显示在组合框中
		var f=-1;
		if(data&&data.length>0){
			for(var i=0;i<data.length;i++){
				if(data[i].id>f){
					f=data[i].id;
				}
			}
		}
		$(this).combobox('setValue',f);
	}
});
</script>