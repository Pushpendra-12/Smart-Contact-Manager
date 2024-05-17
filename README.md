Smart Contact Manager Web Application
Overview
  The Smart Contact Manager is a web application built using Spring Boot, Thymeleaf, MySQL, and Bootstrap. This application allows users to manage their important contacts efficiently. Users can add new contacts, update existing contacts, and delete contacts they no longer need. The application also features a secure login system powered by Spring Security.

Features
  User Authentication: Secure login and registration using Spring Security.
  Contact Management: Add, update, and delete contacts.
  Responsive Design: Built with Bootstrap for a responsive and user-friendly interface.
  Database Integration: Uses MySQL to store user and contact information.
Technologies Used
  Spring Boot: Framework for building the web application.
  Thymeleaf: Templating engine for rendering HTML.
  MySQL: Database for storing user and contact data.
  Bootstrap: Front-end framework for a responsive design.
  Spring Security: Provides authentication and authorization.
Prerequisites
  Java Development Kit (JDK) 8 or higher
  Maven
  MySQL database

Setup Instructions
Step 1: Clone the Repository
git clone https://github.com/yourusername/smart-contact-manager.git
cd smart-contact-manager

Step 2: Configure the Database
Create a MySQL database named smart_contact_manager and update the database configuration in src/main/resources/application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/smart_contact_manager
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update

Step 3: Build the Project
Use Maven to build the project:
mvn clean install

Step 4: Run the Application
Start the Spring Boot application:
mvn spring-boot:run
The application will be accessible at http://localhost:8080.

Using the Application
Registration and Login
Register: Navigate to /register to create a new account.
Login: Navigate to /login to log in with your credentials.
Contact Management
Add Contact: Use the "Add Contact" button to create a new contact.
Update Contact: Click on a contact's "Edit" button to update their information.
Delete Contact: Click on a contact's "Delete" button to remove them from your contact list


Project Structure
src/main/java/com/smartcontactmanager: Contains the main application code.
controller: Handles HTTP requests and mappings.
entity: Defines the JPA entities.
repository: Interfaces for database access.
service: Contains business logic.
config: Configuration classes for Spring Security.
src/main/resources/templates: Thymeleaf templates for the views.
src/main/resources/static: Static resources (CSS, JavaScript).
src/main/resources/application.properties: Configuration properties.

Security Configuration
Spring Security is configured in com.smartcontactmanager.config.SecurityConfig. It secures the login and registration pages, and restricts access to contact management features to authenticated users.


Contact
For any questions or feedback, please contact:

Name: Pushpendra 
Email: pushpendrakashyap560@gmail.com
GitHub: Pushpendra-12
