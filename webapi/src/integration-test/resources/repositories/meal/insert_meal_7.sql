INSERT INTO users (id, email, encrypted_password, role, deleted, full_name, daily_calorie_limit)
                  VALUES (1, 'filter_my_meal_toTime_1@gmail.com', 'encrypted_password', 0, 0, 'Test Abc', 0);
INSERT INTO users VALUES (2, 'filter_my_meal_toTime_2@gmail.com', 'encrypted_password', 1, 0, 'Test Abc', 0);

INSERT INTO meals (id, name, calories, consumed_date, consumed_time, consumer_id, deleted)
                  VALUES (1, 'eat on toTime', 500, '2017-09-20', '15:30:00', 1, 0);
INSERT INTO meals VALUES (2, 'eat before toTime', 500, '2014-09-19', '15:20:00', 1, 0);
INSERT INTO meals VALUES (3, 'I cannot be found as I deleted', 500, '2015-09-18', '12:00:00', 1, 1);
INSERT INTO meals VALUES (4, 'hello from different user', 500, '2000-09-19', '10:20:00', 2, 0);
