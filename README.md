# Loan Applications App

This app allows users to sign up as customers, after which they can log in with their provided credentials and either 
create a new loan application or view their existing ones.
It also allows an adviser type user to log in and view all loan applications that have been created by customers.

## Backend
Backend is written in Java 17 using the Spring Boot framework and Gradle for build management.
I've used JUnit 5 and AssertJ for the very high level / basic test coverage.
Currently configured to use on-disk H2 for persistence but this can easily be swapped out for a different persistence layer.

To build and test:
* Run the Gradle "build" task - either from your IDE (after loading the build.gradle file) or using "./gradle build" on cmd

To run:
* From your IDE, run the main class: src/main/java/kam/dnb/loanapp/LoanApp.java
* OR to run from the command line, run the following command (Java 17 minimum): 
    
`java -jar build/libs/loanapp-0.0.1-SNAPSHOT.jar`

## Frontend
The frontend is written using React and the Bootstrap framework and builds with NPM (9.5.0).

To build and run:
* Ensure Node / NPM is installed
* From the project directory (same level as the package.json), run the following commands:

```
npm ci
npm start
```

This will start the app on port 3000.

## Docker and screenshots

Both backend and frontend have associated (tested) Docker build files.

Some screenshot to show the app screens (functional but not pretty):

![Landing page](screens/1_Landing.png?raw=true "Landing page")
![Sign up](screens/2_Signup.png?raw=true "Sign up")
![Login](screens/3_Login.png?raw=true "Login")
![Logged in customer view](screens/4_LoggedInCustomer.png?raw=true "Logged in customer view")
![Loan application](screens/5_LoanApplication.png?raw=true "Loan application")
![Customer loans view](screens/6_CustomerLoansView.png?raw=true "Customer loans view")
![Adviser loans view](screens/7_AdviserLoansView.png?raw=true "Adviser loans view")
![Adviser customer view](screens/8_AdviserCustomerView.png?raw=true "Adviser customer view")

