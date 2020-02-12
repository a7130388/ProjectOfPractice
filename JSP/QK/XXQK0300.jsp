<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" contentType="text/html; charset=BIG5"%>



<%-- 
�{���GXXQK0300.jsp
�@�̡G�����o
�\��G�ҥ~�ɶ��n�O
�����G2019-06-21
--%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=big5">
<%@ include file="/html/CM/header.jsp"%>

<title>KPI�зǬd��/���@</title>


<%-- �פJ�~��Javascript �P css, �s�W�Щ�U�� --%>

<%-- General CSS Files --%>
<link rel="stylesheet" href="${htmlBase}/XX/QK/css/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${htmlBase}/XX/QK/css/fontawesome/css/all.css">
<link rel="stylesheet" href="${htmlBase}/XX/QK/css/fullcalendar/fullcalendar.min.css">

<%-- Template CSS --%>
<link rel="stylesheet" href="${htmlBase}/XX/QK/css/bootstrap-daterangepicker/daterangepicker.css">
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
<script src="${htmlBase}/XX/QK/js/loaders/loaders.css.js"></script>
<script src="${htmlBase}/XX/QK/js/jquery-validation/jquery.validate.min.js"></script>
<script src="${htmlBase}/XX/QK/js/jquery-validation/messages_zh_TW.js"></script>
<script src="${htmlBase}/XX/QK/js/bootstrap-daterangepicker/daterangepicker.js"></script>
<script src="${htmlBase}/XX/QK/js/fullcalendar/fullcalendar.min.js"></script>

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
		.btn-margin {
			margin: 4px 4px;
			width: 75px;
		}
	</style>
<script>
	$(document).ready(function(){
		XXQK0300().initApp();
	});
   <%----------------------���͵e������----------------------%>
   function XXQK0300(){

		var buttons = {
			insert : function() {
				actions.btn('insertEXP',true,'�s�W');
			},
			update : function() {
				actions.btn('updateEXP',false,'�ק�');
			},
			delete : function() {
				actions.btn('deleteEXP',true,'�R��');
			}	
		}
	
		var actions = {
			btn : function(url,chgval,msg) {
					
				<%-- �e���ˮ� --%>
				jQuery.validator.addMethod('space',function(value, element){					
					return $.trim(value) != '' ;
				}, '<font color="red" size="3px">�����J�Ů�</font>');	

				<%-- �e���ˮ� --%>

				jQuery.validator.addMethod('range',function(value, element){					
					return value > 12 ? false : true ;
				}, '<font color="red" size="3px">�W�X�ɶ��d��</font>');	


				$('#form1').validate({
					rules:{	
						EXC_HOURS:{number:true,space:true,range:true},	
						MEMO:'space'
					}
				});

				if($('#form1').valid()){
					var ajaxurl = '${dispatcher}/XXQK_0300/'+url;
					if(chgval){
						var emp_nm='';
						var emp_id='';				
						$(".emp.active").each(function( index ) {
							emp_nm = emp_nm + $(this).text() + ',';
							emp_id = emp_id + $(this).val() + ',';						
						});	
						$('#form1').find('input[name="EMP_NM"]').val(emp_nm.slice(0,-1));
						$('#form1').find('input[name="EMP_ID"]').val(emp_id.slice(0,-1));
					}	
					var formdata = $('#form1').serializeArray();										
					var OKfunction = function(data){
						var error = msg+'�ҥ~�ɶ�����';
						if(data.ErrMsg.returnCode != 0){
							swal({
							type: 'error',
							title: error,
							showConfirmButton: false,
							timer: 1000
							});
							return;
						}
						$("#myEvent").fullCalendar('removeEvents'); 
						let event = JSON.parse(data.rtnException);
						$("#myEvent").fullCalendar('addEventSource', event);
						var succ = msg+'�ҥ~�ɶ�����';
						swal({
							type: 'success',
							title: succ,
							showConfirmButton: false,
							timer: 1000
						});
					};
					$.runAjax(ajaxurl, formdata,OKfunction,true);
					$('#myModal').modal('toggle');
				}		
			},
			<%-- ���o�H���ɸ�� --%>
			modal : function(url,chgval) {
				var getEMP = function(data) {
					$.each(data.rtnEMP, function(i) { 
						Group = data.rtnEMP[i];
						$('#group').append('<button class="btn btn-outline-primary btn-margin emp" value="'+Group.EMP_ID+'">'+Group.EMP_NM+'</button>');
					});				
				};
				$.runAjax('${dispatcher}/XXQK_0300/QueryEmployee', "",getEMP, true);
			},
			<%-- ���ͤ�� --%>
			callendar : function() {
				let event = <c:out value='${rtnException}' escapeXml='false' default='[]' />;							
				$("#myEvent").fullCalendar({
					height : 800,
					defaultView : 'month',
					defaultDate : Date(),
					firstDay : 0,					
					displayEventTime : false,
					header: {
						left: 'prev,next',
						center: 'title',
						right: 'today,month,listMonth'
					},
					monthNames	: ['�@��', '�G��', '�T��', '�|��', '����', '����', '�C��', '�K��', '�E��', '�̤�', '�̤@��', '�̤G��'],
					monthNamesShort:['�@��', '�G��', '�T��', '�|��', '����', '����', '�C��', '�K��', '�E��', '�̤�', '�̤@��', '�̤G��'],
					dayNames:['�P����','�P���@','�P���G','�P���T','�P���|','�P����','�P����'],
					dayNamesShort:['�P����','�P���@','�P���G','�P���T','�P���|','�P����','�P����'],
					buttonText :{
						today : '����',
						month : '�����',
						listMonth : '�C�����'
					},
					eventLimit: true,
					editable: false,
					selectable: true,		
					selectHelper: true,
					select: function(start, end) {
						if(start._d<=new Date()){
							$('#EXC_HOURS-error').remove();
							$('#MEMO-error').remove();
							$('#form1').find('.error').removeClass('error');
							$('#form1').find('.valid').removeClass('valid');						
							$('#insert').show();
							$('#delete').hide();
							$('#update').hide();
							$(".emp").each(function( index ) {
								$(this).removeClass("active");
								$(this).removeClass("disabled");
								$(this).prop("disabled", false);							
							});
							$('#form1').find('input[name="MEMO"]').val("");
							$('#form1').find('input[name="EXC_HOURS"]').val("");
							$('#form1').find('input[name="start"]').val(moment(start).format("YYYY-MM-DD 08:30"));
							$('#form1').find('input[name="EXC_DATE"]').val(moment(start).format("YYYY-MM-DD"));
							$('#myModal').modal('show');
						}							
					},
					eventClick: function(info) {
						$('#insert').hide();
						$('#delete').show();
						$('#update').show();
						$(".emp").each(function( index ) {
							$(this).addClass("disabled");
							$(this).prop("disabled", true);
							if($(this).text()==info.title){
								$(this).addClass("active");
							}else{
								$(this).removeClass("active");
							}
						});					
						
						$('#form1').find('input[name="EMP_NM"]').val(info.title);
						$('#form1').find('input[name="EMP_ID"]').val(info.emp_id);
						$('#form1').find('input[name="MEMO"]').val(info.memo);
						$('#form1').find('input[name="EXC_DATE"]').val(info.start._i);
						$('#form1').find('input[name="EXC_HOURS"]').val(info.exc_hours);
							
						$('#myModal').modal('show');
					},
					eventColor: '#45ca41'							
				});			
				$("#myEvent").fullCalendar('addEventSource', event);
			},
			datechange : function() {
				$('#myEvent').fullCalendar('gotoDate', new Date( $('#START_DATE').val()));
			}



		}
		return {
			initApp : function() {
								
				$('span:contains("�ҥ~�ɶ��n�O")').parents('li').addClass('active');
				$('#navtitle').text('�ҥ~�ɶ��n�O');

				$('body').on('click','[data-stopPropagation]',function (e) {
					e.stopPropagation();
				});
				$('#START_DATE').daterangepicker({
									"showDropdowns": true,
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
				actions.modal();
				actions.callendar();
				$('#START_DATE').on('change',actions.datechange);
				$('#insert').on( 'click',buttons.insert);
				$('#update').on( 'click',buttons.update);
				$('#delete').on( 'click',buttons.delete);
		
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
							<h4>�ҥ~�ɶ��n�O</h4>
						</div>
						<div class="card-body">
							<div class="fc-overflow">
								<div class="form-row">
									<div class="form-group col-md-12">
										<div class="col-md-3">
											<label>������ܤ���G</label>
											<input type="text" class="form-control pull-left" id="START_DATE">
										</div>
									</div>
								</div>
								<div id="myEvent"></div>
							</div>
						</div>									
					</div>
				</div>
			</div>
			<div class="row" id="load" style="display:none"><div style="margin:0px auto;"><div class="loader-inner line-spin-fade-loader"></div></div></div>
		</section>	
      </div>
	  	<div class="modal" id="myModal">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<!-- Modal Header -->
					<div class="modal-header">
						<h4 class="modal-title">��ܤH��</h4>
						<button type="button" class="close" data-dismiss="modal">&times;</button>
					</div>
					<!-- Modal body -->
					<div class="modal-body">
						<div class="card">
							<div class="card-header">
								<h4>�H��:</h4>
							</div>
							<div class="card-body">
								<div class="btn-group" data-toggle="buttons-checkbox">
									<div class="form-row" id="group">																
									</div>
								</div>
								<form id="form1">
									<div class="form-row" style="margin-top:15px;">
										<div class="form-group col-md-6">
											<h6><label >�п�J�ҥ~�ɼ�</label></h6>
											<input type="text" class="form-control" name="EXC_HOURS" value="" required>
											<input type="hidden" class="form-control" name="start" id="start" value="">
											<input type="hidden" class="form-control" name="EMP_ID" id="EMP_ID" value="">
											<input type="hidden" class="form-control" name="EMP_NM" id="EMP_NM" value="">
											<input type="hidden" class="form-control" name="EXC_DATE" id="EXC_DATE" value="">
										</div>								
										<div class="form-group col-md-6">	
											<h6><label >�п�J��]</label></h6>
											<input type="text" class="form-control" name="MEMO" value="" required>
										</div>
									</div>
								</form>
								<div class="form-group" align="right">
									<button type="button" class="btn btn-primary btn-margin" id="insert">�s�W</button>
									<button type="button" class="btn btn-primary btn-margin" id="update" style="display:none">�ק�</button>
									<button type="button" class="btn btn-primary btn-margin" id="delete" style="display:none">�R��</button>
									<button type="button"class="btn btn-primary btn-margin" data-dismiss="modal">����</button>
								</div>
							</div>		
						</div>
					</div>							
				</div>
			</div>
		</div>
    </div>
</div>
</body>
</html>