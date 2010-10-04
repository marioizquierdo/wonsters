/*
 * Comportamiento de los botones para asignar turnos del monstruo. 
 * Este script se incluye en la vista in_game/monsters/monster.jspx. En particular cumple los siguientes características:
 * 
 *  -Hay botones para restar/sumar un turno, y para añadir/quitar todos los turnos posibles en esa tarea.
 *  -Avisa cuando no se pueden asignar más turnos con un efecto sobre los turnos libres disponibles.
 *  -No dejar meter números negativos (si el value es menor que cero, se cambia a cero).
 *  -Previene en la inerción de letras (solo acepta números)
 *  -También funciona introduciendo números directamente en los inputs (y además es no intrusivo, por lo que si se quita el script se puede seguir utilizando el formulario)
 */
$(function(){
	
	var monster_free_turns_element = $('#monster_tasks span.free_turns_value'); // Elemento cuyo .text() es el número de turnos libres actual (valor que se va modificando)
	var monster_total_assigned_turns_element = $('#monster_tasks_submit span.total_assigned_turns'); // Elemento cuyo .text() es el número de turnos asignados a tareas actual (valor que se va modificando)
	
	// Wrapper de la función JavaScript parseInt, pero asegurándose que es en base 10 y si no es un integer devuelve 0 por defecto.
	var parse_int = function(value) {
		return parseInt(value, 10) || 0;
	};
	
	// Turnos libres del mounstro en total, constante que no se cambia.
	// Se lee del valor que aparece inicialmente en la vista, que es válido solo al cargar la página (luego se va a modificar).
	var MONSTER_FREE_TURNS = parse_int(monster_free_turns_element.text());
	
	// Bucle for para cada input. La función callback recibe como argumento el objeto jquery que representa a cada input.
	var forEachInput = function(callback) {
		$('#monster_tasks input.turns_input').each(function() {
	        var input = $(this);
	        callback(input);
	    });
	};
	
	// Número de turnos asignados en total, es decir, la suma de todos los inputs del formulario.
	var totalAssignedTurns = function() {
	    var assigned_turns = 0;
	    forEachInput(function(input) {
	        assigned_turns += getVal(input);
	    });
	    return assigned_turns;
	};
	
	// Turnos libres que todavia no están asignados.
	var freeTurns = function() {
		return MONSTER_FREE_TURNS - totalAssignedTurns();
	};
	
	// Dado un input devuelve el elemento action que lo contiene.
	var actionElementOf = function(input) {
		return input.parents('.monsterActionSuggestion');
	};
	
	// Devuelve el targetValue asociado a ese input como un elemento jQuery al cual se puede leer y escribir con .text()
	var targetValueElementOf = function(input) {
		return actionElementOf(input).find('.targetValueInfo span.targetValue');
	};
	
    // Hay información guardada como elementos ocultos en el html
    // de el mismo elemento '.monsterActionSuggestion' donde está el input.
    // La info se guarda en '.suggested_action_extra_data', y cada atributo que se puede acceder siguiendo el convenio
    // ".MASuggestion_{{action_attr}}". Con esta función se facilita la obtención de información asociada a un input.
    // Las propiedades que hay son:
    //   * actionInfo(input, "targetValue")                 // Valor del targetVaue original (antes de ser modificado).
    //   * actionInfo(input, "targetValueIncreasePerTurn")  // Incremento por cada turno gastado.
    //   * actionInfo(input, "maxTurnsToAssign")            // Número máximo de turnos que se pueden asignar a una tarea.
    var actionInfo = function(input, action_attr) {
    	var action = actionElementOf(input); // obtiene el elemento action de ese input
    	return parse_int(action.find('.suggested_action_extra_data .MASuggestion_'+ action_attr).text());
    };
	
	// Hace un highlight del elemento en rojo.
	var redHighlight = function(jquery_element) {
		jquery_element.effect('highlight', {color: '#cc4444'});
	};
	
	// Hace un highlight del elemento en verde.
	var greenHighlight = function(jquery_element) {
		jquery_element.effect('highlight', {color: '#44cc55'});
	};
    
	// Comprueba que el valor escrito en el input es correcto.
	// El valor es incorrecto si se trata de:
	//   * un string que no sea convertible a un numero entero
	//   * un número menor que cero
	//   * un número mayor que la cantidad de turnos libres disponible
	//   * un número mayor que la cantidad de turnos que se pueden aplicar a esa tarea
	// Si el valor es incorrecto, lo convierte a uno que lo sea (cero o el maximo permitido).
	// Si no lo es lo sobreescribe con un valor correcto:
	var validateActionInputValue = function(input) {
		var input_val = parseInt(input.val(), 10);
		
		// Cualquier otra cosa que no sea un entero se convierte a cadena vacía, , que se interpreta como 0. 
		if (!input_val && input_val !== 0) {
			input.val(''); return;
		}
		
		// Menor que cero no vale, se convierte a cero.
		if (input_val < 0) { 
			input.val(0); return;
		}
		
		// asegura que el total no exceda a MONSTER_FREE_TURNS
		var turns_over = totalAssignedTurns() - MONSTER_FREE_TURNS;
		if (turns_over > 0) {
			input.val(input_val - turns_over); input_val = input.val();
		}
		
		// asegurar que no sea mayor de lo permitido para esa tarea
		var max_turns_allowed = actionInfo(input, "maxTurnsToAssign");
		if (max_turns_allowed && input_val > max_turns_allowed) {
			input.val(max_turns_allowed);
		}
	};
	
	// Actualiza la información mostrada en base a los valores de los inputs.
	// Despues de asignar un valor válido a un input hay que actualizar la información que se muetra al usuario:
	//  * turnos libres del monstruo: "#monster_tasks span.MONSTER_FREE_TURNS_value"
	//  * targetValue que tendrá esa tarea si se asignan los turnos indicados: ".targetValue"
	// El input es una referencia al que se acaba de modificar. Si se indica, los demás inputs no tienen por que ser actualizados.
	var refreshViewInfo = function(input) {
	    // Actualizar turnos libres
		var old_free_turns = parse_int(monster_free_turns_element.text());
		var new_free_turns = freeTurns();
		var ft_element = monster_free_turns_element;
		
		ft_element.text(new_free_turns);
		if (new_free_turns === 0) { ft_element.addClass('no_free_turns'); } else { ft_element.removeClass('no_free_turns'); } // marcar en rojo si es 0
		if(old_free_turns === 0 && new_free_turns === 0) { redHighlight(monster_free_turns_element); } // efecto resaltar si se pasa a cero de repente.
		if(new_free_turns > old_free_turns) { greenHighlight(ft_element); }
	    
		// Actualizar turnos asignados 
		monster_total_assigned_turns_element.text(totalAssignedTurns());
		
	    // Actualizar el targetValue del cada input
		var refreshTargetValue = function(input) {
		    var original_target_value = actionInfo(input, "targetValue"); // Valor del targetVaue original (sin modificar)
		    var target_value_increase_per_turn = actionInfo(input, "targetValueIncreasePerTurn"); // Incremento por cada turno gastado
		    var turns_to_use = getVal(input);
		    var target_value_element = targetValueElementOf(input);
		    var old_target_value = target_value_element.text();
		    var new_target_value = original_target_value + turns_to_use * target_value_increase_per_turn;
		    var max_turns_allowed = actionInfo(input, "maxTurnsToAssign");
		    
		    target_value_element.text(new_target_value);
		    if(turns_to_use == max_turns_allowed) { redHighlight(target_value_element); } // se resalta en rojo si no se ha podido mejorar
		    if(turns_to_use < max_turns_allowed && new_target_value > old_target_value) { greenHighlight(target_value_element); } // se resalta en verde cuando se mejora
		};
		if(input) { // Si se ha especificado un input como parámetro, solo se actualiza el targetValue de ese input.
			refreshTargetValue(input);
		} else { // Sino, se actualiza el targetValue de cada uno de los inputs del formulario.
			forEachInput(function(input) {
				refreshTargetValue(input);
			});
		}

	};
	
	// Similar a input.val(), solo que acepta la cadena vacía (o cualquier otra cosa que no sea un entero) y
	// lo interpreta como 0. Además input puede ser un String que es el identificador del input, o un objeto jQuery. 
	var getVal = function(input) {
		return parse_int(input.val());
	};
	
	// Fija un valor en un input de una tarea, valida el resultado (cambiándolo por uno válido si es necesario)
	// y refresca los datos relacionados en la vista.
	var setVal = function(input, value) {
    	input.val(value); 					// fija la cantidad indicada
    	validateActionInputValue(input); 	// valida y corrige el resultado
    	refreshViewInfo(input); 			// refresca los datos informativos en la vista
	};

	
	// alias de setVal(input, value)
	var setTurnsToActionInput = function(input, turns) {
		setVal(input, turns);
	};
	
	// Añade una cantidad de turnos a un input (el valor puede ser negativo para restar),
	// y después valida el resultado, modificándolo por un valor válido si es necesario.
	var addTurnsToActionInput = function(input, turns) {
		var turns_added = parse_int(turns);
		setVal(input, getVal(input) + turns_added);
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
		setVal(input, 0);
	};

	
	
	/**** Añadir manejadores a los eventos de los botones ****/
	                
	// add_turn_button
	$('#monster_tasks button.add_turn_button').click(function(){
	    var input_id = $(this).attr('id').replace('_add_turn_button', '_turns_input');
	    addTurnToActionInput($('#'+input_id));
	    return false;
	});
	
	// remove_turn_button
	$('#monster_tasks button.remove_turn_button').click(function() {
	   var input_id = $(this).attr('id').replace('_remove_turn_button', '_turns_input');
	    subtractTurnFromActionInput($('#'+input_id));
	    return false;
	});
	
	// add_all_button
	$('#monster_tasks button.add_all_button').click(function() {
	   var input_id = $(this).attr('id').replace('_add_all_button', '_turns_input');
	   addMaxTurnsToActionInput($('#'+input_id));
	   return false;
	});
	
	// remove_all_button
	$('#monster_tasks button.remove_all_button').click(function() {
	    var input_id = $(this).attr('id').replace('_remove_all_button', '_turns_input');
	    subtractAllTurnsFromActionInput($('#'+input_id));
	    return false;
	});
	                
	// turns_input typing   
	$('#monster_tasks input.turns_input').bind('change keyup click paste', function(e) {
		var input = $(this);
		validateActionInputValue(input); 	// valida y corrige el resultado
    	refreshViewInfo(input); 			// refresca los datos informativos en la vista
	    return false;
	});
	
	// Previene el formulario de ser enviado al pulsar enter (keycode 13) sobre un input
	$('form[name=monsterActionsToDoForm]').keydown(function(e) {
	    if (e.which == 13) { return false; }
	});
      
	// Recalcula los turnos libres al recargar la pagina (ya que se mantienen los valores de los inputs).
	refreshViewInfo();
});
