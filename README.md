# Account transfer

### Application stack
- Java 8
- Jersey
- Grizzly 2
- Gradle 5.5.1

### Project Implementation Outline
- Rest service exposed for Creating Account, Transfer money, Withdraw
- Glass fish Hk2 for dependency Injection 
- used Project Lombok for auto generating code (example :- setters and getters), please follow steps in configuring Lombok
- Test cases and code coverage
- Followed Domain Driven Design

### From the Project home
 if Gradle is installed
 ```
 gradle clean build
 ```
 else can use portable gradle wrapper to build, below is the command
 ```
 gradlew clean build
 ```


### To run application
 ```
 execute com.assignment.backend.BankApplication
 ```
 ## List of services 
 #### creation of account (please execute this service to create accounts)
```
http://localhost:8080/Bank/account/create (POST)
sample request
{
    "accountNumber": "123",
    "amount": "10000",
    "firstName":"Santosh",
    "lastName":"Kumar"
}
```

#### Withdraw amount from account (POST)
```
http://localhost:8080/Bank/account/withdraw
sample request
{
  "accountNumber": "456",
  "amount": "1000"
}
```
#### Deposit amount from account (POST)
```
http://localhost:8080/Bank/account/deposit
sample request
{
  "accountNumber": "456",
  "amount": "1000"
}
```

#### Get List of Accounts (GET)
```
http://localhost:8080/Bank/account/list
sample request
{
  "accountNumber": "456",
  "amount": "1000"
}
```
#### Transfer amount (POST)
```
http://localhost:8080/Bank/account/transfer/
sample request
{
    "fromAccount": "123",
    "toAccount": "456",
    "amount": 1000
}
```

### Technical Debt
- [X] Exception Handler - currently showing default error messages for input validation
- [ ] Input validation Error, logs, properties and exception messages into properties file
- [X] decide between framework less, java express or jersey and jersey grizzly2 server and java express
- [ ] change data store from in memory concurrency hash map to in memory data base with lightweight ORM framework
- [ ] soft delete of Accounts
- [ ] fat jar creation for execution
- [ ] refactoring of code and packages to follow domain driven design
- [ ] test cases for mutual exclusion
- [ ] improve code coverage
- [ ] gradle tasks for Integration test cases and unit test case 

### Known Issues

- Gradle task jar is not creating executable jar. Should research
- 

### Useful Links

### For Test Case and code coverage Report
     <PROJECT_HOME>build\reports\jacoco\test\html\index.html (Coverage)
     <PROJECT_HOME>\build\reports\tests\test\index.html (Test)



###Configuring Lombok
   - Visit https://projectlombok.org/setup/intellij for intellij IDE
   - Visit https://projectlombok.org/setup/eclipse for eclipse IDE
