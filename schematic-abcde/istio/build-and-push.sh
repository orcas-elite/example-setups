#!/usr/bin/env bash
# Example script automating the build process of the generated services and push generated docker images to docker hub

function builda () {
	cd gen-a
	mvn clean package
	docker build -t orcaselite/schematic-abcde:gen-a .
	docker push orcaselite/schematic-abcde:gen-a
	mvn clean
}

function buildb () {
	cd gen-b
	mvn clean package
	docker build -t orcaselite/schematic-abcde:gen-b .
	docker push orcaselite/schematic-abcde:gen-b
	mvn clean
}

function buildc () {
	cd gen-c
	mvn clean package
	docker build -t orcaselite/schematic-abcde:gen-c .
	docker push orcaselite/schematic-abcde:gen-c
	mvn clean
}

function buildd () {
	cd gen-d
	mvn clean package
	docker build -t orcaselite/schematic-abcde:gen-d .
	docker push orcaselite/schematic-abcde:gen-d
	mvn clean
}

function builde () {
	cd gen-e
	mvn clean package
	docker build -t orcaselite/schematic-abcde:gen-e .
	docker push orcaselite/schematic-abcde:gen-e
	mvn clean
}

builda &
buildb &
buildc &
buildd &
builde &
wait
