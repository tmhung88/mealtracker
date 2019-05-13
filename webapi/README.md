# Requirements:
- JDK 11
- Maven 3
- Docker CE 19
---
# How to test
- To run unit tests only
```
./mvnw clean test
```
- To run integration tests only
```
./mvnw clean verify -P integration-test
```
- To run all tests:
```
./mvnw clean verify -P ci-server

```
---
# How to run
- From the project directory, execute that script
``` 
./local-env/app.sh 
```
    
- In case the script is not executable, please set this permission first:
```
chmod 0755 ./local-env/app.sh
```
