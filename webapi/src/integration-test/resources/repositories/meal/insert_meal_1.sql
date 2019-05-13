INSERT INTO users (id, email, encrypted_password, role, deleted, full_name, daily_calorie_limit)
                  VALUES (1, 'user1@gmail.com', 'encrypted_password', 0, 0, 'Test Abc', 0);
INSERT INTO users VALUES (2, 'manager2@gmail.com', 'encrypted_password', 1, 0, 'Test Abc', 0);


INSERT INTO meals (id, name, calories, consumed_date, consumed_time, consumer_id, deleted)
                  VALUES (1, 'deleted user pie', 500, '2019-04-03', '12:30:00', 1, 0);
INSERT INTO meals VALUES (2, 'still alive user pie', 500, '2019-04-03', '12:30:00', 1, 0);
INSERT INTO meals VALUES (3, 'deleted manager pie', 500, '2019-04-03', '12:30:00', 2, 0);
