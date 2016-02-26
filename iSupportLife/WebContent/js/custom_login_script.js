
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
			 alert(document.getElementById("user_id").value);
			 if (window.XMLHttpRequest)
			 {// code for IE7+, Firefox, Chrome, Opera, Safari
			     xmlhttp=new XMLHttpRequest();
			 }
			 else
			 {// code for IE6, IE5
			 	xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			 }
			 xmlhttp.onreadystatechange=function(){
				 alert(xmlhttp.status);
			 	if (xmlhttp.readyState==4 && xmlhttp.status==200)
				{
			 		alert(xmlhttp.responseText.toJSON());
					document.getElementById("res").innerHTML=xmlhttp.responseText.toJSON();
					top.document.getElementById('user_name_td').innerHTML='New User';
					top.document.getElementById('loading').style.display='none';
				}
			  
			 }
			  xmlhttp.open("POST","/iSupportLife/upload",true);
			  xmlhttp.send("user_id="+document.getElementById("user_id").value);		 
			
		}

