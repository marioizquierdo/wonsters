/**
 * Scripts comunes para el in_game_layout. 
 */

$( function() { 
	
	// TipTip plugin Examples
	// Definir que elementos de la vista llevan tooltips
	$('#menu_info_content .icon').tipTip({
		edgeOffset: 8,
		fadeIn: 55,
		maxWidth: '320px'
	});
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

	
	
	// Menu desplegable
	var menu = $("div#menu_logo");
	var options = $("div#options");
	menu.hover(function() { // mouseover
		options.show();
		
	}, function() { // mouseleave
		options.hide();
	});
});