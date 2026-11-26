# Medical Appointment System Backend

A Spring Boot REST API for managing the day-to-day operations of a clinic or hospital. The system provides a centralized backend for user accounts, doctor schedules, patient appointments, prescriptions, laboratory services, rooms, billing, payments, and patient feedback.

The API uses stateless JWT authentication and role-based authorization for patients, doctors, staff, and administrators. It also includes validation, centralized exception handling, email verification and password recovery, and sample development data.

## Main Features

- **Authentication and account security**
- **Role-based access control**
- **User and staff administration**
- **Appointment management**
- **Room and doctor scheduling**
- **Prescription and medication management**
- **Laboratory management**
- **Clinical-record safeguards**
- **Billing and payment management**
- **Patient feedback and moderation**
- **API reliability and development support**

## Technology Stack

| Area | Technology |
| --- | --- |
| Language | Java 25 |
| Framework | Spring Boot 4.0.4 |
| Web API | Spring Web MVC |
| Persistence | Spring Data JPA / Hibernate |
| Database | MySQL |
| Security | Spring Security, JWT, BCrypt |
| Validation | Jakarta Bean Validation |
| Email | Spring Mail (SMTP) |
| Build | Maven Wrapper |
| Utilities | Lombok |
| Testing | JUnit 5, Spring Boot Test |

## Prerequisites

Install the following before starting:

- [JDK 25](https://jdk.java.net/25/)
- [MySQL Server](https://dev.mysql.com/downloads/mysql/)
- Git

You do not need to install Maven separately because the repository includes the Maven Wrapper.

Verify the installations:

```bash
java --version
git --version
mysql --version
```

## Installation and Setup

### 1. Clone the repository

```bash
git clone <repository-url>
cd medical-appointment-backend
```

Replace `<repository-url>` with the URL of this Git repository.

### 2. Start MySQL

Ensure that the MySQL service is running. By default, the application connects to MySQL on `localhost:3306` and uses a database named `clinic_management_system`.

The connection URL contains `createDatabaseIfNotExist=true`, so the database is created automatically when the configured MySQL user has permission to create databases. Alternatively, create it manually:

```sql
CREATE DATABASE clinic_management_system;
```

### 3. Configure the application

Open `src/main/resources/application.properties` and provide values suitable for your computer. At minimum, review the database, mail, and JWT settings:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/clinic_management_system?allowPublicKeyRetrieval=true&useSSL=false&createDatabaseIfNotExist=true
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_email_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

jwt.secret=replace_with_a_long_random_secret
jwt.expiration-ms=10600000
```

For Gmail, use an App Password instead of your normal account password. Email configuration is required for verification and password-reset messages.

Do not commit real database passwords, mail credentials, or JWT secrets. For local use, properties can be overridden with environment variables. Spring Boot maps names such as `SPRING_DATASOURCE_PASSWORD`, `SPRING_MAIL_PASSWORD`, and `JWT_SECRET` to the corresponding properties.

PowerShell example:

```powershell
$env:SPRING_DATASOURCE_USERNAME = "root"
$env:SPRING_DATASOURCE_PASSWORD = "your_mysql_password"
$env:SPRING_MAIL_USERNAME = "your_email@gmail.com"
$env:SPRING_MAIL_PASSWORD = "your_email_app_password"
$env:JWT_SECRET = "replace_with_a_long_random_secret"
```

Bash example:

```bash
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=your_mysql_password
export SPRING_MAIL_USERNAME=your_email@gmail.com
export SPRING_MAIL_PASSWORD=your_email_app_password
export JWT_SECRET=replace_with_a_long_random_secret
```

### 4. Run the application

On Windows:

```powershell
.\mvnw.cmd org.springframework.boot:spring-boot-maven-plugin:4.0.4:run
```

On macOS or Linux:

```bash
chmod +x mvnw
./mvnw org.springframework.boot:spring-boot-maven-plugin:4.0.4:run
```

When startup succeeds, the terminal displays:

```text
Ready on http://localhost:8080
```

Open `http://localhost:8080/` to confirm that the API is running.

## Development Data

`DataInitializer` runs when the application starts and creates sample users, doctors, patients, rooms, appointments, prescriptions, laboratory records, bills, and feedback when they do not already exist.

Useful sample accounts include:

| Role | Email | Password |
| --- | --- | --- |
| Super administrator | `superadmin@hospital.com` | `Password@123` |
| Doctor | `doctor@hospital.com` | `Password@123` |
| Staff | `staff@hospital.com` | `Password@123` |
| Patient | `patient@hospital.com` | `Password@123` |

These accounts are intended only for local development. Disable or remove the initializer and change all default credentials before using the application with real or production data.

## API Overview

The REST API uses the `/api/v1` prefix. Major resource groups include:

| Resource | Base endpoint |
| --- | --- |
| Authentication | `/api/v1/auth` |
| Users | `/api/v1/users` |
| Doctors | `/api/v1/doctor` |
| Appointments | `/api/v1/appointment` |
| Rooms and schedules | `/api/v1/room`, `/api/v1/room-schedule` |
| Prescriptions | `/api/v1/prescription`, `/api/v1/prescription-items` |
| Medications | `/api/v1/medication` |
| Laboratories and tests | `/api/v1/laboratory`, `/api/v1/lab-test` |
| Lab orders and results | `/api/v1/lab-order`, `/api/v1/lab-results` |
| Billing and payments | `/api/v1/billing`, `/api/v1/payment` |
| Feedback | `/api/v1/feedback` |

Public authentication endpoints include registration, login, email verification, and password recovery. Other endpoints require a JWT in the request header:

```http
Authorization: Bearer <your-jwt-token>
```

Example login request:

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"patient@hospital.com","password":"Password@123"}'
```

Browser clients are currently allowed from `http://localhost:3000`. Update the CORS configuration in `SecurityConfig.java` if the frontend runs at a different origin.

## Build and Test

Run the tests:

```powershell
.\mvnw.cmd test
```

Compile, test, and package the project:

```powershell
.\mvnw.cmd clean package
```

MySQL must be available with valid credentials when running the application-context test.

## Project Structure

```text
src/main/java/com/medical/appointment/
|-- config/       Application initialization
|-- controller/   REST API controllers
|-- dto/          Request and response objects
|-- exception/    Error responses and global exception handling
|-- model/        JPA entities and enums
|-- repository/   Spring Data JPA repositories
|-- security/     JWT and Spring Security configuration
|-- service/      Business logic
`-- util/         Password-related utilities
```

## Troubleshooting

- **Database connection failure:** confirm that MySQL is running and check the datasource URL, username, and password.
- **Access denied when creating the database:** create `clinic_management_system` manually or grant the MySQL user database-creation permission.
- **Email sending failure:** verify the SMTP username and App Password and ensure that SMTP access is enabled for the account.
- **`401 Unauthorized`:** log in and send the returned JWT as a Bearer token.
- **CORS error in a browser:** add the frontend origin to `SecurityConfig.java`.
- **Port 8080 already in use:** set `server.port` in `application.properties` or run with `--server.port=8081`.

## Group Members

- **IT25101952** - Lekamwasam N. L. P. M.
- **IT25101953** - Nanayakkara Y. S.
- **IT25101955** - Wijesekara M. G. N. L.
- **IT25101958** - Chamila A. L. G.
- **IT25101973** - Patabendi M. K. K.
- **IT25101986** - Thashmina P. G. D.

## License

This project is available under the [MIT License](LICENSE).
