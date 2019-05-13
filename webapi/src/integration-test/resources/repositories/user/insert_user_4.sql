INSERT INTO users (id, email, encrypted_password, role, deleted, full_name, daily_calorie_limit)
                  VALUES (4, 'user@abc.com', 'encrypted_password', 0, 0, 'Test Abc', 0);
INSERT INTO users VALUES (5, 'manager@abc.com', 'encrypted_password', 1, 0, 'Test Abc', 0);
INSERT INTO users VALUES (6, 'admin@abc.com', 'encrypted_password', 2, 0, 'Test Abc', 0);
INSERT INTO users VALUES (7, 'deleted_user@abc.com', 'encrypted_password', 0, 1, 'Test Abc', 0);
INSERT INTO users VALUES (8, 'deleted_manager@abc.com', 'encrypted_password', 1, 1, 'Test Abc', 0);
INSERT INTO users VALUES (9, 'deleted_admin@abc.com', 'encrypted_password', 2, 1, 'Test Abc', 0);
