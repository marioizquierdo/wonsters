/*
 * getHTML - jQuery Plugin
 * version: 0.1 (29/04/2009)
 * Created by: Mario Izquierdo
 * 
 * API:
 * 	$.getHTML(options)
 *  	Load HTML data using an HTTP GET request, implemented over the $.ajax() jQuery function.
 *  	Returns: XMLHttpRequest.
 *  	Note: The user needs make sure the server sends the correct MIME type as "text/html" 
 *  		in the response, otherwise the funtion doesn't works correctly.
 *  	Options may be:
 *  		- url: where to load the html fragment. Example: 'http://www.myApp.com/loadHTML.do'
 *  		- data: Data to be sent to the server. Examples: 'name=John&location=Boston', or
 *  				{name: 'John', location: 'Boston'}.
 *  		- update: may be a String representing the id of a DOM element like 'myAjaxContent', 
 *  				or a jQuery object like $("#myAjaxContent") or like $(".myAjaxContentClass").
 *  				The referred DOM element(s) will update its inner HTML with the ajax returned html
 *  				on success local event (executed secuentially with success callback).
 *  				This option doesn't have a default value and is mandatory. 
 *  		- beforeSend: A pre-callback to modify the XMLHttpRequest object before it is sent.
 *  				Is a function(XMLHTTPRequest) like in $.ajax().
 *  		- success: A function to be called if the request succeeds. Like in $.ajax().
 *  		- error: A function to be called if the request fails. Like in $.ajax().
 *  				It has a default text predefined.
 * 			- complete: A function to be called when the request finishes (after success and error 
 * 					callbacks are executed). Like in $.ajax().
 * 			- (...) other options used in function $.ajax() will be delegated.
 * 		A default can be set for any option (except 'update' and 'error') with $.ajaxSetup().
 * 
 */

(function($){
	
// Por ahora el error mostrado por defecto se pone aquí
// Mejorando este plugin devería poder indicarse de forma declarativa (algo como $.ajaxSetup())
var onErrorHTML = '<div class="actionInfo_ERROR"> Connection ERROR </div>';
	
$.extend({
	getHTML: function(options) {
		
		/**
		 *  Get options and merge with defaults
		 *  Not especified options may still have a default weather $.ajaxSetup() was called before.
		 *  From here, options will be the variable opt (to no alter the options object)
		 */
	
		var defaults = {
				type: "GET",
				dataType: "html"
		};
		var opt = $.extend(defaults, options);
	
		
		/**
		 * Override opt with our values and functions
		 */
		
		if(typeof opt.update == 'string') {
			opt.update = $('#'+opt.update);
		}
		
		opt.success = function(data, textStatus) {
			var data_content_div = $(data)[1]; //? i dont know why
			
			// Update the DOM element
			opt.update.html($(data_content_div).html());

			// And execute user success callback if defined
			if(options.success) {
				options.success(data, textStatus);
			}
		}
		
		opt.error = function(XMLHttpRequest, textStatus, errorThrown) {
			// error callback has higher priority
			if(options.error) {
				options.error(XMLHttpRequest, textStatus, errorThrown);
				
			// if error callback is not defined, then use the default one.
			} else {
				opt.update.html(onErrorHTML);
			}
		}
		
		/**
		 * Do the ajax request 
		 */
		
		return $.ajax(opt);
	}
});

////Debug function 
//var showAttrs = function(object) {
//	var result = '';
//	var attr;
//	for (attr in object) {
//	    if (typeof object[attr] !== 'function') {
//	        result += attr + ': ' + object[attr] + '\n';
//	    }
//	}
//	return result;
//}

})(jQuery);