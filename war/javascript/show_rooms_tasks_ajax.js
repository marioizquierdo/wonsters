/**
 *   Añade comportamiento Ajax a los enlaces que muestra las tareas de cada sala.
 */
$(function() {
    $('li.show_room_tasks a').click(function() {
    	var container = $('#load_room_tasks_container')
    	var show_room_url = $(this).attr('href'); // la url se pone en el propio enlace por accesibilidad y sencilled
    	
    	container.load(show_room_url, function() {
    		container.show().autoscroll();
        });
        return false;
    });
});

/**
 * Define la función autoscroll para jQuery
 * Mueve la pantalla a la parte superior (igual que al pulsar un enlace normal)
 */
$.fn.autoscroll = function() {
	jQuery('html, body').animate({
		scrollTop: this.offset().top
	}, 0);
	return this;
};