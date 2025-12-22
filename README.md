# E-Commerce Microservices Application

[![Status](https://img.shields.io/badge/status-development-orange)](https://github.com)
[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)](https://spring.io/projects/spring-boot)
[![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2023.0.1-blue)](https://spring.io/projects/spring-cloud)

A modern, scalable e-commerce microservices application built with Spring Boot and Spring Cloud. This project demonstrates microservices architecture patterns including service discovery, inter-service communication, and distributed system design.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [API Documentation](#api-documentation)
- [Running the Application](#running-the-application)
- [Development Status](#development-status)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## 🎯 Overview

This e-commerce application is built using a microservices architecture pattern, where each service handles a specific business domain. The system consists of multiple independent services that communicate with each other through REST APIs and service discovery mechanisms.

### Key Highlights

- **Microservices Architecture**: Modular, independently deployable services
- **Service Discovery**: Netflix Eureka for service registration and discovery
- **Multi-Database Support**: PostgreSQL for relational data, MongoDB for document storage
- **Containerized Deployment**: Docker and Docker Compose for easy deployment
- **Observability**: Loki integration for centralized logging
- **RESTful APIs**: Clean, RESTful API design following industry best practices

## ✨ Features

### User Service
- User registration and management
- User profile operations (CRUD)
- MongoDB-based document storage

### Product Service
- Product catalog management
- Product search functionality
- Inventory management
- PostgreSQL-based relational storage

### Order Service
- Shopping cart management
- Order creation and processing
- Integration with User and Product services
- PostgreSQL-based order storage

### Service Discovery
- Netflix Eureka server for service registration
- Automatic service discovery and load balancing
- Health monitoring and service status tracking

### Observability
- Centralized logging with Loki
- Grafana Alloy for log aggregation
- Structured logging across all services

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      Client Applications                      │
└───────────────────────┬─────────────────────────────────────┘
                        │
        ┌───────────────┴───────────────┐
        │                               │
┌───────▼────────┐            ┌────────▼────────┐
│  Eureka Server │            │   API Gateway   │
│   (Port 8761)  │            │   (Future)      │
└───────┬────────┘            └─────────────────┘
        │
        ├──────────────┬───────────────┬──────────────┐
        │              │               │              │
┌───────▼──────┐ ┌─────▼──────┐ ┌─────▼──────┐ ┌────▼──────┐
│ User Service │ │Product Svc │ │Order Svc   │ │  Others   │
│  (Port 8082) │ │(Port 8081) │ │(Port 8083) │ │           │
└───────┬──────┘ └─────┬──────┘ └─────┬──────┘ └───────────┘
        │              │               │
        │              │               │
┌───────▼──────┐ ┌─────▼──────┐ ┌─────▼──────┐
│   MongoDB    │ │ PostgreSQL │ │ PostgreSQL │
│  (Port 27017)│ │(Port 5432) │ │(Port 5432) │
└──────────────┘ └────────────┘ └────────────┘
```

### Service Communication Flow

1. **Service Registration**: All microservices register themselves with Eureka Server on startup
2. **Service Discovery**: Services discover each other through Eureka using service names
3. **Inter-Service Communication**: REST-based communication using service names (e.g., `user-service`, `product-service`)
4. **Database Isolation**: Each service maintains its own database for data isolation

## 🛠️ Technology Stack

### Backend
- **Java 21**: Modern Java features and performance improvements
- **Spring Boot 3.2.5**: Application framework
- **Spring Cloud 2023.0.1**: Microservices infrastructure
- **Spring Data JPA**: Database persistence for PostgreSQL services
- **Spring Data MongoDB**: Document database persistence
- **Netflix Eureka**: Service discovery and registration
- **Lombok**: Boilerplate code reduction

### Databases
- **PostgreSQL 15**: Relational database for Product and Order services
- **MongoDB 7**: Document database for User service

### DevOps & Tools
- **Docker & Docker Compose**: Containerization and orchestration
- **Maven**: Build and dependency management
- **Grafana Loki**: Log aggregation system
- **Grafana Alloy**: Log collection agent
- **pgAdmin**: PostgreSQL administration tool

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK) 21** or higher
- **Maven 3.6+** for building the project
- **Docker Desktop** (for containerized deployment) or
- **PostgreSQL 15+** and **MongoDB 7+** (for local development)
- **Git** for version control
- **IDE** (IntelliJ IDEA, Eclipse, or VS Code recommended)

## 🚀 Getting Started

### Clone the Repository

```bash
git clone <repository-url>
cd ecom_app_microservice
```

### Local Development Setup

#### 1. Start Infrastructure Services

Start the databases and Eureka server using Docker Compose:

```bash
docker-compose up -d postgres mongodb eureka-server pgadmin
```

This will start:
- PostgreSQL on port `5432`
- MongoDB on port `27017`
- Eureka Server on port `8761`
- pgAdmin on port `5050`

#### 2. Verify Database Setup

The `init-databases.sql` script automatically creates the required databases:
- `product` database for Product Service
- `order` database for Order Service

#### 3. Build the Project

Build all microservices:

```bash
# Build all services
mvn clean install

# Or build individual services
cd user && mvn clean install
cd ../product && mvn clean install
cd ../order && mvn clean install
cd ../eureka-server && mvn clean install
```

#### 4. Run Services Locally

Start services in the following order:

**Terminal 1 - Eureka Server:**
```bash
cd eureka-server
mvn spring-boot:run
```

**Terminal 2 - User Service:**
```bash
cd user
mvn spring-boot:run
```

**Terminal 3 - Product Service:**
```bash
cd product
mvn spring-boot:run
```

**Terminal 4 - Order Service:**
```bash
cd order
mvn spring-boot:run
```

### Docker Deployment

To run all services using Docker:

```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

**Note**: Comment out service definitions in `docker-compose.yml` if you prefer to run services locally.

### Access Points

Once all services are running:

- **Eureka Dashboard**: http://localhost:8761
- **User Service**: http://localhost:8082
- **Product Service**: http://localhost:8081
- **Order Service**: http://localhost:8083
- **pgAdmin**: http://localhost:5050 (admin@admin.com / admin)

## 📁 Project Structure

```
ecom_app_microservice/
├── eureka-server/          # Service discovery server
│   ├── src/
│   └── Dockerfile
├── user/                   # User microservice
│   ├── src/
│   │   ├── main/java/      # Java source code
│   │   └── main/resources/ # Configuration files
│   ├── logs/               # Application logs
│   └── pom.xml
├── product/                # Product microservice
│   ├── src/
│   ├── logs/
│   └── pom.xml
├── order/                  # Order microservice
│   ├── src/
│   ├── logs/
│   └── pom.xml
├── evaluate-loki/          # Observability setup
│   ├── docker-compose.yaml
│   ├── loki-config.yaml
│   └── alloy-local-config.yaml
├── docker-compose.yml      # Main Docker Compose file
├── init-databases.sql      # Database initialization script
└── README.md               # This file
```

## 📚 API Documentation

### User Service (Port 8082)

**Base URL**: `http://localhost:8082/api/users`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| POST | `/api/users` | Create a new user |
| PUT | `/api/users/{id}` | Update user |

**Example Request:**
```bash
# Create User
curl -X POST http://localhost:8082/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com"
  }'
```

### Product Service (Port 8081)

**Base URL**: `http://localhost:8081/api/products`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | Get all products |
| GET | `/api/products/search?keyword={keyword}` | Search products |
| POST | `/api/products` | Create a new product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Delete product |

**Example Request:**
```bash
# Create Product
curl -X POST http://localhost:8081/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "stock": 10
  }'
```

### Order Service (Port 8083)

**Base URL**: `http://localhost:8083`

| Method | Endpoint | Description | Headers |
|--------|----------|-------------|---------|
| POST | `/api/cart` | Add item to cart | `X-User-Id: {userId}` |
| GET | `/api/cart` | Get user's cart | `X-User-Id: {userId}` |
| DELETE | `/api/cart/items/{productId}` | Remove item from cart | `X-User-Id: {userId}` |
| POST | `/api/orders` | Create order from cart | `X-User-Id: {userId}` |

**Example Request:**
```bash
# Add to Cart
curl -X POST http://localhost:8083/api/cart \
  -H "Content-Type: application/json" \
  -H "X-User-Id: user123" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'

# Create Order
curl -X POST http://localhost:8083/api/orders \
  -H "X-User-Id: user123"
```

## 🔄 Running the Application

### Development Mode

1. **Start Infrastructure**:
   ```bash
   docker-compose up -d postgres mongodb eureka-server
   ```

2. **Start Services** (in separate terminals):
   ```bash
   # Terminal 1
   cd eureka-server && mvn spring-boot:run
   
   # Terminal 2
   cd user && mvn spring-boot:run
   
   # Terminal 3
   cd product && mvn spring-boot:run
   
   # Terminal 4
   cd order && mvn spring-boot:run
   ```

3. **Verify Services**:
   - Check Eureka Dashboard: http://localhost:8761
   - All services should appear as "UP"

### Production Mode (Docker)

```bash
# Start all services
docker-compose up -d

# Check service status
docker-compose ps

# View logs
docker-compose logs -f [service-name]
```

### Observability Setup

To enable centralized logging with Loki:

```bash
cd evaluate-loki
docker-compose up -d
```

This will start:
- Loki (log aggregation)
- Grafana Alloy (log collection)
- Nginx Gateway (port 3100)

## 🚧 Development Status

**Current Status**: 🟡 **In Development**

This project is currently under active development. The following features are implemented:

✅ **Completed:**
- Basic microservices architecture
- Service discovery with Eureka
- User, Product, and Order services
- RESTful API endpoints
- Database integration (PostgreSQL & MongoDB)
- Docker containerization
- Basic logging infrastructure

🔄 **In Progress:**
- API Gateway implementation
- Enhanced error handling
- Comprehensive testing
- API documentation (OpenAPI/Swagger)
- Security implementation (JWT, OAuth2)

📋 **Planned:**
- Payment service integration
- Notification service
- Caching layer (Redis)
- Message queue (RabbitMQ/Kafka)
- Monitoring and metrics (Prometheus, Grafana)
- CI/CD pipeline
- Kubernetes deployment manifests

## 🤝 Contributing

Contributions are welcome! This project is in active development, and we appreciate any feedback or contributions.

### How to Contribute

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow Java coding conventions
- Write meaningful commit messages
- Add tests for new features
- Update documentation as needed
- Ensure all services start without errors

## 📄 License

This project is currently unlicensed. All rights reserved.

## 📧 Contact

**Aritra Das**

- **Email**: aritradas75@gmail.com
- **Project**: E-Commerce Microservices Application

---

**Note**: This project is in active development. Some features may be incomplete or subject to change. For questions or support, please contact the project maintainer.

