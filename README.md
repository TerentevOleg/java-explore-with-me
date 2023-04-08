# Explore with me

### Functional
There are 2 microservices made as modules in project:

Ewm-service - contains everything necessary for operation:
- Viewing events without authorization;
- Ability to create and manage categories;
- Events and working with them - creating, moderation;
- User requests to participate in an event - request, confirmation, rejection.
- Creating and managing compilations.
- Adding and deleting likes of events, forming ratings. 
- Runs on port 8080.

Stats-server - stores the number of views and allows to make various selections for analyzing the application usage.
- Separate service for collecting statistics.
- Runs on port 9090.

---
### Stack
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/spring%20Boot-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/postgresql-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Postman](https://img.shields.io/badge/postman-%23FF6C37.svg?style=for-the-badge&logo=postman&logoColor=white)
