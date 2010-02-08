$( function() { 
	var menu = $("div#menu_logo");
	var options = $("div#options");

	menu.bind("mouseover", function() {
		options.show();
		
	}).bind("mouseleave",function(){
		options.hide();
	});
});