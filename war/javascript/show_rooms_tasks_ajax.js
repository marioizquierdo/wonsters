/*
    Añade comportamiento Ajaxa a los enlaces que muestra las tareas de cada sala.
*/

$(function() {
    $('li.show_room_tasks a').click(function() {
    	container = $('#load_room_tasks_container');
        $.getHTML({
           url: $(this).attr('href'),
           update: container,
           success: function() {
        		container.show();
        		container.autoscroll();
        	}
        });
        return false;
    });
});

$(function(){
	$.fn.autoscroll = function() {
		jQuery('html,body').animate({
			scrollTop: this.offset().top},
		0);
		return this;
	};
});