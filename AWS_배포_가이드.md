# 🚀 AWS EC2로 MyPlay 서버 배포하기

## 📋 준비사항

### 1. AWS 계정 및 프리티어 확인
- AWS 계정 생성 (신용카드 필요, 하지만 프리티어는 무료)
- 프리티어 한도: EC2 t2.micro 월 750시간 (1년간)

### 2. 필요한 도구
- AWS CLI (선택사항)
- SSH 클라이언트 (Windows: PuTTY 또는 WSL)

## 🛠️ 1단계: EC2 인스턴스 생성

### 1.1 EC2 대시보드 접속
1. AWS 콘솔 로그인 → EC2 서비스 선택
2. "인스턴스 시작" 버튼 클릭

### 1.2 인스턴스 설정
```
이름: myplay-server
AMI: Amazon Linux 2 (프리티어 사용 가능)
인스턴스 유형: t2.micro (프리티어)
키 페어: 새로 생성 (myplay-keypair.pem 다운로드)
보안 그룹: 새로 생성
```

### 1.3 보안 그룹 설정 (중요!)
```
인바운드 규칙:
- SSH (22): 내 IP
- HTTP (80): 0.0.0.0/0
- 커스텀 TCP (8080): 0.0.0.0/0  # Spring Boot 앱용
- MySQL (3306): 인스턴스 내부만 (선택사항)
```

## 🔧 2단계: 서버 환경 설정

### 2.1 SSH 연결
```bash
# Windows (PowerShell)
ssh -i "myplay-keypair.pem" ec2-user@[퍼블릭-IP]

# chmod 400 myplay-keypair.pem (권한 오류 시)
```

### 2.2 Java 17 설치
```bash
# Amazon Linux 2
sudo yum update -y
sudo yum install -y java-17-amazon-corretto-devel

# Java 버전 확인
java -version
```

### 2.3 MySQL 설치 및 설정
```bash
# MySQL 8.0 설치
sudo yum install -y mysql-server
sudo systemctl start mysqld
sudo systemctl enable mysqld

# MySQL 보안 설정
sudo mysql_secure_installation

# 데이터베이스 생성
mysql -u root -p
CREATE DATABASE myplay;
exit
```

### 2.4 애플리케이션 디렉토리 생성
```bash
sudo mkdir -p /home/ec2-user/myplay/logs
sudo chown ec2-user:ec2-user /home/ec2-user/myplay -R
```

## 📦 3단계: 애플리케이션 배포

### 3.1 JAR 파일 업로드
```bash
# 로컬에서 (Windows PowerShell)
scp -i "myplay-keypair.pem" .\build\libs\myplay-0.0.1-SNAPSHOT.jar ec2-user@[퍼블릭-IP]:/home/ec2-user/myplay/
```

### 3.2 배포 스크립트 업로드
```bash
# 로컬에서
scp -i "myplay-keypair.pem" .\deploy.sh ec2-user@[퍼블릭-IP]:/home/ec2-user/myplay/
```

### 3.3 환경변수 설정
```bash
# EC2에서
nano ~/.bashrc

# 다음 내용 추가:
export DB_PASSWORD="your_mysql_password"
export KAKAO_CLIENT_ID="3f53ffa96a908a795ee96ed27c164a14"

# 적용
source ~/.bashrc
```

### 3.4 배포 실행
```bash
cd /home/ec2-user/myplay
chmod +x deploy.sh
./deploy.sh
```

## 🌐 4단계: 도메인 및 HTTPS (선택사항)

### 4.1 Elastic IP 할당
1. EC2 → Elastic IP 주소 → 주소 할당
2. 인스턴스에 연결

### 4.2 도메인 연결 (Route 53 또는 외부 DNS)
```
A 레코드: api.myplay.com → Elastic IP
```

### 4.3 SSL 인증서 (Let's Encrypt)
```bash
# Certbot 설치
sudo yum install -y certbot

# SSL 인증서 발급
sudo certbot certonly --standalone -d api.myplay.com
```

## 🔍 5단계: 모니터링 및 관리

### 5.1 로그 확인
```bash
# 애플리케이션 로그
tail -f /home/ec2-user/myplay/logs/app.log

# 스프링 부트 로그
tail -f /home/ec2-user/myplay/logs/myplay.log
```

### 5.2 프로세스 관리
```bash
# 프로세스 확인
ps aux | grep java

# 서버 재시작
./deploy.sh

# 서버 중지
pkill -f "myplay.*jar"
```

### 5.3 자동 시작 설정
```bash
# crontab 등록
crontab -e

# 다음 줄 추가 (재부팅 시 자동 시작)
@reboot /home/ec2-user/myplay/deploy.sh
```

## 💰 6단계: 비용 관리

### 6.1 프리티어 모니터링
- AWS Billing 대시보드에서 사용량 확인
- 프리티어 한도 초과 시 알림 설정

### 6.2 리소스 최적화
```bash
# 메모리 사용량 확인
free -h

# 디스크 사용량 확인
df -h

# 자바 힙 메모리 제한 (필요시)
java -Xmx512m -jar myplay-0.0.1-SNAPSHOT.jar
```

## 🚀 배포 완료!

배포가 완료되면 다음 URL로 접속 가능:
- `http://[퍼블릭-IP]:8080/api/home/today-plays`
- Swagger: `http://[퍼블릭-IP]:8080/swagger-ui.html` (추가 시)

## 🔧 트러블슈팅

### 자주 발생하는 문제
1. **연결 거부**: 보안 그룹에서 8080 포트 열렸는지 확인
2. **메모리 부족**: t2.micro는 1GB RAM으로 제한적
3. **DB 연결 실패**: MySQL 서비스 상태 및 비밀번호 확인
4. **파일 권한**: chmod로 실행 권한 부여

### 로그 기반 디버깅
```bash
# 자세한 로그 확인
tail -100 /home/ec2-user/myplay/logs/app.log

# 시스템 로그
sudo tail -f /var/log/messages
```

---

이 가이드를 따라하면 AWS 프리티어로 MyPlay 서버를 성공적으로 배포할 수 있습니다! 🎉 