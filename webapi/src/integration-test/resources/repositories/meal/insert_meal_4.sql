INSERT INTO users (id, email, encrypted_password, role, deleted, full_name, daily_calorie_limit)
                  VALUES (1, 'filter_my_meal1@gmail.com', 'encrypted_password', 0, 0, 'Test Abc', 0);
INSERT INTO users VALUES (2, 'filter_my_meal2@gmail.com', 'encrypted_password', 1, 0, 'Test Abc', 0);

INSERT INTO meals (id, name, calories, consumed_date, consumed_time, consumer_id, deleted)
                  VALUES (1, 'eat on fromDate', 500, '2017-02-10', '12:30:00', 1, 0);
INSERT INTO meals VALUES (2, 'eat after fromDate', 500, '2017-02-11', '03:30:00', 1, 0);
INSERT INTO meals VALUES (3, 'I cannot be found as I deleted', 500, '2017-02-12', '12:30:00', 1, 1);
INSERT INTO meals VALUES (4, 'hello from different user', 500, '2017-02-11', '12:30:00', 2, 0);
