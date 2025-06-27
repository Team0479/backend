-- EC2 서버에서 디버깅용 쿼리들
-- MySQL에 접속해서 실행

USE myplay;

-- 1. 데이터베이스와 테이블 확인
SHOW TABLES;

-- 2. 사용자 테이블 확인
SELECT * FROM user LIMIT 5;

-- 3. 캘린더 엔트리 테이블 확인
SELECT * FROM calendar_entry LIMIT 5;

-- 4. user_id = 6인 사용자 확인
SELECT * FROM user WHERE id = 6;

-- 5. user_id = 6의 캘린더 일정 확인
SELECT * FROM calendar_entry WHERE user_id = 6;

-- 6. 전체 캘린더 일정 개수 확인
SELECT COUNT(*) as total_entries FROM calendar_entry;

-- 7. 사용자별 캘린더 일정 개수
SELECT user_id, COUNT(*) as entry_count 
FROM calendar_entry 
GROUP BY user_id;

-- 8. 공연 데이터 확인
SELECT * FROM performance LIMIT 5;

-- User 테이블에 profile_completed 컬럼 추가
ALTER TABLE user ADD COLUMN profile_completed BOOLEAN DEFAULT false NOT NULL;

-- 기존 사용자 중 닉네임과 프로필 이미지가 모두 설정된 사용자는 true로 업데이트
UPDATE user 
SET profile_completed = true 
WHERE nickname IS NOT NULL 
  AND nickname != '' 
  AND profile_image IS NOT NULL 
  AND profile_image != '';

-- 확인 쿼리
SELECT id, email, nickname, profile_image, profile_completed 
FROM user 
ORDER BY id; 