$.extend({
   runAjax: function(url, params, successCallBack, isAsync){
        //console.log('run ajax url : ' +  url + '//params : ' +  params+" , "+isAsync);
        $.ajax({
            cache: false,
            type:'POST', //���whttp�Ѽƶǿ�榡��POST					?
            url: url,//�ШD�ؼЪ�url�A�i�burl���[�WGET�ѼơA�p www.xxxx.com?xx=yy&xxx=yyy
            contentType : 'application/x-www-form-urlencoded; charset=utf-8',
            data: params, //�n�ǵ��ؼЪ�params��id=formId��Form��ǦC��(serialize)�����ȡA�����t��name������value					?
            dataType: 'json',  	//�ؼ�url�B�z����^�Ǫ��Ȥ�type�A���C���@��JSON Object
                                //Ajax���\����檺function�Aresponse���^�Ǫ���					?
                                //���d�C�^�Ǫ�JSON Object�����e�榡�p�k�ҥ�: {userName:XXX,uswerInterest:[y1,y2,y3,...]}	
            async: (isAsync == false) ? false : true, //isAsync�]�w�Atrue=�D�P�B�Afalse=�P�B		
            error: function(xhr) {
						alert('�o�Ϳ��~');
					},				
            success :  function(data){
                if(data.ErrMsg.returnCode != 0){
                    toastr.options = {'positionClass': 'toast-bottom-right',
                                        'timeOut': '2000'}; // ��m�����O}
                    toastr.error( data.ErrMsg.msgDesc );
                    return;
                }
                if(successCallBack){
                    successCallBack(data);
                }
            },  
            beforeSend : function(){
					$.blockUI({
                        message: $('#load') ,
                        css: { borderWidth: '0px', backgroundColor: 'transparent' } 
                    });
                    $('.loader-inner').show();
			},
            //Ajax���ѫ�n���檺function�A���Ҭ��L�X���~�T��
            error:function(xhr, ajaxOptions, thrownError){
                toastr.options = {'positionClass': 'toast-bottom-right',
												'timeOut': '5000'}; // ��m�����O}
                toastr.error('Ajax���楢��');
                toastr.error(xhr.status + '<br>' + thrownError);
            },        
            complete: function() {             
                $.unblockUI(); 
                $('.loader-inner').hide();
            }
        });
    }    
});
