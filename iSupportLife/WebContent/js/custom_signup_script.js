function AJAXPost(formId) {
	top.document.getElementById('loading').style.display='block';
    var elem   = document.getElementById(formId).elements;
    //var url    = document.getElementById(formId).action;        
    var params = "action=signup&";
    var value;

    for (var i = 0; i < elem.length; i++) {
        if (elem[i].tagName == "SELECT") {
            value = elem[i].options[elem[i].selectedIndex].value;
        } else {
            value = elem[i].value;                
        }
        params += elem[i].name + "=" + encodeURIComponent(value) + "&";
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
	 		document.getElementById('content').style.display='none';
	 		document.getElementById('confirm').style.display='block';
	 		top.document.body.scrollTop = top.document.documentElement.scrollTop = 0;
	 		top.document.getElementById('loading').style.display='none';
		}
	  
	 }

    xmlhttp.open("POST","/iSupportLife/master",true);
    xmlhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlhttp.setRequestHeader("Content-length", params.length);
    xmlhttp.setRequestHeader("Connection", "close");
    xmlhttp.send(params);
}