        $(function() {
          $('#slides').slidesjs({
            height: 235,
            navigation: false,
            pagination: false,
            effect: {
              fade: {
                speed: 400
              }
            },
            callback: {
                start: function(number)
                {
                    $("#slider_content1,#slider_content2,#slider_content3").fadeOut(500);
                },
                complete: function(number)
                {
                    $("#slider_content" + number).delay(500).fadeIn(1000);
                }
            },
            play: {
                active: false,
                auto: true,
                interval: 6000,
                pauseOnHover: false,
                effect: "fade"
            }
          });
        });


		function showSignupForm(){
        	document.getElementById('place_holder').style.display='block';
        	document.getElementById('home').style.display='none';
        	document.getElementById('homeBarContent').style.display='none';
        	document.getElementById('object_content').height="650px";
        	document.getElementById('object_content').data='registration.html';
        	document.getElementById('changeBarTitle').innerHTML='Registration';
        	document.getElementById('slider_section').style.display='none';
		}

        function showHome(){        	
        	document.getElementById('place_holder').style.display='none';
        	document.getElementById('home').style.display='block';
        	document.getElementById('homeBarContent').style.display='block';
        	document.getElementById('slider_section').style.display='block';
        	document.getElementById('changeBarTitle').innerHTML='';
        }

        function showLogin(){
        	document.getElementById('place_holder').style.display='block';
        	document.getElementById('home').style.display='none';
        	document.getElementById('slider_section').style.display='none';
        	document.getElementById('object_content').data='login.html';
        	document.getElementById('object_content').height="400px";
        	document.getElementById('homeBarContent').style.display='none';
        	document.getElementById('changeBarTitle').innerHTML='Login';
        }
        
		function logout(){
			document.getElementById('login_menu').style.display='block';
			document.getElementById('logout_menu').style.display='none';
			document.getElementById('user_name_td').innerHTML='Guest';
			document.getElementById('homeBarContent').style.display='block';
		}