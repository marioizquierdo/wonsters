/*
 * Estas funciones se ocupan de la consistencia y funcionamiento del gesto de acciones del mounstro.
 * 
 * */


$(function(){
	
	// Turnos libres que hay actualmente en la pantalla (este es dinamico)
	var free_turns_value = function() {
	    return parseInt($('span.free_turns_value').text()) || 0;
	};
	
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
	    $('input.turns_input').each(function() {
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
	    var turns_avaliable = free_turns_value();
	    turns_avaliable = parseInt(turns_avaliable) + validarInput(target_input.val());
	    target_input.val(0);
	    refreshFreeTurns();
	};
	
	
	// Function para actualizar los turnos libres en funcion de lo que hay en los inputs. 
	//El target_input es el input que se ha cambiado.
	var refreshFreeTurns = function(target_input) {
	    var turns_to_use = 0;
	    $('input.turns_input').each(function() {
	        var turns_input = $(this);
	        turns_input.val(validarInput(turns_input.val()));
	        turns_to_use += parseInt(turns_input.val()) || 0;
	    });
	    
	    var turns_avaliable = monster_free_turns - turns_to_use;
	    if (turns_avaliable<0 && (target_input != undefined)){
	        addMaxTurns(target_input);
	        turns_avaliable = 0;
	    }
	    
	    if (turns_avaliable>0) $('span.free_turns_value').css('color','black');
	    else $('span.free_turns_value').css('color','red').effect('pulsate',[],200);
	    
	   $('span.free_turns_value').text(turns_avaliable);
	   return false;
	};
	 
    var addCuantityInput = function(target_input,cantidad) {
    	cantidad = parseInt(cantidad) || 0;
    	var target_input_value = validarInput(target_input.val());
    	target_input.val(target_input_value + cantidad);
    	refreshFreeTurns(target_input);
    	return false;
    };
	                
	                    // Comportamiento del boton de anhadir un turno mas
	$('form[name=monsterActionsToDoForm] button.add_turn_button').click(function(){
	    var target_input = '#' + $(this).attr('id').replace('_add_turn_button','_turns_input');
	    addCuantityInput($(target_input),1);
	    return false;
	});
	
	// Comportamiento del boton de quitar un turno
	$('form[name=monsterActionsToDoForm] button.remove_turn_button').click(function(){
	   var target_input = '#' + $(this).attr('id').replace('_remove_turn_button','_turns_input');
	    addCuantityInput($(target_input),-1);
	    return false;
	});
	
	// Comportamiento del boton de anhadir todos los turnos
	$('form[name=monsterActionsToDoForm] button.add_all_button').click(function(){
	   var target_input = '#' + $(this).attr('id').replace('_add_all_button','_turns_input');
	   addMaxTurns($(target_input));
	   return false;
	});
	
	//Comportamiento del boton de quitar todos los turnos
	$('form[name=monsterActionsToDoForm] button.remove_all_button').click(function(){
	    var target_input = '#' + $(this).attr('id').replace('_remove_all_button','_turns_input');
	    removeAllTurns($(target_input));
	    return false;
	});
	                
	// Comportamiento cuando se cambia el valor de un input a mano     
	$('input.turns_input').bind('change keyup click paste', function(e){
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