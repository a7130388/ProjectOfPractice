$.extend({
   runAjax: function(url, params, successCallBack, isAsync){
        //console.log('run ajax url : ' +  url + '//params : ' +  params+" , "+isAsync);
        $.ajax({
            cache: false,
            type:'POST', //指定http參數傳輸格式為POST					?
            url: url,//請求目標的url，可在url內加上GET參數，如 www.xxxx.com?xx=yy&xxx=yyy
            contentType : 'application/x-www-form-urlencoded; charset=utf-8',
            data: params, //要傳給目標的params為id=formId的Form其序列化(serialize)為的值，之內含有name的物件value					?
            dataType: 'json',  	//目標url處理完後回傳的值之type，此列為一個JSON Object
                                //Ajax成功後執行的function，response為回傳的值					?
                                //此範列回傳的JSON Object的內容格式如右所示: {userName:XXX,uswerInterest:[y1,y2,y3,...]}	
            async: (isAsync == false) ? false : true, //isAsync設定，true=非同步，false=同步		
            error: function(xhr) {
						alert('發生錯誤');
					},				
            success :  function(data){
                if(data.ErrMsg.returnCode != 0){
                    toastr.options = {'positionClass': 'toast-bottom-right',
                                        'timeOut': '2000'}; // 位置的類別}
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
            //Ajax失敗後要執行的function，此例為印出錯誤訊息
            error:function(xhr, ajaxOptions, thrownError){
                toastr.options = {'positionClass': 'toast-bottom-right',
												'timeOut': '5000'}; // 位置的類別}
                toastr.error('Ajax執行失敗');
                toastr.error(xhr.status + '<br>' + thrownError);
            },        
            complete: function() {             
                $.unblockUI(); 
                $('.loader-inner').hide();
            }
        });
    }    
});
