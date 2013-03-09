var jQueryVersion = '1.5.2';
var jQueryRemoteProtocol = (("https:" == document.location.protocol) ? "https://" : "http://");
document.write(unescape("%3Cscript src='" + jQueryRemoteProtocol + "ajax.googleapis.com/ajax/libs/jquery/" + jQueryVersion + "/jquery.min.js' type='text/javascript'%3E%3C/script%3E"));

if(typeof jQuery === 'undefined'){document.write(unescape("%3Cscript src='files/js/jquery/jquery-" + jQueryVersion + ".min.js' type='text/javascript'%3E%3C/script%3E"));}
