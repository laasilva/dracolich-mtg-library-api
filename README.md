# dracolich-mtg-library-api

A reactive REST API for Magic: The Gathering card data, powered by Scryfall bulk data. Provides card search with filters, set browsing, format legality, symbols, rulings, and art properties for 90,000+ cards.

## Prerequisites

- Java 25
- MongoDB running on `localhost:27017`
- Maven 3.9+

## Quick Start

```bash
# Build
mvn clean install -s ~/.m2/settings-personal.xml

# Run (port 8080, dev profile auto-active)
mvn spring-boot:run -pl mtg-library-api-web -s ~/.m2/settings-personal.xml
```

The API starts on `http://localhost:8080/dracolich/mtg-library/api/v0/`.

Swagger UI is available at `http://localhost:8080/dracolich/mtg-library/api/v0/swagger-ui.html`.

## Configuration

| Variable | Default | Description |
|----------|---------|-------------|
| `PORT` | `8080` | Server port |
| `MONGODB_URI` | `mongodb://localhost:27017/dracolich-mtg-library` | MongoDB connection URI |
| `MONGODB_DATABASE` | `dracolich-mtg-library` | Database name |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:3000,http://localhost:5173` | Allowed CORS origins |

## API Endpoints

Base path: `/dracolich/mtg-library/api/v0/`

All responses are wrapped in the standard `DmdResponse` envelope:

```json
{
  "success": true,
  "httpStatus": "200 OK",
  "message": "Request processed successfully",
  "payload": { ... },
  "errors": null
}
```

### Cards

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/cards/{id}` | Fetch card by ID |
| `GET` | `/cards?name={name}` | Fetch card by exact name |
| `POST` | `/cards/search?name={name}&page=0&size=10` | Search cards with filters (paginated) |
| `POST` | `/cards/random` | Fetch a random card matching filters |

**Search request body:**

```json
{
  "colors": ["R", "U"],
  "types": ["CREATURE"],
  "keywords": ["flying"],
  "legalIn": "COMMANDER",
  "minManaValue": 2.0,
  "maxManaValue": 5.0
}
```

All fields are optional. Combine with `?name=` query param for name filtering.

### Sets

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/sets/{id}` | Fetch set by ID |
| `GET` | `/sets?code={code}` | Fetch set by code (e.g., `MH3`) |
| `POST` | `/sets/search?page=0&size=10` | Search sets with filters (paginated) |

### Formats

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/formats/` | Fetch all game formats |

### Symbols

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/symbols/{id}` | Fetch symbol by ID |
| `GET` | `/symbols?symbol={symbol}` | Fetch symbol by notation (e.g., `{W}`, `{U}`) |

### Subtypes

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/subtypes/{id}` | Fetch subtype by ID |
| `GET` | `/subtypes?code={code}` | Fetch subtype by code |
| `GET` | `/subtypes/search?page=0&size=20` | Browse subtypes (paginated) |

### Art Properties

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/art-properties/{id}` | Fetch art property by ID |
| `GET` | `/art-properties?card_id={cardId}` | Fetch all printings/art for a card |

### Rulings

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/rulings/{id}` | Fetch ruling by ID |

### Sync

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/sync` | Trigger Scryfall bulk data sync |

## Data Source

Card data is sourced from [Scryfall](https://scryfall.com/docs/api) bulk data exports. The sync endpoint triggers a full import of:

- Cards (90,000+)
- Sets
- Rulings
- Symbols
- Card layouts
- Subtypes
- Game formats
- Art properties

## Module Structure

| Module | Purpose |
|--------|---------|
| `mtg-library-api-web` | Spring Boot app, controllers, services, entities, repositories |
| `mtg-library-api-integration` | Scryfall API client and DTOs for external data import |
| `mtg-library-api-dto` | Public DTOs for API consumers (published to GitHub Packages) |

## Project Structure

```
dracolich-mtg-library-api/
├── mtg-library-api-dto/           # Public DTOs (consumed by dracolich-ai-api)
│   └── src/main/java/
│       └── dm/dracolich/mtgLibrary/dto/
│           ├── enums/             # Color, CardType, Format, Legality, etc.
│           ├── records/           # CardSearchRecord, PageRecord
│           └── *.java             # CardDto, CardFaceDto, GameFormatDto, etc.
│
├── mtg-library-api-integration/   # Scryfall sync client
│   └── src/main/java/
│       └── dm/dracolich/mtgLibrary/integration/
│           ├── client/            # Scryfall API client
│           ├── config/            # Client configuration
│           └── dto/               # Scryfall-specific response DTOs
│
└── mtg-library-api-web/           # Main application
    └── src/main/java/
        └── dm/dracolich/mtgLibrary/web/
            ├── config/            # CORS, MongoDB, seed config
            ├── controller/        # REST controllers (9)
            ├── entity/            # MongoDB document entities
            ├── mapper/            # MapStruct entity-DTO mappers
            ├── repository/        # Reactive MongoDB repositories
            └── service/           # Business logic
```

## Tech Stack

- **Java 25** with preview features
- **Spring Boot 4.0** with WebFlux (reactive)
- **MongoDB** with Reactive Streams
- **MapStruct 1.6.3** for entity-DTO mapping
- **SpringDoc OpenAPI 3.0.1** for Swagger UI
- **Lombok** for boilerplate reduction
- **forge:common** for DmdResponse envelope, error handling

## Running Tests

```bash
mvn test -s ~/.m2/settings-personal.xml
```
