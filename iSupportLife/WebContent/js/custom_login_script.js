
		function showSignupForm(){
        	top.document.getElementById('place_holder').style.display='block';
        	top.document.getElementById('home').style.display='none';
        	top.document.getElementById('homeBarContent').style.display='none';
        	top.document.getElementById('object_content').height="650px";
        	top.document.getElementById('object_content').data='registration.html';
        	top.document.getElementById('changeBarTitle').innerHTML='Registration';
		}
		
		function login(){
			 top.document.getElementById('loading').style.display='block';
			 if (window.XMLHttpRequest)
			 {// code for IE7+, Firefox, Chrome, Opera, Safari
			     xmlhttp=new XMLHttpRequest();
			 }
			 else
			 {// code for IE6, IE5
			 	xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			 }
			 xmlhttp.onreadystatechange=function(){
				 //alert(xmlhttp.status);
			 	if (xmlhttp.readyState==4 && xmlhttp.status==200)
				{
			 		var obj = JSON.parse(xmlhttp.responseText);
			 		//alert(obj.age);
			 		if(obj.err_msg != ''){
			 			document.getElementById("res").innerHTML=obj.err_msg;
			 		} else{			 			
			 			top.document.getElementById('user_name_td').innerHTML=obj.name;
			 			showHomeFromLogin();
			 		}										
					top.document.getElementById('loading').style.display='none';
				}
			  
			 }
			  xmlhttp.open("POST","/iSupportLife/upload",true);
			  xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			  xmlhttp.send("user_id="+document.getElementById("user_id").value);		 
			
		}
		
		function showHomeFromLogin(){   
			top.document.getElementById('login_menu').style.display='none';
			top.document.getElementById('logout_menu').style.display='block';
        	top.document.getElementById('place_holder').style.display='none';
        	top.document.getElementById('home').style.display='block';
        	top.document.getElementById('homeBarContent').style.display='none';
        	top.document.getElementById('slider_section').style.display='block';
        	top.document.getElementById('changeBarTitle').innerHTML='';
        }
		

		

