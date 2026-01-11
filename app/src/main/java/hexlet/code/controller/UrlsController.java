package hexlet.code.controller;

import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.NotFoundResponse;

import io.javalin.http.Context;
import io.javalin.validation.ValidationException;


import java.sql.SQLException;
import java.util.Optional;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {

    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();

        var page = new UrlsPage(urls);
        page.setFlash(ctx.consumeSessionAttribute("flash"));

        ctx.render("urls/index.jte", model("page", page));
    }



//    public static void show(Context ctx) {
//        var id = ctx.pathParamAsClass("id", Long.class).get();
//        var url = UrlRepository.find(id)
//                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
//        var page = new UrlPage(url);
//        ctx.render("posts/show.jte", model("page", page));
//    }
//
    public static void create(Context ctx) throws SQLException {
        var name = ctx.formParam("url");

        if (UrlRepository.findByName(name).isPresent()) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.redirect(NamedRoutes.urlsPath());
        }

        var url = new Url(name);
        UrlRepository.save(url);

        ctx.sessionAttribute("flash", "Страница успешно добавлена");
        ctx.redirect(NamedRoutes.urlsPath());
    }
}
