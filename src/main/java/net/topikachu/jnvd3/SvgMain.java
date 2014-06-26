package net.topikachu.jnvd3;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.tools.shell.Global;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;

public class SvgMain {
	public static void main(String[] args) throws IOException {

		URL url = new URL("http://www.example.com");

		InputStream chart = SvgMain.class.getClassLoader().getResourceAsStream(
				"chart.html");

		StringWebResponse response = new StringWebResponse(
				IOUtils.toString(chart), url);
		WebClient client = new WebClient(BrowserVersion.CHROME);
		new WebConnectionWrapper(client) {

			public WebResponse getResponse(WebRequest request)
					throws IOException {
				String fileName =request.getUrl().getFile().substring(1);
				
				return null;
			}
		};
		client.setAlertHandler(new AlertHandler() {

			public void handleAlert(Page page, String message) {
				System.out.println(message);

			}
		});
		HtmlPage page = HTMLParser.parseHtml(response,
				client.getCurrentWindow());

		loadJs(page, "d3.min.js");
		loadJs(page, "nv.d3.min.js");
		// com.gargoylesoftware.htmlunit.util.WebClientUtils.attachVisualDebugger(client)
		// ;
		loadJs(page, "draw.js");
		client.waitForBackgroundJavaScript(10000);
		System.out.println(page.asXml());

		// Reader envjs = new InputStreamReader(SvgMain.class.getClassLoader()
		// .getResourceAsStream("env.rhino.1.2.js"));
		// cx.evaluateReader(scope, envjs, "env.rhino.1.2.js", 1, null);
		// Reader sizzle = new InputStreamReader(SvgMain.class.getClassLoader()
		// .getResourceAsStream("sizzle.js"));
		// cx.evaluateReader(scope, sizzle, "sizzle.js", 1, null);
		// Reader d3js = new InputStreamReader(SvgMain.class.getClassLoader()
		// .getResourceAsStream("d3.min.js"));
		// cx.evaluateReader(scope, d3js, "env.rhino.1.2.js", 1, null);
		//
		// Reader nvd3js = new InputStreamReader(SvgMain.class.getClassLoader()
		// .getResourceAsStream("nv.d3.min.js"));
		// cx.evaluateReader(scope, nvd3js, "nv.d3.min.js", 1, null);
		//
		//
		// Reader drawjs = new InputStreamReader(SvgMain.class.getClassLoader()
		// .getResourceAsStream("draw.js"));
		// cx.evaluateReader(scope, drawjs, "draw.js", 1, null);
		//
		// String code= "var returndoc=document.innerHTML";
		// cx.evaluateString(scope, code, "return", 1, null);
		// Object jsdoc = scope.get("returndoc");
		// String document=(String) cx.jsToJava(jsdoc, String.class);
		// System.out.println(document);
	}

	private static void loadJs(HtmlPage page, String jsName) throws IOException {
		InputStream envjs = SvgMain.class.getClassLoader().getResourceAsStream(
				jsName);

		page.executeJavaScript(IOUtils.toString(envjs));
	}
}
