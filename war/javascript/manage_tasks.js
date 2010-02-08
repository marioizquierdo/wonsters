
$( function() {
	
	// Definimos el evento que abre la ventana para cada tarea.
	// La ventana se abre y luego se carga su contenido via Ajax, pasandole
	// la informacion guardada en la propia tarea (eso se hace en la página monster.jspx).
	$(".task").each(function(i) {
		var task_wrapper = $(this);
		task_wrapper.click(function(e) {
			monsterTasksManager.getManagerWindow()
				.open(e.pageX + 10, e.pageY - 8)
				.loadAjaxContent(task_wrapper.data("task"));
		});
	});
	
});

/**
 * Objeto para gestionar las tareas de un monstruo. Se construye en la vista
 * del mosntruo, en un script que le pasa el identificador del monstruo, la
 * edad, y todas las cosas necesarias para hacer las peticiones Ajax de las tareas.
 * En la vista MonsterContent.jspx se crea el objeto monsterTasksManager utilizando esta función.
 */
var newMonsterTasksManager = function(attrs) {
	var monsterId = attrs.monsterId || 0; // Identificador del monstruo
	var ageState = attrs.ageState || 'Child'; // Edad del monstruo (Child, Adult, Old)
	var ajaxContentURL = attrs.ajaxContentURL;
	var taskManagerWindowId = attrs.taskManagerWindowId || 'task_manager_window';
	
	/**
	 * Objeto que encapsula el comportamiento de la ventana de edición
	 * de las tareas (para seleccionar otra tarea).
	 * Esta función es el constructor, que inicializa una ventana cerrada (invisible).
	 * @param windowId es el id del div que representa la ventana en el DOM.
	 */
	var newManagerWindow = function(managerWindowId) {
		
		// Objeto jQuery que encapsula el div donde está la ventana
		var div = $('#'+managerWindowId);
		var div_default_content = div.html();
		
		// Identificador del timeout cuando se hace mouseout para cerrar la ventana.
		var timeoutToClose;
		
		var close_speed = "slow"; 
		var isOpened = false;
		
		
		// Método para abrir la ventana. Las coordenadas (en pixeles)
		// de la parte superior izquierda de la ventana (en el lugar donde se abre)
		// son opcionales, si no se indican se toma la posición anterior de la ventana.
		var open = function(left, top) {
			clearTimeout(timeoutToClose);
			timeoutToClose = setTimeout(function() {close(1800);}, 2500);
			
			if(left && top) { // Cambia la posición solo si se indica una
				div.css({
					'display': 'none',
					'position': 'absolute',
					'left': left+'px', 
					'top': top+'px'
				});
			} else {
				div.css({
					'display': 'none',
					'position': 'absolute'
				});
			}
			div.html(div_default_content);
			div.slideDown("fast", function() {isOpened = true;});
			return this;
		};
		
		// Método para cerrar la ventana. Speed es el parametro usado en el efecto.
		var close = function(speed) {
			var speed = speed || "normal";
			
			isOpened = false;
			timeoutToClose = 0;
			div.fadeOut(speed);
			return this;
		};
		
		// Método que carga el contenido via Ajax y lo inserta en la ventana.
		var loadAjaxContent = function(sendData) {
			var defaultData = {monsterId: monsterId, ageState: ageState};
			var data = $.extend(defaultData, sendData);
			
			$.getHTML({
				url: ajaxContentURL,
				data: data,
				update: div
			});
			return this;
		}
		
		// Carga el contenido de un div (que puede ser oculto) y lo inserta en la ventana
		// element puede ser el objeto jQuery con el elemento o bien un String que es el id de ese elemento
		var loadContentFromDomElement = function(element) {
			var element = typeof element == 'string' ?
					$('#'+element) : element;
			div.html(element.html());
			return this;
		}
		
		/* Inicialización de la ventana */
		
		close(); // la ventana se inicializa cerrada
		
		div.mouseleave(function() { // al apartar el ratón se cierra (con timeout)
			timeoutToClose = setTimeout(function() {close("slow");}, 500);
		});
		
		div.mouseenter(function() { // al volver a poner el ratón encima se cancela el timeout
			if(timeoutToClose != 0) {
				clearTimeout(timeoutToClose);
			}
		});
		
		div.click(function () { // Al pinchar en cualquier sitio cierra inmediatamente
			close("fast");
		});
		
		// Devuelve el objeto con los métodos interesantes y atributos.
		return {
			div: div, open: open, close: close, loadAjaxContent: loadAjaxContent, 
			loadContentFromDomElement: loadContentFromDomElement
		};
		
	};
	
	/**
	 * En esta variable se cachea la una única instancia de managerWindow
	 */
	var managerWindow;
	
	/**
	 * public String taskHTML(Task task)
	 * Devuelve el contenido en HTML para un div de una tarea
	 */
	var taskHTML = function(task) {
		
		// Read task values
		description = task.description || '???';
		turn = ThearsmonstersLib.pad2(task.turn);
		
		return (
			'<div class="data_label">'+ turn +'</div>'+
			'<div class="data_value">'+ description +'</div>'
			);
	}

	return {
		
		/**
		 * Escribe una tarea en el div identificado por taskDivId 
		 * y guarda el objeto Task con sus atributos mediante la función data
		 * en ese mismo div (asi luego puede ser recuperada con facilidad).
		 * @param taskDivId es el id del div donde se inserta la tarea.
		 * 			El HTML de la tarea anterior será reemplazado dentro de ese div.
		 * @param task objeto Task que contiene los datos de la tarea (turn, text, etc).
		 */
		setTask: function(taskDivId, task) {
			$('#'+taskDivId)
				.html(taskHTML(task))
				.data('task', task);
		},
		
		/**
		 * Devuelve la instancia de la ventana para cambiar de tarea.
		 * @param managerWindowId es el id del DIV que representa dicha ventana en el DOM.
		 * 		Solamente es necesario la primera vez que se hace la llamada, pues luego
		 * 		se hace caché en una variable y en las próximas llamadas el parámetro es ignorado.
		 */
		getManagerWindow: function(managerWindowId) {
			var managerWindowId = managerWindowId || taskManagerWindowId;
			if(!managerWindow) {
				managerWindow = newManagerWindow(managerWindowId);
			}
			return managerWindow;
		},
		
		getMonsterId: function() {
			return monsterId;
		},
		
		getAgeState: function() {
			return ageState;
		}
	}
	
	
}; 