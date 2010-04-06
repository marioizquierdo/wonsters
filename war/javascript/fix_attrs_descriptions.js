$( function() {
	var BRIEF_LENGTH = 55;
	var HIDDEN_DESC_CLASS = "hidden_attribute_description";
		
	$('.attribute_row .attribute_description').each(function() {
		var e = $(this);
		var description = e.text();
		var bief = description.substring(0, BRIEF_LENGTH);
		e.text(bief);
		if(bief.length == BRIEF_LENGTH) {
			e.append('...');
			e.append('<div class="'+HIDDEN_DESC_CLASS+' attribute_details_content"'+
			' style="display:none">'+description+'</div>')
		} else {
			e.append('<div class="'+HIDDEN_DESC_CLASS+'"/>')
		}
	});
	
	$('.attribute_row').each(function() {
		var e = $(this);
		e.find('.attribute_level, .attribute_name, .attribute_description')
			.css('cursor', 'help')
			.tipTip({
			    delay: 100,
			    fadeIn: 100,
			    fadeOut: 400,
			    defaultPosition: 'left',
			    content: '<h3>'+ e.find('.hidden_m_icon').html() +'&nbsp;'+
		        	e.find('.attribute_name').text() +'</h3>'+ 
		        	'<div>'+ e.find('.'+HIDDEN_DESC_CLASS).text() +'</div>'+
		        	e.find('.hidden_attribute_details').html()
	    	})
	    	;
	});
});