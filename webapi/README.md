# Requirements:
- JDK 11
- Maven 3
- Docker CE 19 
    
Docker is for running integration tests against MySQL and setup an MySQL instance to run the app.

Docker settings of the Mysql container can be found at ./webapi/local-env
    
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


# Sample Data:

- Pass of all users are `test1234`
 
