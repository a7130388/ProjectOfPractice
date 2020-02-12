<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=BIG5"%>



<%-- 
�{���GXXQK0100.jsp
�@�̡G���W��(�ѦҦ�RSD0Z001)
�\��G����KPI�зǬd��
�����G2019-05-14
--%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<%@ include file="/html/CM/header.jsp"%>

<title>KPI��Ƭd��</title>


<%-- �פJ�~��Javascript �P css, �s�W�Щ�U��--%>
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

<%-- TableExport�ץXexcel�M�� --%>
<script src="${htmlBase}/XX/QK/js/SheetJS/xlsx.core.js"></script>
<script src="${htmlBase}/XX/QK/js/FileSaver/dist/FileSaver.js"></script>
<script src="${htmlBase}/XX/QK/js/TableExport-5.2.0/tableexport.js"></script>
<script src="${htmlBase}/XX/QK/js/bootstrap-daterangepicker/daterangepicker.js"></script>

<script>
	$(document).ready(function(){
		XXQK0100().initApp();
	});
   <%----------------------���͵e������----------------------%>
   function XXQK0100(){
		
		var buttons = {
			<%-- �d�� --%>
			doQuery : function(GRP_ID,kindName){
				
			
				
				<%-- KIND = 0 ���լd�� --%>
				var KIND = 0;
				$('#kindName').html(kindName + 'KPI');
				
				<%-- KIND = 1 ����d�� --%>
				if(!GRP_ID){
					$('#kindName').html( $("#DIVList :selected").text() +'KPI');
					KIND = 1;
				}
				
				<%-- ��ƥ]�q --%>
				var reqMap = JSON.stringify({ 
						START_DATE : $('#START_DATE').val(),  
						END_DATE : $('#END_DATE').val(),
						DIV_NO : $('#DIVList').val(),
						GRP_ID : GRP_ID,
						KIND : KIND
				});
				
				<%-- �P�_���O�_�s�b�A������椺�e --%>
				if ( $.fn.dataTable.isDataTable( '#table-1' ) ){
					$('#table-1').DataTable().destroy();
					$('#table-1 tbody').empty();
				}
						
				var successaction = function(data) { 
						<%--ø�sDOM table --%>
						var Standard_head = '<th>KPI</th>';
						var PERIOD_head = '<th>�ؼлP������</th>';
						var Detail_head = '<th>EMP_NM</th>';
						var customCheck = '<div class="dropdown-title text-center" style="font-size:16px;">���/�������</div>';
						var datacolumn = [{title:'�}�o�H��', data: "EMP_NM", width: "8%"}];			
						var tab='';					
						var colspan = {};
						var cellnum = {};
						var KPI_DetailNM = {};
						var celltimes = 0;				
						<%--�P�_colspan�ƶq--%>
						$.each(data.rtnKPI_DetailList, function(i) { 
							var KPI_ID = data.rtnKPI_DetailList[i].KPI_ID;
							var KPI_DETAIL = data.rtnKPI_DetailList[i].KPI_DETAIL;
							var KPI_DETAIL_ID = data.rtnKPI_DetailList[i].KPI_DETAIL_ID;
							celltimes++;
							<%--�p��colspan�ƶq--%>
							if (!(KPI_ID in colspan)){
								colspan[KPI_ID] = 0;
							}
							<%--�p��P�_�̾ڬO�ĴX��cell--%>
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
							<%--�ͦ��Ϫ�tab--%>			
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

						<%--���ͪ�����W��--%>
						$('#table-1-head').empty()
										  .append('<tr>' + Standard_head + '</tr>')
										  .append('<tr>' + PERIOD_head + '</tr>')
										  .append('<tr  width="30%">' + Detail_head + '</tr>');
						
						<%--�����������ﶵ--%>
						$('#customCheck').empty().append(customCheck);
						
						<%--���ͭ���--%>
						$('#myTab').empty().append(tab);

						<%--��ܪ��--%>				  
						$('#tablecard').show();					  
										
						<%--���ͪ��--%>
						var rtnKPI_StandardList = data.rtnKPI_StandardList;	
						actions.creatTable(data.rtnList,datacolumn,rtnKPI_StandardList,KPI_DetailNM);
						
						actions.scantab(rtnKPI_StandardList,cellnum);						
						
						
						<%--���Ҫ�l�I��--%>
						$('#chart-tab').click();
																
						
						if(!data.EMP_ID){
							$('#chartcard').show();
						}					
						swal({
						type: 'success',
						title: 'KPI��Ƭd�ߦ��\',
						showConfirmButton: false,
						timer: 1000
						}).then(result => {
							$('html, body').animate({scrollTop: $('#cardhead').offset().top }, {duration: 700,easing: 'swing'});
						});
					};

					$.runAjax('${dispatcher}/XXQK_0100/query', {reqMap : reqMap}, successaction, true);			

			},

			<%--�^�쳻��--%>
			goTop : function(){
				$('html,body').animate({ scrollTop: 0 }, 500);
			},
			
			<%--�ץX--%>
			doExport : function(){
				$('#table-1').tableExport({
					type:'excel',
					formats: ['xlsx'],
					exportButtons: true
				});
				$('.xlsx').hide().click();
			},
			
			<%--�������--%>
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
			
			<%--���m--%>
			doReset : function(){
				var table = $('#table-1').DataTable();
				$.fn.dataTable.ext.search.pop();				
				table.draw();				
				$('#myTab').find('.active').click();
			},
			
			<%--���o�ո��--%>
			getGRP : function(){
				$('#tablecard').hide();
				$('#chartcard').hide();
				$('#GroupList').empty()
								.selectric('destroy')
								.append('<option value="">����</option>')
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
				
			<%--���ͪ��--%>
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

			
			<%--���Utab�ƥ�A�̷�tab���Ҳ��͹Ϫ�--%>
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
			<%--ø�s�P�_�̾ڬOD���Ϫ�--%>
			makedownpie : function(cellnum,up,down,unit){			
				var table = $('#table-1').DataTable();
				var data = {labels: ['�W�V�з�','�ŦX�з�','�C��з�'], value: [] }
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
				
				<%--�Ϫ��I���ƥ�--%>
				$('.makechart').unbind('click').click(function(evt){
					var activePoints = myChart1.getElementsAtEvent(evt); 
					var chartData = activePoints[0]._chart.config.data;
					var idx = activePoints[0]._index;
					var label = chartData.labels[idx];
					var value = chartData.datasets[0].data[idx];
					
					<%--�I��Ϫ�N�J�зǹL�odatatable--%>
					$.fn.dataTable.ext.search.pop();
					$.fn.dataTable.ext.search.push(			
						function( settings, data, dataIndex ) {
							var avg = parseFloat( data[cellnum] ); 
							if(avg!==''){
								if(label == '�W�V�з�'){
									if(avg < down){
									return true;
									}
								} else if(label == '�C��з�'){
									if(avg > up){
									return true;
									}
								} else if(label == '�ŦX�з�'){
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
					
			<%--ø�s�P�_�̾ڬOU���Ϫ�--%>
			makeuppie : function(cellnum,up,down,unit){
				
				var table = $('#table-1').DataTable();
				var data = {labels: ['�W�V�з�','�ŦX�з�','�C��з�'], value: [] }
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
				
				<%--�Ϫ��I���ƥ�--%>
				$('.makechart').unbind('click').click(function(evt){ 
						var activePoints = myChart1.getElementsAtEvent(evt); 
						var chartData = activePoints[0]._chart.config.data;
						var idx = activePoints[0]._index;
						var label = chartData.labels[idx];
						var value = chartData.datasets[0].data[idx];		
						<%--�I��Ϫ�N�J�зǹL�odatatable--%>
						$.fn.dataTable.ext.search.pop();
						$.fn.dataTable.ext.search.push(			
							function( settings, data, dataIndex ) {					
								var avg =  parseFloat(data[cellnum])  ; 
								var t_up = data[2] ;
								var t_down =data[1] ;
								if(avg!==''){
									if(label == '�W�V�з�'){
										if(avg > t_up){
											return true;
										}
									} else if(label == '�C��з�'){
										if(avg < t_down){
											return true;
										}
									} else if(label == '�ŦX�з�'){
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
				
			<%--����KPI�з��ܧ�������C��--%>
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
				 
				$('span:contains("KPI��Ƭd��")').parents('li').addClass('active'); 
				$('#navtitle').text('KPI��Ƭd��');
				$('body').on('click','[data-stopPropagation]',function (e) {
					e.stopPropagation();
				});			
			
				<%--�קKdatatable fix-column �]��--%>
				$(window).resize(function() {
					  $.fn.dataTable
						.tables( { visible: true, api: true } )
						.columns.adjust()
						.fixedColumns().relayout();
				});
				
				<%--�������tab�ɶ]��--%>
				$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {					
						$.fn.dataTable.tables( {visible: true, api: true} )
									  .columns.adjust()
									  .fixedColumns()
									  .relayout();
				} );
				
				<%--�]�w�_�鬰���@�Ӥ�e--%>
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
											'�@��',
											'�G��',
											'�T��',
											'�|��',
											'����',
											'����',
											'�C��',
											'�K��',
											'�E��',
											'�̤�',
											'�̤@��',
											'�̤G��'
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
											'�@��',
											'�G��',
											'�T��',
											'�|��',
											'����',
											'����',
											'�C��',
											'�K��',
											'�E��',
											'�̤�',
											'�̤@��',
											'�̤G��'
										]
									}});
				<%--�u���U�ƮɥX�{�^�쳻�����s--%>
				$(window).scroll(function () {
					if ($(window).scrollTop() >= 50) {
						$('#btn_top').fadeIn();
					}
					else {
						$('#btn_top').fadeOut();
					}
				});

				<%-------------- ���s�I���]�w --------------%>
				$('#search').on('click',function(){						<%--�d��--%>
					if($('#GroupList').val() != ''){
						$('#myTab2').empty();
					}
					buttons.doQuery($('#GroupList').val(),$("#GroupList option:selected").text());
				});				
				$('#myTab2').on('click','.nav-item',function(){			<%--�էO�ഫ�ɬd��--%>
					buttons.doQuery($(this).val()||'',$(this).children().html());
				});   
				$('#btn_top').on('click',buttons.goTop);				<%--�^�쳻�����s�I���ƥ�--%>
				$('#ExportReporttoExcel').on('click',buttons.doExport); <%--�ץX��excel���--%>
				$('#customCheck').on( 'click','.custom-control-input',buttons.doHideColumn); <%--�������--%>
				$('#reset').on( 'click',buttons.doReset);  <%--���m--%>
				$('#DIVList').on( 'change',buttons.getGRP); <%--�������--%>
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
							<h4>�d�߱���G</h4>
						</div>
						<div class="card-body">
							<div class="form-row">
								<div class="form-group col-md-6">
									<label>��O�G</label>
									<select class="form-control selectric" id="DIVList">   
										<c:forEach items="${rtnDIVList}" var="item" varStatus="loop">
											<option value="${item.DIV_NO}">${item.DIV_NM}</option>
										</c:forEach>
									</select>
								</div>
								<div class="form-group col-md-6" >
									<label>�էO�G</label>
									<select class="form-control selectric" id="GroupList">
										<option value="">����</option>
										<c:forEach items="${rtnGroupList}" var="item" varStatus="loop">
											<option value="${item.GRP_ID}">${item.GRP_NM}</option>
										</c:forEach>
									</select>
								</div>
							</div>						
							<div class="form-row mt-3">
								<div class="form-group col-md-5">
									<label>�_��G</label>
									<input type="text" class="form-control pull-left" id="START_DATE">
								</div>
								<div class="form-group col-md-5">
									<label>�W��G</label>
									<input type="text" class="form-control pull-right" id="END_DATE">
								</div>
								<div class="form-group col-md-2" align="center">
										<br/><br/><button type="button" class="btn btn-primary" id="search">�d��</button>
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
									<h4 id="kindName">��KPI</h4>
								</div>
								<div class="col-sm-20 col-md-10" >
									<nav class="navbar navbar-expand-lg main-navbar">
										<div class="col-sm-20 col-md-10"></div>
										<ul class="navbar-nav navbar-right">
											<li><a href="javascript:void(0)"><img src="${htmlBase}/XX/QK/css/ionicons/png/512/ios7-undo.png" height="30" width="30" id="reset" class="redo-table" data-toggle="tooltip" data-placement="top" title="���m"></a></li>&nbsp;&nbsp;&nbsp;
											<li><a href="javascript:void(0)"><img src="${htmlBase}/XX/QK/css/ionicons/png/512/ios7-download.png" height="30" width="30" id="ExportReporttoExcel" data-toggle="tooltip" data-placement="top" title="�ץX"></a></li>&nbsp;&nbsp;&nbsp;
											<li class="dropdown">
												<a href="#" data-toggle="dropdown">
													<img alt="image" src="${htmlBase}/XX/QK/css/ionicons/png/512/ios7-cog.png" height="30" width="30" data-toggle="tooltip" data-placement="top" title="���/�������">
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
				<button type="button" class="btn btn-primary" style=" position: fixed;bottom: 10px;right: 10px;display: none;"  data-toggle="tooltip" data-placement="top" id="btn_top"title="�^�쳻��"><img src="${htmlBase}/XX/QK/css/ionicons/png/512/arrow-up-a.png" height="30" width="30" ></button> 
			</section>
      	</div>
    </div>
  </div> 

</body>
</html>