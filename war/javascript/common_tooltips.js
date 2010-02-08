// En este .js se definen los tooltips que seran utilizados en la aplicacion
// Se utiliza el plugin jquery.tooltip.js 
// http://docs.jquery.com/Plugins/Tooltip#API_Documentation
$( function() { 
	$('img, a, .s_ico, .m_ico, div').not("ul#pikame img").tooltip({	
	    track: true, 
	    delay: 400, 
	    showURL: false, 
	    showBody: " - ", 
	    fade: 250 
	});
	$('span, h3').tooltip({
	    delay: 100, 
	    showURL: false, 
	    showBody: " - ", 
	    fade: 250 
	});
});