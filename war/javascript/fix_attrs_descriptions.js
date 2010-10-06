$( function() {
	
	/**
	 * Poner en el texto del tooltip como HTML con icono
	 */
	var HIDDEN_DESC_CLASS = "hidden_attribute_description";
	$('.attribute_row').each(function() {
		var e = $(this);
		e.find('.attribute_level, .attribute_name, .attribute_description')
			.css('cursor', 'help')
			.tipTip({
			    delay: 100,
			    fadeIn: 100,
			    fadeOut: 400,
			    maxWidth: '300px',
			    defaultPosition: 'left',
			    content: e.find('.hidden_attribute_details').html()
	    	});
	});
});