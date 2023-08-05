# Ideas Project

## Description

"Ideas Project" is a web application where users can create an account, suggest their own ideas, and publish them for others to explore. Users can like or dislike ideas, and the creators receive notifications for these interactions. Additionally, the platform has a special type of user called "Experts" who can evaluate the appropriateness of ideas. When an idea is evaluated, the creator is notified via email and within the application.

## Features

### Users

- **Account Management:** Users can easily register and log in to their accounts, where they have full control over their profile information, including their name, surname, and avatar.

- **Create and Manage Ideas:** Users can unleash their creativity by crafting new ideas with a wealth of details, such as a name, description, captivating images, and important attached PDF documents. They have the freedom to update and delete their own ideas.

- **Idea Metrics and Sorting:** Users can efficiently navigate through their creations and sort them based on various metrics to gain valuable insights.

- **Interact with Ideas:**  Engaging with other users' ideas is seamless; users can express their appreciation or feedback by liking or disliking them, resulting in thoughtful notifications for the creators.

- **View Liked/Disliked Ideas:** Users can effortlessly access a list of all the ideas they have liked and disliked and conveniently sort them based on specific metrics.

- **Idea History:** The application keeps a detailed record of users' idea history, which includes created, updated, and deleted ideas, enabling users to track their journey of innovation.

- **Notifications:** Users stay informed through timely notifications regarding significant events, such as likes, dislikes, or expert estimations.
### Experts

In addition to the features available to regular users, experts have the following capabilities:

- **Evaluate Ideas:** As experts, they play a pivotal role in the platform by evaluating other users' ideas for appropriateness, contributing to the overall quality of the content.

- **View Evaluation Metrics:** Accessing and organizing a list of ideas they have evaluated based on specific metrics empowers experts to make informed decisions.
## Technology Stack
The "Ideas Project" is built using the following technology stack:

- **Spring Boot:** The core framework for building the application.
    - [spring-boot-starter-data-jpa](https://spring.io/projects/spring-boot#learn)
    - [spring-boot-starter-mail](https://spring.io/projects/spring-boot#learn)
    - [spring-boot-starter-security](https://spring.io/projects/spring-boot#learn)
    - [spring-boot-starter-web](https://spring.io/projects/spring-boot#learn)
    - [spring-boot-starter-validation](https://spring.io/projects/spring-boot#learn)

- **QueryDSL:** A powerful library for type-safe querying and manipulation of JPA entities.
    - [querydsl-jpa](https://querydsl.com/)
    - [querydsl-apt](https://querydsl.com/)

- **Spring Data Envers:** Enables auditing of entities using Hibernate Envers for versioning and history tracking.
    - [spring-data-envers](https://spring.io/projects/spring-data#envers)

- **Database:**
    - [PostgreSQL](https://www.postgresql.org/): A robust and feature-rich relational database.
    - [Liquibase](https://www.liquibase.org/): A database schema version control and migration tool.
  
- **API Documentation:**
    - [Springdoc OpenAPI](https://springdoc.org/): Provides automated OpenAPI documentation and UI for the API.
        - [springdoc-openapi-starter-webmvc-ui](https://springdoc.org/#actuator-and-springdoc-openapi-ui): The OpenAPI UI starter for Spring WebMVC.

- **Amazon S3 SDK:** A software development kit for Amazon S3, enabling file storage capabilities (e.g., PDF documents).
    - [software.amazon.awssdk:s3-transfer-manager](https://aws.amazon.com/sdk-for-java/)

- **Auth0 Java JWT:** A library for parsing and validating JWT tokens.
    - [com.auth0:java-jwt](https://github.com/auth0/java-jwt)
- **Docker:** A platform for containerizing and deploying applications, providing consistency across development and production environments.

- **Testing Frameworks:**
    - [JUnit 5](https://junit.org/junit5/): A popular testing framework for Java.
    - [Mockito](https://site.mockito.org/): A mocking framework for unit testing.
    - [Testcontainers](https://www.testcontainers.org/): Enables easy integration testing using real database instances.

## Database Schema

Below is the database schema used in the "Ideas Project" application:

![img_6.png](images/img_6.png)

## Testing

Testing is a critical aspect of the "Ideas Project" to ensure the reliability and stability of the application. I have implemented various levels of tests to cover different parts of the codebase.

### Test Coverage

Test suite has achieved a test coverage of **`76%`**, as depicted in the following image:

![img_5.png](images/img_5.png)

### Test Levels

1. **Unit Tests:** I have added unit tests that focus on testing individual components and functions in isolation. These tests help verify the correctness of critical algorithms and functionalities.

2. **Integration Tests:** Integration tests validate the interaction between different components and modules to ensure they work harmoniously together.

## API Documentation
The API documentation is generated using Swagger and provides an interactive UI to explore the endpoints and their functionality. Below are some images of the API documentation:

![img_1.png](images/img_1.png)
![img.png](images/img.png)
![img_2.png](images/img_2.png)
![img_3.png](images/img_3.png)
![img_4.png](images/img_4.png)
To access the live API documentation, run the application and navigate to the following URL:
http://localhost:8080/swagger-ui/index.html

### Using Postman for API Testing

You can also use Postman, a popular API testing tool, to interact with the "Ideas Project" API. I have created a public Postman workspace that contains a collection of requests for testing various endpoints and scenarios.

To access the Postman workspace:

1. Open Postman and log in (or create an account if you don't have one).
2. Navigate to the following link: 
<div style="text-align:left">
<a href="https://www.postman.com/mission-specialist-29542612/workspace/ideas-project" target="_blank" style="background-color:#FF6C37; color:white; padding:10px 10px; border-radius:5px; text-decoration:none; font-weight:bold;">Open Postman Workspace</a>
</div>

3. In the workspace, you will find a collection with pre-configured requests. Click the "Run in Postman" button to import the collection into your Postman application.
4. Once imported, you can start testing the API endpoints using the provided requests. Make sure to adjust the request parameters as needed.

Using Postman can provide a convenient way to explore and test the API's functionality beyond what is offered by the Swagger UI.


## Installation

To get the "Ideas Project" up and running on your local machine, follow these steps:

### 1. Build the JAR File

First, you need to build the JAR file of the application using the provided Gradle task:

```bash
./gradlew jarBuild
```
### 2. Configure Environment Variables

Before running the application using Docker Compose, you need to specify some environment variables. These variables are stored in a .env file located in the root directory of the project.
Edit the `.env` file and provide appropriate values for the following properties:

#**The app will work if you don't specify the following properties, but functions Project.create,accept,reject will throw exceptions**
1. MAIL_EMAIL=your_email
2. MAIL_PASSWORD=your_email_password
3. JWT_SECRET=some_secret
4. AWS_ID=your_s3_access_id
5. AWS_KEY=your_s3_access_key
6. AWS_BUCKET=your_s3_bucket_name

### 3. Run with Docker Compose

Now that you have built the JAR file and configured the environment variables, you can use Docker Compose to set up and run the application and database:
```bash
docker-compose up -d --build
```
This command will start the application and the associated PostgreSQL database in detached mode. Docker Compose will use the configuration specified in the docker-compose.yml file.

### 4. Access the Application

1. Once the containers are up and running, you can access the "Ideas Project" application by navigating to:
   http://localhost:8080.
2. Also, you can use Swagger in order to test API, go to this address:
   http://localhost:8080/swagger-ui/index.html
3. Or use already prepared Postman collection to test API:
   https://www.postman.com/mission-specialist-29542612/workspace/ideas-project
