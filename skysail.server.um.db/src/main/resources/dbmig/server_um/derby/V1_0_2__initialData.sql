
INSERT INTO um_users (id, username, password) VALUES (1, 'Izzy', 'izzy');

INSERT INTO um_users (id, username, password) VALUES (2, 'Linus', 'linus');

INSERT INTO um_roles (id, name) VALUES (1, 'admin');

INSERT INTO um_users_um_roles (SkysailUser_ID, roles_ID) VALUES (1, 1);

INSERT INTO um_users_um_roles (SkysailUser_ID, roles_ID) VALUES (2, 1);


