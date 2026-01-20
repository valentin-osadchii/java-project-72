package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import org.junit.jupiter.api.BeforeEach;
import hexlet.code.repository.UrlRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.sql.SQLException;

import io.javalin.testtools.JavalinTest;


public class AppTest {
    private Javalin app;

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
        UrlRepository.removeAll();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Проверить");
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Сайты");
        });
    }

    @Test
    public void testUrlPage() {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url("https://some-domain.org:8080/example/path");
            UrlRepository.save(url);

            var response = client.get(NamedRoutes.urlPath("1"));
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://some-domain.org:8080");
        });
    }


    @Test
    public void testUrlNotFound() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath("1"));
            assertThat(response.code()).isEqualTo(404);
        });
    }


    @Test
    public void testAddNewUrlFollowsRedirectAndShowsFlash() {
        JavalinTest.test(app, (server, client) -> {
            // Создаем клиент с поддержкой Cookie
            var httpClient = new okhttp3.OkHttpClient.Builder()
                    .cookieJar(new okhttp3.JavaNetCookieJar(new java.net.CookieManager()))
                    .followRedirects(true)
                    .build();

            var requestBody = new okhttp3.FormBody.Builder()
                    .add("url", "https://www.example2.com")
                    .build();

            var request = new okhttp3.Request.Builder()
                    .url("http://localhost:" + server.port() + NamedRoutes.urlsPath())
                    .post(requestBody)
                    .build();

            try (var response = httpClient.newCall(request).execute()) {
                String body = response.body().string();
                assertThat(response.code()).isEqualTo(200);
                assertThat(body).contains("Страница успешно добавлена");
                assertThat(body).contains("https://www.example2.com");
            }
        });
    }

    @Test
    public void testAddExistingUrlFollowsRedirectAndShowsFlash() {
        JavalinTest.test(app, (server, client) -> {
            var name = "https://some-domain.org:8080/example/path";
            var expectedName = "https://some-domain.org:8080";
            var url = new Url(expectedName);
            UrlRepository.save(url);

            var httpClient = new okhttp3.OkHttpClient.Builder()
                    .cookieJar(new okhttp3.JavaNetCookieJar(new java.net.CookieManager()))
                    .followRedirects(true)
                    .build();

            var requestBody = new okhttp3.FormBody.Builder()
                    .add("url", name)
                    .build();

            var request = new okhttp3.Request.Builder()
                    .url("http://localhost:" + server.port() + NamedRoutes.urlsPath())
                    .post(requestBody)
                    .build();

            try (var response = httpClient.newCall(request).execute()) {
                String body = response.body().string();
                assertThat(response.code()).isEqualTo(200);
                assertThat(body).contains("Страница уже существует");
                assertThat(body).contains(expectedName);
            }
        });
    }


    @Test
    public void testIncorrectUrlAndShowsFlash() {
        JavalinTest.test(app, (server, client) -> {
            var name = "https://some space domain.org";

            var httpClient = new okhttp3.OkHttpClient.Builder()
                    .cookieJar(new okhttp3.JavaNetCookieJar(new java.net.CookieManager()))
                    .followRedirects(true)
                    .build();

            var requestBody = new okhttp3.FormBody.Builder()
                    .add("url", name)
                    .build();

            var request = new okhttp3.Request.Builder()
                    .url("http://localhost:" + server.port() + NamedRoutes.urlsPath())
                    .post(requestBody)
                    .build();

            try (var response = httpClient.newCall(request).execute()) {
                String body = response.body().string();
                assertThat(response.code()).isEqualTo(200);
                assertThat(body).contains("Некорректный URL");
            }
        });
    }

}
