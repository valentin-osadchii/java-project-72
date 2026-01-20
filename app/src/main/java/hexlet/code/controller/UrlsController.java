package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;


import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;

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
