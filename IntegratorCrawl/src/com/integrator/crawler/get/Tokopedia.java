package com.integrator.crawler.get;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class Tokopedia {

	public static void main(String[] args) {
		Tokopedia tokopedia = new Tokopedia();
		tokopedia.test();
	}

	public void test() {
		try {
			WebClient webClient = new WebClient(BrowserVersion.CHROME);
			webClient.getCookieManager().setCookiesEnabled(true);
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setTimeout(2000);
			webClient.getOptions().setUseInsecureSSL(true);
			// overcome problems in JavaScript
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setPrintContentOnFailingStatusCode(false);
			webClient.setCssErrorHandler(new SilentCssErrorHandler());
			
			HtmlPage htmlPage = webClient.getPage("https://m.tokopedia.com/authorize?response_type=code&client_id=1003&redirect_uri=https%3A%2F%2Fm.tokopedia.com%2Fappauth%2Fcode&scope=&state=eyJsZCI6Imh0dHBzOi8vbS50b2tvcGVkaWEuY29tLyIsInJlZiI6Imh0dHBzOi8vbS50b2tvcGVkaWEuY29tLyIsInV1aWQiOiIwOTI3Mzc3Yi0xYjYyLTRmOTQtYWY3MS00OWEwNWMxZWVkYzYiLCJ0aGVtZSI6Im1vYmlsZSJ9&theme=mobile");
			
			System.out.println(htmlPage.asXml());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
