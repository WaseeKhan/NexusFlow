# NexusFlow - CRM

A CRM web application built with Java Spring Boot and MySQL.

## Technology Used

- Java (Spring Boot)
- MySQL
- Thymeleaf
- Spring Security (with OAuth2)
- Maven

## Tools Used

- Postman
- Maven
- MySQL Workbench 

## Prerequisites

- Java 17 or above
- Maven 3.x
- MySQL 9.x

## Setup Instructions

### 1. Clone the Repository

```sh
git clone https://github.com/waseeKhan/nexusflow.git
cd nexusflow
```

### 2. Configure Database

- Create a MySQL database named `nexusflow`.
- Update `src/main/resources/application.properties` with your MySQL username and password if different from default.

### 3. Configure OAuth2 Secrets

- Copy your OAuth2 credentials into `src/main/resources/secrets.properties`:

  ```
  google.oauth2.client-id=YOUR_GOOGLE_CLIENT_ID
  google.oauth2.client-secret=YOUR_GOOGLE_CLIENT_SECRET

  github.oauth2.client-id=YOUR_GITHUB_CLIENT_ID
  github.oauth2.client-secret=YOUR_GITHUB_CLIENT_SECRET
  ```

### 4. Build the Project

```sh
mvn clean install
```

### 5. Run the Application

```sh
mvn spring-boot:run
```

The application will start on [http://localhost:8080/home](http://localhost:8080/home).

### 6. Access the Application

- Visit [http://localhost:8080/home](http://localhost:8080/home) in your browser.
- Register a new user or use OAuth2 login (Google/GitHub).

## Common Commands

- **Run tests:**  
  ```sh
  mvn test
  ```
- **Package as JAR:**  
  ```sh
  mvn package
  ```

## Notes

- Make sure MySQL is running before starting the application.
- The database schema will be auto-created/updated on startup.
- For OAuth2 login, set up your credentials in the Google/GitHub developer console.