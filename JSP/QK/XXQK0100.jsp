<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=BIG5"%>



<%-- 
程式：XXQK0100.jsp
作者：黃名玄(參考自RSD0Z001)
功能：全科KPI標準查詢
完成：2019-05-14
--%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<%@ include file="/html/CM/header.jsp"%>

<title>KPI資料查詢</title>


<%-- 匯入外部Javascript 與 css, 新增請放下面--%>
<%--  General CSS Files --%>
<link rel="stylesheet" href="${htmlBase}/XX/QK/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${htmlBase}/XX/QK/css/fontawesome/css/all.css">

<%-- Template CSS --%>
<link rel="stylesheet" href="${htmlBase}/XX/QK/css/bootstrap-daterangepicker/daterangepicker.css">
<link rel="stylesheet" href="${htmlBase}/XX/QK/css/FixedColumns-3.2.5/fixedColumns.bootstrap4.min.css">
<link rel="stylesheet" href="${htmlBase}/XX/QK/css/datatables/Buttons-1.2.4/buttons.bootstrap4.min.css">
<link rel="stylesheet" href="${htmlBase}/XX/QK/css/TableExport-5.2.0/tableexport.min.css">
<link rel="stylesheet" href="${htmlBase}/XX/QK/css/jquery-selectric/selectric.css">
<link rel="stylesheet" href="${htmlBase}/XX/QK/css/DataTables-1.10.16/css/dataTables.bootstrap4.min.css">
<link rel="stylesheet" href="${htmlBase}/XX/QK/css/style.css">
<link rel="stylesheet" href="${htmlBase}/XX/QK/css/components.css">
<link rel="stylesheet" href="${htmlBase}/XX/QK/css/loaders/loaders.min.css">
<link rel="stylesheet" href="${htmlBase}/XX/QK/css/toastr/toastr.css">

<%-- General JS Scripts --%>
<script src="${htmlBase}/XX/QK/js/jquery-3.3.1.min.js"></script>
<script src="${htmlBase}/XX/QK/js/popper.min.js"></script>
<script src="${htmlBase}/XX/QK/js/bootstrap/bootstrap.min.js"></script>
<script src="${htmlBase}/XX/QK/js/jquery.nicescroll.min.js"></script>
<script src="${htmlBase}/XX/QK/js/moment.js"></script>
<script src="${htmlBase}/XX/QK/js/stisla.js"></script>
<script src="${htmlBase}/XX/QK/js/jquery.blockUI.js"></script>
<script src="${htmlBase}/XX/QK/js/sweetalert2/sweetalert2.all.js"></script>
<script src="${htmlBase}/XX/QK/js/toastr/toastr.js"></script>
<script src="${htmlBase}/XX/QK/js/runAjax.js"></script>

<%-- Template JS File --%>
<script src="${htmlBase}/XX/QK/js/AlertHandler.js"></script>
<script src="${htmlBase}/XX/QK/js/assets/scripts.js"></script>
<script src="${htmlBase}/XX/QK/js/assets/custom.js"></script>
<script src="${htmlBase}/XX/QK/js/DataTables-1.10.16/jquery.dataTables.min.js"></script>
<script src="${htmlBase}/XX/QK/js/DataTables-1.10.16/dataTables.bootstrap4.min.js"></script>
<script src="${htmlBase}/XX/QK/js/loaders/loaders.css.js"></script>
<script src="${htmlBase}/XX/QK/js/jquery-selectric/jquery.selectric.min.js"></script>
<script src="${htmlBase}/XX/QK/js/jquery-validation/jquery.validate.min.js"></script>
<script src="${htmlBase}/XX/QK/js/jquery-validation/messages_zh_TW.js"></script>
<script src="${htmlBase}/XX/QK/js/assets/chart.min.js"></script>
<script src="${htmlBase}/XX/QK/js/FixedColumns-3.2.5/dataTables.fixedColumns.min.js"></script>

<%-- TableExport匯出excel套件 --%>
<script src="${htmlBase}/XX/QK/js/SheetJS/xlsx.core.js"></script>
<script src="${htmlBase}/XX/QK/js/FileSaver/dist/FileSaver.js"></script>
<script src="${htmlBase}/XX/QK/js/TableExport-5.2.0/tableexport.js"></script>
<script src="${htmlBase}/XX/QK/js/bootstrap-daterangepicker/daterangepicker.js"></script>

<script>
	$(document).ready(function(){
		XXQK0100().initApp();
	});
   <%----------------------產生畫面物件----------------------%>
   function XXQK0100(){
		
		var buttons = {
			<%-- 查詢 --%>
			doQuery : function(GRP_ID,kindName){
				
			
				
				<%-- KIND = 0 為組查詢 --%>
				var KIND = 0;
				$('#kindName').html(kindName + 'KPI');
				
				<%-- KIND = 1 為科查詢 --%>
				if(!GRP_ID){
					$('#kindName').html( $("#DIVList :selected").text() +'KPI');
					KIND = 1;
				}
				
				<%-- 資料包裹 --%>
				var reqMap = JSON.stringify({ 
						START_DATE : $('#START_DATE').val(),  
						END_DATE : $('#END_DATE').val(),
						DIV_NO : $('#DIVList').val(),
						GRP_ID : GRP_ID,
						KIND : KIND
				});
				
				<%-- 判斷表格是否存在，移除表格內容 --%>
				if ( $.fn.dataTable.isDataTable( '#table-1' ) ){
					$('#table-1').DataTable().destroy();
					$('#table-1 tbody').empty();
				}
						
				var successaction = function(data) { 
						<%--繪製DOM table --%>
						var Standard_head = '<th>KPI</th>';
						var PERIOD_head = '<th>目標與執行方案</th>';
						var Detail_head = '<th>EMP_NM</th>';
						var customCheck = '<div class="dropdown-title text-center" style="font-size:16px;">顯示/隱藏欄位</div>';
						var datacolumn = [{title:'開發人員', data: "EMP_NM", width: "8%"}];			
						var tab='';					
						var colspan = {};
						var cellnum = {};
						var KPI_DetailNM = {};
						var celltimes = 0;				
						<%--判斷colspan數量--%>
						$.each(data.rtnKPI_DetailList, function(i) { 
							var KPI_ID = data.rtnKPI_DetailList[i].KPI_ID;
							var KPI_DETAIL = data.rtnKPI_DetailList[i].KPI_DETAIL;
							var KPI_DETAIL_ID = data.rtnKPI_DetailList[i].KPI_DETAIL_ID;
							celltimes++;
							<%--計算colspan數量--%>
							if (!(KPI_ID in colspan)){
								colspan[KPI_ID] = 0;
							}
							<%--計算判斷依據是第幾個cell--%>
							if (!(KPI_ID in cellnum)){
								cellnum[KPI_ID] = celltimes;
							}else{
								cellnum[KPI_ID]=cellnum[KPI_ID]+1;
							}					
							KPI_DetailNM[KPI_DETAIL_ID] = KPI_DETAIL;
							colspan[KPI_ID] = colspan[KPI_ID] + 1;
							Detail_head = Detail_head + '<th>' + KPI_DETAIL_ID + '</th>';
							datacolumn.push({title:KPI_DETAIL, data: KPI_DETAIL_ID,defaultContent:'', width: '5%'});
							if(KPI_DETAIL_ID!='A1' && KPI_DETAIL_ID!='A2'){
								customCheck = customCheck + '<div class="dropdown-item has-icon">' +
																	'<div class="custom-control custom-checkbox">' +
																		'<input type="checkbox" class="custom-control-input" id="customCheck' + i + '" name="customCheck1" checked>' +
																		'<label class="custom-control-label" for="customCheck' + i + '">' + KPI_DETAIL + '</label>' +
																	'</div>' +
																'</div>';
							}
						});
						$.each(data.rtnKPI_StandardList, function(i) { 
							var KPI_ID = data.rtnKPI_StandardList[i].KPI_ID;
							var Standard = data.rtnKPI_StandardList[i];	
							<%--生成圖表tab--%>			
							if(i==0){
								tab = '<li class="nav-item"><a class="nav-link active show redo-table" id="chart-tab" data-toggle="tab" href="#'+KPI_ID+'" role="tab" aria-controls="home" aria-selected="true">'+Standard.KPI_NAME+'</a></li>';								
							}else{
								tab = tab + '<li class="nav-item"><a class="nav-link redo-table" id="profile-tab" data-toggle="tab" href="#'+KPI_ID+'" role="tab" aria-controls="home" aria-selected="true">'+Standard.KPI_NAME+'</a></li>';								
							}					
							Standard_head = Standard_head + '<th colspan="' + colspan[KPI_ID] + '">' + Standard.KPI_NAME + '</th>';
							PERIOD_head = PERIOD_head + '<th colspan="' + colspan[KPI_ID] + '">' + Standard.PERIOD + '<br/>' + Standard.STD_LOW +'~'+ Standard.STD_UP + Standard.UNIT+ '</th>';
						});
						
						if(!GRP_ID){
							$('#myTab2').empty().append('<li class="nav-item">' +
													'<a class="nav-link active show" id="home-tab" data-toggle="tab" href="#KPI1" role="tab" aria-controls="home" aria-selected="true">'+ $("#DIVList :selected").text() +'</a>' +
												'</li>');
							$.each(data.groupList, function(i) { 
								groupList = data.groupList[i];
								$('#myTab2').append('<li class="nav-item" value="' + groupList.GRP_ID + '">' +
														'<a class="nav-link" data-toggle="tab" href="#KPI1" role="tab" aria-controls="home" aria-selected="false">' + groupList.GRP_NM +'</a>' +
													'</li>');
							});
						}

						<%--產生表格欄位名稱--%>
						$('#table-1-head').empty()
										  .append('<tr>' + Standard_head + '</tr>')
										  .append('<tr>' + PERIOD_head + '</tr>')
										  .append('<tr  width="30%">' + Detail_head + '</tr>');
						
						<%--產生隱藏欄位選項--%>
						$('#customCheck').empty().append(customCheck);
						
						<%--產生頁籤--%>
						$('#myTab').empty().append(tab);

						<%--顯示表格--%>				  
						$('#tablecard').show();					  
										
						<%--產生表格--%>
						var rtnKPI_StandardList = data.rtnKPI_StandardList;	
						actions.creatTable(data.rtnList,datacolumn,rtnKPI_StandardList,KPI_DetailNM);
						
						actions.scantab(rtnKPI_StandardList,cellnum);						
						
						
						<%--頁籤初始點擊--%>
						$('#chart-tab').click();
																
						
						if(!data.EMP_ID){
							$('#chartcard').show();
						}					
						swal({
						type: 'success',
						title: 'KPI資料查詢成功',
						showConfirmButton: false,
						timer: 1000
						}).then(result => {
							$('html, body').animate({scrollTop: $('#cardhead').offset().top }, {duration: 700,easing: 'swing'});
						});
					};

					$.runAjax('${dispatcher}/XXQK_0100/query', {reqMap : reqMap}, successaction, true);			

			},

			<%--回到頂部--%>
			goTop : function(){
				$('html,body').animate({ scrollTop: 0 }, 500);
			},
			
			<%--匯出--%>
			doExport : function(){
				$('#table-1').tableExport({
					type:'excel',
					formats: ['xlsx'],
					exportButtons: true
				});
				$('.xlsx').hide().click();
			},
			
			<%--隱藏欄位--%>
			doHideColumn : function(){
				var th = $(this).parent().find('label').text();
				var table = $('#table-1').DataTable();
				for(var i = 1; i < table.columns()[0].length; i++){
					var title = table.column(i).header();
					if($(title).html() == th ){
						if($(this).prop('checked')==false){
							table.column(i).visible(false,false);
						}
						else{
							table.column(i).visible(true,false);
						}
					}
				}
				$.fn.dataTable
					.tables( { visible: true, api: true } )
					.columns.adjust()
					.fixedColumns().relayout();
			},
			
			<%--重置--%>
			doReset : function(){
				var table = $('#table-1').DataTable();
				$.fn.dataTable.ext.search.pop();				
				table.draw();				
				$('#myTab').find('.active').click();
			},
			
			<%--取得組資料--%>
			getGRP : function(){
				$('#tablecard').hide();
				$('#chartcard').hide();
				$('#GroupList').empty()
								.selectric('destroy')
								.append('<option value="">全選</option>')
								.selectric('refresh');
				$.runAjax('${dispatcher}/XXQK_0200/getGRP',  {DIV_NO : $('#DIVList').val()}, Callback.getGRP, true);				
			}	
		}

		var Callback = {
			getGRP : function(data) {
				$.each(data.rtnGroupList, function(i) { 
					Group = data.rtnGroupList[i];
					$('#GroupList').append('<option value="' + Group.GRP_ID + '">' + Group.GRP_NM + '</option>');
				});
				$('#GroupList').selectric('refresh'); 
			}
		}
		
		var actions = {
				
			<%--產生表格--%>
			creatTable : function(rtnList,datacolumn,rtnKPI_StandardList,KPI_DetailNM){
				var rtnList = JSON.parse(rtnList);			
				$('#table-1').DataTable({
					responsive: true,				
					scrollX: true,				
					data: rtnList,
					columns: datacolumn,
					drawCallback:function() {
						actions.datacolor(rtnKPI_StandardList,KPI_DetailNM);		
					},		
					destroy:true,				
					scrollCollapse: true,
					fixedColumns : true,
					fixedColumns:   {
						leftColumns: 1,
						heightMatch: 'auto'
					}
				});
			
			},

			
			<%--註冊tab事件，依照tab標籤產生圖表--%>
			scantab : function (rtnKPI_StandardList,cellnum){
				$('#myTab').on('click','.nav-link',function(){
					var table = $('#table-1').DataTable();
					$.fn.dataTable.ext.search.pop();				
					table.draw();
					var NAME=$(this).text();
					i = 0;
					$.each(rtnKPI_StandardList, function() {
						var KPI_NAME = rtnKPI_StandardList[i].KPI_NAME;
						var KPI_UNIT = rtnKPI_StandardList[i].UNIT;
						if(KPI_NAME == NAME) 
						{
							var Standard = rtnKPI_StandardList[i];	
							var judge=Standard.JUDGE;	
							var num = cellnum[Standard.KPI_ID];			
							if(judge=='U'){							
								actions.makeuppie(num,Standard.STD_UP,Standard.STD_LOW,KPI_UNIT);							
							} else{
								actions.makedownpie(num,Standard.STD_UP,Standard.STD_LOW,KPI_UNIT);
							}													
						}
						i++;					
					});
				})
			},
			<%--繪製判斷依據是D的圖表--%>
			makedownpie : function(cellnum,up,down,unit){			
				var table = $('#table-1').DataTable();
				var data = {labels: ['超越標準','符合標準','低於標準'], value: [] }
				var under=0;
				var between=0;
				var upper=0;
				if(unit=='%'){
					up = up/100;
					down = down/100;
				}	
				
				table.column(cellnum).data().each(function(value,index){
					if(value!== ''){
						if(value <= up && value >= down){
							between++;					
						} else if(value < down){
							upper++;
						} else if(value > up){
							under++;
						}								
					}	
					
				});
				data.value.push(upper);	
				data.value.push(between);	
				data.value.push(under);						
				$('#myChart').replaceWith('<canvas id="myChart" class="makechart" width="600" height="400"></canvas>');
				var canvas = $('#myChart')[0];	
			  	var ctx = canvas.getContext('2d');	
				var myChart1 = new Chart(ctx, {
				type: 'pie',
				data: {
				labels: data.labels,
					datasets: [{
					data: data.value,
					backgroundColor: ['#6777ef','#63ed7a','#fc544b']
					}]  
				},
				options: {
					responsive: true,
					maintainAspectRatio: false,
					legend: {
					position: 'bottom',
					}
				}
				});
				
				<%--圖表點擊事件--%>
				$('.makechart').unbind('click').click(function(evt){
					var activePoints = myChart1.getElementsAtEvent(evt); 
					var chartData = activePoints[0]._chart.config.data;
					var idx = activePoints[0]._index;
					var label = chartData.labels[idx];
					var value = chartData.datasets[0].data[idx];
					
					<%--點選圖表代入標準過濾datatable--%>
					$.fn.dataTable.ext.search.pop();
					$.fn.dataTable.ext.search.push(			
						function( settings, data, dataIndex ) {
							var avg = parseFloat( data[cellnum] ); 
							if(avg!==''){
								if(label == '超越標準'){
									if(avg < down){
									return true;
									}
								} else if(label == '低於標準'){
									if(avg > up){
									return true;
									}
								} else if(label == '符合標準'){
									if(avg >= down && avg <= up){
									return true;
									}
								}					
								return false;							
							}
						}
					);
					table.draw();	
					$('html, body').animate({scrollTop: $('#cardhead').offset().top }, {duration: 700,easing: 'swing'});	
				}); 	
			},
					
			<%--繪製判斷依據是U的圖表--%>
			makeuppie : function(cellnum,up,down,unit){
				
				var table = $('#table-1').DataTable();
				var data = {labels: ['超越標準','符合標準','低於標準'], value: [] }
				var under=0;
				var between=0;
				var upper=0;	
				if(unit=='%'){
					up = up/100;
					down = down/100;
				}	
				var i = 0;
				table.column(cellnum).data().each(function(value,index){
				
					var St_value = table.row(index).data();
					var down = St_value['A1'];
					var up = St_value['A2'];
					
					var comval = table.row(index).data()['TOTAL_DAY'];					
		
					if(comval!= undefined ){
											
						if(comval > table.row(index).data()['A2']){
							upper++;							
						} else if(comval < table.row(index).data()['A1']){
							under++;
						}else{
							between++;
						}
					}
				});		
				
				
				data.value.push(upper);	
				data.value.push(between);	
				data.value.push(under);						
				$('#myChart').replaceWith('<canvas id="myChart" class="makechart" width="600" height="400"></canvas>');
				var canvas = $('#myChart')[0];			
				var ctx = canvas.getContext('2d');	
				var myChart1 = new Chart(ctx, {
				type: 'pie',
				data: {
				labels: data.labels,
					datasets: [{
					data: data.value,
					backgroundColor: ['#6777ef','#63ed7a','#fc544b']
					}]  
				},
				options: {
					responsive: true,
					maintainAspectRatio: false,
					legend: {
					position: 'bottom',
					}
				}
				});
				
				<%--圖表點擊事件--%>
				$('.makechart').unbind('click').click(function(evt){ 
						var activePoints = myChart1.getElementsAtEvent(evt); 
						var chartData = activePoints[0]._chart.config.data;
						var idx = activePoints[0]._index;
						var label = chartData.labels[idx];
						var value = chartData.datasets[0].data[idx];		
						<%--點選圖表代入標準過濾datatable--%>
						$.fn.dataTable.ext.search.pop();
						$.fn.dataTable.ext.search.push(			
							function( settings, data, dataIndex ) {					
								var avg =  parseFloat(data[cellnum])  ; 
								var t_up = data[2] ;
								var t_down =data[1] ;
								if(avg!==''){
									if(label == '超越標準'){
										if(avg > t_up){
											return true;
										}
									} else if(label == '低於標準'){
										if(avg < t_down){
											return true;
										}
									} else if(label == '符合標準'){
										if(avg >= t_down && avg <= t_up){
											return true;
										}
									}					
									return false;
								}
							}
						);
						table.draw();		
						$('html, body').animate({scrollTop: $('#cardhead').offset().top }, {duration: 700,easing: 'swing'});
					}			
				); 	
			},
				
			<%--按照KPI標準變更資料顯示顏色--%>
			 datacolor : function(rtnKPI_StandardList,KPI_DetailNM){
				var i = 0;
				var table2 = $('#table-1');
				var tableLen = table2.find('tr').length;
				$.each(rtnKPI_StandardList, function(i) {
					
					var Standard = rtnKPI_StandardList[i];
					var num = 0;
					for(var i = 1; i < $('#table-1 tr:eq(2) th').length; i++){
						var title = $('#table-1 tr:eq(2) th:eq(' + i +')').children().html();
						if(title == KPI_DetailNM[Standard.ACORD_ID]){
							num = i;
							break;
						}
					}
					var up = parseFloat(Standard.STD_UP);
					var down = parseFloat(Standard.STD_LOW);

					if(Standard.UNIT=='%'){
						up = up/100;
						down = down/100;
					}
					
					if(Standard.JUDGE=='U'){
						
						for (var j = 3; j < tableLen; j++) {
							value = parseFloat(table2.find('tr:eq(' + j + ') td:eq(' + num + ')').html());
							var t_up =  parseFloat(table2.find('tr:eq(' + j + ') td:eq(2)').html());
							var t_down =  parseFloat(table2.find('tr:eq(' + j + ') td:eq(1)').html());						
							if(value <= t_up && value >= t_down){
								table2.find('tr:eq(' + j + ') td:eq(' + num + ')').css('color','#6C757D');					
							} else if(value < t_down){
								table2.find('tr:eq(' + j + ') td:eq(' + num + ')').css('color','red');
							} else if(value > t_up){
								table2.find('tr:eq(' + j + ') td:eq(' + num + ')').css('color','blue');
							}
						}	
					}else{
						for (var j = 3; j < tableLen; j++) {
							value = parseFloat(table2.find('tr:eq(' + j + ') td:eq(' + num + ')').html());				
							if(value <= up && value >= down){
								table2.find('tr:eq(' + j + ') td:eq(' + num + ')').css('color','#6C757D');					
							} else if(value < down){
								table2.find('tr:eq(' + j + ') td:eq(' + num + ')').css('color','blue');
							} else if(value > up){
								table2.find('tr:eq(' + j + ') td:eq(' + num + ')').css('color','red');
							}	
						}
					}
					i++;					
				});	
			}
			
		}
	
		
		return {
			initApp : function() {
				 
				$('span:contains("KPI資料查詢")').parents('li').addClass('active'); 
				$('#navtitle').text('KPI資料查詢');
				$('body').on('click','[data-stopPropagation]',function (e) {
					e.stopPropagation();
				});			
			
				<%--避免datatable fix-column 跑版--%>
				$(window).resize(function() {
					  $.fn.dataTable
						.tables( { visible: true, api: true } )
						.columns.adjust()
						.fixedColumns().relayout();
				});
				
				<%--防止切換tab時跑版--%>
				$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {					
						$.fn.dataTable.tables( {visible: true, api: true} )
									  .columns.adjust()
									  .fixedColumns()
									  .relayout();
				} );
				
				<%--設定起日為當日一個月前--%>
				var Today = new Date();
				var month = Today.getMonth();
				if (month < 10){ 
					month = '0' + month; 
				}
				var date = Today.getDate();
				if (date < 10){ 
					date = '0' + date; 
				}
				var defday = Today.getFullYear() + '-'+ month + '-' + date;	
				$('#START_DATE').daterangepicker({
									format: 'YYYY-MM-DD',
									singleDatePicker: true,									
									minYear: 1901,
									maxDate: new Date(),
									startDate: defday,
									locale: {
										 'format': 'YYYY-MM-DD',
										 'monthNames': [
											'一月',
											'二月',
											'三月',
											'四月',
											'五月',
											'六月',
											'七月',
											'八月',
											'九月',
											'十月',
											'十一月',
											'十二月'
										]
									}});
				$('#END_DATE').daterangepicker({
									format: 'YYYY-MM-DD',
									singleDatePicker: true,									
									minYear: 1901,
									maxDate: new Date(),
									locale: {
										 'format': 'YYYY-MM-DD',
										  'monthNames': [
											'一月',
											'二月',
											'三月',
											'四月',
											'五月',
											'六月',
											'七月',
											'八月',
											'九月',
											'十月',
											'十一月',
											'十二月'
										]
									}});
				<%--滾輪下滑時出現回到頂部按鈕--%>
				$(window).scroll(function () {
					if ($(window).scrollTop() >= 50) {
						$('#btn_top').fadeIn();
					}
					else {
						$('#btn_top').fadeOut();
					}
				});

				<%-------------- 按鈕點擊設定 --------------%>
				$('#search').on('click',function(){						<%--查詢--%>
					if($('#GroupList').val() != ''){
						$('#myTab2').empty();
					}
					buttons.doQuery($('#GroupList').val(),$("#GroupList option:selected").text());
				});				
				$('#myTab2').on('click','.nav-item',function(){			<%--組別轉換時查詢--%>
					buttons.doQuery($(this).val()||'',$(this).children().html());
				});   
				$('#btn_top').on('click',buttons.goTop);				<%--回到頂部按鈕點擊事件--%>
				$('#ExportReporttoExcel').on('click',buttons.doExport); <%--匯出成excel表單--%>
				$('#customCheck').on( 'click','.custom-control-input',buttons.doHideColumn); <%--欄位隱藏--%>
				$('#reset').on( 'click',buttons.doReset);  <%--重置--%>
				$('#DIVList').on( 'change',buttons.getGRP); <%--欄位隱藏--%>
			}	
		};
   }


</script>
</head>
<body>
  <div>
    <div class="main-wrapper">      
       <%@ include file="/html/XX/QK/js/sidebar.jsp"%>
      <div class="main-content" style="padding-top: 110px;">
        <section class="section">
			<div class="row">
				<div class="col-12">
					<div class="card">
						<div class="card-header">
							<h4>查詢條件：</h4>
						</div>
						<div class="card-body">
							<div class="form-row">
								<div class="form-group col-md-6">
									<label>科別：</label>
									<select class="form-control selectric" id="DIVList">   
										<c:forEach items="${rtnDIVList}" var="item" varStatus="loop">
											<option value="${item.DIV_NO}">${item.DIV_NM}</option>
										</c:forEach>
									</select>
								</div>
								<div class="form-group col-md-6" >
									<label>組別：</label>
									<select class="form-control selectric" id="GroupList">
										<option value="">全選</option>
										<c:forEach items="${rtnGroupList}" var="item" varStatus="loop">
											<option value="${item.GRP_ID}">${item.GRP_NM}</option>
										</c:forEach>
									</select>
								</div>
							</div>						
							<div class="form-row mt-3">
								<div class="form-group col-md-5">
									<label>起日：</label>
									<input type="text" class="form-control pull-left" id="START_DATE">
								</div>
								<div class="form-group col-md-5">
									<label>訖日：</label>
									<input type="text" class="form-control pull-right" id="END_DATE">
								</div>
								<div class="form-group col-md-2" align="center">
										<br/><br/><button type="button" class="btn btn-primary" id="search">查詢</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="row" id="load" style="display:none"><div style="margin:0px auto;"><div class="loader-inner line-spin-fade-loader"></div></div></div>
			<div class="row"  id="chartcard" style="display:none">
				<div class="col-12">
					<div class="card">
						<ul class="nav nav-tabs" id="myTab" role="tablist">
						</ul>
						<div class="tab-content tab-bordered" id="myTabContent">
							<div class="tab-pane fade active show" id="KPI1" role="tabpanel" aria-labelledby="home-tab">
								<div class="card-body" style="height:350px;"><canvas id="myChart" class="makechart" width="600" height="400"></canvas></div>
							</div>					             
						</div>
					</div>
				</div>
			</div>
			
			<div class="row" id="tablecard" style="display:none">
              <div class="col-12">
                <div class="card">
					<ul class="nav nav-tabs" id="myTab2" role="tablist">							
					</ul>
					<div class="tab-content tab-bordered" id="myTabContent2">
						<div class="tab-pane fade active show" id="KPI1" role="tabpanel" aria-labelledby="home-tab">				
								<div class="card-header" id="cardhead">
								<div class="col-sm-4 col-md-2" >
									<h4 id="kindName">科KPI</h4>
								</div>
								<div class="col-sm-20 col-md-10" >
									<nav class="navbar navbar-expand-lg main-navbar">
										<div class="col-sm-20 col-md-10"></div>
										<ul class="navbar-nav navbar-right">
											<li><a href="javascript:void(0)"><img src="${htmlBase}/XX/QK/css/ionicons/png/512/ios7-undo.png" height="30" width="30" id="reset" class="redo-table" data-toggle="tooltip" data-placement="top" title="重置"></a></li>&nbsp;&nbsp;&nbsp;
											<li><a href="javascript:void(0)"><img src="${htmlBase}/XX/QK/css/ionicons/png/512/ios7-download.png" height="30" width="30" id="ExportReporttoExcel" data-toggle="tooltip" data-placement="top" title="匯出"></a></li>&nbsp;&nbsp;&nbsp;
											<li class="dropdown">
												<a href="#" data-toggle="dropdown">
													<img alt="image" src="${htmlBase}/XX/QK/css/ionicons/png/512/ios7-cog.png" height="30" width="30" data-toggle="tooltip" data-placement="top" title="顯示/隱藏欄位">
												</a>
												<div class="dropdown-menu dropdown-menu-right" data-stopPropagation="true" style="width:250px;font-family: Microsoft JhengHei;" id="customCheck">
												</div>
											</li>
										</ul>
									</nav>
								</div>
							</div>
							<div class="card-body" >
								<table class="table table-striped" id="table-1">
									<thead id="table-1-head">
									</thead>
									<tbody >
									</tbody>
								</table>
							</div>
						</div>
					</div>
						</div>
					</div>
				</div>
				<button type="button" class="btn btn-primary" style=" position: fixed;bottom: 10px;right: 10px;display: none;"  data-toggle="tooltip" data-placement="top" id="btn_top"title="回到頂部"><img src="${htmlBase}/XX/QK/css/ionicons/png/512/arrow-up-a.png" height="30" width="30" ></button> 
			</section>
      	</div>
    </div>
  </div> 

</body>
</html>