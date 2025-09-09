# SkinVibe - E-commerce Platform

A modern e-commerce platform for skincare products built with Spring Boot, MySQL, Thymeleaf, and Tailwind CSS.

## Features

- **User Management**: Registration, login, and profile management
- **Product Catalog**: Browse products by category with search and filtering
- **Shopping Cart**: Add, update, and remove items from cart
- **Order Management**: Complete order placement and tracking
- **Address Management**: Manage shipping and billing addresses
- **Responsive Design**: Modern UI with Tailwind CSS
- **Security**: Spring Security with role-based access control

## Technology Stack

- **Backend**: Spring Boot 3.2.0
- **Database**: MySQL 8.0
- **Frontend**: Thymeleaf templates with Tailwind CSS
- **Security**: Spring Security
- **Build Tool**: Maven
- **Java Version**: 17

## Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher

## Setup Instructions

### 1. Database Setup

1. Install MySQL 8.0
2. Create a database named `skinvibe_db`:
   ```sql
   CREATE DATABASE skinvibe_db;
   ```
3. Create a user for the application:
   ```sql
   CREATE USER 'skinvibe_user'@'localhost' IDENTIFIED BY 'skinvibe_password';
   GRANT ALL PRIVILEGES ON skinvibe_db.* TO 'skinvibe_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

### 2. Application Configuration

Update the database configuration in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/skinvibe_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=skinvibe_user
spring.datasource.password=skinvibe_password
```

### 3. Build and Run

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd skin-vibe
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

4. Access the application at: `http://localhost:8080`

## Default Accounts

The application comes with pre-configured accounts:

- **Admin Account**:
  - Username: `admin`
  - Password: `admin123`
  - Role: ADMIN

- **Test User Account**:
  - Username: `testuser`
  - Password: `user123`
  - Role: USER

## Sample Data

The application automatically creates sample data on first startup:

- 6 product categories (Cleansers, Moisturizers, Serums, etc.)
- 8 sample products with detailed information
- Product images from Unsplash
- Complete product details including ingredients and usage instructions

## Project Structure

```
src/
├── main/
│   ├── java/com/skinvibe/
│   │   ├── config/          # Configuration classes
│   │   ├── controller/      # Web controllers
│   │   ├── model/          # JPA entities
│   │   ├── repository/     # Data repositories
│   │   ├── service/        # Business logic services
│   │   └── SkinVibeApplication.java
│   └── resources/
│       ├── templates/      # Thymeleaf templates
│       └── application.properties
└── test/                   # Test classes
```

## Key Features Explained

### User Authentication
- Secure user registration and login
- Password encryption using BCrypt
- Role-based access control (USER/ADMIN)

### Product Management
- Product catalog with categories
- Search and filtering functionality
- Product details with images and specifications
- Stock management

### Shopping Cart
- Add/remove items from cart
- Update quantities
- Persistent cart for logged-in users
- Cart total calculation

### Order Processing
- Complete checkout process
- Address management
- Order tracking and status updates
- Order history for users

### Responsive Design
- Mobile-first responsive design
- Modern UI with Tailwind CSS
- Intuitive user experience
- Accessible navigation

## API Endpoints

### Public Endpoints
- `GET /` - Home page
- `GET /home` - Home page with products
- `GET /products` - Product listing
- `GET /products/{id}` - Product details
- `GET /search` - Product search
- `GET /auth/login` - Login page
- `GET /auth/register` - Registration page

### Protected Endpoints (Requires Authentication)
- `GET /cart` - Shopping cart
- `POST /cart/add` - Add to cart
- `POST /cart/update` - Update cart item
- `POST /cart/remove` - Remove from cart
- `GET /orders` - Order history
- `GET /orders/checkout` - Checkout page
- `POST /orders/place` - Place order
- `GET /addresses` - Address management
- `POST /addresses/add` - Add address
- `POST /addresses/edit/{id}` - Edit address

## Development

### Running in Development Mode
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Database Schema
The application uses JPA/Hibernate with automatic schema generation. Tables are created automatically on startup.

### Customization
- Modify `application.properties` for configuration changes
- Update templates in `src/main/resources/templates/`
- Add new controllers in `com.skinvibe.controller`
- Extend services in `com.skinvibe.service`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For support and questions, please contact the development team or create an issue in the repository.
