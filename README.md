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
- **JUnit 5** for unit tests

## Project Structure

- **Controller**: Handles HTTP requests and responses.
- **Service**: Contains business logic.
- **Repository**: Manages database interactions.
- **Entity**: Represents database tables.
- **DTO**: Data Transfer Objects for API responses.

## API Endpoints

### Product Management

- **Create Product**
  - **Description**: Adds a new product to the store.
  - **Endpoint**: `POST /products`
  - **Request Body**:
    ```json
    {
      "name": "Special Smelly Cheese",
      "price": 20.99,
      "labels": ["FOOD", "LIMITED"]
    }
    ```
  - **Response Body**:
    ```json
    {
      "productId": 23,
      "name": "Special Smelly Cheese",
      "price": 20.99,
      "addedAt": "2022/10/14",
      "labels": ["FOOD", "LIMITED"]
    }
    ```

- **Get All Products**
  - **Description**: Retrieves a list of all products in the store.
  - **Endpoint**: `GET /products`
  - **Response Body**:
    ```json
    [
      {
        "productId": 1,
        "name": "Fanta",
        "price": 5.99,
        "addedAt": "2012/12/02",
        "labels": ["DRINK"]
      }
    ]
    ```

- **Get Product by ID**
  - **Description**: Retrieves details of a product by its ID.
  - **Endpoint**: `GET /products/{id}`
  - **Response Body**:
    ```json
    {
      "productId": 1,
      "name": "Fanta",
      "price": 5.99,
      "addedAt": "2012/12/02",
      "labels": ["DRINK"]
    }
    ```

- **Delete Product**
  - **Description**: Deletes a product by its ID.
  - **Endpoint**: `DELETE /products/{id}`

### Shopping Cart Management

- **Create Cart**
  - **Description**: Creates a new, empty shopping cart.
  - **Endpoint**: `POST /carts`
  - **Response Body**:
    ```json
    {
      "cartId": 1,
      "products": [],
      "checkedOut": false
    }
    ```

- **Get All Carts**
  - **Description**: Retrieves a list of all shopping carts.
  - **Endpoint**: `GET /carts`
  - **Response Body**:
    ```json
    [
      {
        "cartId": 1,
        "products": [
          {
            "productId": 123,
            "quantity": 2
          }
        ],
        "checkedOut": false
      },
      {
        "cartId": 2,
        "products": [],
        "checkedOut": false
      }
    ]
    ```

- **Update Cart**
  - **Description**: Adds and removes products to a shopping cart or updates quantities of existing products.
  - **Endpoint**: `PUT /carts/{id}`
  - **Request Body**:
    ```json
    [
      {
        "productId": 123,
        "quantity": 2
      }
    ]
    ```
  - **Response Body**:
    ```json
    {
      "cartId": 1,
      "products": [
        {
          "productId": 123,
          "quantity": 2
        }
      ],
      "checkedOut": false
    }
    ```

- **Checkout Cart**
  - **Description**: Marks a cart as checked out and calculates the total cost.
  - **Endpoint**: `POST /carts/{id}/checkout`
  - **Response Body**:
    ```json
    {
      "cart": {
        "cartId": 1,
        "products": [
          {
            "productId": 123,
            "quantity": 2
          }
        ],
        "checkedOut": true
      },
      "totalCost": 11.98
    }
    ```
