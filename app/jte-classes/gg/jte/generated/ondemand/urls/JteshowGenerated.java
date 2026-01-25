package gg.jte.generated.ondemand.urls;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.util.DateUtils;
import hexlet.code.util.NamedRoutes;
public final class JteshowGenerated {
	public static final String JTE_NAME = "urls/show.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,4,4,4,6,6,9,9,20,20,20,21,21,21,25,25,25,31,31,31,31,31,31,31,31,31,46,46,48,48,48,49,49,49,50,50,50,51,51,51,52,52,52,53,53,53,55,55,59,59,59,59,59,4,4,4,4};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, UrlPage page) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtepageGenerated.render(jteOutput, jteHtmlInterceptor, new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n        <div class=\"container-lg mt-5\">\r\n            <h1>Сайт: https://some-domain.org</h1>\r\n\r\n            <table class=\"table table-bordered table-hover mt-3\">\r\n                <tbody>\r\n                    <tr>\r\n                        <td>ID</td>\r\n                        <td>Имя</td>\r\n                    </tr>\r\n                    <tr>\r\n                        <td>");
				jteOutput.setContext("td", null);
				jteOutput.writeUserContent(page.getUrl().getId());
				jteOutput.writeContent("</td>\r\n                        <td>");
				jteOutput.setContext("td", null);
				jteOutput.writeUserContent(page.getUrl().getName());
				jteOutput.writeContent("</td>\r\n                    </tr>\r\n                    <tr>\r\n                        <td>Дата создания</td>\r\n                        <td>");
				jteOutput.setContext("td", null);
				jteOutput.writeUserContent(DateUtils.format(page.getUrl().getCreatedAt()));
				jteOutput.writeContent("</td>\r\n                    </tr>\r\n                </tbody>\r\n            </table>\r\n\r\n            <h2 class=\"mt-5\">Проверки</h2>\r\n            <form method=\"post\"");
				var __jte_html_attribute_0 = NamedRoutes.urlPathCheck(page.getUrl().getId());
				if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
					jteOutput.writeContent(" action=\"");
					jteOutput.setContext("form", "action");
					jteOutput.writeUserContent(__jte_html_attribute_0);
					jteOutput.setContext("form", null);
					jteOutput.writeContent("\"");
				}
				jteOutput.writeContent(">\r\n                <button type=\"submit\" class=\"btn btn-primary\">Запустить проверку</button>\r\n            </form>\r\n\r\n\r\n            <table class=\"table table-bordered table-hover mt-3\">\r\n                <thead>\r\n                    <tr><th class=\"col-1\">ID</th>\r\n                        <th class=\"col-1\">Код ответа</th>\r\n                        <th>title</th>\r\n                        <th>h1</th>\r\n                        <th>description</th>\r\n                        <th class=\"col-2\">Дата проверки</th>\r\n                    </tr></thead>\r\n                <tbody>\r\n                ");
				for (var urlCheck : page.getUrl().getUrlChecks()) {
					jteOutput.writeContent("\r\n                    <tr>\r\n                        <td class=\"col-1\">");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(urlCheck.getId());
					jteOutput.writeContent("</td>\r\n                        <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(urlCheck.getStatusCode());
					jteOutput.writeContent("</td>\r\n                        <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(urlCheck.getTitle());
					jteOutput.writeContent("</td>\r\n                        <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(urlCheck.getH1());
					jteOutput.writeContent("</td>\r\n                        <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(urlCheck.getDescription());
					jteOutput.writeContent("</td>\r\n                        <td class=\"col-2\">");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(DateUtils.format(urlCheck.getCreatedAt()));
					jteOutput.writeContent("</td>\r\n                    </tr>\r\n                ");
				}
				jteOutput.writeContent("\r\n                </tbody>\r\n            </table>\r\n        </div>\r\n    ");
			}
		}, page);
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		UrlPage page = (UrlPage)params.get("page");
		render(jteOutput, jteHtmlInterceptor, page);
	}
}
