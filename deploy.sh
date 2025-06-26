#!/bin/bash

# MyPlay 서버 배포 스크립트

echo "🚀 MyPlay 서버 배포를 시작합니다..."

# 1. 기존 프로세스 종료
echo "📋 기존 프로세스를 확인하고 종료합니다..."
PID=$(pgrep -f "myplay.*jar")
if [ ! -z "$PID" ]; then
    echo "기존 MyPlay 프로세스 종료 중... (PID: $PID)"
    kill -9 $PID
    sleep 2
fi

# 2. 애플리케이션 시작
echo "🔥 MyPlay 애플리케이션을 시작합니다..."
nohup java -jar /home/ec2-user/myplay/myplay-0.0.1-SNAPSHOT.jar \
    --spring.profiles.active=prod \
    --server.port=8080 \
    > /home/ec2-user/myplay/logs/app.log 2>&1 &

# 3. 프로세스 확인
sleep 5
if pgrep -f "myplay.*jar" > /dev/null; then
    echo "✅ MyPlay 서버가 성공적으로 시작되었습니다!"
    echo "🌐 서버 주소: http://$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4):8080"
else
    echo "❌ 서버 시작에 실패했습니다. 로그를 확인해주세요."
    tail -20 /home/ec2-user/myplay/logs/app.log
fi 