# Ecommerce Application

#### Ecommerce App using Springboot and Angular
This is a Single Page Appliaction with client-side rendering. It includes frontend and backend directory.
The frontend client makes API calls to the backend server when it is running.

#### Live Demo: [https://animated-purpose-production.up.railway.app](https://animated-purpose-production.up.railway.app) Deployed on Railway platform


## Features
- REST API
- Docker
- JWT authentication
- Cookie based visitors' shopping cart
- Persistent customers' shopping cart
- Cart & order management
- Checkout
- Catalogue
- Order management
- Pagination
## Technology Stacks

**Backend**
  - Java 11
  - Spring Boot 2.2
  - Spring Security
  - JWT Authentication
  - Spring Data JPA
  - Hibernate
  - MYSQL
  - Maven

**Frontend**
  - Angular 7
  - Angular CLI
  - Bootstrap


## How to  Run
export env variable PROFILE and set it to dev.
Start the backend server before the frontend client using mvn spring-boot:run.  

**Backend**

  1. Install [MYSQL](https://www.mysql.org/download/) 
  2. Configure datasource in `application-dev.properties`.
  3. `cd backend`.
  4. export PROFILE=dev
  5. Run `mvn clean package`.
  6. Run `mvn spring-boot:run`.
  7. The backend server is running on [localhost:8080]().

**Frontend**
  1. Install [Node.js and npm](https://www.npmjs.com/get-npm)
  2. `cd frontend`.
  3. Run `npm install`.
  4. Run `npm run-script dev`
  5. The frontend client is running on [localhost:4200]().
  
Note: The backend API url is configured in `src/environments/environment.ts` of the frontend project. It is `localhost:8080/api` by default.
  
#### Run in Docker
You can build the image and run the container with Docker. 
1. Build backend project
```bash
cd backend
mvn package
```
2. Build fontend project
```bash
cd frontend
npm install
npm run-script dev
```

