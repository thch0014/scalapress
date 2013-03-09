$(document)
		.ready(
				function() {
					$
							.cookieCuttr({
								cookieAnalytics : false,
								cookieMessage : 'We use cookies on this website, you can <a href="{{cookiePolicyLink}}" title="read about our cookies">read about them here</a>. To use the website as intended please...',
								cookiePolicyLink : '/privacypolicy'
							});
				});