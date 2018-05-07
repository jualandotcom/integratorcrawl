package com.innoq.sample.htmlunit;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class HtmlUnitExample {
	// define usage of firefox, chrome or IE
	private static final WebClient webClient = new WebClient(BrowserVersion.CHROME);

	public static void main(String[] args) {
		webClient.getCookieManager().setCookiesEnabled(true);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setTimeout(2000);
		webClient.getOptions().setUseInsecureSSL(true);
		// overcome problems in JavaScript
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setPrintContentOnFailingStatusCode(false);
		webClient.setCssErrorHandler(new SilentCssErrorHandler());
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.javascript.StrictErrorReporter")
				.setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject")
				.setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument")
				.setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.html.HtmlScript").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit.javascript.host.WindowProxy")
				.setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache").setLevel(Level.OFF);
		HtmlPage page = null;
		try {
			List<HtmlAnchor> searchResults = new ArrayList<>();
			page = webClient
					.getPage("http://confluence.arc42.org/display/templateEN/" + "arc42+Template+%28English%29+-+Home");
			final HtmlForm searchForm = htmlElementByXPath(page, "//form[contains(@action,'pagetreesearch')]",
					HtmlForm.class).get();
			final HtmlInput searchField = htmlElementByXPath(page,
					"//input[@name='queryString' and contains(@class ,'medium-field')]", HtmlInput.class).get();
			searchField.setValueAttribute("Requirements");
			HtmlSubmitInput submit = (HtmlSubmitInput) searchForm.getElementsByAttribute("input", "type", "submit")
					.get(0);
			page = submit.click();
			Optional<HtmlAnchor> nextLink = null;
			do {
				searchResults.addAll(page.getByXPath("//a[contains(@class,'search-result-link')]").stream()
						.map(HtmlAnchor.class::cast).collect(Collectors.toList()));
				nextLink = htmlElementByXPath(page, "//a[@class='pagination-next']", HtmlAnchor.class);
				if (nextLink.isPresent()) {
					page = nextLink.get().click();
				}
			} while (nextLink.isPresent());
			searchResults.stream().map(a -> a.getTextContent() + "->" + a.getAttribute("href"))
					.forEach(System.out::println);
		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}
	}

	public static <T> Optional<T> htmlElementByXPath(DomNode node, String xpath, Class<T> type) {
		return node.getByXPath(xpath).stream().filter(o -> type.isAssignableFrom(o.getClass())).map(type::cast)
				.findFirst();
	}
}