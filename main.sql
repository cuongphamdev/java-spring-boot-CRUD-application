CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY ,
    name VARCHAR NOT NULL
);

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR NOT NULL ,
    email VARCHAR UNIQUE NOT NULL ,
    password VARCHAR NOT NULL,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE posts (
    id BIGSERIAL PRIMARY KEY ,
    title VARCHAR NOT NULL ,
    content TEXT NOT NULL ,
    created_at TIMESTAMP NOT NULL ,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE tags (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR NOT NULL
);

CREATE TABLE tag_posts (
    post_id BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    tag_id BIGINT NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
    PRIMARY KEY (post_id, tag_id)
);

CREATE TABLE comments (
    id BIGSERIAL PRIMARY KEY NOT NULL ,
    content TEXT NOT NULL ,
    post_id BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    parent_id BIGINT REFERENCES comments(id) ON DELETE CASCADE
);

INSERT INTO roles (name)
VALUES
('user'),
('admin');

INSERT INTO users (name, email, password, role_id)
VALUES
('Test user 1', 'test1@gmail.com', '123456', 2),
('Test user 2', 'test2@gmail.com', '123456', 2),
('Test user 3', 'test3@gmail.com', '123456', 2),
('Test user 4', 'test4@gmail.com', '123456', 2),
('Test user 5', 'test5@gmail.com', '123456', 2),
('Administrator', 'admin@gmail.com', '123456', 1);

INSERT INTO posts(title, content, user_id, created_at)
VALUES
('Post title 1', 'Lorem ipsum dolor sit amet, mei nobis graece sensibus ad, his possit sensibus salutandi an, ferri copiosae eu cum. Eu dicit solet voluptatum duo, doming saperet ei eum. Ut nam virtute nonumes constituam, usu diam partem insolens id. Qui vitae scripta aliquid te. Simul veritus eum in.', 1, CURRENT_TIMESTAMP),
('Post title 2', 'Lorem ipsum dolor sit amet, mei nobis graece sensibus ad, his possit sensibus salutandi an, ferri copiosae eu cum. Eu dicit solet voluptatum duo, doming saperet ei eum. Ut nam virtute nonumes constituam, usu diam partem insolens id. Qui vitae scripta aliquid te. Simul veritus eum in.', 1, CURRENT_TIMESTAMP),
('Post title 3', 'Lorem ipsum dolor sit amet, mei nobis graece sensibus ad, his possit sensibus salutandi an, ferri copiosae eu cum. Eu dicit solet voluptatum duo, doming saperet ei eum. Ut nam virtute nonumes constituam, usu diam partem insolens id. Qui vitae scripta aliquid te. Simul veritus eum in.', 2, CURRENT_TIMESTAMP),
('Post title 4', 'Lorem ipsum dolor sit amet, mei nobis graece sensibus ad, his possit sensibus salutandi an, ferri copiosae eu cum. Eu dicit solet voluptatum duo, doming saperet ei eum. Ut nam virtute nonumes constituam, usu diam partem insolens id. Qui vitae scripta aliquid te. Simul veritus eum in.', 5, CURRENT_TIMESTAMP),
('Post title 5', 'Lorem ipsum dolor sit amet, mei nobis graece sensibus ad, his possit sensibus salutandi an, ferri copiosae eu cum. Eu dicit solet voluptatum duo, doming saperet ei eum. Ut nam virtute nonumes constituam, usu diam partem insolens id. Qui vitae scripta aliquid te. Simul veritus eum in.', 2, CURRENT_TIMESTAMP);

INSERT INTO tags (name)
VALUES
('Java'),
('PHP'),
('JavaScript'),
('Ruby'),
('Rust');

INSERT INTO tag_posts(post_id, tag_id)
VALUES
(1,2),
(1,3),
(1,4),
(2,1),
(2,3),
(2,2);

INSERT INTO comments (content, post_id, user_id, parent_id)
VALUES
('Lorem ipsum dolor sit amet, commodo adolescens cu est. Noluisse deserunt ne vel. Dicam eleifend eu vis, decore altera aliquip eu ius. Ex duo nostro neglegentur comprehensam, ut sint aperiri.', 1, 1, NULL),
('Lorem ipsum dolor sit amet, commodo adolescens cu est. Noluisse deserunt ne vel. Dicam eleifend eu vis, decore altera aliquip eu ius. Ex duo nostro neglegentur comprehensam, ut sint aperiri.', 1, 2, 2),
('Lorem ipsum dolor sit amet, commodo adolescens cu est. Noluisse deserunt ne vel. Dicam eleifend eu vis, decore altera aliquip eu ius. Ex duo nostro neglegentur comprehensam, ut sint aperiri.', 2, 2, 3),
('Lorem ipsum dolor sit amet, commodo adolescens cu est. Noluisse deserunt ne vel. Dicam eleifend eu vis, decore altera aliquip eu ius. Ex duo nostro neglegentur comprehensam, ut sint aperiri.', 2, 1, 4),
('Lorem ipsum dolor sit amet, commodo adolescens cu est. Noluisse deserunt ne vel. Dicam eleifend eu vis, decore altera aliquip eu ius. Ex duo nostro neglegentur comprehensam, ut sint aperiri.', 2, 3, 5);
