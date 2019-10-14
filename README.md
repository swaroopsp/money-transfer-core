# Spring Boot bank transfer Project
REST APIs implemented using Spring Boot

## How to Run

* Build the project by running `mvn clean package` inside module
* Once successfully built, run the service by using the following command:
```
java -jar  money-transfer-core/money-transfer-core-0.0.1-SNAPSHOT.jar
```

## REST APIs Endpoints
### Create a bank account
```
POST /account
Accept: application/json
Content-Type: application/json

{
"name" : "1111",
"balance" : 100
}

```

### Retrieve a bank account
```
Get /account/{accountNumber}
Accept: application/json
Content-Type: application/json

```

### Transfer between the accounts
```
PUT /account/transfer
Accept: application/json
Content-Type: application/json

{
"from" : "1111",
"to" : "2222",
"amount": 10
}
```

### Get information about system health, configurations, etc.
```
Not implemented yet.

```
### To view Swagger 2 API docs
```
Not implemented yet.
```
