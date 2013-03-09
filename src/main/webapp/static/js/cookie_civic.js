cookieControl({
	introText : '<p>This site uses some unobtrusive cookies to store information on your computer.</p>',
	fullText : '<p>Some cookies on this site are essential, and the site won\'t work as expected without them. These cookies are set when you submit a form, login or interact with the site by doing something that goes beyond clicking on simple links.</p><p>We also use some non-essential cookies to anonymously track visitors or enhance your experience of the site. If you\'re not happy with this, we won\'t set these cookies but some nice features of the site may be unavailable.</p><p>To control third party cookies, you can also adjust your <a href="browser-settings" target="_blank">browser settings</a>.</p><p>By using our site you accept the terms of our <a href="http://www.tekheads.co.uk/privacy">Privacy Policy</a>.</p>',
	position : 'left', // left or right
	shape : 'triangle', // triangle or diamond
	theme : 'dark', // light or dark
	startOpen : true,
	autoHide : 5000,
	subdomains : true,
	protectedCookies : [], // list the cookies you do not want deleted
	// ['analytics', 'twitter']
	consentModel : 'information',
	onAccept : function() {
	},
	onReady : function() {
	},
	onCookiesAllowed : function() {
	},
	onCookiesNotAllowed : function() {
	},
	countries : 'United Kingdom,Netherlands' // Or supply a list ['United
	// Kingdom', 'Greece']
});
