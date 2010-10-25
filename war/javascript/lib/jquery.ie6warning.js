/**
 * IE6 Warning (display a warning for IE6 to use better browser) - for jQuery 1.3.x
 * @name      jquery.ie6warning.js
 * @author    Boris Huai
 * @version   0.0.666
 * @date      30-Jun-2010
 * @copyright 
 * @license   
 * @homepage  
 * @example   
*/
jQuery(function($) {

  jQuery.extend({
    warnIE6: function(description, visit) { 
      if (true) {
    	var content = '<div id="ie6-warning">';
  	    content +=   '<div class="ie6_transparent"></div>';
  		content +=   '<div id="ie6_main">';
  		content +=     '<h2 class="ie6_title" title="No Internet Explorer 6"></h2>';
  		content +=     '<p class="ie6_cont">'+description+'</p>';
  		content +=     '<ul id="ie6_browsers">';
  		content +=       '<li><a class="ie8" title="Download Internet Explorer 8" href="http://www.microsoft.com/windows/internet-explorer/worldwide-sites.aspx">Internet Explorer 8</a></li>';
  		content +=       '<li><a class="firefox" target="_blank" title="Download Firefox" href="http://www.mozilla.com/firefox/">Firefox</a></li>';
  		content +=       '<li><a class="chrome" target="_blank" title="Download Chrome" href="http://www.google.com/chrome/">Chrome</a></li>';
  		content +=       '<li><a class="opera" target="_blank" title="Download Opera" href="http://www.opera.com/download/">Opera</a></li>';
  		content +=       '<li><a class="safari" target="_blank" title="Download Safari" href="http://www.apple.com.cn/safari/download/">Safari</a></li>';
  		content +=     '</ul>';
  		content +=     '<p id="ie6-continue"><a href="#">'+visit+'</p>';
  		content +=   '<div>';
  		content += '</div>';

        $("body").append(content);
        $("html").css("overflow", "hidden");
        $("#ie6-warning #ie6-continue a").click(function() {
      	  $("html").css("overflow", "auto");
      	  $("#ie6-warning").remove();
      	  return false;
        });
	  }
     }
   });
  
  
  
  
});
