-- 테스트용 데이터 삽입 스크립트
USE myplay;

-- 테스트 사용자 추가 (ID 6 보장)
INSERT INTO user (id, email, nickname, profile_image, role) VALUES
(6, 'test6@example.com', '테스트유저6', 'https://example.com/profile6.jpg', 'USER')
ON DUPLICATE KEY UPDATE 
    email = VALUES(email),
    nickname = VALUES(nickname);

-- 다른 테스트 사용자들도 추가
INSERT INTO user (email, nickname, profile_image, role) VALUES
('test1@example.com', '테스트유저1', 'https://example.com/profile1.jpg', 'USER'),
('test2@example.com', '테스트유저2', 'https://example.com/profile2.jpg', 'USER'),
('test3@example.com', '테스트유저3', 'https://example.com/profile3.jpg', 'USER'),
('test4@example.com', '테스트유저4', 'https://example.com/profile4.jpg', 'USER'),
('test5@example.com', '테스트유저5', 'https://example.com/profile5.jpg', 'USER')
ON DUPLICATE KEY UPDATE 
    email = VALUES(email),
    nickname = VALUES(nickname);

-- 테스트용 캘린더 일정 추가 (user_id = 6)
INSERT INTO calendar_entry (user_id, performance_id, scheduled_date, memo) VALUES
(6, 1, '2024-12-25', '크리스마스 특별 공연'),
(6, 2, '2024-12-31', '신년맞이 공연'),
(6, 3, '2025-01-15', '새해 첫 콘서트'),
(6, 4, '2025-02-14', '발렌타인 데이 특별공연'),
(6, 5, '2025-03-01', '3월 정기공연')
ON DUPLICATE KEY UPDATE 
    memo = VALUES(memo);

-- 다른 사용자들에게도 일정 추가
INSERT INTO calendar_entry (user_id, performance_id, scheduled_date, memo) VALUES
(1, 1, '2024-12-20', '첫 번째 사용자 일정'),
(2, 2, '2024-12-22', '두 번째 사용자 일정'),
(3, 3, '2024-12-24', '세 번째 사용자 일정'),
(4, 4, '2024-12-26', '네 번째 사용자 일정'),
(5, 5, '2024-12-28', '다섯 번째 사용자 일정')
ON DUPLICATE KEY UPDATE 
    memo = VALUES(memo);

-- 결과 확인
SELECT '테스트 데이터 삽입 완료!' as message;
SELECT * FROM user WHERE id = 6;
SELECT * FROM calendar_entry WHERE user_id = 6; 