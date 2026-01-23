-- BEGIN (write your solution here)
DROP TABLE IF EXISTS urls;

CREATE TABLE urls (
    id BIGSERIAL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    created_at timestamp NOT NULL
);

DROP TABLE IF EXISTS url_checks;

CREATE TABLE url_checks (
    id BIGSERIAL PRIMARY KEY AUTO_INCREMENT,
    url_id BIGSERIAL REFERENCES urls(id) ON DELETE CASCADE,
    status_code int,
    h1 varchar,
    title varchar,
    description text,
    created_at timestamp NOT NULL
);
-- END
