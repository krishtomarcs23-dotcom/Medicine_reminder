# Medicine Reminder - Quick Start Guide

## Project Structure
```
medicine_reminder/
├── pom.xml
├── SPEC.md
└── src/
    └── main/
        ├── java/com/medicinereminder/
        │   ├── MedicineReminderApplication.java
        │   ├── config/
        │   │   ├── SecurityConfig.java
        │   │   ├── WebConfig.java
        │   │   └── GlobalExceptionHandler.java
        │   ├── controller/
        │   │   ├── AuthController.java
        │   │   ├── UserController.java
        │   │   └── AdminController.java
        │   ├── dto/
        │   │   ├── RegisterRequest.java
        │   │   ├── MedicineDTO.java
        │   │   └── DashboardDTO.java
        │   ├── entity/
        │   │   ├── User.java
        │   │   ├── Medicine.java
        │   │   ├── ReminderStatus.java
        │   │   ├── Prescription.java
        │   │   ├── Frequency.java
        │   │   └── Status.java
        │   ├── repository/
        │   │   ├── UserRepository.java
        │   │   ├── MedicineRepository.java
        │   │   ├── ReminderStatusRepository.java
        │   │   └── PrescriptionRepository.java
        │   └── service/
        │       ├── UserService.java
        │       ├── MedicineService.java
        │       ├── ReminderStatusService.java
        │       ├── PrescriptionService.java
        │       ├── EmailService.java
        │       └── impl/ (implementations)
        └── resources/
            ├── application.properties
            ├── static/
            │   ├── css/style.css
            │   └── js/main.js
            └── templates/
                ├── login.html
                ├── register.html
                ├── dashboard.html
                ├── medicines.html
                ├── medicine-form.html
                ├── prescriptions.html
                ├── admin-dashboard.html
                ├── admin-users.html
                ├── admin-medicines.html
                ├── admin-prescriptions.html
                ├── access-denied.html
                └── layout.html
```

## How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Run the Application
```bash
cd medicine_reminder
mvn spring-boot:run
```

The application will start at: http://localhost:9090

### Access Points
- Login: http://localhost:9090/login
- Register: http://localhost:9090/register
- Admin Panel: http://localhost:9090/admin (requires ROLE_ADMIN)
- H2 Console: http://localhost:9090/h2-console

## Default Configuration

### Database (H2)
- URL: jdbc:h2:mem:medicinedb
- Username: sa
- Password: (empty)

### Email Configuration
Update `application.properties` with your SMTP settings:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

### Create Admin User
The first user registered will be a regular user. To create an admin user:
1. Register a new user
2. Manually update the database (via H2 console) to change role:
   ```sql
   UPDATE users SET role = 'ROLE_ADMIN' WHERE email = 'admin@example.com';
   ```

## Features
- Session-based authentication (no JWT)
- Role-based access control (ROLE_USER, ROLE_ADMIN)
- Medicine reminder management
- Prescription file upload
- Email notifications
- Admin dashboard with user/medicine management
- Modern responsive UI

## Technology Stack
- Spring Boot 3.2.1
- Spring Security (Session-based)
- Spring Data JPA
- H2 Database
- Thymeleaf
- HTML/CSS/JavaScript (Vanilla)
