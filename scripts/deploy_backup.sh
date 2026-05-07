#!/bin/bash

# 1. 변수 설정
REPOSITORY=/home/ubuntu/app
PROJECT_NAME=comprag-app  # 위에서 설정한 jar 파일명 (확장자 제외)

echo "> Build 파일 복사"
cp $REPOSITORY/build/libs/*.jar $REPOSITORY/

echo "> 현재 구동 중인 애플리케이션 PID 확인"
CURRENT_PID=$(pgrep -fl $PROJECT_NAME | grep jar | awk '{print $1}')

echo "> 현재 구동 중인 애플리케이션 PID: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"
echo "> $JAR_NAME 에 실행권한 추가"
chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"
# DB 비번 등은 시스템 환경변수나 GitHub Secrets에서 주입된 상태여야 함
nohup java -jar \
    -Dspring.profiles.active=prod \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &