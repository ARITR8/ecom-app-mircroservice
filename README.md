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
- **API Gateway**: Spring Cloud Gateway for unified entry point and routing
- **Service Discovery**: Netflix Eureka for service registration and discovery
- **Multi-Database Support**: PostgreSQL for relational data, MongoDB for document storage
- **Containerized Deployment**: Docker and Docker Compose for easy deployment
- **Observability**: Loki integration for centralized logging with Grafana Alloy
- **Distributed Tracing**: Zipkin integration for request tracing across services
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

### API Gateway

- Unified entry point for all microservices (Port 8089)
- Dynamic routing to backend services via service discovery
- Request logging and monitoring
- Load balancing across service instances
- Integration with Zipkin for distributed tracing

### Service Discovery

- Netflix Eureka server for service registration
- Automatic service discovery and load balancing
- Health monitoring and service status tracking

### Observability

- Centralized logging with Loki and MinIO storage
- Grafana Alloy for log collection and aggregation
- Structured logging across all services
- Distributed tracing with Zipkin
- Spring Boot Actuator endpoints for monitoring

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      Client Applications                      │
└───────────────────────┬─────────────────────────────────────┘
                        │
                ┌───────▼────────┐
                │  API Gateway   │
                │  (Port 8089)   │
                └───────┬────────┘
                        │
        ┌───────────────┴───────────────┐
        │                               │
┌───────▼────────┐            ┌────────▼────────┐
│  Eureka Server │            │   Zipkin        │
│   (Port 8761)  │            │   (Port 9411)   │
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
                        │
                ┌───────▼────────┐
                │  Loki + Alloy   │
                │  (Port 3100)    │
                └─────────────────┘
```

### Service Communication Flow

1. **Client Request**: All client requests first hit the API Gateway (Port 8089)
2. **Service Registration**: All microservices register themselves with Eureka Server on startup
3. **Service Discovery**: Gateway and services discover each other through Eureka using service names
4. **Request Routing**: Gateway routes requests to appropriate microservices based on path patterns
5. **Inter-Service Communication**: REST-based communication using service names (e.g., `user-service`, `product-service`)
6. **Distributed Tracing**: All requests are traced through Zipkin for observability
7. **Logging**: All service logs are collected by Grafana Alloy and forwarded to Loki
8. **Database Isolation**: Each service maintains its own database for data isolation

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
- **Grafana Loki**: Log aggregation system with MinIO storage backend
- **Grafana Alloy**: Log collection agent for centralized logging
- **Zipkin**: Distributed tracing system for request tracking
- **Spring Boot Actuator**: Health checks and metrics endpoints
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

**Terminal 5 - API Gateway:**

```bash
cd gateway
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

- **API Gateway**: http://localhost:8089 (Unified entry point for all services)
- **Eureka Dashboard**: http://localhost:8761
- **Zipkin Tracing UI**: http://localhost:9411
- **User Service** (Direct): http://localhost:8082
- **Product Service** (Direct): http://localhost:8081
- **Order Service** (Direct): http://localhost:8083
- **pgAdmin**: http://localhost:5050 (admin@admin.com / admin)
- **Loki Gateway**: http://localhost:3100 (Log aggregation endpoint)

## 📁 Project Structure

```
ecom_app_microservice/
├── eureka-server/          # Service discovery server
│   ├── src/
│   │   ├── main/java/      # Java source code
│   │   └── main/resources/ # Configuration files
│   ├── Dockerfile
│   └── pom.xml
├── gateway/                # API Gateway service
│   ├── src/
│   │   ├── main/java/      # Gateway logic & filters
│   │   └── main/resources/ # Routing configuration
│   ├── logs/               # Gateway logs
│   └── pom.xml
├── user/                   # User microservice
│   ├── src/
│   │   ├── main/java/      # Java source code
│   │   └── main/resources/ # Configuration files
│   ├── logs/               # Application logs
│   └── pom.xml
├── product/                # Product microservice
│   ├── src/
│   │   ├── main/java/      # Java source code
│   │   └── main/resources/ # Configuration files
│   ├── logs/               # Application logs
│   └── pom.xml
├── order/                  # Order microservice
│   ├── src/
│   │   ├── main/java/      # Java source code
│   │   └── main/resources/ # Configuration files
│   ├── logs/               # Application logs
│   └── pom.xml
├── evaluate-loki/          # Observability setup
│   ├── docker-compose.yaml # Loki & Alloy services
│   ├── loki-config.yaml    # Loki configuration
│   ├── alloy-local-config.yaml # Alloy log collector config
│   ├── gateway.conf        # Nginx gateway config for Loki
│   ├── .data/              # MinIO data storage (ignored)
│   └── data/               # Loki data storage (ignored)
├── docker-compose.yml      # Main Docker Compose file
├── init-databases.sql      # Database initialization script
└── README.md               # This file
```

## 📚 API Documentation

> **Note**: All API requests should go through the API Gateway at `http://localhost:8089` for proper routing, load balancing, and tracing. Direct service access is also available for development purposes.

### API Gateway (Port 8089)

**Base URL**: `http://localhost:8089`

The API Gateway provides a unified entry point for all microservices. All routes are automatically load-balanced through Eureka service discovery.

**Available Routes:**

- `/api/users/**` → Routes to User Service
- `/api/products/**` → Routes to Product Service
- `/api/orders/**` → Routes to Order Service
- `/api/cart/**` → Routes to Order Service (Cart endpoints)

**Actuator Endpoints:**

- `/actuator/health` → Health check
- `/actuator/gateway/routes` → View all configured routes
- `/actuator/metrics` → Application metrics

### User Service (Port 8082)

**Base URL (Direct)**: `http://localhost:8082/api/users`  
**Base URL (via Gateway)**: `http://localhost:8089/api/users`

| Method | Endpoint          | Description       |
| ------ | ----------------- | ----------------- |
| GET    | `/api/users`      | Get all users     |
| GET    | `/api/users/{id}` | Get user by ID    |
| POST   | `/api/users`      | Create a new user |
| PUT    | `/api/users/{id}` | Update user       |

**Example Requests:**

Via API Gateway (Recommended):

```bash
# Create User
curl -X POST http://localhost:8089/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com"
  }'
```

Direct to Service:

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

**Base URL (Direct)**: `http://localhost:8081/api/products`  
**Base URL (via Gateway)**: `http://localhost:8089/api/products`

| Method | Endpoint                                 | Description          |
| ------ | ---------------------------------------- | -------------------- |
| GET    | `/api/products`                          | Get all products     |
| GET    | `/api/products/search?keyword={keyword}` | Search products      |
| POST   | `/api/products`                          | Create a new product |
| PUT    | `/api/products/{id}`                     | Update product       |
| DELETE | `/api/products/{id}`                     | Delete product       |

**Example Requests:**

Via API Gateway (Recommended):

```bash
# Create Product
curl -X POST http://localhost:8089/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "stock": 10
  }'
```

Direct to Service:

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

**Base URL (Direct)**: `http://localhost:8083`  
**Base URL (via Gateway)**: `http://localhost:8089`

| Method | Endpoint                      | Description            | Headers               |
| ------ | ----------------------------- | ---------------------- | --------------------- |
| POST   | `/api/cart`                   | Add item to cart       | `X-User-Id: {userId}` |
| GET    | `/api/cart`                   | Get user's cart        | `X-User-Id: {userId}` |
| DELETE | `/api/cart/items/{productId}` | Remove item from cart  | `X-User-Id: {userId}` |
| POST   | `/api/orders`                 | Create order from cart | `X-User-Id: {userId}` |

**Example Requests:**

Via API Gateway (Recommended):

```bash
# Add to Cart
curl -X POST http://localhost:8089/api/cart \
  -H "Content-Type: application/json" \
  -H "X-User-Id: user123" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'

# Create Order
curl -X POST http://localhost:8089/api/orders \
  -H "X-User-Id: user123"
```

Direct to Service:

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
   # Terminal 1 - Eureka Server
   cd eureka-server && mvn spring-boot:run

   # Terminal 2 - User Service
   cd user && mvn spring-boot:run

   # Terminal 3 - Product Service
   cd product && mvn spring-boot:run

   # Terminal 4 - Order Service
   cd order && mvn spring-boot:run

   # Terminal 5 - API Gateway
   cd gateway && mvn spring-boot:run
   ```

3. **Verify Services**:
   - Check Eureka Dashboard: http://localhost:8761
   - All services (including gateway) should appear as "UP"
   - Test API Gateway: http://localhost:8089/actuator/health
   - View Gateway Routes: http://localhost:8089/actuator/gateway/routes

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

To enable centralized logging with Loki and distributed tracing with Zipkin:

**1. Start Zipkin (for distributed tracing):**

```bash
# Zipkin is already included in the main docker-compose.yml
docker-compose up -d zipkin
```

**2. Start Loki Stack (for centralized logging):**

```bash
cd evaluate-loki
docker-compose up -d
```

This will start:

- **Loki** (log aggregation with MinIO storage backend)
- **Grafana Alloy** (log collection agent, port 12345)
- **Nginx Gateway** (Loki API gateway, port 3100)
- **MinIO** (object storage for Loki data)

**3. Verify Observability:**

- Zipkin UI: http://localhost:9411
- Loki Gateway: http://localhost:3100
- Alloy Metrics: http://localhost:12345/metrics

## 🚧 Development Status

**Current Status**: 🟡 **In Development**

This project is currently under active development. The following features are implemented:

✅ **Completed:**

- Basic microservices architecture
- Service discovery with Eureka
- **API Gateway with Spring Cloud Gateway** ✨
- User, Product, and Order services
- RESTful API endpoints
- Database integration (PostgreSQL & MongoDB)
- Docker containerization
- **Centralized logging with Loki and Grafana Alloy** ✨
- **Distributed tracing with Zipkin** ✨
- **Request logging filter in Gateway** ✨
- **Spring Boot Actuator endpoints** ✨
- **MinIO storage backend for Loki** ✨

🔄 **In Progress:**

- Enhanced error handling and circuit breakers
- Comprehensive testing
- API documentation (OpenAPI/Swagger)
- Security implementation (JWT, OAuth2)
- Rate limiting in Gateway

📋 **Planned:**

- Payment service integration
- Notification service
- Caching layer (Redis)
- Message queue (RabbitMQ/Kafka)
- Monitoring and metrics (Prometheus, Grafana dashboards)
- CI/CD pipeline
- Kubernetes deployment manifests
- API rate limiting and throttling
- Request/Response transformation in Gateway
- Authentication and authorization filters

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
