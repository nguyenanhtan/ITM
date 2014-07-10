 WebFontConfig = {
    google: { families: [ 'Roboto+Condensed:400,700:latin,vietnamese' ] }
  };
  (function() {
    var wf = document.createElement('script');
    wf.src = ('https:' == document.location.protocol ? 'https' : 'http') +
      '://ajax.googleapis.com/ajax/libs/webfont/1/webfont.js';
    wf.type = 'text/javascript';
    wf.async = 'true';
    var s = document.getElementsByTagName('script')[0];
    s.parentNode.insertBefore(wf, s);
  })();
function cookies()
{
	var ck = document.cookie;
	alert(ck);
}
function checkLogin()
{
	if(document.cookie.username == null)
	{
		alert("Ban phai dang nhap");
		 window.location.assign("Login.jsp");
	}
	else
	{
		return document.cookie.username;
	}
}
function writeHeader(usr,title)
{
	var headHtml = '<div id="header"><h1 id="head-title">'+title+'</h1></div>';
	headHtml+='<div id="prf-user"><div id="goto-home">';
	headHtml+='<a href=home.jsp><img src="./img/home-icon.png" title="Back to home"></a>'+
	'</div>'+
	'<div id="logout"><a href=Logout.jsp><img src="./img/inside-logout-icon.png" title="logout"></a></div>'+
	'<div id="hello-user">'+
	'Xin chào '+usr+	
	'</div></div>';
	$("#header-block").html(headHtml);
	document.getElementsByTagName('title')[0].innerHTML = title;
}
