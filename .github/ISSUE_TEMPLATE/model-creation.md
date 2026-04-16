### Description
Create the **[Model Name]** model class to represent the **[Entity/Description]** in the database.

**Fields:**
- **user_id**: Unique identifier for the user (primary key).
- **email**: Email of the user.
- **password_hash**: Securely stored hashed password.
- **role_type**: (int) Role of the user (e.g., 1 = 'patient', 2 = 'doctor').
- **first_name**: User's first name.
- **last_name**: User's last name.
- **phone**: Contact phone number.
- **is_active**: Boolean indicating if the account is active.
- **created_at**: Timestamp of when the user was created.
- **updated_at**: Timestamp of the last update to the user’s details.
