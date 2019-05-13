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


# Sample Data:

- In order to have good testing experience, please import sample data of 9000 meals distributed across 300 users. It can
be downloaded at:

`https://www.dropbox.com/s/bdyulwio8gymie0/sample_data.sql?dl=0`

- Pass of all users are `test1234`
 
