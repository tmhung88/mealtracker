INSERT INTO users (id, email, encrypted_password, role, deleted, full_name, daily_calorie_limit)
                  VALUES (1, 'user1@gmail.com', 'encrypted_password', 0, 0, 'Test Abc', 0);
INSERT INTO users VALUES (2, 'manager2@gmail.com', 'encrypted_password', 1, 0, 'Test Abc', 0);


INSERT INTO meals (id, name, calories, consumed_date, consumed_time, consumer_id, deleted)
                  VALUES (1, 'user cake', 500, '2018-11-05', '12:30:00', 1, 0);
INSERT INTO meals VALUES (2, 'deleted user cake', 500, '2018-11-05', '03:30:00', 1, 1);
INSERT INTO meals VALUES (3, 'another day user milk', 500, '2019-04-05', '12:30:00', 1, 0);
INSERT INTO meals VALUES (4, 'manager bread', 500, '2018-11-05', '12:30:00', 2, 0);
