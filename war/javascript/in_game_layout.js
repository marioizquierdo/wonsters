/**
 * Scripts comunes para el in_game_layout. 
 */

$( function() { 
	
	// TipTip plugin Examples
	// Definir que elementos de la vista llevan tooltips
//	$('img, a, .s_ico, .m_ico, div').not("ul#pikame img").not('.lairRoom').not('img.clickable_help').tipTip({	
//		delay: 1000,
//		defaultPosition: 'right',
//		fadeIn: 50,
//		maxWidth: '300px'
//	});
//	$('span, h3').tipTip({
//		delay: 200,
//		defaultPosition: 'left',
//		fadeIn: 50,
//		maxWidth: '300px'
//	});
//	$('.room').tipTip({
//		delay: 1000,
//		defaultPosition: 'bottom',
//		fadeIn: 200,
//		maxWidth: '200px'
//	});
//	$('img.clickable_help').tipTip({
//		activation: 'click',
//		delay: 0,
//		keepAlive: true,
//		maxWidth: 300,
//		defaultPosition: 'right',
//		maxWidth: '450px'
//	});
	
	
	// Menu desplegable
	var menu = $("div#menu_logo");
	var options = $("div#options");
	menu.hover(function() { // mouseover
		options.show();
		
	}, function() { // mouseleave
		options.hide();
	});
});