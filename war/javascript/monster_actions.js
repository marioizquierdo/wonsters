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
	
	// Turnos libres que hay actualmente en la pantalla (este es dinamico)
	var freeTurnsValue = function() {
	    return parseInt($('#monster_tasks span.free_turns_value').text()) || 0;
	};
	
	//Turnos libres del mounstro en total. (Este es estatico y no cambia)
	var monster_free_turns = freeTurnsValue();
	
	//Esta funcion devuelve el valor validado de valor_input. Si es una cadena  o menor que cero devuelve 0.
	var validarInput = function(valor_input){
	        if (valor_input <= 0) {
	            return 0;
	        }
	        return (parseInt(valor_input) || 0);
	};
	
	//Asigna al target_input todos los turnos libres en pantalla
	var addMaxTurns= function (target_input){
	    var turns_to_use = 0;
	    //sumamos todos los inputs menos el que nos pasan
	    $('#monster_tasks input.turns_input').each(function() {
	        var turns_input = $(this)
	        if (turns_input.attr('id') != target_input.attr('id')){
	            turns_to_use += parseInt(turns_input.val()) || 0;
	        }
	    });
	    var turns_avaliable = monster_free_turns - turns_to_use;
	    target_input.val(turns_avaliable);
	    refreshFreeTurns();
	};
	
	var removeAllTurns = function(target_input){
	    var turns_avaliable = freeTurnsValue();
	    turns_avaliable = parseInt(turns_avaliable) + validarInput(target_input.val());
	    target_input.val(0);
	    refreshFreeTurns();
	};
	
	
	// Function para actualizar los turnos libres en funcion de lo que hay en los inputs. 
	// El target_input es el input que se ha cambiado.
	var refreshFreeTurns = function(target_input) {
		// Calcular turnos usados
	    var turns_to_use = 0;
	    $('#monster_tasks input.turns_input').each(function() {
	        var turns_input = $(this);
	        turns_input.val(validarInput(turns_input.val()));
	        turns_to_use += parseInt(turns_input.val()) || 0;
	    });
	    
	    // Calcular turnos libres
	    var turns_avaliable = monster_free_turns - turns_to_use;
	    if ((turns_avaliable < 0) && (target_input != undefined)) {
	        addMaxTurns(target_input);
	        turns_avaliable = 0;
	    }
	    
	    // Poner turnos libres en 'span.free_turns_value'
		$('#monster_tasks span.free_turns_value').text(turns_avaliable);
	    if (turns_avaliable > 0) {
	    	$('#monster_tasks span.free_turns_value').css('color', 'black');
	    } else {
	    	$('#monster_tasks span.free_turns_value').css('color', 'red').effect('pulsate', [], 200);
	    }
	    
	    // Cambiar el targetValue correspondiente a la tarea del input.
	    if(target_input) refreshTargetValue(target_input);
	    
	    return false;
	};
	
	// Refresca el valor del targetValue correspondiente a la tarea del input.
	var refreshTargetValue = function(target_input) {
		var action = target_input.parents('.monsterActionSuggestion');
		var action_attr = function(attr_name) {
			return parseInt(action.find('.suggested_action_extra_data .MASuggestion_' + attr_name).text()) || 0;
		}
		
	    var targetValue = action_attr('targetValue'); // Valor del targetVaue original (sin modificar)
	    var targetValueIncreasePerTurn = action_attr('targetValueIncreasePerTurn'); // Incremento por cada turno gastado
	    var turnsToUse = parseInt(target_input.val()) || 0;
	    
	    var newTargetValue = targetValue + turnsToUse * targetValueIncreasePerTurn;
	    action.find('.targetValueInfo span.targetValue').text(newTargetValue);
	}
	
	// Añadir cierta cantidad al input de la acción
    var addCuantityInput = function(target_input, cuantity) {
    	cuantity = parseInt(cuantity) || 0;
    	var target_input_value = validarInput(target_input.val());
    	target_input.val(target_input_value + cuantity);
    	refreshFreeTurns(target_input);
    	return false;
    };
	                
	// Comportamiento del boton de anhadir un turno mas
	$('form[name=monsterActionsToDoForm] button.add_turn_button').click(function(){
	    var target_input = '#' + $(this).attr('id').replace('_add_turn_button', '_turns_input');
	    addCuantityInput($(target_input), 1);
	    return false;
	});
	
	// Comportamiento del boton de quitar un turno
	$('form[name=monsterActionsToDoForm] button.remove_turn_button').click(function(){
	   var target_input = '#' + $(this).attr('id').replace('_remove_turn_button', '_turns_input');
	    addCuantityInput($(target_input), -1);
	    return false;
	});
	
	// Comportamiento del boton de anhadir todos los turnos
	$('form[name=monsterActionsToDoForm] button.add_all_button').click(function(){
	   var target_input = '#' + $(this).attr('id').replace('_add_all_button', '_turns_input');
	   addMaxTurns($(target_input));
	   return false;
	});
	
	//Comportamiento del boton de quitar todos los turnos
	$('form[name=monsterActionsToDoForm] button.remove_all_button').click(function(){
	    var target_input = '#' + $(this).attr('id').replace('_remove_all_button', '_turns_input');
	    removeAllTurns($(target_input));
	    return false;
	});
	                
	// Comportamiento cuando se cambia el valor de un input a mano     
	$('form[name=monsterActionsToDoForm] input.turns_input').bind('change keyup click paste', function(e){
	    refreshFreeTurns($(this));
	    return false;
	});
	
	// Previene el formulario de ser enviado al pulsar enter sobre un input
	$('form[name=monsterActionsToDoForm]').keydown(function(e) {
	    if (e.which == 13) {
	      return false;
	    }
	 });
      
	//Hay que recalcular los turnos libres al recargar la pagina
	refreshFreeTurns();
})