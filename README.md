# Organization System

#### It is necessary to design and write two services that will interact with REST using Spring Cloud technologies (for example Netflix, Gateway and Config).

#### The first service has a REST interface and will store user records (id number, first name, last name, phone number and id number of the company in which this user works). This service must have at least 4 CRUD endpoints and an endpoint to return a list of all users. These endpoints should return users containing the company, not the company ID.  

#### The second service stores information about companies (ID, company name, budget, and a list of employee IDs) and provides at least 4 CRUD endpoints and an endpoint that returns a list of all companies. These endpoints should return companies containing a list of users, not a list of user IDs.  

#### It is necessary to write Docker compose - which raises services and provides services to the outside via http  
#### Technology - java of the max version    
#### Spring Boot (spring cloud, spring web, spring data…) for services and linking them  
#### A database deployed using Docker (for example, PostgreSQL)  

## Technology

- **JDK 24**
- **Spring Boot (Web, Data, Actuator, Validation)**
- **Spring Cloud (Eureka, OpenFeign, Gateway, Config)**
- **PostgreSQL**
- **Docker Compose**
- **E2E tests with testcontainers & REST Assured**
- **MapStruct, Lombok, Spring Test**

## TODO

- **Spring Cloud (LoadBalancer, Resilience4j, Contract Verifier with Wiremock)**
- **Mixed blocking OpenFeign & event-driven RabbitMQ interaction**
- 
