DELETE FROM user_roles;
DELETE FROM meal;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meal (date_time, description, calories, user_id)
VALUES ('2020-10-18 08:15:00.000000', 'Обед (User)', 860, 100000),
('2020-10-19 08:14:03.000000', 'Завтрак (User)', 250, 100000),
('2020-10-19 13:00:03.000000', 'Обед (User)', 700, 100000),
('2020-10-19 19:00:03.000000', 'Ужин (User)', 900, 100000),
('2020-10-19 09:00:00.000000', 'Завтрак (Admin)', 750, 100001),
('2020-10-19 20:00:00.000000', 'Ужин (Admin)', 250, 100001);
