package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UrlCheck {

    private Long id;

    private int statusCode;
    private String title;
    private String h1;
    private String description;
    private Url url;

    private final LocalDateTime createdAt = LocalDateTime.now();

}
