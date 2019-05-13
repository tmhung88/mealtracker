INSERT INTO users (id, email, encrypted_password, role, deleted, full_name, daily_calorie_limit)
                  VALUES (1, 'listExistingMeals1@gmail.com', 'encrypted_password', 1, 1, 'Owner Details', 0);

INSERT INTO meals (id, name, calories, consumed_date, consumed_time, consumer_id, deleted)
                  VALUES (1, 'im active', 500, '2017-09-20', '15:30:00', 1, 0);
INSERT INTO meals VALUES (2, 'im deleted', 500, '2014-09-19', '00:20:00', 1, 0);
