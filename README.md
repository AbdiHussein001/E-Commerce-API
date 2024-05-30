# E-Commerce-API

# Store Management Application

## Overview

This Spring Boot application manages store products and shopping carts. It includes functionalities for creating, updating, and deleting products, as well as creating and managing shopping carts.

## Features

- **Product Management**: Create, delete, and list products.
- **Shopping Cart Management**: Create, update, list, and checkout shopping carts.
- **Validation**: Enforce product name uniqueness and restricted labels.
- **Persistence**: Store data using a relational database (H2 for in-memory testing).

## Technologies Used

- **Java 17**
- **Spring Boot 3.3.0**
- **Spring Data JPA**
- **H2 Database** (for testing)
- **Jakarta Persistence API** (JPA)
- **JUnit 5** for unit tests

## Project Structure

- **Controller**: Handles HTTP requests and responses.
- **Service**: Contains business logic.
- **Repository**: Manages database interactions.
- **Entity**: Represents database tables.
- **DTO**: Data Transfer Objects for API responses.

## Getting Started

### Prerequisites

- Java 17
- Maven 3.x
