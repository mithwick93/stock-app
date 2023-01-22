# Stock REST API

Java Spring Boot backend REST application that implements REST endpoints to handle a simple CRUD operations of Stocks

## How to run the application

### Installation

* This app requires Git, Java 17, Maven and Docker to build, test and run.

#### Run API with docker compose

* This is the recommended approach for consuming the API locally.

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

#### Run API without Docker

* This is the recommended approach for developing the API locally.

1. Setup a postgres database locally.
    1. MAC / LINUX
        ```
        DB_PASSWORD=password docker-compose -f common-services.yml up
        ```

    2. WINDOWS (Powershell)
       ```
        $env:DB_PASSWORD="password"
        docker-compose -f common-services.yml up
       ```

2. Run the application using maven pointing to local database.
    1. MAC / LINUX
       ```
       dbUrl=jdbc:postgresql://localhost:5432/stock-db dbUsername=postgres dbPassword=password ./mvnw spring-boot:run
       ```

    2. WINDOWS (Powershell)

       ```
       $env:dbUrl="jdbc:postgresql://localhost:5432/stock-db"
       $env:dbUsername="postgres"
       $env:dbPassword="password"
       ./mvnw spring-boot:run
       ```

### Run all tests

```
./mvnw clean verify
```

## REST API

* Swagger UI is available at http://localhost:8080/swagger-ui/index.html#/

    ```
    GET    /api/stocks       - Get a list of stocks.
    GET    /api/stocks/{id}  - Get one stock from using stock id.
    POST   /api/stocks       - Create a stock.
    PUT    /api/stocks/{id}  - Update a single stock by id.
    DELETE /api/stocks/{id}  - Delete a single stock by id.
    
    GET    /actuator/health  - Server healthcheck endpoint.
    ```

## License Information

- Apache v2 License

<pre>
Copyright(c) 2023 M.S. Wickramarathne

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>