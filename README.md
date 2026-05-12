# Stock Portfolio Service

## What We Built
This is a Spring Boot backend for stock portfolio and order booking operations.
It manages traders, sectors, stocks, baskets, holdings, and orders.
It also provides portfolio summary and overlap/risk analysis for a trader.

## Tech Stack
- Java 17
- Spring Boot (Web, Validation, JPA)
- PostgreSQL
- Maven

## How It Is Structured
- Controllers expose REST APIs.
- Service implementations contain business logic.
- Repositories handle database operations.
- DTOs shape API request/response payloads.
- `CacheDataMap` keeps in-memory ID-to-name maps for trader/stock/sector/basket data.

## Endpoints

### Traders (`/traders`)
- `GET /traders` - list traders
- `GET /traders/{id}` - trader by id
- `POST /traders` - create trader
- `PUT /traders/{id}` - update trader
- `DELETE /traders/{id}` - delete trader

### Sectors (`/sectors`)
- `GET /sectors/` - list sectors
- `GET /sectors/{id}` - sector by id
- `POST /sectors` - create sector
- `PUT /sectors/{id}` - update sector
- `DELETE /sectors/{id}` - delete sector

### Stocks (`/stocks`)
- `GET /stocks/` - list stocks
- `GET /stocks/detailsById/{id}` - stock by id
- `GET /stocks/detailsByName/{name}` - stock by name
- `POST /stocks` - create stock
- `PUT /stocks/{id}` - update stock
- `DELETE /stocks/{id}` - delete stock

### Baskets (`/baskets`)
- `GET /baskets` - list baskets
- `POST /baskets` - create basket
- `PUT /baskets/{id}` - update basket
- `DELETE /baskets/{id}` - delete basket

### Basket-Stock Mapping (`/basket-stocks`)
- `POST /basket-stocks` - add one stock to basket
- `POST /basket-stocks/bulkAddStockToBasket/{basketId}` - bulk add stocks
- `POST /basket-stocks/bulkRemoveStockFromBasket/{basketId}` - bulk remove stocks
- `DELETE /basket-stocks/{basketId}/{stockId}` - remove one stock from basket

### Orders (`/orders`)
- `POST /orders` - place order
- `GET /orders/{orderId}/{traderId}` - get one order
- `GET /orders/byTraderId/{traderId}` - trader order history
- `PUT /orders/{orderId}/fill/trader/{traderId}` - fill pending order
- `PUT /orders/{orderId}/cancel/trader/{traderId}` - cancel pending order

### Holdings (`/holdings`)
- `PUT /holdings/` - update holding
- `GET /holdings/trader/{traderId}/stock/{stockId}` - holding for trader+stock
- `GET /holdings/trader/{traderId}` - all holdings for trader

### Portfolio (`/portfolios`)
- `GET /portfolios/trader/{id}` - returns trader positions and sector breakdown

### Overlap (`/overlaps`)
- `GET /overlaps/trader/{id}` - overlap with baskets and risk flag

## What Services Are Doing
- `TradersServiceImpl` - creates, updates, deletes, and fetches trader data.
- `SectorsServiceImpl` - sector CRUD with duplicate checks.
- `StockServiceImpl` - stock CRUD with name duplicate checks.
- `BasketServiceImpl` - basket CRUD and basket status handling.
- `BasketStockServiceImpl` - manages stock membership in baskets (single + bulk operations).
- `OrderServiceImpl` - places, fills, and cancels orders; validates order state; updates trader holdings on fill.
- `HoldingsServiceImpl` - adds/updates holdings and returns trader holdings view.
- `PortfolioServiceImpl` - builds aggregated portfolio output (positions + sector breakdown).
- `OverlapServiceImpl` - calculates overlap percentage between trader holdings and each basket, then sets risk level.

## Exception Handling
`GlobalExceptionHandler` is used with `@ControllerAdvice`.

Handled exceptions include:
- `DataNotFoundException` -> `404 NOT_FOUND`
- `DuplicateDataException` -> currently returned as `400 BAD_REQUEST` (payload status field is `409`)
- `IllegalArgumentException` -> `400 BAD_REQUEST`
- `MethodArgumentNotValidException` -> `400 BAD_REQUEST` with field-wise validation messages
- `ConstraintViolationException` -> `400 BAD_REQUEST` with validation messages
- `DataIntegrityViolationException` -> `409 CONFLICT` with mapped duplicate/constraint message
- `IllegalOrderStateException` -> `500 INTERNAL_SERVER_ERROR`
- `MaxPendingOrdersCountException` -> `500 INTERNAL_SERVER_ERROR`
- `NotEnoughSharesException` -> `500 INTERNAL_SERVER_ERROR`
- generic `Exception` -> `500 INTERNAL_SERVER_ERROR`

## Response Handling
- Success responses use `ResponseEntity` from controllers.
- Typical status codes:
  - `200 OK` for read/update operations
  - `201 CREATED` for create operations
  - `204 NO_CONTENT` for delete/bulk remove operations
- Error responses:
  - `ErrorResponse` DTO: `status`, `message`, `error`, `timestamp`, optional `path`
  - Validation errors may return `Map<String, String>` of field-to-message

## Request Tracing
`MDCRequestFilters` adds request ID support:
- Reads `X-Request-Id` from incoming request (or generates one).
- Adds it into MDC as `requestId`.
- Returns the same ID in response header.
- Logging pattern includes the request ID.


## Enhancements planned
- Authentication and authorization (e.g. JWT) to secure endpoints and restrict access based on user roles.
- Caching frequently accessed data (e.g. stock prices, sector info) in redis instead of local.
- Pagination and filtering support for list endpoints (e.g. stocks, orders).
- Aspect-oriented programming (AOP) for cross-cutting concerns like logging, performance monitoring, and security.