#!/bin/bash

APP_NAME=comprag-app
REPOSITORY=/home/ubuntu/app  # 본인 계정명에 맞게 수정 (ubuntu 또는 ec2-user)

echo "> 현재 구동 중인 애플리케이션 PID 확인"
CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 새 애플리케이션 배포"
# GitHub Secrets에서 등록한 DB 비번 등을 여기서 환경변수로 사용 가능
nohup java -jar $REPOSITORY/$APP_NAME.jar \
    --spring.profiles.active=prod \
    --spring.datasource.password=${DB_PASSWORD} \
    > $REPOSITORY/nohup.out 2>&1 &

echo "> 배포 완료"