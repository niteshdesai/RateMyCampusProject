# RateMyCampus - Complete Project Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Technology Stack](#technology-stack)
3. [Database Design](#database-design)
4. [Backend Architecture](#backend-architecture)
5. [API Documentation](#api-documentation)
6. [Frontend Design](#frontend-design)
7. [Implementation Guide](#implementation-guide)
8. [Testing & Deployment](#testing--deployment)

---

## Project Overview

**RateMyCampus** is a comprehensive college and teacher rating platform that allows students to rate and review colleges, departments, courses, and teachers. The system provides a complete academic ecosystem for student feedback and institutional transparency.

### Key Features
- College and department management
- Course and teacher assignments
- Student and teacher rating systems
- Multi-level admin management (College Admin, Department Admin)
- Image upload capabilities
- RESTful API architecture
- Professional frontend interface

---

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.x
- **Language**: Java 17+
- **Database**: MySQL/PostgreSQL
- **ORM**: JPA/Hibernate
- **Security**: Spring Security with JWT
- **Build Tool**: Maven

### Frontend
- **HTML5**: Semantic markup
- **CSS3**: Modern styling with Flexbox/Grid
- **JavaScript**: jQuery for DOM manipulation
- **AJAX**: Asynchronous data handling
- **Design**: Professional light theme with blue/orange color scheme

### Development Tools
- **IDE**: IntelliJ IDEA / Eclipse
- **Version Control**: Git
- **API Testing**: Postman
- **Database**: MySQL Workbench / pgAdmin

---

## Database Design

### Entity Relationships

```
College (1) ←→ (N) Department
College (1) ←→ (N) Course
College (1) ←→ (N) Student
College (1) ←→ (N) Teacher
College (1) ←→ (N) CollegeAdmin

Department (1) ←→ (N) Course
Department (1) ←→ (N) Student
Department (1) ←→ (N) Teacher
Department (1) ←→ (N) DepartmentAdmin

Course (1) ←→ (N) Student
Course (N) ←→ (N) Teacher (via TeacherCourse)

Student (1) ←→ (N) Rating (College ratings)
Student (1) ←→ (N) RatingTeacher (Teacher ratings)

Teacher (1) ←→ (N) RatingTeacher
```

### Core Entities

#### College
- `cid` (Primary Key)
- `cname` (College name)
- `cdesc` (Description)
- `cactivity` (Activities)
- `address` (Location)
- `cimg` (Image path)
- `email` (Contact email)

#### Department
- `deptId` (Primary Key)
- `deptName` (Department name)
- `desc` (Description)
- `college` (Foreign Key to College)

#### Course
- `c_id` (Primary Key)
- `cName` (Course name)
- `cDuration` (Duration in semesters)
- `cSince` (Year started)
- `college` (Foreign Key to College)
- `department` (Foreign Key to Department)

#### Student
- `sid` (Primary Key)
- `enrollment` (Enrollment number)
- `sname` (Student name)
- `ssem` (Current semester)
- `ssection` (Section)
- `sgender` (Gender)
- `smobile` (Mobile number)
- `scity` (City)
- `simg` (Profile image)
- `semail` (Email)
- `college` (Foreign Key to College)
- `department` (Foreign Key to Department)
- `course` (Foreign Key to Course)

#### Teacher
- `tid` (Primary Key)
- `tname` (Teacher name)
- `tsem` (Semester)
- `tsection` (Section)
- `timg` (Profile image)
- `college` (Foreign Key to College)
- `department` (Foreign Key to Department)

#### TeacherCourse (Junction Table)
- `id` (Primary Key)
- `teacher` (Foreign Key to Teacher)
- `course` (Foreign Key to Course)

#### Rating
- `id` (Primary Key)
- `score` (Rating 1-5)
- `college` (Foreign Key to College)
- `student` (Foreign Key to Student)

#### RatingTeacher
- `id` (Primary Key)
- `score` (Rating 1-5)
- `teacher` (Foreign Key to Teacher)
- `student` (Foreign Key to Student)
- `course` (Foreign Key to Course)

#### CollegeAdmin
- `id` (Primary Key)
- `name` (Admin name)
- `email` (Email)
- `password` (Encrypted password)
- `mobile` (Mobile number)
- `imagePath` (Profile image)
- `college` (Foreign Key to College)

#### DepartmentAdmin
- `hodId` (Primary Key)
- `username` (Username)
- `password` (Encrypted password)
- `name` (HOD name)
- `email` (Email)
- `daImg` (Profile image)
- `department` (Foreign Key to Department)
- `college` (Foreign Key to College)

---

## Backend Architecture

### Package Structure
```
com.ratemycampus/
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── CollegeController.java
│   ├── DepartmentController.java
│   ├── CourseController.java
│   ├── StudentController.java
│   ├── TeacherController.java
│   ├── TeacherCourseController.java
│   ├── RatingController.java
│   ├── RatingTeacherController.java
│   ├── CollegeAdminController.java
│   ├── DepartmentAdminController.java
│   └── LoginController.java
├── entity/
│   ├── College.java
│   ├── Department.java
│   ├── Course.java
│   ├── Student.java
│   ├── Teacher.java
│   ├── TeacherCourse.java
│   ├── Rating.java
│   ├── RatingTeacher.java
│   ├── CollegeAdmin.java
│   └── DepartmentAdmin.java
├── repository/
│   ├── CollegeRepository.java
│   ├── DepartmentRepository.java
│   ├── CourseRepository.java
│   ├── StudentRepository.java
│   ├── TeacherRepository.java
│   ├── TeacherCourseRepository.java
│   ├── RatingRepository.java
│   ├── RatingTeacherRepository.java
│   ├── CollegeAdminRepository.java
│   └── DepartmentAdminRepository.java
├── service/
│   ├── CollegeService.java
│   ├── DepartmentService.java
│   ├── CourseService.java
│   ├── StudentService.java
│   ├── TeacherService.java
│   ├── RatingService.java
│   ├── RatingTeacherService.java
│   ├── CollegeAdminService.java
│   ├── DepartmentAdminService.java
│   └── LoginService.java
├── dto/
│   ├── CollegeDTO.java
│   ├── DepartmentDTO.java
│   ├── CourseDTO.java
│   ├── StudentDTO.java
│   ├── TeacherDTO.java
│   ├── RatingDTO.java
│   ├── RatingTeacherDTO.java
│   ├── CollegeAdminDTO.java
│   ├── DepartmentAdminDTO.java
│   └── DtoMapper.java
├── security/
│   ├── JwtUtil.java
│   └── JwtFilter.java
└── exception/
    └── ResourceNotFoundException.java
```

### Key Design Patterns
- **MVC Pattern**: Controllers handle requests, Services contain business logic, Repositories manage data access
- **DTO Pattern**: Data Transfer Objects for API input/output
- **Repository Pattern**: Abstract data access layer
- **Service Layer**: Business logic encapsulation
- **JWT Authentication**: Stateless authentication mechanism

---

## API Documentation

### Base URL
```
http://localhost:8080
```

### Authentication Endpoints
```
POST /auth/student
POST /auth/hod
POST /auth/college-admin
POST /auth/admin
```

### College Management
```
GET    /api/colleges                    # Get all colleges
GET    /api/colleges/{id}               # Get college by ID
POST   /api/colleges/addcollege         # Create college (multipart)
PUT    /api/colleges/{id}               # Update college
DELETE /api/colleges/{id}               # Delete college
GET    /api/colleges/search?name={name} # Search colleges
GET    /api/colleges/city/{city}        # Get colleges by city
```

### Department Management
```
GET    /api/departments                 # Get all departments
GET    /api/departments/{id}            # Get department by ID
POST   /api/departments                 # Create department
PUT    /api/departments/{id}            # Update department
DELETE /api/departments/{id}            # Delete department
GET    /api/departments/college/{id}    # Get departments by college
```

### Course Management
```
GET    /api/courses                     # Get all courses
GET    /api/courses/{id}                # Get course by ID
POST   /api/courses                     # Create course
PUT    /api/courses/{id}                # Update course
DELETE /api/courses/{id}                # Delete course
```

### Student Management
```
GET    /api/students                    # Get all students
GET    /api/students/{id}               # Get student by ID
GET    /api/students/enroll/{number}    # Get student by enrollment
POST   /api/students/addStudent         # Create student (multipart)
PUT    /api/students/{id}               # Update student
DELETE /api/students/{id}               # Delete student
GET    /api/students/college/{id}       # Get students by college
GET    /api/students/department/{id}    # Get students by department
GET    /api/students/course/{id}        # Get students by course
```

### Teacher Management
```
GET    /api/teachers                    # Get all teachers
GET    /api/teachers/{id}               # Get teacher by ID
POST   /api/teachers                    # Create teacher (multipart)
PUT    /api/teachers/{id}               # Update teacher
DELETE /api/teachers/{id}               # Delete teacher
GET    /api/teachers/college/{id}       # Get teachers by college
GET    /api/teachers/department/{id}    # Get teachers by department
```

### Teacher-Course Assignment
```
GET    /api/teachers/{id}/courses       # Get teacher's courses
POST   /api/teachers/{id}/courses       # Assign courses to teacher
DELETE /api/teachers/{id}/courses       # Unassign courses from teacher
```

### Rating System
```
GET    /api/ratings                     # Get all college ratings
GET    /api/ratings/{id}                # Get rating by ID
POST   /api/ratings                     # Create college rating
PUT    /api/ratings/{id}                # Update rating
DELETE /api/ratings/{id}                # Delete rating
```

### Teacher Rating System
```
GET    /api/rating-teachers             # Get all teacher ratings
GET    /api/rating-teachers/{id}        # Get rating by ID
POST   /api/rating-teachers             # Create teacher rating
PUT    /api/rating-teachers/{id}        # Update rating
DELETE /api/rating-teachers/{id}        # Delete rating
GET    /api/rating-teachers/teacher/{id} # Get ratings by teacher
GET    /api/rating-teachers/student/{id} # Get ratings by student
```

### Admin Management
```
GET    /api/college-admin/all           # Get all college admins
GET    /api/college-admin/{id}          # Get admin by ID
POST   /api/college-admin/addcollegeAdmin # Create admin (multipart)
PUT    /api/college-admin/{id}          # Update admin
DELETE /api/college-admin/delete/{id}   # Delete admin
```

```
GET    /api/hod                         # Get all department admins
GET    /api/hod/{id}                    # Get admin by ID
POST   /api/hod                         # Create admin (multipart)
PUT    /api/hod/{id}                    # Update admin
DELETE /api/hod/{id}                    # Delete admin
```

### Sample API Requests

#### Create College
```bash
POST http://localhost:8080/api/colleges/addcollege
Content-Type: multipart/form-data

college: {
  "cname": "ABC Institute of Technology",
  "cdesc": "A premier institute offering multiple programs.",
  "cactivity": "Clubs, sports, fests",
  "address": "123 Tech Park, City",
  "email": "info@abcit.edu"
}
image: [file]
```

#### Create Student
```bash
POST http://localhost:8080/api/students/addStudent
Content-Type: multipart/form-data

student: {
  "enrollment": "ENR20250001",
  "sname": "John Doe",
  "ssem": 3,
  "ssection": "A",
  "sgender": "Male",
  "smobile": "9876543210",
  "scity": "Metropolis",
  "semail": "john.doe@example.com",
  "collegeId": 1,
  "departmentId": 1,
  "courseId": 1
}
image: [file]
```

#### Create Teacher Rating
```bash
POST http://localhost:8080/api/rating-teachers
Content-Type: application/json

{
  "score": 4,
  "teacherId": 1,
  "studentId": 1,
  "courseId": 2
}
```

#### Assign Courses to Teacher
```bash
POST http://localhost:8080/api/teachers/5/courses
Content-Type: application/json

{
  "courseIds": [1, 2, 3]
}
```

---

## Frontend Design

### Color Scheme
- **Primary Blue**: `#2563eb` (Royal Blue)
- **Secondary Orange**: `#f97316` (Vibrant Orange)
- **Light Blue**: `#dbeafe` (Backgrounds)
- **Light Orange**: `#fed7aa` (Accents)
- **White**: `#ffffff` (Content areas)
- **Light Gray**: `#f8fafc` (Card backgrounds)
- **Dark Gray**: `#1e293b` (Text)
- **Medium Gray**: `#64748b` (Secondary text)

### Typography
- **Headers**: Inter or Poppins (sans-serif)
- **Body**: Inter or system fonts
- **Base size**: 14px, Headers: 18-24px

### Key Pages
1. **Landing Page**: Hero section, featured colleges, statistics
2. **College Listing**: Search, filters, college cards, pagination
3. **College Detail**: Information, departments, courses, ratings
4. **Teacher Directory**: Search, filtering, teacher cards
5. **Student Dashboard**: Profile, rating history, favorites
6. **Authentication**: Login/registration forms

### Responsive Design
- Mobile-first approach
- Breakpoints: 320px, 768px, 1024px, 1200px
- Touch-friendly interface
- Optimized for all devices

---

## Implementation Guide

### Backend Setup
1. **Prerequisites**
   - Java 17+
   - Maven 3.6+
   - MySQL/PostgreSQL
   - IDE (IntelliJ IDEA/Eclipse)

2. **Database Setup**
   ```sql
   CREATE DATABASE ratemycampus;
   USE ratemycampus;
   ```

3. **Application Properties**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ratemycampus
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```

4. **Build and Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

### Frontend Setup
1. **File Structure**
   ```
   frontend/
   ├── index.html
   ├── pages/
   ├── css/
   ├── js/
   └── images/
   ```

2. **Dependencies**
   - jQuery 3.6.0+
   - Modern CSS (Grid/Flexbox)
   - Font Awesome (icons)

3. **Development Server**
   - Use any local server (Live Server, Python HTTP server)
   - Configure CORS for API communication

### API Integration
1. **Base Configuration**
   ```javascript
   const API_BASE_URL = 'http://localhost:8080';
   ```

2. **AJAX Examples**
   ```javascript
   // Get colleges
   $.ajax({
       url: API_BASE_URL + '/api/colleges',
       method: 'GET',
       success: function(data) {
           displayColleges(data);
       }
   });

   // Create rating
   $.ajax({
       url: API_BASE_URL + '/api/ratings',
       method: 'POST',
       contentType: 'application/json',
       data: JSON.stringify(ratingData),
       success: function(data) {
           showSuccess('Rating submitted successfully');
       }
   });
   ```

---

## Testing & Deployment

### Testing Strategy
1. **Unit Testing**: Service layer methods
2. **Integration Testing**: API endpoints
3. **Frontend Testing**: User interface functionality
4. **Performance Testing**: Load testing for concurrent users

### Deployment
1. **Backend Deployment**
   - Build JAR file: `mvn clean package`
   - Deploy to cloud platform (AWS, Azure, GCP)
   - Configure environment variables
   - Set up database connections

2. **Frontend Deployment**
   - Optimize assets (minify CSS/JS)
   - Deploy to web server or CDN
   - Configure API endpoints for production

3. **Database Deployment**
   - Set up production database
   - Configure backups and monitoring
   - Implement data migration scripts

### Monitoring & Maintenance
- Application performance monitoring
- Error logging and alerting
- Database performance optimization
- Regular security updates
- User feedback collection

---

## Security Considerations

### Authentication & Authorization
- JWT-based authentication
- Role-based access control
- Password encryption
- Session management

### Data Protection
- Input validation and sanitization
- SQL injection prevention
- XSS protection
- CSRF protection

### API Security
- Rate limiting
- Request validation
- Error handling
- Logging and monitoring

---

## Future Enhancements

### Planned Features
1. **Advanced Analytics**: Rating trends, insights
2. **Mobile Application**: Native mobile app
3. **Social Features**: Comments, discussions
4. **Notification System**: Email/SMS alerts
5. **Advanced Search**: Full-text search capabilities
6. **API Documentation**: Swagger/OpenAPI
7. **Microservices**: Service decomposition
8. **Real-time Features**: WebSocket integration

### Scalability Considerations
- Database optimization
- Caching strategies
- Load balancing
- CDN integration
- Microservices architecture

---

## Conclusion

RateMyCampus is a comprehensive academic rating platform that provides transparency and feedback mechanisms for educational institutions. The project demonstrates modern web development practices with a focus on user experience, security, and scalability.

The combination of Spring Boot backend and modern frontend technologies creates a robust, maintainable, and user-friendly platform that can serve as a foundation for educational technology solutions.

---

**Document Version**: 1.0  
**Last Updated**: 2024  
**Project Status**: Development Complete  
**Next Phase**: Testing & Deployment
