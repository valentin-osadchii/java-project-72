package gg.jte.generated.ondemand.urls;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.util.NamedRoutes;
public final class JteindexGenerated {
	public static final String JTE_NAME = "urls/index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,3,3,3,5,5,7,7,8,8,9,9,9,10,10,13,14,15,16,24,24,25,25,27,27,27,28,28,28,28,28,28,28,28,28,28,28,28,32,32,33,33,35,35,35,35,35,3,3,3,3};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, UrlsPage page) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n        ");
				if (page.getFlash() != null) {
					jteOutput.writeContent("\r\n            <p>");
					jteOutput.setContext("p", null);
					jteOutput.writeUserContent(page.getFlash());
					jteOutput.writeContent("</p>\r\n        ");
				}
				jteOutput.writeContent("\r\n\r\n        <h1>Сайты</h1>\r\n    ");
				jteOutput.writeContent("\r\n    ");
				jteOutput.writeContent("\r\n    ");
				jteOutput.writeContent("\r\n    ");
				jteOutput.writeContent("\r\n        <table>\r\n            <tr>\r\n                <th>ID</th>\r\n                <th>Имя</th>\r\n                <th>Последняя проверка</th>\r\n                <th>Код ответа</th>\r\n            </tr>\r\n            ");
				if (!page.getUrls().isEmpty()) {
					jteOutput.writeContent("\r\n                ");
					for (var url : page.getUrls()) {
						jteOutput.writeContent("\r\n                    <tr>\r\n                        <td>");
						jteOutput.setContext("td", null);
						jteOutput.writeUserContent(url.getId());
						jteOutput.writeContent("</td>\r\n                        <td><a");
						var __jte_html_attribute_0 = NamedRoutes.urlPath(url.getId());
						if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
							jteOutput.writeContent(" href=\"");
							jteOutput.setContext("a", "href");
							jteOutput.writeUserContent(__jte_html_attribute_0);
							jteOutput.setContext("a", null);
							jteOutput.writeContent("\"");
						}
						jteOutput.writeContent(">");
						jteOutput.setContext("a", null);
						jteOutput.writeUserContent(url.getName());
						jteOutput.writeContent("</a></td>\r\n                        <td></td>\r\n                        <td></td>\r\n                    </tr>\r\n                ");
					}
					jteOutput.writeContent("\r\n            ");
				}
				jteOutput.writeContent("\r\n        </table>\r\n    ");
			}
		});
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		UrlsPage page = (UrlsPage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
