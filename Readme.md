<p align="center">
  <a href="https://spring.io/projects/spring-boot"><img src="https://www.devopsschool.com/blog/wp-content/uploads/2022/02/spring-boot-logo.png" alt="Spring Boot" height="200"></a>
</p>

<p align="center">
    <em>Java Spring Boot (Spring Boot) is a tool that makes developing web application and microservices with Spring Framework faster and easier</em>
</p>

---

**Source Code**:

https://github.com/spring-projects/spring-boot.git

---

Spring Boot helps you to create Spring-powered, production-grade applications and services with absolute minimum fuss. It takes an opinionated view of the Spring platform so that new and existing users can quickly get to the bits they need.

You can use Spring Boot to create stand-alone Java applications that can be started using java -jar or more traditional WAR deployments. We also provide a command-line tool that runs Spring scripts.

Our primary goals are:

Provide a radically faster and widely accessible getting started experience for all Spring development.

Be opinionated, but get out of the way quickly as requirements start to diverge from the defaults.

Provide a range of non-functional features common to large classes of projects (for example, embedded servers, security, metrics, health checks, externalized configuration).

Absolutely no code generation and no requirement for XML configuration.

## Project Description

_This is a take on full-fledged spring boot 
Demo Application Involving User Management,
Custom Pagination/Filter/Search,
Custom Exception Handling, Custom Media Handling,
Flyway DB Migrations And Docker_

## Requirements

JDK 8+

maven

Java 11+

## Installation

<div class="termy">

Setup an application-dev.properties file in src/main/resources with following sample
```console
server.address=
server.port=
spring.datasource.url=jdbc:postgresql://address:port/db
spring.datasource.username=
spring.datasource.password=

// set this to none if you want to use flyway
spring.jpa.hibernate.ddl-auto=none

// flyway config
// set to false to disable flyway db migrations
spring.flyway.enabled=true
spring.flyway.locations=db/migration
spring.flyway.baseline-on-migrate=true

// mail configs
spring.mail.host=
spring.mail.port=
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

```
</div>

<div class="termy">

Using mvn package

```console
$ mvn install
```
</div>

<div class="termy">

Run the Project
```console
$ mvn spring-boot:run 
```
</div>

<div class="termy">

Follow the swagger Ui Documentation
```console
http://{SERVER_HOST}:{SERVER_PORT}/api/docs
```
</div>

# Try it out with [Docker](https://www.docker.com/)

<div class="termy">

Dockerize Personally

```console
$ docker build -t advanced-spring-boot-demo .

$ docker run -p 8090:8090 advanced-spring-boot-demo
```

</div>

<div class="termy">

Run the Project via Docker Compose Directly

```console
$ docker-compose up 
```

</div>
