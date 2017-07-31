## ContentMetadataService

This is a service to get content metadata for videos based on levels.

### Prerequisites 
  
```bash
  Maven
  Java 8
```
  
### Frameworks and libraries used
  
  ```bash
    Spring Boot - Development framework
    Lombok - Logger
    Unirest - Http Utility
    Springfox - Swagger documentation
    Jackson - Json parser
    Hamcrest - Test util
  ```

### Build

```bash
mvn clean package
```

#### Launch the application

```bash
mvn spring-boot:run
```

The service will be accessible in 
```bash
http://localhost:9090/media?filter=CENSORING&level=UNCENSORED
```

#### Documentation 
Live documentation is done in swagger. Launch the application and see the swagger documentation in below url.
```bash
http://localhost:9090/swagger-ui.html
```