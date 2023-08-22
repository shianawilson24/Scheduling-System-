This project involves the creation of a GUI-based Java application that simulates real-world software development tasks. The main objective is to develop a scheduling desktop application for a global consulting organization. The application must interact with a MySQL database and meet specific business requirements outlined by the client.

Key tasks within the project include:

Internationalization and GUI Development:

Designing a login form that accepts usernames and passwords, displaying appropriate error messages.
Determining the user's location (ZoneId) and showing it on the login form.
Implementing language translation based on the user's computer language setting.
Automatic translation of error messages to English or French, aligned with the user's language preference.
Customer Record Management:

Creating functionalities for adding, updating, and deleting customer records.
Collecting customer data, such as name, address, postal code, and phone number using text fields.
Generating auto-generated Customer IDs and utilizing combo boxes for division and country selection.
Allowing viewing and updating of customer information with data autopopulated for edits.
Appointment Handling:

Implementing capabilities for adding, updating, and deleting appointments.
Auto-generating and disabling Appointment IDs throughout the application.
Recording appointment details, including title, description, location, contact, type, start/end date and time, Customer/User IDs.
Utilizing a TableView to display appointments, with consideration for local time zone display.
Appointment Time Management:

Storing appointment times in UTC and displaying them according to the user's local time zone.
Verifying local time against ET business hours before storing in the database.
Input Validation and Error Handling:

Incorporating input validation to prevent scheduling outside business hours, overlapping appointments, and incorrect login.
Providing customized error messages for each type of input error.
Upcoming Appointment Alerts:

Creating alerts for appointments within 15 minutes of user login.
Ensuring robustness of alerts to trigger based on local computer time.
Reporting:

Generating reports showcasing customer appointments by type and month.
Crafting schedules for each contact, encompassing appointment details.
Developing an additional customized report as specified.
Lambda Expressions:

Integrating two lambda expressions to optimize code efficiency.
User Activity Tracking:

Recording user log-in attempts, dates, time stamps, and success status in a login_activity.txt file
