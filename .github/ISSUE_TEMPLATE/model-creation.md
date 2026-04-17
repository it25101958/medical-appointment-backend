### Model: [Name]

**Architecture**
* Type: [Shared PK | Standalone | Bridge]
* Relationships: [e.g., Doctor to Patient]
* Fetch Strategy: Lazy

**Fields and Validation**

| Field | Data Type | Constraints / Validation |
| :--- | :--- | :--- |
| id | Integer | @Id / @MapsId (if applicable) |
| [Field] | [Type] | @NotNull, @Size, @Pattern |
| [Enum] | Enum | @Enumerated(EnumType.STRING) |

**Technical Requirements**
* Timestamps: CreatedAt and UpdatedAt (Hibernate)
* Annotations: Lombok @Getter, @Setter, @NoArgsConstructor
* Compiler Fix: Manual getValue() for Enums (Java 25)

**Acceptance Criteria**
* JPA column lengths match validation constraints
* Entity compiles successfully via `mvn clean compile`
* No circular dependencies in relationships
