CREATE TABLE users (
    id BIGSERIAL PRIMARY ,
    name VARCHAR NOT NULL ,
    email VARCHAR UNIQUE NOT NULL ,
    password VARCHAR NOT NULL
);

-- insert query

INSERT INTO users  (name, email, password)
VALUES ("name", "email@example.com", "password");

-- update query

UPDATE users
SET
    name = "New Name",
    password = "new password"
WHERE id = 1;

-- delete query || Remove a user and reference in posts and comments

DELETE FROM comments as c
USING
posts as p,
users as u,
WHERE c.post_id = p.id
AND c.user_id = u.id
AND u.id = 1;

DELETE FROM posts as p
USING users as u
WHERE p.user_id = u.id
AND u.id = 1;

DELETE FROM users as u
WHERE u.id = 1;

-- select

SELECT id, name, email, password FROM users
-- WHERE condition_here


