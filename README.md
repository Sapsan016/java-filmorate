Репозиторий проекта Filmorate


<picture>
    <source media="(prefers-color-scheme: dark)" srcset="src/main/resources/FilmorateDB_diagramm.png">
    <img src="src/main/resources/FilmorateDB_diagramm.png">
</picture>

Примеры основных запросов

INSERT INTO users (user_name, email, birthday, login)
VALUES ('Inan Inanov', 'ivanov1@gmail.com', 'iivan);

SELECT user_name FROM users WHERE id = 1;

SELECT friend_id FROM user_friends WHERE user_id = 1 AND status = 'true';

INSERT INTO user_friends (user_id, friend_id, status)
VALUES (1, 2, true);

UPDATE user_friends SET status = 'false' WHERE friend_id = 2;

DELETE FROM users WHERE id = 1;

INSERT INTO films (film_name, description, release_date, duration, rating)
VALUES ('Film1', 'New film', '2000-01-01', 200, 'G');

SELECT film_name FROM users WHERE id = 1;

INSERT INTO film_likes (film_id, user_id)
VALUES (1, 2);

SELECT COUNT(user_id) FROM film_likes WHERE film_id = 1;

DELETE FROM films WHERE id = 1;
