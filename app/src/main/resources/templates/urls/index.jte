@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.util.NamedRoutes

@param UrlsPage page

@template.layout.page(
    content = @`
        <h1>Сайты</h1>
    <%--    <form action="/courses" method="get">--%>
    <%--        <input type="text" required name="term" value="${page.getTerm()}">--%>
    <%--        <input type="submit" value="Search">--%>
    <%--    </form>--%>
        <table>
            <tr>
                <th>ID</th>
                <th>Имя</th>
                <th>Последняя проверка</th>
                <th>Код ответа</th>
            </tr>
            @if(!page.getUrls().isEmpty())
                @for(var url : page.getUrls())
                    <tr>
                        <td>${url.getId()}</td>
                        <td><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></td>
                        <td></td>
                        <td></td>
                    </tr>
                @endfor
            @endif
        </table>
    `
)