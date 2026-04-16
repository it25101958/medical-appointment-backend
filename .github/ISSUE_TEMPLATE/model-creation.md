---
name: 'Create Model Class'
about: 'Template for creating a new model class'
title: 'Create [Model] Model Class'
labels: ['feature', 'model', 'backend']
assignees: ''

---

### Description
Create the **[Model]** model class to represent the **[Entity Description]** in the database.

**Fields:**
- **[Field1]**: [Description of the field]. [Any validation requirements, e.g., `@NotNull`, `@Size`, `@Pattern`, etc.]
- **[Field2]**: [Description of the field]. [Any validation requirements, e.g., `@Email`, `@Min`, etc.]
- **[Field3]**: [Description of the field]. [Any validation requirements, e.g., `@Past`, `@Future`, etc.]
- **[Field4]**: [Description of the field]. [Any validation requirements, e.g., `@Length`, `@NotEmpty`, etc.]
- **[Field5]**: [Description of the field]. [Any validation requirements, e.g., `@Column`, `@ManyToOne`, etc.]
  
### Validation:
- Add **appropriate validation annotations** to fields like **email**, **phone**, **date**, and others based on the entity’s requirements.
- For the **[field]**, ensure it defaults to **[default value]** if not provided.

### Acceptance Criteria:
- **Given** the new [Model] entity is created,  
  **When** the model data is persisted,  
  **Then** the necessary fields like **[field1]** and **[field2]** should be saved properly.
  
- **Given** a [Model] is created without specifying certain fields,  
  **When** the entity is saved,  
  **Then** the missing fields should default to **[default value]**.

### Additional Information:
- Ensure the **[Field1]** is **unique** (if required) and validated properly.
- If relationships are required, ensure they are defined correctly (e.g., **`@ManyToOne`**, **`@OneToMany`**).
- Ensure **timestamps** like **created_at** and **updated_at** are managed with **`@CreationTimestamp`** and **`@UpdateTimestamp`**.

---

### Labels:
- **`backend`**
- **`feature`**
- **`model`**
- **`high-priority`**

### Assignees:
- Assign this task to the developer responsible for creating the [Model] class (e.g., "John Doe").
