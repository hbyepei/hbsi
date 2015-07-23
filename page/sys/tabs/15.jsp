<%@ page language="java" pageEncoding="UTF-8"%>
<script src="../../js/jquery.idTabs.min.js"></script>
<style>
.docAnalysis_tabsbox ul {
	border-bottom: 2px solid #AED0EA;
}
.docAnalysis_tabsbox ul li {
	display: inline-block;
	border: 2px solid #AED0EA;
	border-top-left-radius:0.5em;
	border-top-right-radius:0.5em;
	border-bottom: none;
	line-height: 30px;
	height: 30px;
	text-align: center;
	
}
.docAnalysis_tabsbox ul li a{
	display: inline-block;
	min-width: 80px; 
	font-size: 12px;
	text-decoration: none
}
.docAnalysis_tabsbox ul li a.selected {
	background-color: #F0F5FD;
	display: block;
	margin: 0px;
	padding-bottom: 2px;
	font-weight: bold;
}

.docAnalysis_tabsbox ul li a {
border-top-left-radius:0.5em;
	border-top-right-radius:0.5em;
	text-decoration: none;
	color:#3779C9;
}

.monthAnalysis_tabs {
	border-left: 2px solid #AED0EA;
	border-right: 2px solid #AED0EA;
	border-bottom: 2px solid #AED0EA;
/* 	min-height: 210px; */
}

.flexbox_col {
	display: -webkit-flex;
	-webkit-flex-flow: column nowrap;
	display: -moz-flex;
	-moz-flex-flow: column nowrap;
	display: -ms-flex;
	-ms-flex-flow: column nowrap;
	display: -flex;
	flex-flow: column nowrap;
}

.flexitem {
	-webkit-flex: 1;
	-moz-flex: 1;
	-ms-flex: 1;
	flex: 1;
}
#experts_rank li{
list-style-type:none;
border-bottom:1px solid #bbb;
width:90%;
min-width:200px;
text-align: left;
background: #D7EBF9;
line-height:30px;
height:30px;
margin-bottom: 3px;
padding-left:10px;
vertical-align: middle;
}
#experts_rank li:hover {
	cursor: pointer;
	background: #CEE9F8;
	font-weight: bold;
}
</style>
<div style="height:50%;overflow: hidden;">
	<div style="width: 75%; height:100%; float: left;">
		<div style="padding-left: 20px;font-size: 14px;">
			<div id="docAnalysis_tabs" class="docAnalysis_tabsbox  flexbox_col" style="height: 95%;">
				<ul>
					<li><a href='#docAnalysis_tabsA0' class='selected'>任务</a></li>
					<li><a href='#docAnalysis_tabsA1' class=''>用户</a></li>
					<li><a href='#docAnalysis_tabsA2' class=''>汽车</a></li>
					<li><a href="#docAnalysis_tabsA3" class=''>月度统计</a></li>
					<li><a href="#docAnalysis_tabsA4" class=''>事故部位</a></li>
				</ul>
				<div class="flexitem">
					<div id='docAnalysis_tabsA0' style="min-height: 200px;"></div>
					<div id='docAnalysis_tabsA1' style="min-height: 200px;"></div>
					<div id='docAnalysis_tabsA2' style="min-height: 200px;"></div>
					<div id='docAnalysis_tabsA3' style="min-height: 200px;"></div>
					<div id='docAnalysis_tabsA4' style="min-height: 200px;"></div>
				</div>
			</div>
		</div>
	</div>
	<div  class="flexbox_col" style="width:25%;  height:100%;  float: right;font-size: 14px;overflow: auto; padding-top: 20px;">
		<span style="padding-left:40px;">
			<img  src="../../js/easyui/themes/icons/yp_expert.png" style="vertical-align: middle;"/>
			<b style="vertical-align: middle;">参与的专家</b>
		</span>
		<ul id="experts_rank">
		</ul>
	</div>
</div>
<div style="height: 50%;">
	<div class="flexbox_col" id="B_container" style="padding:0px 20px;font-size: 14px; height:100%;">
		<div style="display: flex;	height: 25px;	background: #D7EBF9">
			<span><img src="../../js/easyui/themes/icons/yp_document.png" style="vertical-align: middle;"/><b style="vertical-align: middle;">调解情况统计</b></span> 
			<span style=" margin-left: auto;margin-right: 10px;color:#2CA3E1;" id="curTime"><b>时间范围:</b>
			从 <input id="starttime_15" class="easyui-datebox" name="starttime" style="width:90px" value="半月前" data-options="editable:false"> 
			至: <input id="endtime_15" class="easyui-datebox" name="endtime" style="width:90px" value="今天" data-options="editable:false"></span>
			<a href="javascript:void(0);" class="easyui-linkbutton" onclick="timeFilter();">确定</a> 
		</div>
		<div id="docMonthAnalysis_tabs" class="docAnalysis_tabsbox  flexbox_col  flexitem">
			<ul style="padding-left:0;margin-bottom: 0;height: 30px;">
				<li><a href='#docMonthAnalysis_tabsB0' class='selected'>任务数：0</a></li>
				<li><a href='#docMonthAnalysis_tabsB1' class=''>消费者申请</a></li>
				<li><a href='#docMonthAnalysis_tabsB2' class=''>商家申请</a></li>
				<li><a href='#docMonthAnalysis_tabsB3' class=''>调解完成</a></li>
				<li><a href='#docMonthAnalysis_tabsB4' class=''>调解终止</a></li>
				<li><a href='#docMonthAnalysis_tabsB5' class=''>不予受理</a></li>
			</ul>
			<div class="flexitem  monthAnalysis_tabs" id="tabsB_container">
				<div id='docMonthAnalysis_tabsB0'></div>
				<div id='docMonthAnalysis_tabsB1'></div>
				<div id='docMonthAnalysis_tabsB2'></div>
				<div id='docMonthAnalysis_tabsB3'></div>
				<div id='docMonthAnalysis_tabsB4'></div>
				<div id='docMonthAnalysis_tabsB5'></div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	var docAnalysis_tabs="docAnalysis_tabsA0";
	var docMonthAnalysis_tabs="docMonthAnalysis_tabsB0";
	var currentDiv_B='docMonthAnalysis_tabsB0';
	var stime=undefined;
	var etime=undefined;
	var year=undefined;//供a3页面使用
	var loadTimer=undefined;//计时器
	$("#docAnalysis_tabs ul").idTabs(docAnalysis_tabs);
	$("#docMonthAnalysis_tabs ul").idTabs(docMonthAnalysis_tabs);

	//获取参与调解的专家信息对象数组，其中专家参与信息对象格式如下：
	////{排名图标:rankIcon,图像:'image',姓名:name,次数：count,最近参与日期:recentTime}
		$.post(yp.bp()+"/sys/docAnalysisAction!getRankInfo.action",{},function(r){
			if(r.ok){
				var ep=r.object;
				for(var i=0;i<ep.length;i++){
					var eri=ep[i];
					var rankIcon="<img src='"+yp.bp()+eri.rankIcon+"' style='width:20px;height:20px;vertical-align:middle;'/>";
					var image="<img src='"+yp.bp()+eri.image+"' style='width:20px;height:20px;vertical-align:middle;'/>";
					var recentTime="<span style='float:right;margin-right:4px;'>"+eri.recentTime+"</span>";
					var licontent="<div><span>"+rankIcon+"&nbsp;"+image+"&nbsp;"+eri.name+"&nbsp;("+eri.count+")</span>"+recentTime+"</div>";
					var li=document.createElement("li");
					li.setAttribute("id","eri"+eri.id );
					$(li).on("click",function(){
						addTab("menubtn21");
						//先要判断专家信息页是否加载完毕，完毕后才能调用下面函数
						$.messager.progress({title:'提示', msg:'稍候…',icon:'icon-yp_waiting', interval : 300}); 
						 loadTimer=window.setInterval("doSearch("+this.id.substring(3)+");", 100);
					});
					//$(li).append(licontent);
					$("#experts_rank").append($(li).append(licontent));
				}
			}else{
				$("#experts_rank").append("<li>"+r.msg+"</li>");
			};
		},"json");
		
		function doSearch(eid){
			var time=0;
			var el=0;
			var sl=0;
			if(time>50){//时间到，关闭进度条，清除计时器
				if(loadTimer){
					window.clearInterval(loadTimer);
				}
				$.messager.progress('close');
			}else{//刷新el和sl的值,time++
				el=$("#expertList_21").length;
				sl=$("#searchbox_21").length;
				if(el>0&&sl>0){//已经加载的话，也要关闭进度条、清除计时器并返回
					if(loadTimer){
						window.clearInterval(loadTimer);
					}
					searchExpert(eid,"ID");
					$.messager.progress('close');
				}else{
					time++;
				}
			}
		}
	//为每个选项卡绑定单击事件，单击时动态加载数据
	var tabsA=$("#docAnalysis_tabs ul li a");
	var tabsB=$("#docMonthAnalysis_tabs ul li a");
	bindEvents(tabsA);
	bindEvents(tabsB);
	function bindEvents(tab){
		for(var i=0;i<tab.length;i++){
			var a=tab[i];
			$(a).click(function(){
				var href=this.href;
				var divID=href.substring(href.lastIndexOf("#")+1,href.length);
				if(divID.substring(divID.length-2,divID.length-1)=='B'){
					currentDiv_B=divID;
				}
				loadTabsData(divID,stime,etime);
			});
		}
	}
	
	//执行数据加载
	function loadTabsData(divID,stime,etime){
		var area=divID.substring(divID.length-2);
		var url=yp.bp()+"/sys/docAnalysisAction!getData.action";
		var params={"div":area,"path":"tabs/docAnalysis/"};
		//if(area.substring(0,1)=="B"&&stime&&etime){
		if(stime&&etime){
			var stime_string=getFormatDate(stime, "yyyy-MM-dd");
			var etime_string=getFormatDate(etime, "yyyy-MM-dd");
			timeInfo={"startTime":stime_string,"endTime":etime_string};
			params=yp.mergeObject(params, timeInfo);
		}
		//将区域信息传至后台，后台构造特定的jsp页面，成功后返回成功信息及对应的jsp页面地址
		 //$.messager.progress({title:'提示', msg:'正在加载…',icon:'icon-yp_waiting', interval : 300}); 
		$.post(url,params,function(r){
			if(r.ok){//构建成功
				var dimense=undefined;
				var data= r.object[1];
				if(area=="B0"){//如果选中的是B0选项卡，则还应该更改选项卡的标题中的数量值
					$("a[href='#docMonthAnalysis_tabsB0']").html("任务数："+data.total);
				} 
				if(area.substring(0,1)=="B"){
					dimense={"width":$("#B_container").width()-5,"height":$("#B_container").height()-98};
					data=yp.mergeObject(data, dimense);
				}
				if(area=="A3"){
					dimense={"width":$("#B_container").width()*0.75,"height":$("#B_container").height()*0.8};
					data=yp.mergeObject(data, dimense);
				}
				var page=r.object[0];//要加载的页面
				//r.object[1]是要提供的数据，是一个Map结构的数据，其中的key是字符串，表示参数名而value则是一个对象类型的数据，表示参数值
				var p=yp.obj2JsonString(data);//将该JS对象转换成字符串形式用于Post给目标页面，这样做的好处是在以在目标页面中一次性取出所有参数
				//在id为divID的div中载入page路径指定的页面，并向页面传递这个参数——p
				$("#"+divID).load(page,{"p":p},function(){//目标页面中，通过 var data=${param['p']}，即可取得一个包含所有参数的JS对象,而在java中也可以直接通过String param = request.getParameter("p")来取得字参数的符串值
					//$.messager.progress('close');
				});
			}else{
				$.messager.alert('提示',r.msg,'info' );
				//$.messager.progress('close');
			}
		},"json");
	}

	//以下函数是为了在火狐浏览器中撑开B区域中显示图表的div的高度至剩余空间的高度
	function resetDivHeight(){
		$("#tabsB_container").height($("#B_container").height()-98);
	}
	function timeFilter(){
		var st = Date.parse($("#starttime_15").datebox('getValue'));
		var et = Date.parse($("#endtime_15").datebox('getValue'));
	   	var today=new Date();
		if (isNaN(et)){
			etime=today;
		} else {
			etime=new Date(et);
			if(etime>today){
				etime=today;
			}
		}
		if (isNaN(st)){
			stime=new Date(etime-(15*24*3600*1000));
		} else {
			stime=new Date(st);
			var tem=new Date(etime-(5*24*3600*1000));//五天前的
			if(stime>tem){//起始日期选择的太靠近的话，至少选择5天的
				stime=tem;
			}
		}
		loadTabsData(currentDiv_B,stime,etime);
	}
	
	$(function(){
		//首次打开时，加载默认选项卡的数据
		loadTabsData(docAnalysis_tabs,stime,etime);
		loadTabsData(docMonthAnalysis_tabs,stime,etime);
		resetDivHeight();
		window.onresize=function(){//当浏览器窗口大小发生改变时重新设置div高度
			resetDivHeight();
       };
	});
</script>