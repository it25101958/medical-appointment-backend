---
name: 'User Story'
about: 'Template for creating a new user story'
title: 'User Story: [Brief Description of the Story]'
labels: ['user story', 'feature']
assignees: ''

---

### User Story
**As a** [type of user],  
**I want to** [do something],  
**So that** [I can achieve a goal or benefit].

---

### Assumptions
- [Assumption 1: What assumptions are you making to proceed with this user story?]
- [Assumption 2: Any constraints or limitations assumed?]

---

### Acceptance Criteria
- **Given** [a condition],  
  **When** [an action is performed],  
  **Then** [an expected result happens].

For example:

1. **Given** the user has registered,  
   **When** they log in with valid credentials,  
   **Then** they should be redirected to their dashboard.
   
2. **Given** a user wants to create an appointment,  
   **When** they enter valid details for the appointment,  
   **Then** the appointment should be saved in the system.

---

### Additional Information
- [Add any additional details, design considerations, or references that might be useful for this user story.]

---

### Example User Story:

**As a** **patient**,  
**I want to** **create an appointment with a doctor**,  
**So that** **I can schedule a consultation**.

---

### Assumptions
- The user must be logged in to book an appointment.
- The system will only allow appointments during business hours.

---

### Acceptance Criteria
- **Given** the patient is logged in,  
  **When** they fill out the appointment form and submit,  
  **Then** an appointment should be created and confirmed.

- **Given** the patient chooses a doctor and time slot,  
  **When** the appointment is confirmed,  
  **Then** the patient should receive a confirmation email.

---

### How to Use This Template:
1. **Title**: Provide a concise title for the user story, e.g., "User Story: Create Appointment."
2. **User Story**: Follow the standard **As a [user], I want to [action], so that [outcome]** format.
3. **Assumptions**: List any assumptions or constraints for the task, such as pre-existing conditions or expected environment.
4. **Acceptance Criteria**: Define the conditions that must be met for the story to be considered complete. This is typically written in the **Given-When-Then** format.
5. **Additional Information**: Include any extra details or references (e.g., mockups, links to external documents).

---

### Example of how this would look in GitHub:

**Title**: User Story: Create Appointment

**User Story**:
As a **patient**,  
I want to **create an appointment with a doctor**,  
So that **I can schedule a consultation**.

**Assumptions**:
- The user must be logged in to book an appointment.
- The system will only allow appointments during business hours.

**Acceptance Criteria**:
1. **Given** the patient is logged in,  
   **When** they fill out the appointment form and submit,  
   **Then** an appointment should be created and confirmed.

2. **Given** the patient chooses a doctor and time slot,  
   **When** the appointment is confirmed,  
   **Then** the patient should receive a confirmation email.
