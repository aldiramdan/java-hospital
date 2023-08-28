<h1 align="center">
  Hospital App
</h1>
<h2 align="center">
  Java Restfull Api With Spring Boot & MySQL
</h2>

## 🛠️ Installation Steps

1. Clone the repository

```bash
git clone https://github.com/aldiramdan/java-hospital.git
```

2. Set Up Database

```sh
# Database
spring.datasource.url=jdbc:mysql://your-db-host:your-db-port/your-db-name
spring.datasource.username=your-db-user
spring.datasource.password=your-db-password

# Spring App
server.port=your-port-server
server.servlet.context-path=/api
# endpoint: localhost:8080/api
```

3. Run the app

```bash
mvn spring-boot:run
```

4. Base URL endpoint

```sh
http://localhost:8080/api
```

5.Swagger URL endpoint

```sh
http://localhost:8080/api/swagger-ui/index.html
```

🌟 You are all set!

## 💻 Built with

- [Java](https://www.java.com/en/): programming language
- [SpringBoot](https://start.spring.io/): for handle http request
- [MySQL](https://www.mysql.com/): for DBMS
