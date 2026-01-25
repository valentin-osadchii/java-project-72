package gg.jte.generated.ondemand.urls;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.util.DateUtils;
import hexlet.code.util.NamedRoutes;
public final class JteindexGenerated {
	public static final String JTE_NAME = "urls/index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,4,4,4,6,6,9,9,18,18,19,19,21,21,21,22,22,22,22,22,22,22,22,22,22,22,22,23,23,24,24,24,25,25,25,26,26,29,29,32,32,33,33,35,35,35,35,35,4,4,4,4};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, UrlsPage page) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n        <h1>Сайты</h1>\r\n        <table class=\"table\">\r\n            <tr>\r\n                <th>ID</th>\r\n                <th>Имя</th>\r\n                <th>Последняя проверка</th>\r\n                <th>Код ответа</th>\r\n            </tr>\r\n            ");
				if (!page.getItems().isEmpty()) {
					jteOutput.writeContent("\r\n                ");
					for (var urlListItem : page.getItems()) {
						jteOutput.writeContent("\r\n                    <tr>\r\n                        <td>");
						jteOutput.setContext("td", null);
						jteOutput.writeUserContent(urlListItem.getId());
						jteOutput.writeContent("</td>\r\n                        <td><a");
						var __jte_html_attribute_0 = NamedRoutes.urlPath(urlListItem.getId());
						if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
							jteOutput.writeContent(" href=\"");
							jteOutput.setContext("a", "href");
							jteOutput.writeUserContent(__jte_html_attribute_0);
							jteOutput.setContext("a", null);
							jteOutput.writeContent("\"");
						}
						jteOutput.writeContent(">");
						jteOutput.setContext("a", null);
						jteOutput.writeUserContent(urlListItem.getName());
						jteOutput.writeContent("</a></td>\r\n                        ");
						if (urlListItem.getLastCheck() != null) {
							jteOutput.writeContent("\r\n                            <td>");
							jteOutput.setContext("td", null);
							jteOutput.writeUserContent(DateUtils.format(urlListItem.getLastCheck().getCreatedAt()));
							jteOutput.writeContent("</td>\r\n                            <td>");
							jteOutput.setContext("td", null);
							jteOutput.writeUserContent(urlListItem.getLastCheck().getStatusCode());
							jteOutput.writeContent("</td>\r\n                        ");
						} else {
							jteOutput.writeContent("\r\n                            <td></td>\r\n                            <td></td>\r\n                        ");
						}
						jteOutput.writeContent("\r\n\r\n                    </tr>\r\n                ");
					}
					jteOutput.writeContent("\r\n            ");
				}
				jteOutput.writeContent("\r\n        </table>\r\n    ");
			}
		}, page);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		UrlsPage page = (UrlsPage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
