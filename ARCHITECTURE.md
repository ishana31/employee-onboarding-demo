# Architecture & Structural Design

This repository is designed for code structure, dependency graph, and semantic chunking analysis within the Agentic Software Factory.

## Layered Hierarchy

```
Controller (REST API Endpoints)
    │
    ▼
Service Layer (Interfaces & Implementations)
    │
    ├─► Business Orchestration & Workflows (WorkflowOrchestratorService, TaskAssignmentService)
    ├─► Domain Services (Department, Manager, Employee, Onboarding, Notification)
    │
    ▼
Repository Layer (Spring Data JPA)
    │
    ▼
Entity Layer (JPA Relational Mapping with Base Superclasses)
```

## Architectural Characteristics

### 1. Model Inheritance & Shared Concerns
- `BaseEntity`: Provides common auditing fields (`id`, `createdAt`, `updatedAt`) using JPA lifecycle listeners.
- `Person`: Inherits from `BaseEntity`, consolidating identity and contact fields (`firstName`, `lastName`, `email`, `phoneNumber`, `getFullName()`) shared across `Employee` and `Manager`.
- Concrete entities (`Employee`, `Manager`, `Department`, `Onboarding`, `OnboardingTask`, `Notification`) extend these base models, testing analyzer capability to navigate inheritance graphs.

### 2. Entity Relationships
- **Department 1:N Employee**: Department tracks its assigned workforce.
- **Department 1:N Manager**: Multiple department leads/managers can belong to a single department.
- **Manager 1:N Employee**: Direct report hierarchy.
- **Employee 1:1 Onboarding**: Each employee has at most one active onboarding record.
- **Onboarding 1:N OnboardingTask**: Cascade-managed task checklist with lifecycle synchronization.

### 3. Business Orchestration & Inter-Service Dependencies
- `WorkflowOrchestratorService`: Central logic engine that evaluates priority-weighted progress, triggers milestone notifications, and executes escalation logic for overdue items.
- `TaskAssignmentService`: Generates customized task workflows dynamically based on employee attributes (e.g., department code `ENG` generates developer-specific orientation items).
- `EmployeeServiceImpl` depends on `DepartmentRepository`, `ManagerRepository`, `NotificationService`, and `EmployeeMapper`.
- `OnboardingTaskServiceImpl` delegates to `WorkflowOrchestratorService` upon task completion, which reads related tasks and updates the parent `Onboarding` entity within a declarative transaction.

### 4. Semantic Chunking Attributes
- Explicit DTO mappings ensure entities are never leaked to the web boundary.
- Mappers are decoupled `@Component` beans rather than reflective bytecode generation, providing clear syntactic reference anchors.
- Methods contain loops, conditional branches (e.g., enhanced switch expressions for task weights), and multi-step validation checks suitable for semantic chunking and AST-based reasoning.
