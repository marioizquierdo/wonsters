/**
 * Scripts comunes para el front_page_layout. 
 */

$( function() { 
	
	// TipTip plugin
	// Definir que elementos de la vista llevan tooltips
	$('*[title]').tipTip({	
		delay: 1000,
		defaultPosition: 'right',
		fadeIn: 100,
		fadeOut: 500
	});
});