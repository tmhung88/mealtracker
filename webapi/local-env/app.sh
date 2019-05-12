#!/bin/bash

#
echo ">>>>>>>>>>>>>>>>>>> Pull MySQL docker image if it's not available locally, run unit tests & integration tests"
./mvnw clean verify -P ci-server

echo ">>>>>>>>>>>>>>>>>>> Starting up docker containers: MySQL"
cd local-env
docker-compose up -d

echo ">>>>>>>>>>>>>>>>>>> Starting the application Meal Tracker"
cd ..
./mvnw clean compile -DskipTests spring-boot:run


