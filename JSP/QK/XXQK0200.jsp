<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=BIG5"%>



<%-- 
程式：XXQK0200.jsp
作者：陳正穎
功能：KPI標準查詢/維護
完成：2019-05-14
--%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<%@ include file="/html/CM/header.jsp"%>

<title>KPI標準查詢/維護</title>


<%-- 匯入外部Javascript 與 css, 新增請放下面 --%>

<%-- General CSS Files --%>
<link rel="stylesheet" href="${htmlBase}/XX/QK/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${htmlBase}/XX/QK/css/fontawesome/css/all.css">

<%-- Template CSS --%>
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

<%-- Template JS File --%>
<script src="${htmlBase}/XX/QK/js/AlertHandler.js"></script>
<script src="${htmlBase}/XX/QK/js/runAjax.js"></script>
<script src="${htmlBase}/XX/QK/js/assets/scripts.js"></script>
<script src="${htmlBase}/XX/QK/js/assets/custom.js"></script>
<script src="${htmlBase}/XX/QK/js/DataTables-1.10.16/jquery.dataTables.min.js"></script>
<script src="${htmlBase}/XX/QK/js/DataTables-1.10.16/dataTables.bootstrap4.min.js"></script>
<script src="${htmlBase}/XX/QK/js/loaders/loaders.css.js"></script>
<script src="${htmlBase}/XX/QK/js/jquery-selectric/jquery.selectric.min.js"></script>
<script src="${htmlBase}/XX/QK/js/jquery-validation/jquery.validate.min.js"></script>
<script src="${htmlBase}/XX/QK/js/jquery-validation/messages_zh_TW.js"></script>

	<style>
		td.details-control {
			background: url('${htmlBase}/CM/assets/img/open.png') no-repeat center center;
			cursor: pointer;
		}
		tr.details td.details-control {
			 background: url('${htmlBase}/CM/assets/img/close.png') no-repeat center center;
		}
		input.error {
			border: 2px solid #f00;
		}
		input.valid {
			border: 2px solid #28a745;
		}
	</style>
<script>
	$(document).ready(function(){
		XXQK0200().initApp();
	});
   <%----------------------產生畫面物件----------------------%>
   function XXQK0200(){

		var buttons = {
			<%-- 查詢 --%>
			doQuery : function(GRP_ID,kindName){			
				
				<%-- KIND = 0 為組查詢 --%>
				var KIND = 0;
				$('#kindName').html(kindName + 'KPI');
				
				<%-- KIND = 1 為科查詢 --%>
				if(!GRP_ID){
					$('#kindName').html( $('#DIVList :selected').text() +'KPI');
					KIND = 1;
				}
				
				<%-- 資料包裹 --%>
				var reqMap = JSON.stringify({ 
						DIV_NO : $('#DIVList').val(),
						GRP_ID : GRP_ID,
						KIND : KIND
				});
		
				<%-- 判斷表格是否存在，移除表格內容 --%>
				if ( $.fn.dataTable.isDataTable( '#table-1' ) ) {
					$('#table-1').DataTable().destroy();
					$('#table-1 tbody').empty();
				}
				var successaction= function(data){
					<%--繪製DOM table --%>
					var Standard_head = '<th>名稱</th>';
					var Standard_body ='';

					<%--按照下拉選單的資料將科中文名稱填入--%>
					$('#DIVList option').each(function(){
						if(data.rtnKPI_StandardList[0].TYPE_ID == $(this).val()){
							Standard_body = '<td>'+$(this).text()+'</td>';
							return false;
						}														
					});
					<%--按照下拉選單的資料將組中文名稱填入--%>					
					if(!Standard_body){
						$('#GroupList option').each(function(){
							if(data.rtnKPI_StandardList[0].TYPE_ID == $(this).val()){
								Standard_body = '<td>'+$(this).text()+'</td>';
							}														
						});
					}
					
					
					var datacolumn = [{title:'名稱', data: 'KPI_NAME', width: '8%'}];						
				
					$.each(data.rtnKPI_StandardList, function(i) { 
						var Standard=data.rtnKPI_StandardList[i];
						var Standard_value = Standard.STD_LOW+'~'+Standard.STD_UP+' ('+Standard.UNIT+')';
						Standard_head = Standard_head + '<th>' + Standard.KPI_NAME + '</th>';
						Standard_body = Standard_body + '<td>' + Standard_value + '</td>';
						
					});
					
					if(!GRP_ID){
					
						$('#myTab2').empty().append('<li class="nav-item" value="' + $('#DIVList').val() + '">' +
												'<a class="nav-link active" id="home-tab" data-toggle="tab" href="#KPI1" role="tab" aria-controls="home">'+ $('#DIVList :selected').text() +'</a>' +
											'</li>');
						$.each(data.groupList, function(i) { 
							var groupList = data.groupList[i];
							$('#myTab2').append('<li class="nav-item" value="' + groupList.GRP_ID + '">' +
													'<a class="nav-link" data-toggle="tab" href="#KPI1" role="tab" aria-controls="home">' + groupList.GRP_NM +'</a>' +
												'</li>');
						});
					}

					<%--產生表格欄位名稱--%>
					$('#table-1-head').empty()
										.append('<tr>' + Standard_head + '</tr>');


					$('#table-1-body').empty()
										.append('<tr>' + Standard_body + '</tr>');					

					<%--顯示表格--%>				  
					$('#tablecard').show();	

					<%--產生表格--%>					
					actions.creatTable();								

					<%--傳遞資料到修改function--%>
					if(data.EMP_ID==0){
						actions.Passvalue(data.rtnKPI_StandardList,data.rtnKPI_DetailList);
				}};	


				$.runAjax('${dispatcher}/XXQK_0200/query', {reqMap : reqMap}, successaction, true);			
			},		
			
			<%--取得組資料--%>
			getGRP : function(){
				$('#tablecard').hide();
				$('#editcard').hide();
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
			},
		
			editSTD : function(data) {
				if(data.ErrMsg.returnCode != 0){
					swal({
					type: 'error',
					title: 'KPI標準修改失敗',
					showConfirmButton: false,
					timer: 1000
					});
					return;
				}
				if($('#myTab2 .active').parent().val()==null){
					buttons.doQuery($('#GroupList').val(),$("#GroupList option:selected").text());										
				} else{
					buttons.doQuery($('#myTab2 .active').parent().val(),$('#myTab2 .active').html());
				}		
				swal({
					type: 'success',
					title: 'KPI標準修改成功',
					showConfirmButton: false,
					timer: 1000
				});
			},
			editdetail : function(data) {
				if(data.ErrMsg.returnCode != 0){
					swal({
						type: 'error',
						title: 'KPI細項修改失敗',
						showConfirmButton: false,
						timer: 1000
					});
					return;
				}
				if($('#myTab2 .active').parent().val()==null){
					buttons.doQuery($('#GroupList').val(),$("#GroupList option:selected").text());
				} else{
					buttons.doQuery($('#myTab2 .active').parent().val(),$('#myTab2 .active').html());
				}		
				swal({
					type: 'success',
					title: 'KPI細項修改成功',
					showConfirmButton: false,
					timer: 1000
				});
			},
			insertSTD : function(data) {
				if(data.ErrMsg.returnCode != 0){
					swal({
						type: 'error',
						title: 'KPI標準新增失敗',
						showConfirmButton: false,
						timer: 1000
					});
					return;
				}
					
				if($('#myTab2 .active').parent().val()==null){
					buttons.doQuery($('#GroupList').val(),$("#GroupList option:selected").text());
				} else{
					buttons.doQuery($('#myTab2 .active').parent().val(),$('#myTab2 .active').html());
				}		
				swal({
					type: 'success',
					title: 'KPI標準新增成功',
					showConfirmButton: false,
					timer: 1000
				});
			},
			insertdetail : function(data) {
				if(data.ErrMsg.returnCode != 0){
					swal({
						type: 'error',
						title: 'KPI細項新增失敗',
						showConfirmButton: false,
						timer: 1000
					});
					return;
				}
				
				if($('#myTab2 .active').parent().val()==null){
					buttons.doQuery($('#GroupList').val(),$("#GroupList option:selected").text());
				} else{
					buttons.doQuery($('#myTab2 .active').parent().val(),$('#myTab2 .active').html());
				}		
				swal({
					type: 'success',
					title: 'KPI細項新增成功',
					showConfirmButton: false,
					timer: 1000
				});
			}
		}

		var actions = {

			<%--產生表格--%>
			creatTable : function(){			
				$('#table-1').DataTable({
					dom:'t',
					responsive: true,				
					scrollX: true,
					destroy:true
					
				});
			},		<%--傳遞list到修改區塊，生成collapse--%>
			Passvalue : function(rtnKPI_StandardList,rtnKPI_DetailList){
				$('#table-1 tbody').unbind('click').on('click', 'tr', function () {
					$('#editcard').show();			
					<%--將頁面滑動至表格處--%>
					$('html, body').animate({scrollTop: $('#editcard').offset().top }, {duration: 700,easing: 'swing'});			
					var table = $('#table-1').DataTable();
					var data = table.row( this ).data();
					var head = '<div class="col-sm-4 col-md-2" >'+'<h4 id="editkindname">'+data[0]+'</h4>'+'</div>'+
									'<div class="col-sm-8 col-md-10" align="right">'+'<button type="button" class="btn btn-primary insert">新增</button>' +'</div>'; 
					$('#editheader').empty().append(head);
					var body=''; 

					$.each(rtnKPI_StandardList, function(i) { 
						var Standard=rtnKPI_StandardList[i];
						var Dbody='<div class="card-header"><div class="col-sm-4 col-md-2" >'+'<h4 id="editkindname">KPI細項</h4>'+'</div>'+
									'<div class="col-sm-8 col-md-10" align="right">'+'<button type="button" class="btn btn-primary insertdetail">新增細項</button></div></div><div class="card-body">';

						$.each(rtnKPI_DetailList, function(j) {
							var Detail=rtnKPI_DetailList[j];
							if(Standard.KPI_ID == Detail.KPI_ID){
								var D_idnum = (j+1)*100;
								Dbody = Dbody+'<div id="accordion2">'+'<div class="accordion">'+'<form>'+								 	
										'<div class="accordion-header" role="button" data-toggle="collapse" data-target="#panel-body-Det'+j+'" aria-expanded="false">'+
										'<h6>'+Detail.KPI_DETAIL+'</h6></div>'+
										'<div class="accordion-body collapsed collapse" id="panel-body-Det'+j+'" data-parent="#accordion2" style="">'+
										'<div class="form-row">'+
										'<div class="form-group col-md-2">'+
										'<label>KPI細項名稱</label>'+'<input type="text" name="KPI_DETAIL" class="form-control" value="'+ Detail.KPI_DETAIL +'"required>'+'</div>'+
										'<div class="form-group col-md-2">'+
										'<label>KPI細項ID</label>'+'<input type="text" name="KPI_DETAIL_ID" class="form-control" value="'+ Detail.KPI_DETAIL_ID +'"required>'+'</div>'+
										'<div class="form-group col-md-2">'+
										'<button type="button" class="btn btn-primary editdetail" style="bottom: 5;position: absolute;" >修改</button></div>'+
										'<div class="form-group col-md-2">'+															 
										'<input type="hidden" name="ORI_ID" value="'+Detail.KPI_DETAIL_ID+'">'+'<input type="hidden" name="TYPE_ID" value="'+Detail.TYPE_ID+'">'+'<input type="hidden" name="KPI_ID" value="'+Detail.KPI_ID+'"></div>'+	
										'</div>'+'</div>'+'</form>'+'</div>'+'</div>';							
							}						
						});	
						Dbody=Dbody+'</div>';

						body = body+ '<div id="accordion">'+'<div class="accordion">'+
									'<form>'+
									'<div class="accordion-header" role="button" data-toggle="collapse" data-target="#panel-body-STD'+i+'" aria-expanded="false">'+
									'<h6>'+Standard.KPI_NAME+'</h6></div>'+
									'<div class="accordion-body collapsed collapse" id="panel-body-STD'+i+'" data-parent="#accordion" style="">'+	
									'<div class="form-row">'+
									'<div class="form-group col-md-2">'+
									'<label>KPI標準名稱</label>'+'<input type="text" name="KPI_NAME" id="KPI_NAME" class="form-control" value="'+ Standard.KPI_NAME +'"required>'+'</div>'+
									'<div class="form-group col-md-1">'+
									'<label>標準下限</label>'+'<input type="text" name="STD_LOW" class="form-control" value="'+ Standard.STD_LOW +'"required >'+'</div>'+
									'<div class="form-group col-md-1">'+                                     
									'<label>標準上限</label>'+'<input type="text" name="STD_UP" class="form-control" value="'+ Standard.STD_UP +'"required>'+'</div>'+
									'<div class="form-group col-md-2">'+                                     
									'<label>標準單位</label>'+'<input type="text" name="UNIT" class="form-control" value="'+ Standard.UNIT +'"required>'+'</div>'+
									'<div class="form-group col-md-2">'+ 
									'<label>標準中文敘述</label>'+'<input type="text" name="PERIOD" class="form-control" value="'+ Standard.PERIOD +'"required>'+'</div>'+	
									'<div class="form-group col-md-2">'+ 
									'<label>數值意義</label>'+'<input type="text" name="JUDGE" class="form-control" placeholder="U=越高越好,D=越低越好" value="'+ Standard.JUDGE +'"required>'+'</div>'+							 
									'<div class="form-group col-md-2">'+                                     
									'<label>判斷依據</label>'+'<input type="text" name="ACORD_ID" class="form-control" value="'+ Standard.ACORD_ID +'"required>'+'</div>'+									 								 
									'<input type="hidden" name="TYPE" value="'+Standard.TYPE+'">'+'<input type="hidden" name="KPI_ID" value="'+Standard.KPI_ID+'">'+'<input type="hidden" name="TYPE_ID" value="'+Standard.TYPE_ID+'">'+'</div>'+
									'<div class="form-row">'+								 
									'<div class="form-group col-md-4">'+
									'<label>模組class</label>'+'<input type="text" name="MODULE_CLASS" class="form-control" value="'+ Standard.MODULE_CLASS +'"required>'+'</div>'+
									'<div class="form-group col-md-3">'+
									'<label>模組method</label>'+'<input type="text" name="MODULE_METHOD" class="form-control" value="'+ Standard.MODULE_METHOD +'"required>'+'</div>'+
									'<div class="form-group col-md-4">'+                                     
									'<label>模組參數</label>'+'<input type="text" name="MODULE_PARAM" class="form-control" value="'+ Standard.MODULE_PARAM +'"required>'+'</div>'+
									'<div class="form-group col-md-1"><button type="button" class="btn btn-primary edit" style="bottom: 5;position: absolute;" >修改</button></div>'+'</div>'+
									'</form>'+Dbody+'</div>'+'</div>'+'</div>';					 
								
					});		
					$('#editbody').empty().append(body); 
					
					$('.edit').on('click',function(){
						var formdata = $(this).parents('form').serializeArray();					
											
						<%--頁面資料檢核--%>					
						var returnVal = true;
						var STD_LOW = parseFloat($(this).parents('form').find('input[name="STD_LOW"]').val());
						var STD_UP = parseFloat($(this).parents('form').find('input[name="STD_UP"]').val());
						if (STD_LOW >= STD_UP) {
							returnVal = false;
						}
															
						jQuery.validator.addMethod('compare',function(value, element){
							return returnVal;
						}, '<font color="red" size="3px">下限需小於上限</font>');

						jQuery.validator.addMethod('space',function(value, element){
							return $.trim(value) != '' ;
						}, '<font color="red" size="3px">不能輸入空格</font>');
						
						jQuery.validator.addMethod("UDconvert",function(value, element){
							var chrnum = /^([U D]+)$/;
						return this.optional(element) || (chrnum.test(value));
						}, '<font color="red" size="3px">只能輸入U跟D</font>');

						$(this).parents('form').validate({
							rules:{	
								KPI_NAME:'space',						
								JUDGE:{UDconvert:true,space:true},
								STD_LOW:{compare:true,space:true,number:true},
								STD_UP:{compare:true,space:true,number:true},
								UNIT:'space',
								PERIOD:'space',
								ACORD_ID:'space',
								MODULE_CLASS:'space',
								MODULE_METHOD:'space',
								MODULE_PARAM:'space'
							}
						});		
						
						var ajax = $(this).parents('form').valid();
				
						if(ajax){	
							$.runAjax('${dispatcher}/XXQK_0200/update', formdata, Callback.editSTD,true);
						}					
					});
					
					$('.editdetail').on('click',function(){
						var formdata = $(this).parent().parent().parent().parent().serializeArray(); 

						<%--頁面資料檢核--%>
						$(this).parent().parent().parent().parent().validate({
								rules:{							
									KPI_DETAIL_ID:'space',		
									KPI_DETAIL:'space'
								}
							});		
						var ajax = $(this).parent().parent().parent().parent().valid();	

						if(ajax){							
							$.runAjax('${dispatcher}/XXQK_0200/updateDetail', formdata, Callback.editdetail,true);
						}
					});
					
					$('.insert').on('click',function(){		
						var idnum = $('#editbody').children('#accordion').length+1;				
						$('#editbody').append('<div id="accordion">'+'<div class="accordion">'+'<form>'+
											'<div class="accordion-header" role="button" data-toggle="collapse" data-target="#panel-body-STD'+ idnum +'" aria-expanded="false">'+
											'<h6 ><label>KPI標準名稱</label>:<input type="text" name="KPI_NAME" class="form-control col-md-2" required><h6>'+'</div>'+
											'<div class="accordion-body collapsed collapse" id="panel-body-STD'+ idnum +'" data-parent="#accordion" style="">'+											
											'<div class="form-row">'+
											'<div class="form-group col-md-2">'+
											'<label>標準下限</label>'+'<input type="text" name="STD_LOW" class="form-control" required>'+'</div>'+
											'<div class="form-group col-md-2">'+                                     
											'<label>標準上限</label>'+'<input type="text" name="STD_UP" class="form-control" required>'+'</div>'+
											'<div class="form-group col-md-2">'+                                     
											'<label>標準單位</label>'+'<input type="text" name="UNIT" class="form-control" required>'+'</div>'+
											'<div class="form-group col-md-2">'+ 
											'<label>標準中文敘述</label>'+'<input type="text" name="PERIOD" class="form-control" required>'+'</div>'+
											'<div class="form-group col-md-2">'+ 
											'<label>數值意義</label>'+'<input type="text" name="JUDGE" class="form-control" placeholder="U=越高越好,D=越低越好" required>'+'</div>'+
											'<div class="form-group col-md-2">'+                                     
											'<label>判斷依據</label>'+'<input type="text" name="ACORD_ID" class="form-control" placeholder="請填寫KPI細項的ID"required>'+'</div>'+							 
											'<input type="hidden" name="KPI_ID" >'+'<input type="hidden" name="TYPE" value="'+rtnKPI_StandardList[0].TYPE+'">'+'<input type="hidden" name="TYPE_ID" value="'+rtnKPI_StandardList[0].TYPE_ID+'">'+'</div>'+
											'<div class="form-row">'+
											'<div class="form-group col-md-3">'+
											'<label>模組class</label>'+'<input type="text" name="MODULE_CLASS" class="form-control" required>'+'</div>'+
											'<div class="form-group col-md-4">'+
											'<label>模組method</label>'+'<input type="text" name="MODULE_METHOD" class="form-control" required>'+'</div>'+
											'<div class="form-group col-md-4">'+                                     
											'<label>模組參數</label>'+'<input type="text" name="MODULE_PARAM" class="form-control" placeholder="用逗點分開"required>'+'</div>'+
											'<div class="form-group col-md-1"><button type="button" class="btn btn-primary insertSTD" style="bottom: 5;position: absolute;">新增</button></div>'+
											'</div>'+'</div>'+'</form>'+'</div>'+'</div>');	

						$('.insertSTD').on('click',function(){					
							var formdata = $(this).parents('form').serializeArray();

							<%--頁面資料檢核--%>
							var returnVal = true;
							var STD_LOW = parseFloat($(this).parents('form').find('input[name="STD_LOW"]').val());
							var STD_UP = parseFloat($(this).parents('form').find('input[name="STD_UP"]').val());
							if (STD_LOW >= STD_UP) {
								returnVal = false;
							}

							jQuery.validator.addMethod('space',function(value, element){
								return $.trim(value) != '' ;
							}, '<font color="red" size="3px">不能輸入空格</font>');

							jQuery.validator.addMethod("compare",function(value, element){
								return returnVal;
							}, '<font color="red" size="3px">下限需小於上限</font>');
							
							jQuery.validator.addMethod("UDconvert",function(value, element){
								var chrnum = /^([U D]+)$/;
							return this.optional(element) || (chrnum.test(value));
							}, '<font color="red" size="3px">只能輸入U跟D</font>');

							$(this).parents('form').validate({
								rules:{							
									KPI_NAME:'space',						
									JUDGE:{UDconvert:true,space:true},
									STD_LOW:{compare:true,space:true,number:true},
									STD_UP:{compare:true,space:true,number:true},
									UNIT:'space',
									PERIOD:'space',
									ACORD_ID:'space',
									MODULE_CLASS:'space',
									MODULE_METHOD:'space',
									MODULE_PARAM:'space'
								}
							});		
							
							var ajax = $(this).parents('form').valid();

							if(ajax){
								$.runAjax('${dispatcher}/XXQK_0200/insertSTD', formdata, Callback.insertSTD,true);									
							}					
						});
					});
					
				
					
					
					$('.insertdetail').on('click',function(){
						var NewDet = $(this).parents('.card-header').next().children('#accordion2').length+1;
						var DKPI_ID = $(this).parent().parent().parent().find('input[name="KPI_ID"]').val();				
						$(this).parents('.card-header').next().append('<div id="accordion2">'+'<div class="accordion">'+'<form>'+								 	
																	'<div class="accordion-header" role="button" data-toggle="collapse" data-target="#panel-body-NDet'+NewDet+'" aria-expanded="false">'+
																	'<h6><label>KPI標準細項名稱</label>:<input type="text" name="KPI_DETAIL" class="form-control col-md-2" required><h6>'+'</div>'+
																	'<div class="accordion-body collapsed collapse" id="panel-body-NDet'+NewDet+'" data-parent="#accordion2" style="">'+
																	'<div class="form-row">'+
																	'<div class="form-group col-md-2">'+
																	'<label>KPI細項ID</label>'+'<input type="text" name="KPI_DETAIL_ID" class="form-control" required>'+'</div>'+
																	'<div class="form-group col-md-2">'+
																	'<button type="button" class="btn btn-primary insertContent" style="bottom: 5;position: absolute;" >新增</button></div>'+
																	'<div class="form-group col-md-2">'+															 
																	'<input type="hidden" name="TYPE_ID" value="'+rtnKPI_StandardList[0].TYPE_ID+'">'+'<input type="hidden" name="KPI_ID" value="'+DKPI_ID +'"></div>'+	
																	'</div>'+'</div>'+'</form>'+'</div>'+'</div>');
																	
																	
						$('.insertContent').on('click',function(){
							var formdata = $(this).parent().parent().parent().parent().serializeArray();
							
							<%--頁面資料檢核--%>
							$(this).parent().parent().parent().parent().validate({
								rules:{							
									KPI_DETAIL_ID:'space',		
									KPI_DETAIL:'space'
								}
							});		

							var ajax = $(this).parent().parent().parent().parent().valid();		

							if(ajax){	
							    $.runAjax('${dispatcher}/XXQK_0200/insertDetail', formdata, Callback.insertdetail,true);									
							}
						});
					});					
				} );
			}
		}
		return {
			initApp : function() {
 
				$('span:contains("KPI標準查詢/維護")').parents('li').addClass('active');
				$('#navtitle').text('KPI標準查詢/維護');
				$('body').on('click','[data-stopPropagation]',function (e) {
					e.stopPropagation();
				});		

				<%--避免datatable fix-column 跑版--%>
				$(window).resize(function() {
					  $.fn.dataTable
						.tables( { visible: true, api: true } )
						.columns.adjust();
				});
				
				<%--防止切換tab時跑版--%>
				$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {					
						$.fn.dataTable.tables( {visible: true, api: true} )
									  .columns.adjust();
				} );					
		
				<%-------------- 按鈕點擊設定 --------------%>
				$('#search').on('click',function(){						<%--查詢--%>
					if($('#GroupList').val() != ''){
						$('#myTab2').empty();
					}
					
					<%-- 隱藏修改區塊 --%>
					$('#editcard').hide();

					buttons.doQuery($('#GroupList').val(),$("#GroupList option:selected").text());
				});
				$('#myTab2').on('click','.nav-item',function(){			<%--組別轉換時查詢--%>
					buttons.doQuery($(this).val()||'',$(this).children().html());
					$('#editcard').hide();
				});
				$('#DIVList').on( 'change',buttons.getGRP); <%--欄位隱藏--%>
			}	
		}; 
   }

</script>
</head>
<body>
<div >
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
								<div class="form-group col-md-5">
									<label>科別：</label>
									<select class="form-control selectric" id="DIVList">   
										<c:forEach items="${rtnDIVList}" var="item" varStatus="loop">
											<option value="${item.DIV_NO}">${item.DIV_NM}</option>
										</c:forEach>
									</select>
								</div>
								<div class="form-group col-md-5">
									<label>組別：</label>
									<select class="form-control selectric" id="GroupList">
										<option value="">全選</option>
										<c:forEach items="${rtnGroupList}" var="item" varStatus="loop">
											<option value="${item.GRP_ID}">${item.GRP_NM}</option>
										</c:forEach>
									</select>
								</div>
								<div class="form-group col-md-2" align="center"></br></br>
									<button type="button" class="btn btn-primary" id="search" >查詢</button>									
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>	
			<div class="row" id="load" style="display:none"><div style="margin:0px auto;"><div class="loader-inner line-spin-fade-loader"></div></div></div>
			<div class="row" id="tablecard"  style="display:none">
				<div class="col-12">
					<div class="card" >
						<ul class="nav nav-tabs" id="myTab2" role="tablist">							
						</ul>
						<div class="tab-content tab-bordered" id="myTabContent2">
							<div class="tab-pane fade active show" id="KPI1" role="tabpanel" aria-labelledby="home-tab">				
								<div class="card-header" id="cardhead">
									<div class="col-sm-4 col-md-2" >
										<h4 id="kindName">科KPI</h4>
									</div>							
								</div>
								<div class="card-body" >
									<table class="table table-striped table-hover" id="table-1" style="width:100%">
										<thead id="table-1-head">
										</thead>
										<tbody id="table-1-body">
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>			
			<div class="row" id="editcard" style="display:none">
				<div class="col-12">
					<div class="card">
						<div class="card-header" id="editheader">                       														
						</div>
						<div class="card-body" id="editbody">                  
						</div>
					</div>
				</div>
			</div>			
        </section>
      </div>
    </div>
</div>
</body>
</html>