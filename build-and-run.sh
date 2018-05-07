#!/usr/bin/env bash
# Example script automating the build process of the generated services

CUR_DIR=`pwd`

cd gen-a
mvn clean package
cd $CUR_DIR

cd gen-b
mvn clean package
cd $CUR_DIR

cd gen-c
mvn clean package
cd $CUR_DIR

cd gen-d
mvn clean package
cd $CUR_DIR

cd gen-e
mvn clean package
cd $CUR_DIR

docker-compose up --build
docker-compose down

cd gen-a
mvn clean
cd $CUR_DIR

cd gen-b
mvn clean
cd $CUR_DIR

cd gen-c
mvn clean
cd $CUR_DIR

cd gen-d
mvn clean
cd $CUR_DIR

cd gen-e
mvn clean
cd $CUR_DIR
