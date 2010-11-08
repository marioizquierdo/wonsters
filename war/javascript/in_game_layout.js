/**
 * Scripts comunes para las vistas que vayan en el in_game_layout
 */

$( function() { 
		
	// Menu desplegable
	var menu = $("div#menu_logo");
	var options = $("div#options");
	
	menu.hover(function() { // mouseover
	
		var offset_left = menu.offset().left;
		var offset_top = menu.offset().top + menu.height() - 6;
		options.css({
			"position": "absolute", 
			"left": offset_left+'px', 
			"top": offset_top+'px'
		}).show();
		
	}, function() { // mouseleave
		options.hide();
	});
	
	
	// TipTip plugin Examples
	// Definir que elementos de la vista llevan tooltips
	$('span.show_unit').tipTip({
		edgeOffset: 10,
		fadeIn: 100,
		maxWidth: '320px',
		defaultPosition: 'bottom'
	});
	
});