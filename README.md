# Ideas Project

![Build Status](https://travis-ci.org/[your-username]/[your-repo].svg?branch=master)

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

- Spring Boot (with various starters: data JPA, mail, security, web, validation)
- QueryDSL for JPA querying
- Spring Data Envers for audit history tracking
- PostgreSQL database
- Liquibase for database versioning
- Amazon S3 SDK for file storage (e.g., PDF documents)

## API Documentation

[Link to detailed API documentation or explain how users can access the API endpoints]

## Testing

To run tests, use the following command: `./gradlew test`

## Installation

1. Clone this repository.
2. [Provide instructions on how to set up the database and other configurations]
3. Build the project using Gradle: `./gradlew build`
4. Run the application: `./gradlew bootRun`