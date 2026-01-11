package hexlet.code.model;



import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public final class Url {

    private Long id;
    private String name;
    private LocalDateTime createdAt = LocalDateTime.now();


    public Url(String name) {
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }
}
