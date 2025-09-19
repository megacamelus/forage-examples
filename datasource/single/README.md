# Camel Forage Database Integration

This guide demonstrates how to use Camel Forage to run Camel Routes that interact with a PostgreSQL database.

## Prerequisites

- Apache Camel with Forage support
- PostgreSQL database
- Maven (for Spring Boot export)

## Quick Start

### 1. Start PostgreSQL Database

Launch PostgreSQL using Camel infrastructure:

```bash
camel infra run postgres
```

**Connection Details:**
- Host: `localhost`
- Port: `5432`
- Username: `test`
- Password: `test`
- JDBC URL: `jdbc:postgresql://localhost:5432/postgres`

### 2. Set Up Test Data

Connect to your PostgreSQL instance and create a sample table with test data:

```sql
CREATE TABLE bar (
    id INTEGER PRIMARY KEY,
    content VARCHAR(255)
);

INSERT INTO bar VALUES (1, 'postgres 1');
INSERT INTO bar VALUES (2, 'postgres 2');
```

### 3. Run the Integration

Execute the Camel route with the required dependencies:

```bash
camel run route.camel.yaml forage-datasource-factory.properties \
    --dep=mvn:org.apache.camel.forage:forage-jdbc:1.0-SNAPSHOT \
    --dep=mvn:org.apache.camel.forage:forage-jdbc-postgres:1.0-SNAPSHOT
```

## DataSource Configuration

The integration automatically creates a single datasource named `dataSource` following Camel's naming conventions. This datasource is immediately available for use with Camel components, particularly the `sql` component, without additional configuration.

## Export to Spring Boot

To convert your integration into a Spring Boot application:

```bash
camel export forage-multi-datasource-factory.properties route.camel.yaml forage-datasource-factory.properties \
    --dep=mvn:org.apache.camel.forage:forage-jdbc-starter:1.0-SNAPSHOT \
    --dep=mvn:org.apache.camel.forage:forage-jdbc-postgres:1.0-SNAPSHOT \
    --runtime=spring-boot \
    --gav=com.foo:acme:1.0-SNAPSHOT
```

### Running the Spring Boot Application

Start the exported Spring Boot application:

```bash
mvn spring-boot:run
```

The console output will match the standalone integration. Additionally, if you include Spring Boot Web and Actuator dependencies, you'll gain access to:

- DataSource metrics and monitoring
- Connection pool statistics
- Health checks and endpoints

## Features

- **Single DataSource**: Simplified configuration with one primary database connection
- **Convention over Configuration**: Uses Camel's standard naming conventions
- **Spring Boot Ready**: Easy export to Spring Boot with built-in monitoring
- **Production Ready**: Includes connection pooling and metrics when using actuators
