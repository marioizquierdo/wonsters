/**
 * Scripts comunes para el in_game_layout. 
 */

$( function() { 
	
	// TipTip plugin
	// Definir que elementos de la vista llevan tooltips
	$('img, a, .s_ico, .m_ico, div').not("ul#pikame img").tipTip({	
		delay: 1000,
		defaultPosition: 'right',
		fadeIn: 50,
		fadeOut: 400
	});
	$('span, h3').tipTip({
		delay: 200,
		defaultPosition: 'left',
		fadeIn: 50,
		fadeOut: 400
	});
	
	
	// Menu desplegable
	var menu = $("div#menu_logo");
	var options = $("div#options");
	menu.hover(function() { // mouseover
		options.show();
		
	}, function() { // mouseleave
		options.hide();
	});
});