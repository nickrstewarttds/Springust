# Springust

Starter Spring App developed with SDET August Intake on Tue 29-Sep-2020.

Built with the following starters:

- Spring Web
- H2 Database
- Spring Data JPA
- Lombok

Requires the following Maven dependencies:

- [ModelMapper](https://mvnrepository.com/artifact/org.modelmapper/modelmapper/2.3.8)
- [Swagger-UI v3.0](https://mvnrepository.com/artifact/io.springfox/springfox-boot-starter/3.0.0)

Requirements (if using Eclipse):

- [Spring Tool Suite](https://marketplace.eclipse.org/content/spring-tools-4-aka-spring-tool-suite-4)
- [Lombok](https://projectlombok.org/setup/eclipse)

## API

- Runs out-of-the-box on `localhost:8901` (can be changed within the `application-dev.properties` file)
- H2 console is accessible at the path `/h2` with JDBC URL `jdbc:h2:mem:springust` and default username/password
- Swagger UI showing API endpoints is accessible at the path `/swagger-ui/index.html`

## Showing the One-To-Many relationship with Postman

- Create the `Band` record first
- Then add the `Musician` record to the `Band` record

### Band

Send as `application:json`:

```json
{
  "name":"The Mountain Goats"
}
```

### Musician

Send as `application:json`:
```json
{
  "name":"John Darnielle",
  "strings"6,
  "type":"guitarist,
  "band":{
    "id":1
  }
}
```
