/*
 * Comportamiento de los botones para asignar turnos del monstruo. 
 * Este script se incluye en la vista in_game/monsters/monster.jspx. En particular cumple los siguientes características:
 * 	
 * 	-Hay botones para restar/sumar un turno, y para añadir/quitar todos los turnos posibles en esa tarea.
 * 	-Avisa cuando no se pueden asignar más turnos con un efecto sobre los turnos libres disponibles.
 * 	-No dejar meter números negativos (si el value es menor que cero, se cambia a cero).
 * 	-Previene en la inerción de letras (solo acepta números)
 * 	-También funciona introduciendo números directamente en los inputs (y además es no intrusivo, por lo que si se quita el script se puede seguir utilizando el formulario)
 * */


$(function(){
	
	// Elemento cuyo .text() es el número de turnos libres actual (valor que se va modificando)
	var monster_free_turns_element = $('#monster_tasks span.free_turns_value');
	
	// Turnos libres del mounstro en total, constante que no se cambia.
	// Se lee del valor que aparece inicialmente en la vista, que es válido solo al cargar la página (luego se va a modificar).
	var MONSTER_FREE_TURNS = parseInt(monster_free_turns_element.text()) || 0;
	
	// Número de turnos asignados en total, es decir, la suma de todos los inputs del formulario.
	var totalAssignedTurns = function() {
	    var assigned_turns = 0;
	    $('#monster_tasks input.turns_input').each(function() {
	        var input = $(this);
	        assigned_turns += parseInt(input.val()) || 0;
	    });
	    return assigned_turns;
	};
	
	// Turnos libres que todavia no están asignados.
	var freeTurns = function() {
		return MONSTER_FREE_TURNS - totalAssignedTurns();
	};
	
	// Devuelve el objeto jQuery del taskInput.
	// El parámetro input puede ser el mismo objeto jQuery o bien un String en cuyo caso se interpreta como el id del elemento.
	var getActionInput = function(input) {
		if(typeof input == 'string') {
			return input = $('#'+input); // si es un string, supone que es su id 
		} else {
			return input; // sino supone que es el objeto jQuery.
		}
	};
	
	// Dado un input devuelve el elemento action que lo contiene.
	var actionElementOf = function(input) {
		return input.parents('.monsterActionSuggestion');
	};
	
	// Devuelve el targetValue asociado a ese input como un elemento jQuery al cual se puede leer y escribir con .text()
	var targetValueElementOf = function(input) {
		var input = getActionInput(input);
		return actionElementOf(input).find('.targetValueInfo span.targetValue');
	}
	
    // Hay información guardada como elementos ocultos en el html
    // de el mismo elemento '.monsterActionSuggestion' donde está el input.
    // La info se guarda en '.suggested_action_extra_data', y cada atributo que se puede acceder siguiendo el convenio
    // ".MASuggestion_{{action_attr}}". Con esta función se facilita la obtención de información asociada a un input.
    // Las propiedades que hay son:
    //   * actionInfo(input, "targetValue")                 // Valor del targetVaue original (antes de ser modificado).
    //   * actionInfo(input, "targetValueIncreasePerTurn")  // Incremento por cada turno gastado.
    //   * actionInfo(input, "maxTurnsToAssign")            // Número máximo de turnos que se pueden asignar a una tarea.
    var actionInfo = function(input, action_attr) {
    	var action = actionElementOf(getActionInput(input)); // obtiene el elemento action de ese input
    	return parseInt(action.find('.suggested_action_extra_data .MASuggestion_'+ action_attr).text()) || 0;
    };
    
	// Comprueba que el valor escrito en el input es correcto.
	// El valor es incorrecto si se trata de:
	//   * un string que no sea convertible a un numero entero
	//   * un número menor que cero
	//   * un número mayor que la cantidad de turnos libres disponible
	//   * un número mayor que la cantidad de turnos que se pueden aplicar a esa tarea
	// Si el valor es incorrecto, lo convierte a uno que lo sea (cero o 
	// Si no lo es lo sobreescribe con un valor correcto:
	var validateActionInputValue = function(input) {
		var input = getActionInput(input);
		
		// asegurar que es un numero entero mayor o igual a cero
		var input_val = parseInt(input.val()); 
		if (!input_val || input_val < 0) {
			input.val(0); input_val = 0;
			return;
		}
		
		// asegura que el total no exceda a MONSTER_FREE_TURNS
		var turns_over = totalAssignedTurns() - MONSTER_FREE_TURNS;
		if (turns_over > 0) {
			input.val(input_val - turns_over); input_val = input.val();
			monster_free_turns_element.effect('highlight', {color: '#cc4444'}, 1000).addClass('no_free_turns');
		} else {
			monster_free_turns_element.removeClass('no_free_turns');
		}
		
		// asegurar que no sea mayor de lo permitido para esa tarea
		var max_turns_allowed_to_this_task = actionInfo(input, "maxTurnsToAssign");
		if (input_val > max_turns_allowed_to_this_task) {
			actionElementOf(input).find()
			input.val(max_turns_allowed_to_this_task);
			targetValueElementOf(input).effect('highlight', {color: '#cc4444'});
		} else {
			targetValueElementOf(input).effect('highlight');
		}
	};
	
	// Actualiza la información mostrada en base a los valores de los inputs.
	// Despues de asignar un valor válido a un input hay que actualizar la información que se muetra al usuario:
	//  * turnos libres del monstruo: "#monster_tasks span.MONSTER_FREE_TURNS_value"
	//  * targetValue que tendrá esa tarea si se asignan los turnos indicados: ".targetValue"
	// El input es una referencia al que se acaba de modificar.
	var refreshViewInfo = function(input) {  
	    // Actualizar turnos libres
		var free_turns = freeTurns();
		var ft_element = monster_free_turns_element;
		ft_element.text(free_turns);
	    
	    // Actualizar el targetValue del input que se ha modificado
	    if(input) {
		    var originalTargetValue = actionInfo(input, "targetValue"); // Valor del targetVaue original (sin modificar)
		    var targetValueIncreasePerTurn = actionInfo(input, "targetValueIncreasePerTurn"); // Incremento por cada turno gastado
		    var turnsToUse = input.val();
		    var newTargetValue = originalTargetValue + turnsToUse * targetValueIncreasePerTurn;
		    targetValueElementOf(input).text(newTargetValue);
		}
	};
	

	
	// Fija un valor en un input de una tarea y después valida el resultado, cambiándolo por uno válido si es necesario.
	var setTurnsToActionInput = function(input, turns) {
		var input = getActionInput(input); 	// input puede ser bien un objeto jQuery o bien un string que represente el id del input
    	input.val(turns); 					// fija la cantidad indicada
    	validateActionInputValue(input); 	// valida y corrige el resultado
    	refreshViewInfo(input); 			// refresca los datos informativos en la vista
	};
	
	// Añade una cantidad de turnos a un input (el valor puede ser negativo para restar),
	// y después valida el resultado, modificándolo por un valor válido si es necesario.
	var addTurnsToActionInput = function(input, turns) {
		var input = getActionInput(input);
		var turns_added = parseInt(turns) || 0;
		var current_turns = parseInt(input.val()) || 0;
		setTurnsToActionInput(input, current_turns + turns_added);
    };
	
	// Añadir un turno al input de la acción
    var addTurnToActionInput = function(input) {
    	addTurnsToActionInput(input, 1);
    };
	
	// Asigna al target_input todos los turnos libres en pantalla
	var addMaxTurnsToActionInput = function(input) {
		addTurnsToActionInput(input, freeTurns());
	};
	
	var subtractTurnFromActionInput = function(input) {
    	addTurnsToActionInput(input, -1);
    };
	
	// Pone el valor del target_input a cero.
	var subtractAllTurnsFromActionInput = function(input) {
		setTurnsToActionInput(input, 0);
	};

	
	
	/**** Añadir manejadores a los eventos de los botones ****/
	                
	// add_turn_button
	$('#monster_tasks button.add_turn_button').click(function(){
	    var input_id = $(this).attr('id').replace('_add_turn_button', '_turns_input');
	    addTurnToActionInput(input_id);
	    return false;
	});
	
	// remove_turn_button
	$('#monster_tasks button.remove_turn_button').click(function() {
	   var input_id = $(this).attr('id').replace('_remove_turn_button', '_turns_input');
	    subtractTurnFromActionInput(input_id);
	    return false;
	});
	
	// add_all_button
	$('#monster_tasks button.add_all_button').click(function() {
	   var input_id = $(this).attr('id').replace('_add_all_button', '_turns_input');
	   addMaxTurnsToActionInput(input_id);
	   return false;
	});
	
	// remove_all_button
	$('#monster_tasks button.remove_all_button').click(function() {
	    var input_id = $(this).attr('id').replace('_remove_all_button', '_turns_input');
	    subtractAllTurnsFromActionInput(input_id);
	    return false;
	});
	                
	// turns_input typing   
	$('#monster_tasks input.turns_input').bind('change keyup click paste', function(e) {
		var input = $(this);
		setTurnsToActionInput(input, input.val()); // esto ya valida y actualiza valores.
	    return false;
	});
	
	// Previene el formulario de ser enviado al pulsar enter (keycode 13) sobre un input
	$('form[name=monsterActionsToDoForm]').keydown(function(e) {
	    if (e.which == 13) return false;
	});
      
	// Recalcula los turnos libres al recargar la pagina (ya que se mantienen los valores de los inputs).
	refreshViewInfo();
})