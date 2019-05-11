#!/bin/bash
cd local-env
docker-compose up -d

cd ..
./mvnw clean compile spring-boot:run -Dspring-boot.run.profiles=local
