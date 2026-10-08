# Controlled Change Scenarios for Incremental / Diff Analysis

These scenarios are designed for testing downstream diff-based code analysis, dependency invalidation, and incremental AST re-indexing in the Agentic Software Factory.

> **Note**: Do NOT implement these scenarios in the baseline repository. They serve as controlled input modifications for experimental evaluation.

---

### Scenario 1: Refactor `Employee.jobTitle` to `JobRole` Entity (Cross-Cutting Relationship Change)
- **Goal**: Convert a simple string attribute into a full relational entity.
- **Affected Layers**:
  - `entity`: Introduce `JobRole` entity (`id`, `title`, `bandLevel`, `salaryRangeMin`, `salaryRangeMax`). Replace `jobTitle` string on `Employee` with `@ManyToOne private JobRole role;`.
  - `repository`: Create `JobRoleRepository`.
  - `dto`: Create `JobRoleDTO` and update `CreateEmployeeRequest` / `EmployeeDTO`.
  - `service`: Modify `EmployeeServiceImpl.createEmployee` to validate and link the requested `JobRole`.
  - `mapper`: Update `EmployeeMapper` to map the `JobRole` entity.
- **Analysis Impact**: Tests how effectively the analyzer propagates type changes across entity, DTO, service logic, and mappers.

---

### Scenario 2: Introduce Task Prerequisites & Dependency Graph (Behavioral & Validation Change)
- **Goal**: Support ordered onboarding tasks where a task cannot be completed until prerequisite tasks are finished.
- **Affected Layers**:
  - `entity`: Add `dependsOnTaskId` (or `@ManyToMany private Set<OnboardingTask> prerequisites;`) to `OnboardingTask`.
  - `dto`: Update `OnboardingTaskDTO` to include prerequisite task IDs.
  - `service`: In `OnboardingTaskServiceImpl.updateTaskStatus`, query prerequisite tasks and throw `InvalidOperationException` if any prerequisites are not `COMPLETED`.
  - `test`: Add unit tests in `WorkflowOrchestratorServiceTest` for blocked task scenarios.
- **Analysis Impact**: Validates intra-entity self-referential graph handling and control-flow branch tracking.

---

### Scenario 3: Decouple Notification Delivery with Spring Events (Decoupling & Event-Driven Invalidation)
- **Goal**: Replace direct synchronous `NotificationService.sendNotification` calls with an asynchronous Spring application event model.
- **Affected Layers**:
  - `event`: Introduce `EmployeeRegisteredEvent`, `OnboardingCompletedEvent`, `TaskOverdueEvent` records.
  - `service`: Replace `NotificationService` injections in `EmployeeServiceImpl` and `WorkflowOrchestratorServiceImpl` with `ApplicationEventPublisher`.
  - `listener`: Create `NotificationEventListener` annotated with `@TransactionalEventListener` or `@Async`.
- **Analysis Impact**: Evaluates semantic chunking when coupling shifts from direct compile-time method invocations to pub-sub event architectures.

---

### Scenario 4: Soft-Delete with JPA `@SQLDelete` & `@SQLRestriction` (Data Tier Behavior)
- **Goal**: Replace hard deletion in `Employee` and `Department` with soft-delete semantics (`deleted_at` timestamp / `deleted` flag).
- **Affected Layers**:
  - `common`: Add `deleted` boolean to `BaseEntity`.
  - `entity`: Add Hibernate annotations `@SQLDelete` and `@SQLRestriction("deleted = false")` on `Department` and `Employee`.
  - `service`: Change `deleteDepartment` in `DepartmentServiceImpl` to trigger soft-deletion, and add restore operations.
- **Analysis Impact**: Tests detection of ORM framework annotation-driven behavior without explicit SQL changes.

---

### Scenario 5: Dynamic Checklist Rules Engine (Extensible Strategy Pattern)
- **Goal**: Replace hard-coded department switch-case logic in `TaskAssignmentServiceImpl` with a plugin/strategy pattern (`TaskRuleStrategy`).
- **Affected Layers**:
  - `service`: Create `TaskRuleStrategy` interface with `boolean supports(Employee emp)` and `List<OnboardingTask> createTasks(Employee emp, Onboarding onboarding)`.
  - `service/strategy`: Implement `EngineeringTaskStrategy`, `SalesTaskStrategy`, `DefaultTaskStrategy`.
  - `service/impl`: Refactor `TaskAssignmentServiceImpl` to inject `List<TaskRuleStrategy>` and iterate.
- **Analysis Impact**: Assesses analyzer ability to identify polymorphism, strategy registration, and dynamic runtime dispatch.

---

### Scenario 6: Pagination and Specification-based Filter Querying (API & Data Interface Upgrade)
- **Goal**: Upgrade `EmployeeController.getAllEmployees` from unpaginated list to a paginated endpoint supporting dynamic criteria (filtering by department, status, hire date range).
- **Affected Layers**:
  - `repository`: Extend `JpaSpecificationExecutor<Employee>` in `EmployeeRepository`.
  - `service`: Update `EmployeeService.getAllEmployees` to accept `Pageable` and filter criteria, returning `Page<EmployeeDTO>`.
  - `controller`: Update `EmployeeController.getAllEmployees` parameters to accept pagination and query parameters.
- **Analysis Impact**: Tests API contract versioning and method signature propagation across Controller → Service → Repository boundaries.
