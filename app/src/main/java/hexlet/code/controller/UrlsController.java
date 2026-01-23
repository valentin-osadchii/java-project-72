package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;

import kong.unirest.core.Unirest;
import kong.unirest.core.GetRequest;
import kong.unirest.core.HttpResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {

    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var page = new UrlsPage(urls);

        var flash = ctx.consumeSessionAttribute("flash");
        var flashType = ctx.consumeSessionAttribute("flashType");

        if (flash != null) {
            page.setFlash(flash.toString());
        }
        if (flashType != null) {
            page.setFlashType(flashType.toString());
        }

        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));
        var page = new UrlPage(url);
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void create(Context ctx) throws SQLException {

        var name = ctx.formParam("url");

        if (name == null || name.trim().isEmpty()) {
            ctx.sessionAttribute("flashType", "danger");
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        String normalizedUrl;

        try {
            normalizedUrl = normalizeUrl(name);
        } catch (MalformedURLException e) {
            ctx.sessionAttribute("flashType", "danger");
            ctx.sessionAttribute("flash", "Некорректный URL");

            ctx.redirect(NamedRoutes.rootPath());
            return;
        }

        if (UrlRepository.findByName(normalizedUrl).isPresent()) {
            ctx.sessionAttribute("flashType", "success");
            ctx.sessionAttribute("flash", "Страница уже существует");

            ctx.redirect(NamedRoutes.urlsPath());
            return;
        }

        var url = new Url(normalizedUrl);
        UrlRepository.save(url);

        ctx.sessionAttribute("flashType", "success");
        ctx.sessionAttribute("flash", "Страница успешно добавлена");

        ctx.redirect(NamedRoutes.urlsPath(), HttpStatus.forStatus(301));
    }

    public static void check(Context ctx) throws SQLException, IOException {
        var id = ctx.pathParamAsClass("id", Long.class).get(); //нашли url
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url not found")); //достали url

        UrlCheck checkedUrl = checkUrl(url.getName());
        url.addUrlCheck(checkedUrl);

        UrlCheckRepository.save(checkedUrl);

        ctx.sessionAttribute("flash", Map.of(
                "message", "Страница успешно проверена",
                "type", "success"
        ));

        ctx.redirect(NamedRoutes.urlPathCheck(String.valueOf(id)));
    }

    private static UrlCheck checkUrl(String urlString) throws IOException {
        GetRequest request = Unirest.get(urlString)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .header("Accept", "text/html,application/xhtml+xml");

        HttpResponse<String> response = request.asString();
        int statusCode = response.getStatus();
        String htmlBody = response.getBody();

        String contentType = response.getHeaders().getFirst("Content-Type");
        if (contentType == null || !contentType.toLowerCase().contains("text/html")) {
            throw new IOException("Получен не HTML-контент: " + contentType);
        }

        Document doc = Jsoup.parse(htmlBody, urlString);

        String title = doc.title();
        String h1 = Optional.ofNullable(doc.selectFirst("h1"))
                .map(Element::text)
                .orElse(null);

        String description = Optional.ofNullable(doc.selectFirst("meta[name=description]"))
                .map(meta -> meta.attr("content"))
                .orElseGet(() -> Optional.ofNullable(doc.selectFirst("meta[property=og:description]"))
                        .map(meta -> meta.attr("content"))
                        .orElse(null));

        UrlCheck urlCheck = new UrlCheck();
        urlCheck.setStatusCode(statusCode);
        urlCheck.setTitle(title);
        urlCheck.setH1(h1);
        urlCheck.setDescription(description);

        return urlCheck;
    }



    private static String normalizeUrl(String input) throws MalformedURLException {
        try {
            URI uri = new URI(input);
            URL url = uri.toURL();

            String protocol = url.getProtocol();
            String host = url.getHost();
            int port = url.getPort();

            StringBuilder normalized = new StringBuilder();
            normalized.append(protocol).append("://").append(host);

            if (port != -1) {
                normalized.append(":").append(port);
            }

            return normalized.toString();

        } catch (Exception e) {
            throw new MalformedURLException("Некорректный URL");
        }
    }

}
