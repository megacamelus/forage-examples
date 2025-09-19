# Camel Forage Multi-Datasource Integration

This project demonstrates how to use Camel Forage to run Apache Camel routes that interact with multiple databases (MySQL and PostgreSQL).

## Prerequisites

- Docker (for running database containers)
- Apache Camel CLI
- Maven (for Spring Boot export)

## Database Setup

### MySQL Database

Start a MySQL container:

```bash
docker run -e MYSQL_ROOT_PASSWORD=pwd -p3306:3306 mysql:latest
```

**Connection Details:**
- Host: `localhost`
- Port: `3306`
- User: `root`
- Password: `pwd`
- JDBC URL: `jdbc:mysql://localhost:3306`

**Create test data:**

```sql
CREATE DATABASE test;
CREATE TABLE test.foo(id INTEGER PRIMARY KEY, content VARCHAR(255));
INSERT INTO test.foo VALUES (1, 'mysql 1');
INSERT INTO test.foo VALUES (2, 'mysql 2');
```

### PostgreSQL Database

Start a PostgreSQL container using Camel infrastructure:

```bash
camel infra run postgres
```

**Connection Details:**
- Host: `localhost`
- Port: `5432`
- User: `test`
- Password: `test`
- JDBC URL: `jdbc:postgresql://localhost:5432/postgres`

**Create test data:**

```sql
CREATE TABLE bar(id INTEGER PRIMARY KEY, content VARCHAR);
INSERT INTO bar VALUES (1, 'postgres 1');
INSERT INTO bar VALUES (2, 'postgres 2');
```

## Running the Integration

### Camel CLI

Execute the integration with the following command:

```bash
camel run forage-multi-datasource-factory.properties \
          route.camel.yaml \
          forage-datasource-factory.properties \
          --dep=mvn:org.apache.camel.forage:forage-jdbc:1.0-SNAPSHOT \
          --dep=mvn:org.apache.camel.forage:forage-jdbc-postgres:1.0-SNAPSHOT \
          --dep=mvn:org.apache.camel.forage:forage-jdbc-mysql:1.0-SNAPSHOT
```

The integration creates two datasources (`ds1` and `ds2`) that can be referenced in Camel routes following Camel best practices. Configuration is managed through the properties files:
- `forage-multi-datasource-factory.properties` - Multi-datasource configuration
- `forage-datasource-factory.properties` - Individual datasource settings

### Spring Boot Export

Export the project to a Camel Spring Boot application:

```bash
camel export forage-multi-datasource-factory.properties \
            route.camel.yaml \
            forage-datasource-factory.properties \
            --dep=mvn:org.apache.camel.forage:forage-jdbc-starter:1.0-SNAPSHOT \
            --dep=mvn:org.apache.camel.forage:forage-jdbc-postgres:1.0-SNAPSHOT \
            --dep=mvn:org.apache.camel.forage:forage-jdbc-mysql:1.0-SNAPSHOT \
            --runtime=spring-boot \
            --gav=com.foo:acme:1.0-SNAPSHOT
```

Run the Spring Boot application:

```bash
mvn spring-boot:run
```

## Features

- **Multiple Datasource Support**: Seamlessly work with MySQL and PostgreSQL databases
- **Spring Boot Integration**: Export to Spring Boot for production deployments
- **Monitoring**: When web and actuator dependencies are added, datasource and connection pool metrics are automatically exposed
- **Best Practices**: Follows Apache Camel conventions for datasource configuration and routing

## Dependencies

- `org.apache.camel.forage:forage-jdbc:1.0-SNAPSHOT` (for plain camel)
- `org.apache.camel.forage:forage-jdbc-postgres:1.0-SNAPSHOT` 
- `org.apache.camel.forage:forage-jdbc-mysql:1.0-SNAPSHOT`
- `org.apache.camel.forage:forage-jdbc-starter:1.0-SNAPSHOT` (for Spring Boot)