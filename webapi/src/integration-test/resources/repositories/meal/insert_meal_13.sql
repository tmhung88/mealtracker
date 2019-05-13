INSERT INTO users (id, email, encrypted_password, role, deleted, full_name, daily_calorie_limit)
                  VALUES (1, 'user_findExistingMeal@gmail.com', 'encrypted_password', 1, 0, 'Owner Details', 200);
INSERT INTO users VALUES (2, 'ok_findExistingMeal@gmail.com', 'encrypted_password', 1, 0, 'Owner Details', 200);

INSERT INTO meals (id, name, calories, consumed_date, consumed_time, consumer_id, deleted)
                  VALUES (1, 'my consumer are not deleted yet', 500, '2017-09-20', '15:30:00', 1, 0);
