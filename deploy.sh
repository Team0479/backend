#!/bin/bash

# MyPlay 서버 배포 스크립트

echo "🚀 MyPlay 서버 배포를 시작합니다..."

# 0. 환경변수 확인
echo "🔍 환경변수를 확인합니다..."
if [ -z "$DB_PASSWORD" ]; then
    echo "❌ DB_PASSWORD 환경변수가 설정되지 않았습니다!"
    echo "다음 명령어로 설정하세요: export DB_PASSWORD='your_password'"
    exit 1
fi

if [ -z "$KAKAO_CLIENT_ID" ]; then
    echo "❌ KAKAO_CLIENT_ID 환경변수가 설정되지 않았습니다!"
    echo "다음 명령어로 설정하세요: export KAKAO_CLIENT_ID='your_client_id'"
    exit 1
fi

echo "✅ 환경변수 확인 완료"

# 1. 기존 프로세스 종료
echo "📋 기존 프로세스를 확인하고 종료합니다..."
PID=$(pgrep -f "myplay.*jar")
if [ ! -z "$PID" ]; then
    echo "기존 MyPlay 프로세스 종료 중... (PID: $PID)"
    kill -9 $PID
    sleep 2
fi

# 2. MySQL 연결 테스트
echo "🔌 MySQL 연결을 테스트합니다..."
mysql -u root -p$DB_PASSWORD -e "USE myplay; SELECT 1;" 2>/dev/null
if [ $? -eq 0 ]; then
    echo "✅ MySQL 연결 성공"
else
    echo "❌ MySQL 연결 실패. 비밀번호를 확인해주세요."
    exit 1
fi

# 3. 애플리케이션 시작
echo "🔥 MyPlay 애플리케이션을 시작합니다..."
nohup java -jar /home/ec2-user/myplay/myplay-0.0.1-SNAPSHOT.jar \
    --spring.profiles.active=prod \
    --server.port=8080 \
    > /home/ec2-user/myplay/logs/app.log 2>&1 &

# 4. 프로세스 확인
sleep 5
if pgrep -f "myplay.*jar" > /dev/null; then
    echo "✅ MyPlay 서버가 성공적으로 시작되었습니다!"
    echo "🌐 서버 주소: http://$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4):8080"
    echo "📋 로그 확인: tail -f /home/ec2-user/myplay/logs/app.log"
else
    echo "❌ 서버 시작에 실패했습니다. 로그를 확인해주세요."
    tail -20 /home/ec2-user/myplay/logs/app.log
fi 