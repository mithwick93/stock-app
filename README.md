# Stock REST API

Java Spring Boot backend REST application that implements REST endpoints to handle a simple CRUD operations of Stocks

## How to run the application

### Installation

* This app requires Git, Java 17, Maven and Docker to build, run and deploy.

#### Run with docker

1. Clone source files
    ```
    git clone https://github.com/mithwick93/stocks-api.git
    cd stocks-api
    ```

2. Build application
    ```
    ./mvnw clean install -DskipTests
    ```

3. Run
    1. MAC / LINUX
        ```
        DB_PASSWORD=password docker-compose up --build
        ```
    2. WINDOWS (Powershell)
       ```
       $env:DB_PASSWORD="password"
       docker-compose up --build
       ```
4. The application will be running by default at http://localhost:8080
5. Swagger UI is available at http://localhost:8080/swagger-ui/index.html#/

#### Run without Docker

```
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## REST API Spec

```
GET    /api/stocks       - Get a list of stocks.
GET    /api/stocks/{id}  - Get one stock from using stock id.
POST   /api/stocks       - Create a stock.
PUT    /api/stocks/{id}  - Update a single stock by id.
DELETE /api/stocks/{id}  - Delete a single stock by id.

GET    /actuator/health  - Server healthcheck endpoint.
```

## Libraries

* Spring Boot Starter Web
* Spring Boot Starter Data JPA
* Spring Boot Starter Hateoas
* Spring Boot Starter Validation
* Spring Boot Starter Actuator
* Spring Boot Starter AOP
* Postgresql
* Flyway Core
* Project Lombok
* Open API

## License Information

<pre>
        Copyright(c) 2023 M.S. Wickramarathne
        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at
            https://www.apache.org/licenses/LICENSE-2.0
        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
</pre>