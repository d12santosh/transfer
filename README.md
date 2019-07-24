# transfer
###Technical Challenge: Santosh Dhulipala
### Application stack
- Java 8
- Java-express
- Gradle 5.5.1

### Project Implementation Outline
- Rest service exposed for input
- used Project Lombok for auto generating code (example :- setters and getters), please follow steps in configuring Lombok
- Test cases and code coverage

### From the Project home
 if Gradle is installed
 ```
 gradle clean build
 ```
 else can use portable gradle wrapper to build, below is the command
 ```
 gradlew clean build
 ```


### To run application from the <PROJECT_HOME> folder
 ```
 java -jar build/libs transfer-0.0.1-SNAPSHOT.jar
 ```
 can also use below gradle commands
```
```



### Technical Debt
- [ ] Exception Handler - currently showing default error messages for input validation
- [ ] Input validation Error and exception messages configuration into properties file
- [ ] decide between framework less, java express or jersey and jersey grizzly2 server and java express
- [ ] soft delete of Accounts

### Useful Links

### For Test Case and code coverage Report
     <PROJECT_HOME>build\reports\jacoco\test\html\index.html (Coverage)
     <PROJECT_HOME>\build\reports\tests\test\index.html (Test)



###Configuring Lombok
   - Visit https://projectlombok.org/setup/intellij for intellij IDE
   - Visit https://projectlombok.org/setup/eclipse for eclipse IDE
