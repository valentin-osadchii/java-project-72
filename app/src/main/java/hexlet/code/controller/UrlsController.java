package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlListItem;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;

import kong.unirest.core.Unirest;
import kong.unirest.core.GetRequest;
import kong.unirest.core.HttpResponse;

import kong.unirest.core.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {

    public static void index(Context ctx) throws SQLException {
        List<UrlListItem> items = UrlRepository.getAllWithLastChecks();
        var page = new UrlsPage(items);

        consumeFlashToPage(ctx, page);

        ctx.render("urls/index.jte", model("page", page));
    }


    public static void show(Context ctx) throws SQLException {

        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));
        var page = new UrlPage(url);

        consumeFlashToPage(ctx, page);

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

        UrlCheck checkedUrl = checkUrl(url);

        url.addUrlCheck(checkedUrl);
        UrlCheckRepository.save(checkedUrl);

        ctx.sessionAttribute("flashType", "success");
        ctx.sessionAttribute("flash", "Страница успешно проверена");

        ctx.redirect(NamedRoutes.urlPath(String.valueOf(id)));
    }

    private static UrlCheck checkUrl(Url url) throws IOException {

        UrlCheck urlCheck = new UrlCheck(500, url);
        var urlString = url.getName();

        try {
            HttpResponse<String> response = Unirest.get(urlString)
                    .header("User-Agent", "Mozilla/5.0")
                    .asString();

            urlCheck.setStatusCode(response.getStatus());

            String body = response.getBody();
            if (body == null || body.trim().isEmpty()) {
                return urlCheck;
            }

            try {
                Document doc = Jsoup.parse(body, urlString);
                urlCheck.setTitle(doc.title());

                urlCheck.setH1(doc.selectFirst("h1") != null ? doc.selectFirst("h1").text() : null);

                Element descriptionMeta = doc.selectFirst("meta[name=description], meta[property=og:description]");
                urlCheck.setDescription(descriptionMeta != null ? descriptionMeta.attr("content") : null);
            } catch (Exception ignored) {
            }

        } catch (UnirestException e) {
            if (e.getCause() instanceof java.net.SocketTimeoutException) {
                urlCheck.setStatusCode(408); // Request Timeout
            } else if (e.getMessage().toLowerCase().contains("unknown host")) {
                urlCheck.setStatusCode(404); // Not Found
            } else if (e.getMessage().toLowerCase().contains("connection refused")) {
                urlCheck.setStatusCode(502); // Bad Gateway
            } else {
                urlCheck.setStatusCode(500); // Internal Server Error для всех остальных
            }
        }

        return urlCheck;
    }

    private static void consumeFlashToPage(Context ctx, BasePage page) {
        var flash = ctx.consumeSessionAttribute("flash");
        var flashType = ctx.consumeSessionAttribute("flashType");

        if (flash != null) {
            page.setFlash(flash.toString());
        }
        if (flashType != null) {
            page.setFlashType(flashType.toString());
        }
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
