INSERT INTO users (id, email, encrypted_password, role, deleted, full_name, daily_calorie_limit)
      VALUES (6, 'lookup_user@abc.com', 'encrypted_password', 0, 0, 'Test Abc', 0);
INSERT INTO users VALUES (7, 'lookup_manager@gmail.com', 'encrypted_password', 1, 0, 'Test Abc', 0);
INSERT INTO users VALUES (8, 'lookup_admin@gmail.com', 'encrypted_password', 2, 0, 'Test Abc', 0);
INSERT INTO users VALUES (9, 'lookup_deleted_user@gmail.com', 'encrypted_password', 0, 1, 'Test Abc', 0);
INSERT INTO users VALUES (10, 'lookup_deleted_manager@gmail.com', 'encrypted_password', 1, 1, 'Test Abc', 0);
