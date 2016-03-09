
$(document).ready(function() {  	  
	  $('#id_checkin').datepicker();  
	});

function populateData(obj){
	alert(obj.REQESTOR);
	document.getElementById('id_first_name').value = obj.REQESTOR.split(' ')[0];
	document.getElementById('id_last_name').value = obj.REQESTOR.split(' ')[1];
}

function requestBlood(formId) {
	top.document.getElementById('loading').style.display='block';
    var elem   = document.getElementById(formId).elements;
    //var url    = document.getElementById(formId).action;        
    var params = "action=request_blood&";
    var value;
    var form = new FormData();
    form.append("action", 'request_blood');
    for (var i = 0; i < elem.length; i++) {
        if (elem[i].tagName == "SELECT") {
            value = elem[i].options[elem[i].selectedIndex].value;
        } else {
            value = elem[i].value;                
        }
        //params += elem[i].name + "=" + encodeURIComponent(value) + "&";
        if('uploaded_file' == elem[i].name){
        	form.append(elem[i].name, elem[i].files[0]);
        	form.append("file_name", elem[i].files[0].name);
        	form.append("file_type", elem[i].files[0].type);
        } else{
        	form.append(elem[i].name, value);	
        }                 
    }

    if (window.XMLHttpRequest) {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
    } else { 
        // code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function(){
		 //alert(xmlhttp.status);
	 	if (xmlhttp.readyState==4 && xmlhttp.status==200)
		{
	 		var obj = JSON.parse(xmlhttp.responseText);
	 		if(obj.err_msg != ''){
	 			document.getElementById("confirm").innerHTML=obj.err_msg;
	 		} 
	 		document.getElementById('content').style.display='none';
	 		document.getElementById('confirm').style.display='block';
	 		top.document.body.scrollTop = top.document.documentElement.scrollTop = 0;
	 		top.document.getElementById('loading').style.display='none';
	 		
	 		document.getElementById('request_number').innerHTML='<b>Click to see the uploaded file</b>';
	 		document.getElementById('request_number').value=obj.request_number;
		}
	  
	 }

    xmlhttp.open("POST","/iSupportLife/master",true);
    //xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");     
    /**
     * The request.getParameter() by default recognizes 
     * application/x-www-form-urlencoded requests only. But you're sending 
     * a multipart/form-data request. You need to annotate your servlet class with 
     * @MultipartConfig so that you can get them by request.getParameter(). 
     * **/
    //xmlhttp.setRequestHeader("Content-length", params.length);
    //xmlhttp.setRequestHeader("Connection", "close");
    //alert(params)
    xmlhttp.send(form);
}

function downloadDocument(){
	window.location = "/iSupportLife/master?action=download_doc&request_number="+document.getElementById('request_number').value;
}

function showRequest(){
	top.document.getElementById('loading').style.display='block';
	
	document.getElementById('content').style.display='none';
	document.getElementById('confirm').style.display='block';
	top.document.body.scrollTop = top.document.documentElement.scrollTop = 0;
	top.document.getElementById('loading').style.display='none';
    
	if (window.XMLHttpRequest) {
        // code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
    } else { 
        // code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function(){
		alert(xmlhttp.status);
	 	if (xmlhttp.readyState==4 && xmlhttp.status==200)
		{
	 		var obj = JSON.parse(xmlhttp.responseText);
	 		if(obj.err_msg != ''){
	 			document.getElementById("confirm").innerHTML=obj.err_msg;
	 		} else{
	 			document.getElementById('place_holder').style.display='block';
	        	document.getElementById('home').style.display='none';
	        	document.getElementById('slider_section').style.display='none';
	        	document.getElementById('object_content').data='request.html';
	        	document.getElementById('object_content').height="750px";
	        	document.getElementById('homeBarContent').style.display='none';
	        	document.getElementById('changeBarTitle').innerHTML='Blood Request Form';	        	
	 		}
	 		document.getElementById('content').style.display='none';
	 		document.getElementById('confirm').style.display='block';
	 		top.document.body.scrollTop = top.document.documentElement.scrollTop = 0;
	 		top.document.getElementById('loading').style.display='none';
		}
	  
	 }

    xmlhttp.open("GET","/iSupportLife/master?action=show_request&request_number="+document.getElementById('request_number').value,false);
    xmlhttp.send();
}

