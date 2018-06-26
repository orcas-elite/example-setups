#!/usr/bin/env bash
# Example script automating the build process of the generated services and push generated docker images to docker hub

CUR_DIR=`pwd`

cd gen-a
mvn clean package
docker build -t orcaselite/schematic-abcde:gen-a .
docker push orcaselite/schematic-abcde:gen-a
mvn clean
cd $CUR_DIR

cd gen-b
mvn clean package
docker build -t orcaselite/schematic-abcde:gen-b .
docker push orcaselite/schematic-abcde:gen-b
mvn clean
cd $CUR_DIR

cd gen-c
mvn clean package
docker build -t orcaselite/schematic-abcde:gen-c .
docker push orcaselite/schematic-abcde:gen-c
mvn clean
cd $CUR_DIR

cd gen-d
mvn clean package
docker build -t orcaselite/schematic-abcde:gen-d .
docker push orcaselite/schematic-abcde:gen-d
mvn clean
cd $CUR_DIR

cd gen-e
mvn clean package
docker build -t orcaselite/schematic-abcde:gen-e .
docker push orcaselite/schematic-abcde:gen-e
mvn clean
cd $CUR_DIR
