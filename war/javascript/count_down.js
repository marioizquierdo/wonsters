/*
	Inserta una función que resta un segundo a los elementos del DOM
	que sean de la clase ".countdown".
	Precondiciones: Los elementos de la clase ".countdown" deben contener
	en su interior dos elementos (xej <spam>) de clase c_in y c_out.
	c_in contiene los milisegundos desde EPOCH (normalmente va hidden).
	c_out será donde se ponga la salida (los segundos que faltan).
*/

$( function() {
	var countdowns = $(".countdown");
	
	// Si hay countdowns en la pagina, se definen las funciones y comienza su ejecucion
	if(countdowns.size() > 0) {
	
		
		/**
		 * Si el numero es de un solo digito le añade un cero a la izquierda.
		 */
		var pad2 = ThearsmonstersLib.pad2; // Requiere que se haya cargado el script thearsmonsters_lib.js
		
		/**
		 * Versión segura de parseInt.
		 */
		var parse_int = function(integer) {
			return parseInt(integer, 10) || 0;
		};
	
		/**
		* Parsea la variable 'seconds' (integer) y la pasa al formato  'mm:ss' o 'hh:mm:ss'
		*/
		var parse_hms = function(seconds) {
			seconds = parse_int(seconds);
			if(seconds <= 0) { return '00:00'; }
			
			var hours = Math.floor(seconds/60/60);
			var mins = Math.floor(seconds/60) % 60;
			var secs = Math.floor(seconds) % 60;
			
			if(hours>0) {		
				return pad2(hours)+':'+pad2(mins)+':'+pad2(secs);
				
			} else if(mins>0) {	
				return pad2(mins)+':'+pad2(secs);
				
			} else {
				return '00:'+pad2(secs);
			}
		};
		
		/**
		 * Array con los elementos del dom que sirven de entrada y de salida para
		 * la funcion timedCount. Es un array porque puede haber varios countdowns
		 * ejecutandose simultaneamente.
		 */
		var target_millis = []; // en este array se ponen los milisegundos destino de entrada de cada countdown
		var now_millis; // milisegundos desde epoch en la hora actual del servidor, debería ser el mismo en todos los countdowns
		var client_server_lag; // desfase de hora actual entre el servidor y el cliente. Es importante para que no haya errores graves.
		var c_out = []; // este es el buffer de escritura (jQuery object) para escribir los resultados
		var c_reload_timeout = []; // si se activa el timeout para el reload de la pagina se pone aqui
		
		/**
		 * Reduce los segundos de uno en uno, y cuando sync vale 10 se vuelve
		 * a sincronizar con la entrada (c_in), de este modo se elimina el error
		 * en el cronometro para tiempos muy largos.
		 * Es una funcion recursiva que se llama a si misma con setTimeout, fijado
		 * para ejecutarse cada segundo.
		 */
		var timedCount = function() {
			
			// Recursivamente actualiza los countdowns cada segundo.
			// Lo primero que se hace es poner el timeout, asi evitamos "pequeños desfases" por el tiempo de ejecucion de cada ciclo.
			t = setTimeout(timedCount, 1000);
			
			var client_now_millis = (new Date()).getTime(); // milisegundos desde EPOCH ahora, en el cliente.
			
			countdowns.each(function(i, countdown) { // resta un segundo a cada uno de los countdowns definidos en el documento.
				
				// Inicializar los objetos jQuery para este countdown si no estan creados (primera ejecucion de timedCount)
				if(!target_millis[i]) { target_millis[i] = parse_int($(countdown).find(".c_in .target_millis").text()); }
				if(!now_millis) { now_millis = parse_int($(countdown).find('.c_in .now_millis').text()); }
				if(!client_server_lag) { client_server_lag = client_now_millis - parse_int($(countdown).find('.c_in .now_millis').text());} // es importante que solo se calcule la primera vez porque debe ser un valor fijo
				if(!c_out[i]) { c_out[i] = $(countdown).find(".c_out"); }
				
				// Calcular los segundos que faltan para que termine el countdown, teniendo en cuenta el desfase que pueda haber entre el cliente y el servidor.
				var seconds_left = Math.floor((target_millis[i] - client_now_millis + client_server_lag) / 1000);
		
				// Cuando quedan 5 segundos o menos, se activa el timeout para recargar la página para dentro de 5 segundos.
				// Se hace así por si se carga un timeout que ya es 0, así los reloads se hacen cada 5 segundos y no instantaneamente.
				if(seconds_left < 5 && !c_reload_timeout[i]) {
					c_reload_timeout[i] = setTimeout("window.location.reload()", 5000);
				}
					
				// Actualizar el contenido del countdown con un segundo menos
				c_out[i].text(parse_hms(seconds_left));

			});
			
		};
		
		
		// Ejecutar los countdowns para que comience la cuenta atras
		timedCount(); 
	}

});