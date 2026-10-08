# Employee Onboarding Management System

A realistic Spring Boot domain application developed as a controlled testbed repository for **Agentic Software Factory** research. The repository models employee onboarding workflows, departmental hierarchies, and task tracking with explicit dependencies, transaction boundaries, validation rules, and business orchestration logic.

## Technical Specifications
- **Java**: 24
- **Spring Boot**: 3.4.0
- **Build Tool**: Maven Wrapper (`mvnw` / `mvnw.cmd`)
- **Database**: PostgreSQL (Production/Dev), H2 in-memory (Unit/Integration Testing)
- **Architecture**: Controller → Service → Repository → Entity with decoupled DTOs and Mappers

## Core Domains
1. **Department**: Organizational units (`Engineering`, `Human Resources`, `Finance`, etc.) with code, name, and staffing tracking.
2. **Manager**: Management layer inheriting from `Person`, supervising department members.
3. **Employee**: Personnel record with role, department, direct reporting manager, and associated onboarding process.
4. **Onboarding**: Core lifecycle tracking onboarding status (`NOT_STARTED`, `IN_PROGRESS`, `COMPLETED`, etc.), progress calculation, and scheduled completion targets.
5. **OnboardingTask**: Discrete checklist tasks categorized into IT Setup, HR Paperwork, Security Compliance, and Team Introductions with priority-based completion weighting.
6. **Notification**: Transactional messaging log capturing notifications triggered throughout the employee lifecycle.

## Building and Running

### Prerequisites
- JDK 24 installed
- Git

### Build & Run Tests
```powershell
# Using Maven Wrapper with Java 24
$env:JAVA_HOME="C:\Program Files\Java\jdk-24" # If required on Windows
.\mvnw.cmd clean test
```

### Run Application
```powershell
.\mvnw.cmd spring-boot:run
```

## API Endpoints Overview
- `POST /api/departments` - Create new department
- `GET  /api/departments` - List departments
- `GET  /api/departments/{id}` - Retrieve department details
- `POST /api/managers` - Register department manager
- `GET  /api/managers` - List managers
- `POST /api/employees` - Register employee and trigger welcome notifications
- `GET  /api/employees` - List employees
- `GET  /api/employees/{id}` - Get employee details
- `PATCH /api/employees/{id}/deactivate` - Deactivate employee
- `POST /api/onboarding/initiate/{employeeId}` - Initiate onboarding workflow and generate role-based tasks
- `GET  /api/onboarding/{id}` - Get onboarding status
- `GET  /api/onboarding/employee/{employeeId}` - Get employee onboarding progress
- `PATCH /api/tasks/{id}/status?status=COMPLETED` - Update task status and trigger automated recalculation & completion notifications
- `GET  /api/notifications?email={email}` - Fetch notification logs for an employee
