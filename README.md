# Online Book Store
####
### Glad to introduce the Online Book Store application 
This application is designed to power an online book store, allowing users to browse, search for, and purchase books. It also provides administrative functionalities for managing books, categories, and orders.
####
I appreciate your time, so you can watch the [project presentation](#content).

## Content

1. [Domain Models (Entities)](#domain-models-entities)
2. [Technologies and Tools Used](#technologies-and-tools-used)
3. [Application API](#application-api)
4. [Security](#security)
5. [Running the Application without Docker](#to-run-locally-without-docker)
6. [Running the Application with Docker](#to-run-using-docker)
7. [Instructions for Importing a Test Data Set into Postman](#instructions-for-importing-data)
8. [Accessing Swagger Documentation in a Web Browser](#accessing-swagger-documentation-in-a-web-browser)
9. [Contributing](#contributing)

## Domain Models (Entities)

 - Book: Represents a book available in the store.
 - User: Contains information about the registered user including their authentication details and personal information.
 - Role: Represents the role of a user in the system, for example, admin or user.
 - Category: Represents a category that a book can belong to.
 - Role: Represents the role of a user in the system, for example, admin or user.
 - ShoppingCart: Represents a category that book can belong to.
 - CartItem: Represents an item in a user's shopping cart.
 - Order: Represents an order placed by a user.
 - OrderItem: Represents an item in a user's order.

## Technologies and Tools Used

- **Spring Boot:** The project is built using the Spring Boot framework, providing a robust and scalable backend.
- **Spring Security:** Authentication and authorization are managed using Spring Security to ensure secure access to endpoints.
- **Spring Data JPA:** Spring Data JPA simplifies database operations and enables easy interaction with the database.
- **Swagger:** Swagger is integrated to provide interactive API documentation and testing capabilities.
- **Mapstruct:** Mapstruct is used for object mapping between DTOs and entities.
- **MySQL:** MySQL is used as the database management system.
- **Liquibase:** Liquibase is employed for database version control and management.
- **Docker:** Docker is used for containerization of the application and database.

## Application API
####
[Back to application content](#content)

1. [Authentication Controller](#authentication-controller)
2. [Book Controller](#book-controller)
3. [Category Controller](#category-controller)
4. [Shopping Cart Controller](#shoppingcart-controller)
5. [Order Controller](#order-controller)
6. [Order Item Controller](#orderitem-controller)


## Authentication Controller
The Authentication Controller is responsible for user authentication and registration functionalities, enabling users to
interact with the system and utilize its functionalities.
- [Back to application content](#content)
####

####

### User Registration:
To register as a new user and gain access to the system's features, please follow these steps:

- **POST `/api/auth/register:`** with your registration details.

   - Example request body:
   ```json
   {
     "email": "john.doe@example.com",
     "password": "securePassword123",
     "repeatPassword": "securePassword123",
     "firstName": "John",
     "lastName": "Doe",
     "shippingAddress": "123 Main St, City, Country"
   }
  ```
  - Example of response body:
  ```json
  {
     "id": 1,
     "email": "john.doe@example.com",
     "firstName": "John",
     "lastName": "Doe",
     "shippingAddress": "123 Main St, City, Country"
  }
    ```
###

### User Login:
If you are already a registered user, you can log in to access your account and perform various actions. To log in, please follow these steps:

- **POST `/api/auth/login`** with your login credentials. 

   - Example request body:
   ```json
   {
     "email": "john.doe@example.com",
     "password": "securePassword123"
   }
   ```
  Upon successful login, you will receive an authentication token that should be included in your subsequent requests for secure access.
    - Example response body:
    ```json
   {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
   }
    ```

## Book Controller
The Book Controller provides access to book-related functionalities, allowing users to retrieve and manage book information within the online store.
- [Back to Application API content](#application-api)
- [Back to application content](#content)
####
- **GET /api/books:** Retrieve the book catalog.
    - Example Response Body:

      ```json
      [
        {
          "id": 1,
          "title": "Sample Book 1",
          "author": "Author A",
          "isbn": "9781234567897",
          "price": 19.99,
          "description": "This is a sample book description.",
          "coverImage": "http://example.com/cover1.jpg"
        },
        {
          "id": 2,
          "title": "Sample Book 2",
          "author": "Author B",
          "isbn": "9789876543210",
          "price": 24.99,
          "description": "Another sample book description.",
          "coverImage": "http://example.com/cover2.jpg"
        }
      ]
      ```

- **GET /api/books/{id}:** Retrieve details of a specific book.
    - Example Response Body:

      ```json
      {
        "id": 1,
        "title": "Sample Book 1",
        "author": "Author A",
        "isbn": "9781234567897",
        "price": 19.99,
        "description": "This is a sample book description.",
        "coverImage": "http://example.com/cover1.jpg"
      }
      ```

- **POST /api/books:** Create a new book.
    - Example Request Body:

      ```json
      {
        "title": "Sample Book 3",
        "author": "Author C",
        "isbn": "9781122334455",
        "price": 29.99,
        "description": "Yet another sample book description.",
        "coverImage": "http://example.com/cover3.jpg"
      }
      ```

## Category Controller
The Category Controller enables users to view and manage book categories, making it easier to find books of interest.
- [Back to Application API content](#application-api)
- [Back to application content](#content)
####

- **GET /api/categories:** Retrieve all categories.
- **GET /api/categories/{id}/books:** Retrieve books by a specific category.
- **POST /api/categories:** Create a new category.
    - Example Request Body:

      ```json
      {
        "name": "Fiction",
        "description": "Fiction books"
      }
      ```

- **GET /api/categories/{id}:** Retrieve details of a specific category.
- **PUT /api/categories/{id}:** Update a specific category.
    - Example Request Body:

      ```json
      {
        "name": "Fiction",
        "description": "Fiction books"
      }
      ```

- **DELETE /api/categories/{id}:** Delete a specific category.

## ShoppingCart Controller
The Shopping Cart Controller facilitates shopping cart operations, including adding and removing items for a seamless shopping experience.
- [Back to Application API content](#application-api)
- [Back to application content](#content)
####

- **GET /api/cart:** Retrieve the user's shopping cart.
    - Example Response Body:

      ```json
      {
        "id": 1,
        "userId": 123,
        "cartItems": [
          {
            "id": 1,
            "bookId": 789,
            "quantity": 2
          },
          {
            "id": 2,
            "bookId": 790,
            "quantity": 1
          }
        ]
      }
      ```

- **POST /api/cart:** Add a book to the shopping cart.
    - Example Request Body:

      ```json
      {
        "bookId": 789,
        "quantity": 2
      }
      ```

- **PUT /api/cart/cart-items/{cartItemId}:** Update the quantity of a book in the shopping cart.
    - Example Request Body:

      ```json
      {
        "quantity": 3
      }
      ```

- **DELETE /api/cart/cart-items/{cartItemId}:** Remove a book from the shopping cart.


## Order Controller
The Order Controller allows users to place orders, view their order history, and enables administrators to manage the status of users' orders.
- [Back to Application API content](#application-api)
- [Back to application content](#content)
####

- **POST /api/orders:** Place an order.
    - Example Request Body:

      ```json
      {
        "shippingAddress": "Kyiv, Shevchenko ave, 1"
      }
      ```

- **GET /api/orders:** Retrieve the user's order history.
    - Example Response Body:

      ```json
      [
        {
          "id": 101,
          "userId": 456,
          "orderItems": [
            {
              "id": 1,
              "bookId": 789,
              "quantity": 2
            },
            {
              "id": 2,
              "bookId": 790,
              "quantity": 1
            }
          ],
          "orderDate": "2023-07-25T10:15:30",
          "total": 29.98,
          "status": "COMPLETED"
        },
        {
          "id": 102,
          "userId": 456,
          "orderItems": [
            {
              "id": 3,
              "bookId": 791,
              "quantity": 1
            }
          ],
          "orderDate": "2023-07-23T15:20:45",
          "total": 14.99,
          "status": "PENDING"
        }
      ]
      ```

## OrderItem Controller
The OrderItem Controller provides access to order item information, allowing users to view details of their purchases within an order.
- [Back to Application API content](#application-api)
- [Back to application content](#content)
####

- **GET /api/orders/{orderId}/items:** Retrieve all OrderItems for a specific order (Nested under Order).
    - Example Response Body:

      ```json 
      [
        {
          "id": 1,
          "bookId": 789,
          "quantity": 2
        },
        {
          "id": 2,
          "bookId": 790,
          "quantity": 1
        }
      ]
      ```

- **GET /api/orders/{orderId}/items/{itemId}:** Retrieve a specific OrderItem within an order.
    - Example Response Body:

      ```json
      {
        "id": 2,
        "bookId": 790,
        "quantity": 1
      }
      ```

- **PATCH /api/orders/{id}:** Update order status.
    - Example Request Body:

      ```json
      {
        "status": "DELIVERED"
      }
      ```

## Security
In this section, we'll cover the security requirements and features of Online Book Store Application.
- [Back to application content](#content)
####

### Security Features and Considerations

- **User Authentication:** Users can register and log in to the system to access their accounts and perform various actions.

- **Role-Based Access Control:** Different endpoints are accessible based on user roles. Users with the role USER have limited access compared to users with the role ADMIN.

- **Password Validation:** Passwords are securely hashed and validated for registration and login.

- **JWT Support:** JSON Web Tokens (JWT) are used for authentication and securing API endpoints.

- **Data Validation:** Data validation is implemented in the new DTO classes to ensure the integrity of user inputs.

- **Exception Handling:** Exception handling, including RegistrationException, is implemented to provide meaningful error responses.

- **Field Validation:** The `FieldMatch` annotation is used to check that the password and repeatPassword fields in the UserRegistrationRequestDto are the same.

- **Global Exception Handling:** GlobalExceptionHandler is implemented to handle exceptions gracefully and provide consistent error responses.


### Available endpoints for NOT-AUTHENTICATED Users

- **POST `/api/auth/register:`** (Registration new user)
- **POST `/api/auth/login`** (Login existing user)

### Available endpoints for Users with Role USER

- **GET:** `/api/books` (Retrieve book catalog)
- **GET:** `/api/books/{id}` (Retrieve book details)
- **GET:** `/api/categories` (Retrieve all categories)
- **GET:** `/api/categories/{id}` (Retrieve a specific category by its ID)
- **GET:** `/api/categories/{id}/books` (Retrieve books by a specific category)
- **GET:** `/api/cart` (Retrieve user's shopping cart)
- **POST:** `/api/cart` (Add an item to the shopping cart)
- **PUT:** `/api/cart/cart-items/{cartItemId}` (Update quantity of an item in the shopping cart)
- **DELETE:** `/api/cart/cart-items/{cartItemId}` (Remove an item from the shopping cart)
- **GET:** `/api/orders` (Retrieve user's order history)
- **POST:** `/api/orders` (Place an order)
- **GET:** `/api/orders/{orderId}/items` (Retrieve all OrderItems for a specific order)
- **GET:** `/api/orders/{orderId}/items/{itemId}` (Retrieve a specific OrderItem within an order)

### Available endpoints for Users with Role ADMIN

- **POST:** `/api/books` (Create a new book)
- **PUT:** `/api/books/{id}` (Update book details)
- **DELETE:** `/api/books/{id}` (Remove a book from the catalog)
- **POST:** `/api/categories` (Create a new category)
- **PUT:** `/api/categories/{id}` (Update category details)
- **DELETE:** `/api/categories/{id}` (Delete a category)
- **PATCH:** `/api/orders/{id}` (Update order status)

## Running the Application 

In this section, you will find out how the process of running application.
- [Back to application content](#content)

####
### To run locally without Docker:

To run the application without Docker, follow these steps:

- ***Clone the project repository from the version control system of your choice (e.g., Git) to your local machine.***
   ```bash
    git clone git@github.com:DmytroV95/online-book-store.git
    ```
  or
    ```bash
    git clone https://github.com/DmytroV95/online-book-store.git
    ``` 
- ***Configure Application Properties:***
 Navigate to the src/main/resources directory and locate the application.properties file. Update this file with the necessary configuration, such as the database connection details and other environment-specific settings.
####
- ***Database Setup:***
  Ensure you have a MySQL database server installed and running. Create a database for your application and configure its details in the application.properties file.
####
  - ***Build the Application:***
    Open a terminal in the root directory of the project and use Maven to build the application.
      ```bash
      mvn clean install
    ```
####
- ***Run the Application:***
  Once the build is successful, you can run the application using the following command:
    ```bash
    java -jar target/your-application.jar
  ```
  Replace your-application.jar with the actual name of the generated JAR file.
####
  - ***Access the Application:***
  After the application is up and running, open your web browser and access the application at the specified endpoints. 
  By default, it might be available at http://localhost:8080 unless you've configured a different port in the application.properties file.
####
- [Use this instructions for testing an application](#instructions-for-importing-a-test-data-set-into-postman)

####
### To run using Docker:
####
- ***Docker Setup:*** Ensure that you have Docker installed on your system. You can download and install Docker from the official website: Docker Installation.
####
- ***Docker Compose:*** The application is configured to use Docker Compose for orchestrating containers. Make sure you have Docker Compose installed as well. You can check if it's installed by running:
    ```bash
    docker-compose --version
    ```
- ***Environment Variables:*** Create the ***.env*** file  in project root directory with the necessary environment variables. These variables should include your database connection details and any secret keys required by application.
   ***Use .env.sample file from application root directory as a sample data to connection with docker container with your custom properties.***
####
- ***Build Docker Image:*** In your project root directory, open a terminal and run the following command to build a Docker image of application:
    ```bash
    docker build -t your-image-name .
    ```
####
- ***Start Docker Containers:*** Once the image is built, you can start your Docker containers using Docker Compose by running:
    ```bash
    docker-compose up
    ```
    This command will start the application and any required services (e.g., the database) defined in your docker-compose.yml file.
####
- ***Access the Application:*** After the containers are up and running, you can access your Spring Boot application at the specified endpoints.
####
- [Use this instructions for testing an application](#instructions-for-importing-a-test-data-set-into-postman)


# Instructions for Importing a Test Data Set into Postman
In this section, instructions are provided for importing sample data into application, which can be particularly useful for testing and development purposes.
- [Back to application content](#content)

### Importing Sample Data as JSON in Postman

To import the [Sample Data as JSON](Sample%20JSON%20data) into Postman for testing, follow these steps:

1. Open Postman.

2. Click on the "Import" button in the top left corner.

3. In the "Import" dialog, select the "File" tab.

4. Click on the "Upload Files" button and select the "Sample Data JSON" file from root project directory.

5. Click the "Import" button to add the collection and environment variables to Postman.

### Testing Oline Book Store Application

Now, when you have imported the data, you can use it to test a functional application.
You can create a new user or use an existing one as [admin](#logging-in-as-a-registered-user-role-admin) or [user](#logging-in-as-a-registered-user-role-user). Here are the steps:

### Registering a New User

1. In Postman, select the "Sample Data JSON" collection on the left sidebar.

2. Select the "Register User" request within the collection.

3. Click the "Send" button to register a new user. The request will use the sample data JSON for user registration.

### Logging in as a Registered User (Role USER)

- Use this request body to register as user:
   ```json
   {
     "email": "user@example.com",
     "password": "user123"
   }
   ```

### Logging in as a Registered User (Role ADMIN)

- Use this request body to register as admin:
   ```json
   {
     "email": "admin@example.com",
     "password": "admin123"
   }
   ```
####
## Accessing Swagger Documentation in a Web Browser
In this section, instructions are provided for accessing the Swagger documentation directly from your web browser, allowing you to explore and interact with the API endpoints easily.
- [Back to application content](#content)
### Follow these steps to access the Swagger documentation and explore API endpoints using a web browser:

 - Start the Application
 - Launch your preferred web browser (e.g., Chrome, Firefox, or Edge)
 - In the browser's address bar, enter the URL for the Swagger documentation
   - If the application is running:
      - locally: http://localhost:8080/api/swagger-ui/index.html
      - using Docker: http://localhost:8088/api/swagger-ui/index.html

### ***Authentication and Authorization in Swagger UI:***
To gain access, use the [username and password](#logging-in-as-a-registered-user-role-admin) that you use to log in to the system.
If API endpoints require authorization, just make log in operation to get the authentication token to authenticate yourself.
####
### Explore Endpoints:

Once you access the Swagger UI, you will be presented with a user-friendly interface. Here, you can explore a list of available API endpoints, including their descriptions and supported HTTP methods (e.g., GET, POST, PUT, DELETE).
Swagger UI offers an interactive way to understand, test, and work with ***Online Book Store*** API. Take your time to explore the available endpoints and make test requests as needed.

1. ***Select an Endpoint:***
To get detailed information about a specific endpoint, click on it in the Swagger UI. This will expand the endpoint and display details such as request parameters, request body models (if applicable), and example responses.
####
2. ***Test an Endpoint:***
If you want to test an endpoint interactively, click the "Try it out" button next to the endpoint. This allows you to input parameters, execute requests, and view the responses directly within Swagger UI.
####
3. ***Enjoy Your Exploring!***

####
## Contributing

I welcome contributions from the community! Whether you want to report a bug, suggest an enhancement, or submit code changes, here's how you can get started:
- [Back to application content](#content)
### Reporting Issues

If you come across a bug or have a feature request, please [contact me](https://dmytro.varukha@gmail.com). Make sure to provide a clear and detailed description of the issue or feature request.

### Contributing Code

If you'd like to contribute code to this project, follow these steps:

1. Fork the repository on GitHub.
2. Clone your forked repository to your local machine.
   ```bash
   git clone https://github.com/your-username/your-project.git
3. Create a new branch for your feature or bug fix.
    ```bash
    git checkout -b feature-branch-name
4. Make your changes and commit them.
    ```bash
   git commit -m "Add new feature" (or "Fix bug" for bug fixes)
5. Push your changes to your forked repository on GitHub.
    ```bash
   git push origin feature-branch-name
6. Open a pull request (PR) on the main repository's GitHub page. Be sure to describe your changes in detail and reference the related issue if applicable.

### Code Style
Please follow our code style when contributing. Consistent code style makes it easier to review and maintain the project.

### Code Reviews
All contributions go through a code review process. Feedback and discussions may happen in the pull request. Be prepared to make changes to your code based on the feedback received.

### License
By contributing to this project, you agree to license your contributions under the project's  MIT license.

## Thank you for contributing to this project! Your help is greatly appreciated.

- [Back to application content](#content)
#