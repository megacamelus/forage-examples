# Event Booking System with Apache Camel Forage

This example demonstrates a **transactional event booking system** using Apache Camel with the Forage datasource framework. The system ensures data consistency when booking event seats by implementing atomic transactions that prevent double-booking and maintain database integrity.

## Overview

The application handles event seat reservations through a file-based workflow:
- JSON booking requests are placed in the `data/inbox` folder
- Apache Camel routes process these files and execute database transactions
- Each booking atomically reduces available seats and creates a booking record
- Failed bookings (e.g., sold-out events) trigger transaction rollbacks

## Architecture

- **File Processing Route**: Monitors `data/inbox` for JSON booking files
- **Event Booking Route**: Handles transactional database operations
- **PostgreSQL Database**: Stores events and bookings with referential integrity
- **Forage Framework**: Provides datasource management and transaction support

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- PostgreSQL database running on localhost:5432
- Apache Camel JBang

## Database Setup

### 1. Create Database Schema

```sql
-- Create the events table to store event information
CREATE TABLE events (
    event_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    available_seats INT NOT NULL CHECK (available_seats >= 0)
);

-- Create the bookings table to link users to events
CREATE TABLE bookings (
    booking_id SERIAL PRIMARY KEY,
    event_id INT NOT NULL REFERENCES events(event_id),
    user_id INT NOT NULL,
    booking_time TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Optional: Add an index for faster lookups
CREATE INDEX idx_bookings_event_id ON bookings(event_id);
```

### 2. Insert Sample Data

```sql
-- Insert a popular event with many seats
INSERT INTO events (name, available_seats)
VALUES ('Camel Development Conference', 150);

-- Insert an exclusive event with very limited seats
INSERT INTO events (name, available_seats)
VALUES ('Advanced Messaging Workshop', 1);
```

## Setup and Installation

### 1. Build Forage Dependencies

First, build the required Forage modules:

```bash
mvn clean install
```

### 2. Install Forage Camel JBang Plugin

```
camel plugin add forage \
  --command='forage' \
  --description='Forage Camel JBang Plugin' \
  --artifactId='camel-jbang-plugin-forage' \
  --groupId='org.apache.camel.forage' \
  --version='1.0-SNAPSHOT' \
  --gav='org.apache.camel.forage:camel-jbang-plugin-forage:1.0-SNAPSHOT'
```

## Running the Application

### Option 1: Direct Camel JBang Execution

Run the integration directly with Camel JBang:

```bash
camel run book.camel.yaml forage-datasource-factory.properties \
  --dep=mvn:org.apache.camel.forage:forage-jdbc:1.0-SNAPSHOT \
  --dep=mvn:org.apache.camel.forage:forage-jdbc-postgresql:1.0-SNAPSHOT
```

### Option 2: Spring Boot Export

Export the integration as a Spring Boot application:

```bash
camel export book.camel.yaml forage-datasource-factory.properties \
  --dep=mvn:org.apache.camel.forage:forage-jdbc-starter:1.0-SNAPSHOT \
  --dep=mvn:org.apache.camel.forage:forage-jdbc-postgresql:1.0-SNAPSHOT \
  --runtime=spring-boot
```

Then run the exported Spring Boot application:

```bash
mvn spring-boot:run
```

## Testing the Application

The project includes three sample booking files to demonstrate different scenarios:

### Test Case 1: Successful Booking
```bash
cp booking-1.json data/inbox/
```
- **File**: `booking-1.json` (Event ID: 1, User ID: 456)
- **Expected**: Successfully books a seat for "Camel Development Conference"
- **Result**: Available seats decrease from 150 to 149, booking record created

### Test Case 2: Last Available Seat
```bash
cp booking-2.json data/inbox/
```
- **File**: `booking-2.json` (Event ID: 2, User ID: 789)
- **Expected**: Successfully books the last seat for "Advanced Messaging Workshop"
- **Result**: Available seats decrease from 1 to 0, booking record created

### Test Case 3: Sold Out Event (Transaction Rollback)
```bash
cp booking-3.json data/inbox/
```
- **File**: `booking-3.json` (Event ID: 2, User ID: 999)
- **Expected**: Fails because event is sold out
- **Result**: Transaction rollback, no changes to database, error logged

## How the Transaction Flow Works

1. **File Detection**: The file monitoring route (`file-to-booking-route`) detects JSON files in `data/inbox`
2. **Transaction Start**: The booking route (`event-booking-route`) begins a database transaction
3. **Seat Reservation**: Attempts to update available seats with condition `WHERE available_seats > 0`
4. **Validation**: Checks if exactly one row was updated (seat successfully reserved)
5. **Booking Creation**: If successful, inserts a booking record
6. **Transaction Commit**: Both operations succeed and transaction commits
7. **Error Handling**: If seat unavailable, throws exception and rolls back transaction

## Key Features Demonstrated

- **ACID Transactions**: Ensures data consistency across multiple table operations
- **Optimistic Concurrency**: Uses conditional updates to prevent overselling
- **Error Handling**: Graceful handling of business logic failures
- **File Integration**: Event-driven processing with file system monitoring
- **Database Connection Pooling**: Efficient resource management