/*
	Inserta una funci�n que resta un segundo a los elementos del DOM
	que sean de la clase ".countdown".
	Precondiciones: Los elementos de la clase ".countdown" deben contener
	en su interior dos elementos (xej <spam>) de clase c_in y c_out.
	c_in contiene los milisegundos desde EPOCH (normalmente va hidden).
	c_out ser� donde se ponga la salida (los segundos que faltan).
*/

$( function() {
	var countdowns = $(".countdown");
	
	/**
	 * Si el numero es de un solo digito le añade un cero a la izquierda.
	 */
	var pad2 = ThearsmonstersLib.pad2; // Requiere que se haya cargado el script thearsmonsters_lib.js
	

	/**
	* Parsea la variable 'seconds' (integer) y la pasa al formato  'mm:ss' o 'hh:mm:ss'
	*/
	var parse_hms = function(seconds) {
		seconds = parseInt(seconds);
		if(seconds <= 0) return '00:00'
		
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
			seconds = parseInt(a[0]);
		}
		if(a.length==2){
			seconds = parseInt(a[0])*60 + parseInt(a[1]);
		}
		if(a.length==3){
			seconds = parseInt(a[0])*60*60 + parseInt(a[1])*60 + parseInt(a[2]);
		}

		return seconds;
	};
		
	/**
	 * Variable de sincronizacion que necesita timedCount.
	 * No es un atributo de la funcion timedCount porque setTimeout no puede
	 * invocar funciones con parametros. Inicialmente es cero, se va incrementando
	 * en cada segundo (llamada a timedCount) y al llegar a 10 se reinicia.
	 */
	//var sync = 0;
	
	/**
	 * Array con los elementos del dom que sirven de entrada y de salida para
	 * la funcion timedCount. Es un array porque puede haber varios countdowns
	 * ejecutándose simultaneamente.
	 */
	var end_millis = []; // en este array se ponen los milisegundos de entrada de cada countdown
	var c_out = []; // este es el buffer de escritura (jQuery object) para escribir los resultados
	
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
		
		countdowns.each(function(i, countdown) { // resta un segundo a cada uno de los countdowns definidos en el documento.s
			// Inicializar los objetos jQuery para este countdown si no estan creados (primera iteracion)
			if(!end_millis[i]) end_millis[i] = $(countdown).find(".c_in").html();
			if(!c_out[i]) c_out[i] = $(countdown).find(".c_out");
			
			// Calcular los segundos que faltan
			seconds = Math.floor((end_millis[i] - now_millis) / 1000);
	
			// Al terminar la cuenta atras recarga la página
			if(seconds < 0) {
				window.location.reload();
			}else{
				c_out[i].text(parse_hms(seconds)); // insertar el texto de nuevo
			}
		});
		
		// Recursivamente actualiza los countdowns (cada casi un segundo, porque se supone que tarda un poquito la ejecucion)
		t = setTimeout(timedCount, 999);
		
	}; 
	
	// Si hay countdowns en la pagina, se comienza su ejecucion
	if(countdowns.size() > 0) {
		timedCount(); 
	}
});