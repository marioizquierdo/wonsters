$( function() {
	
	/**
	* Define el margen que tiene la imagen (monstruo) a la derecha y abajo del wrapper,
	* y el tamaño de la imagen.
	* Devuelve un objeto img_position, con los atributos top y left, que son
	* la distancia que debe haber entre la parte superior izquierda de la imagen, 
	* y la parte inferior derecha del wrapper (es decir, el ancho y alto de la imagen
	* más el margen especificado).
	*/
	var special_margin_right_botton = function(race, ageState) {
		// Define margin object and set default values
		var margin = {right: 10, bottom: 10};
		var size = {width: 100, height: 100}; // Es tamaño de cada imagen

		switch(race) {
		case "Bu": 
			switch(ageState) {
			case "Child": margin = {right: 21, bottom: 19}; size = {width: 43, height: 45}; break;
			case "Cocoon": margin = {right: -160, bottom: -45}; size = {width: 279, height: 138}; break;
			case "Adult": margin = {right: 34, bottom: 15}; size = {width: 89, height: 88}; break;
			case "Old": margin = {right: 40, bottom: 23}; size = {width: 87, height: 79}; break;
			}break;
		
		case "Polbo": 
			switch(ageState) {
			case "Child": margin = {right: 21, bottom: 15}; size = {width: 78, height: 70}; break;
			case "Cocoon": margin = {right: -3, bottom: -2}; size = {width: 279, height: 138}; break;
			case "Adult": margin = {right: 26, bottom: 0}; size = {width: 104, height: 113}; break;
			case "Old": margin = {right: 60, bottom: 20}; size = {width: 92, height: 82}; break;
			}break;
		
		case "Lipendula": 
			switch(ageState) {
			case "Child": margin = {right: 56, bottom: 13}; size = {width: 58, height: 61}; break;
			case "Cocoon": margin = {right: -3, bottom: -2}; size = {width: 279, height: 138}; break;
			case "Adult": margin = {right: -11, bottom: 58}; size = {width: 113, height: 110}; break;
			case "Old": margin = {right: 16, bottom: 26}; size = {width: 88, height: 75}; break;
			}break;
		
		case "Ocodomo": 
			switch(ageState) {
			case "Child": margin = {right: 29, bottom: 0}; size = {width: 131, height: 82}; break;
			case "Cocoon": margin = {right: -25, bottom: 30}; size = {width: 279, height: 138}; break;
			case "Adult": margin = {right: -2, bottom: -11}; size = {width: 270, height: 172}; break;
			case "Old": margin = {right: -2, bottom: 15}; size = {width: 240, height: 141}; break;
			}break;
		
		case "Mongo": 
			switch(ageState) {
			case "Child": margin = {right: 92, bottom: 66}; size = {width: 50, height: 68}; break;
			case "Cocoon": margin = {right: -140, bottom: 20}; size = {width: 279, height: 138}; break;
			case "Adult": margin = {right: -35, bottom: 25}; size = {width: 132, height: 148}; break;
			case "Old": margin = {right: 14, bottom: 7}; size = {width: 93, height: 126}; break;
			}break;
		
		case "Electroserpe": 
			switch(ageState) {
			case "Child": margin = {right: 36, bottom: 0}; size = {width: 113, height: 108}; break;
			case "Cocoon": margin = {right: -23, bottom: -2}; size = {width: 279, height: 138}; break;
			case "Adult": margin = {right: 0, bottom: 0}; size = {width: 258, height: 143}; break;
			case "Old": margin = {right: 0, bottom: 3}; size = {width: 199, height: 117}; break;
			}break;
			30
		case "Quad": 
			switch(ageState) {
			case "Child": margin = {right: 17, bottom: 4}; size = {width: 81, height: 115}; break;
			case "Cocoon": margin = {right: -30, bottom: 20}; size = {width: 279, height: 138}; break;
			case "Adult": margin = {right: -40, bottom: 3}; size = {width: 256, height: 172}; break;
			case "Old": margin = {right: -28, bottom: 0}; size = {width: 261, height: 159}; break;
			}break;
		
		case "Ubunto": 31
			switch(ageState) {
			case "Child": margin = {right: 45, bottom: 7}; size = {width: 76, height: 95}; break;
			case "Cocoon": margin = {right: -150, bottom: 15}; size = {width: 279, height: 138}; break;
			case "Adult": margin = {right: 9, bottom: 7}; size = {width: 109, height: 151}; break;
			case "Old": margin = {right: 11, bottom: 5}; size = {width: 111, height: 151}; break;
			}break;
		}
		var img_position = {
				top: margin.bottom + size.height, 
				left: margin.right + size.width };
		return img_position;
	};
		
	
	// Colocamos la imagen en su sitio al cargar la página y cada vez que se reescale la ventana
	$(window).bind("load resize", function() {
		var wrapper = $("#image_up_wrapper");
		var img = $("#image_up_image"); // Imagen dentro del wrapper
		
		// El punto de referencia es la parte inferior derecha del div #image_up_wrapper
		// A partir de la cual se define un margen para la imagen, dependiendo de su raza y edad.
		var img_position = special_margin_right_botton(img.data('race'), img.data('ageState'));
		
		// La posicion es el punto de referencia con el margen aplicado
		var w_offset = wrapper.offset();
		var w_bottom = w_offset.top + wrapper.height();
		var w_right = w_offset.left + wrapper.width();
		var img_offset_top = w_bottom - img_position.top;
		var img_offset_left = w_right - img_position.left;
		
		// Y se pone la imagen con position absolute y en sus coordenadas según la raza y edad
		img.show().css({"position":"absolute", "left": img_offset_left+'px', "top": img_offset_top+'px'});
	});
	
});