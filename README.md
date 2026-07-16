# Medical Appointment System Backend

A backend application for managing the daily operations of a clinic or hospital. It brings patient registration, appointment scheduling, clinical records, laboratory work, room allocation, billing, payments, and feedback into one centralized system.

## Features

- Patient registration, email verification, login, and password recovery
- Secure JWT-based authentication and role-based access control
- Management of patients, doctors, staff, and administrators
- Doctor directory and appointment availability
- Appointment booking, rescheduling, status tracking, and cancellation
- Prescription and medication management
- Laboratory tests, orders, and results
- Room allocation and schedule management
- Billing and payment tracking
- Patient feedback submission and moderation
- Request validation and centralized error handling
- Sample data initialization for local development

## Technology Stack

| Area | Technology |
| --- | --- |
| Language | Java 25 |
| Framework | Spring Boot 4.0.4 |
| Web | Spring Web MVC |
| Persistence | Spring Data JPA / Hibernate |
| Database | MySQL |
| Security | Spring Security, JWT, BCrypt |
| Validation | Jakarta Bean Validation |
| Email | Spring Mail |
| Build | Maven Wrapper |
| Utilities | Lombok |
| Testing | JUnit 5, Spring Boot Test |

## Prerequisites

- JDK 25
- MySQL server
- An SMTP account for verification and password-reset emails

A separate Maven installation is optional because Maven Wrapper scripts are included.

## Running Locally

1. Clone the repository and enter the project directory.

   ```bash
   git clone <repository-url>
   cd medical-appointment-backend
   ```

2. Ensure that MySQL is running.

3. Start the application.

   On Windows:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   On macOS or Linux:

   ```bash
   ./mvnw spring-boot:run
   ```

The application runs on `http://localhost:8080` by default.

The development initializer adds a reusable sample dataset when records do not already exist. Review the initializer before connecting the application to non-development data.

## Build and Test

Run the test suite:

```powershell
.\mvnw.cmd test
```

Create the application JAR:

```powershell
.\mvnw.cmd clean package
```

The application-context test requires a running MySQL instance.

## Group Members

- **IT25101952** – Lekamwasam N. L. P. M.
- **IT25101953** – Nanayakkara Y. S.
- **IT25101955** – Wijesekara M. G. N. L.
- **IT25101958** – Chamila A. L. G.
- **IT25101973** – Patabendi M. K. K.
- **IT25101986** – Thashmina P. G. D.

## License

This project is available under the [MIT License](LICENSE).
