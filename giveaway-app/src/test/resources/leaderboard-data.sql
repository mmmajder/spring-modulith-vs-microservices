DELETE FROM SCORING.scoring_record;
DELETE FROM SCORING.monthly_leaderboard_winners;
DELETE FROM SCORING.monthly_leaderboard;

INSERT INTO SCORING.scoring_record (id, user_email, points, description, timestamp) VALUES
(1, 'user1@example.com', 150, 'Monthly workout points', '2024-12-31 23:59:59'),
(2, 'user2@example.com', 120, 'Referral bonus', '2024-12-25 18:30:00'),
(3, 'user1@example.com', 200, 'Competition winner', '2024-12-15 15:00:00'),
(4, 'user3@example.com', 100, 'Weekly workout points', '2024-12-07 10:00:00'),
(5, 'user2@example.com', 90, 'Bonus activity points', '2024-11-30 14:00:00');

INSERT INTO SCORING.monthly_leaderboard (id, leaderboard_month) VALUES
(1, '2024-12'),
(2, '2024-11');

INSERT INTO SCORING.monthly_leaderboard_winners (monthly_leaderboard_id, email, points) VALUES
(1, 'user1@example.com', 350),
(1, 'user2@example.com', 120),
(1, 'user3@example.com', 100),
(2, 'user2@example.com', 90);