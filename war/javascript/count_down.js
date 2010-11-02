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
		* Parsea la variable 'hms' (string en formato 'ss', 'mm:ss' o 'hh:mm:ss') y lo pasa a segundos (int)
		*/
		var parse_seconds = function(hms) {
			var seconds = 0;
			var a = hms.split(':');
			
			if(a.length==1){
				seconds = parse_int(a[0]);
			}
			if(a.length==2){
				seconds = parse_int(a[0])*60 + parse_int(a[1]);
			}
			if(a.length==3){
				seconds = parse_int(a[0])*60*60 + parse_int(a[1])*60 + parse_int(a[2]);
			}
	
			return seconds;
		};
			
		
		/**
		 * Array con los elementos del dom que sirven de entrada y de salida para
		 * la funcion timedCount. Es un array porque puede haber varios countdowns
		 * ejecutándose simultaneamente.
		 */
		var end_millis = []; // en este array se ponen los milisegundos de entrada de cada countdown
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
			var now_millis = (new Date()).getTime(); // milisegundos desde EPOCH ahora
			var seconds; // segundos que faltan para que termine el countdown
			
			countdowns.each(function(i, countdown) { // resta un segundo a cada uno de los countdowns definidos en el documento.
				// Inicializar los objetos jQuery para este countdown si no estan creados (primera iteracion)
				if(!end_millis[i]) { end_millis[i] = $(countdown).find(".c_in").html(); }
				if(!c_out[i]) { c_out[i] = $(countdown).find(".c_out"); }
				
				// Calcular los segundos que faltan
				seconds = Math.floor((end_millis[i] - now_millis) / 1000);
		
				// Cuando quedan 5 segundos o menos, se activa el timeout para recargar la página para dentro de 5 segundos.
				// Se hace así por si se carga un timeout que ya es 0, así los reloads se hacen cada 5 segundos y no instantaneamente.
				if(seconds < 5 && !c_reload_timeout[i]) {
					c_reload_timeout[i] = setTimeout("window.location.reload()", 5000);
				}
					
				// Actualizar el contenido del countdown con un segundo menos
				c_out[i].text(parse_hms(seconds));

			});
			
			// Recursivamente actualiza los countdowns cada segundo
			t = setTimeout(timedCount, 1000);
			
		};
		
		
		// Ejecutar los countdowns para que comience la cuenta atras
		timedCount(); 
	}

});