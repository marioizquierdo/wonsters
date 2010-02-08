/*
	En la pantalla de cambiar recursos hay que actualizar la cantidad devuelta
	en el cambio cada vez que el usuario teclee un numero.
*/

$(function() {
	// DataIn: Las variables siguientes se inicializan con datos del DOM
	var crdata = $('#change_resources_js_data');
	var maxGarbageAmountEnabled = parseInt(crdata.find('#crdata_maxGarbageAmountEnabled').html());
	var maxMoneyAmountEnabled = parseInt(crdata.find('#crdata_maxMoneyAmountEnabled').html());
	var tradeOfficePercentageCommission = parseInt(crdata.find('#crdata_tradeOfficePercentageCommission').html());
	
	// Outputs: estos son los campos que hay que actualizar para mostrar la informacion
	var $money_earned = $('#change_resources_js_money_earned'); // after spent garbage
	var $garbage_earned = $('#change_resources_js_garbage_earned'); // after spent money
	
	// El resto de aplicar la comision (cuanto queda depues de aplicar la comision)
	var apply_commission_rest = function(value) {
		return Math.floor(value * (100 - tradeOfficePercentageCommission) / 100);
	}
	
	// Devuelve el valor que hay en el input, asegurandose de que no sea erroneo
	var get_input_value = function(input, maxVal) {
		if(!maxVal || maxVal<0) maxVal = 0;
		var value = parseInt(input.val());
		if(!value || value < 0) {
	    	value = 0;
	    	input.val('');
	    }
		if(value > maxVal) {
			value = maxVal;
			input.val(maxVal);
		}
		return value;
	}

	
	// Cuando el usuario cambia el valor del input de basura hay que actualizar los 'Outputs'
	$("input[type='text'][name='garbage']").keyup(function() {
		var value = get_input_value($(this), maxGarbageAmountEnabled);
	    $money_earned.text(apply_commission_rest(value));
	}).keyup();
	
	// Y lo mismo para el input de dinero
	$("input[type='text'][name='money']").keyup(function() {
		var value = get_input_value($(this), maxMoneyAmountEnabled);
	    $garbage_earned.text(apply_commission_rest(value));
	}).keyup();

	
	
});