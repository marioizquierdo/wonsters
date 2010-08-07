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
			$(w+" .window_tag[id^='"+windowId+"_']").addClass('inactive_tag').removeClass('active_tag'); // tags
			    
		    // Y despues muestra solo el div del tag seleccionado
		    $(w+"_"+selectedTag).show().css({'float': 'left', 'width': '100%', 'height': '100%'});
		    $(w+"_"+selectedTag+"_tag").addClass('active_tag').removeClass('inactive_tag');
		},
		
		/**
		 * Muestra en una ventana los detalles de una sala. Se utiliza desde
		 * views/in_game/lair/lair.jspx (en eventos onClick y al final del archivo).
		 */
		showRoom: function(roomType) {
			var wrapper_id = '#roomAttributes';

			// Debe asegurarse de que se muestra la ventana oculta (porque esta ocultado al cargar la pagina)
			$(wrapper_id+'_hide').show();
			
			// Oculta todas las ventanas
			$('.roomAttributes_room').hide();
			
		    // Ydepues muestra solo roomNumber
			var room = $('#roomAttributes_'+roomType);
		    room.show();
		    room.attr('class', 'roomAttributes_room lairRoomBackground '+roomType+'_BIG');
		    
		    $('#roomAttributes_title').text($('#roomAttributes_'+roomType+'_defineTitle').text());
		    
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