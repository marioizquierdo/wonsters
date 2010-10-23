$.reject({ 
		reject: { /* Rejection flags for specific browsers */
		        all: false, /* Covers Everything (Nothing blocked)  */
		        msie5: true, msie6: true /* Covers MSIE 5-6 (Blocked by default) */ 
		    },  
		    display: ['chrome', 'firefox', 'msie'], /* What browsers to display and their order */
		    browserInfo: {
			      chrome: {  
			          text: 'Chrome 2+',  
			          url: 'http://www.google.com/chrome/'  
			      },
		        firefox: {  
		            text: 'Firefox 3.6+',
		            url: 'http://www.mozilla.com/firefox/'
		        },
		        msie: {  
		            text: 'Internet Explorer 7+',  
		            url: 'http://www.microsoft.com/windows/Internet-explorer/default.aspx'  
		        }
		    },
		    header: $('messages_for_jreject_header').text(),
		    paragraph1: $('messages_for_jreject_paragraph1').text(),
		    paragraph2: $('messages_for_jreject_paragraph2').text(),
		    /* close: true, */
		    /* closeMessage: 'By closing this window you acknowledge that your experience on this website may be degraded', */
		    /* closeLink: 'Close This Window', */
		    /* closeURL: '#', // Close URL (Defaults '#')  */
		    /* closeESC: true, // Allow closing of window with esc key   */
		    /* closeCookie: false, // If cookies should be used to remmember if the window was closed (applies to current session only) */
		    imagePath: '/images/browsers/',
		    overlayBgColor: '#000',
		    overlayOpacity: 0.8,
		    fadeOutTime: 'slow'
});