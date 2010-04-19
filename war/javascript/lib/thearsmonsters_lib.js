/**
 * Se define este objeto global que contiene funciones útiles para utilizar desde
 * los demás scripts de la aplicación bajo la firma ThearsmonstersLib.
 * Utiliza jQuery, que debe ser cargado con anterioridad.
 */
var ThearsmonstersLib = function() {

	
	return {
	
		/**
		 * Muestra una de las secciones ocultas en una ventana con pestañas.
		 * Se utiliza desde el customtag window.tagx
		 */
		showWindowSection: function(windowId, selectedTag) {
			var w = '#'+windowId;

		    // Primero oculta dodos los divs y hace los tags inactivos
			$(w+"_content div[id^='"+windowId+"_']").hide(); // ocultar todos los divs dentro de '#windowId' cuyo id comience por 'windowId_'
			$(w+" div.window_tag div[id^='"+windowId+"_']").addClass('window_inactive_tag').removeClass('window_active_tag'); // tags
			    
		    // Y despues muestra solo el div del tag seleccionado
		    $(w+"_"+selectedTag).show().css({'float': 'left', 'width': '100%', 'height': '100%'});
		    $(w+"_"+selectedTag+"_tag").addClass('window_active_tag').removeClass('window_inactive_tag');
		},
		
		/**
		 * Muestra en una ventana los detalles de una sala. Se utiliza desde
		 * lairContents/lair.jspx (en eventos onClick y al final del archivo).
		 */
		showRoom: function(wrapperId, numRooms, roomNumber, roomType) {
			var r = '#'+wrapperId;

			// Debe asegurarse de que se muestra la ventana oculta (porque esta ocultado al cargar la pagina)
			$(r+'_hide').show();
			
			// Oculta todas las ventanas
		    for (var i=0; i<numRooms; i++){
		    	$(r+i).hide();
		    }
		    // Ydepues muestra solo roomNumber
		    $(r+roomNumber).show();
		    $(r+roomNumber).attr('class', 'lairRoomBackground '+roomType+'_BIG');
		    $(r+'_title').text($(r+roomNumber+'_defineTitle').text());
		    
		    return false;
		},
		
		/**
		 * Añade ceros a la izquierda manteniendo el máximo numero de dígitos en 2.
		 */
		pad2: function(number) {
			
			if(!number) return '00';
			
		    if(number.toString().length == 1) { 
		    	return '0' + number.toString();
		    } else {
		    	return number.toString();
		    }
		}
	
	};
}();