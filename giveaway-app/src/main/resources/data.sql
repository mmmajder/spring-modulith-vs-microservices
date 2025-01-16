DELETE FROM workout.gyms;
DELETE FROM workout.training;

-- Insert Gym Data
INSERT INTO workout.gyms (name, address_street, address_city, address_country, address_number, address_latitude, address_longitude, number_of_visits, version)
VALUES
('Power House Gym', 'Main St', 'New York', 'USA', 101, 40.712776, -74.005974, 0, 1),
('Fitness World', 'Second Ave', 'Los Angeles', 'USA', 202, 34.052235, -118.243683, 0, 1),
('Strength Zone', 'Third Blvd', 'Chicago', 'USA', 303, 41.878113, -87.629799, 0, 1);

ALTER TABLE workout.gyms ALTER COLUMN id RESTART WITH 4;

INSERT INTO workout.training (id, check_in_time, check_out_time, gym_id, user_email, version) VALUES
(1,	'2024-12-10 21:03:05.545564', NULL, 1, 	'john.wick@continental.com', 1);
ALTER TABLE workout.training ALTER COLUMN id RESTART WITH 2;

--INSERT INTO USERS (ID, EMAIL, NAME) VALUES
--(1, 'john.wick@continental.com', 'Milan Ajder');
--
--INSERT INTO DEVICES (ID, DEVICE_ID, USER_EMAIL) VALUES
--(1,	'fgpb6GUiR3yX8tQYhim8VI:APA91bF0d8bAf3ck06UVgPm2Pk7C1B2NldXNfOd02--hGphlOUHwfk8sFgUYzxnC9X0hBtL7-fK4zim9ONAU9Az8nJ7IX3hWntLU6Z0VtnbhhWIJOpRMFjY', 'john.wick@continental.com');
--ALTER TABLE DEVICES ALTER COLUMN id RESTART WITH 2;