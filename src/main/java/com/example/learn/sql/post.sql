CREATE TABLE posts (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR,
    content TEXT,
    created_at current_timestamp,
    user_id BIGINT REFERENCES users(id)
);

-- insert query

INSERT INTO posts  (title, content, user_id)
VALUES ("title", "content", 1);

-- update query

UPDATE posts
SET
    title = "New Post Title",
    content = "new content"
WHERE id = 1;

-- delete query

DELETE FROM comments as c
USING posts as p
WHERE c.post_id = p.id;

DELETE FROM posts
WHERE id = 1;


-- select

SELECT * FROM posts
-- WHERE condition_here

