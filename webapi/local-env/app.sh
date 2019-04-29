#!/bin/bash
cd local-env
docker-compose up -d

cd ..
./mvnw clean compile flyway:clean flyway:migrate spring-boot:run -Dspring-boot.run.profiles=local
