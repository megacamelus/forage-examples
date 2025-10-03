# Event Batching for IoT Analytics

This example demonstrates a common use case in IoT systems: batching events for analytics processing using Apache Camel's aggregation pattern with persistent storage.

## Use Case

In IoT environments, devices often send events at irregular intervals. Processing each event individually can be inefficient and costly. This pattern aggregates events into batches based on:

- **Count**: Complete the batch when 5 events are received
- **Timeout**: Complete the batch after 5000ms (5 seconds) if fewer events are received

This ensures timely processing while optimizing for batch operations, which is essential for:
- Reducing database write operations
- Minimizing API calls to analytics platforms
- Optimizing network bandwidth
- Enabling efficient batch analytics processing

## How It Works

Events are aggregated by their `eventId` header using a custom aggregation strategy (`MyAggregationStrategy`) that collects event bodies into a list. The aggregation state is persisted in PostgreSQL using a JDBC aggregation repository, ensuring durability across restarts.

### Aggregation Strategy

The `MyAggregationStrategy` (event-batching.camel.yaml:20, MyAggregationStrategy.java:12) collects exchange bodies into an `ArrayList`. Each incoming event's body is appended to the list and returned as the aggregated result.

### Completion Criteria

The aggregation completes when either condition is met:
- **Completion Size**: 5 events have been collected, OR
- **Completion Timeout**: 5000ms (5 seconds) have elapsed since the first event in the batch

## Running the Example

### Prerequisites

1. **Start PostgreSQL**
   ```bash
   camel infra run postgresql
   ```

2. **Create the aggregation tables**
   ```sql
   CREATE TABLE event_aggregation (
           id varchar(255) NOT NULL,
           exchange bytea NOT NULL,
           version BIGINT NOT NULL,
           constraint event_aggregation_pk PRIMARY KEY (id)
   );

   CREATE TABLE event_aggregation_completed (
           id varchar(255) NOT NULL,
           exchange bytea NOT NULL,
           version BIGINT NOT NULL,
           constraint event_aggregation_completed_pk PRIMARY KEY (id)
   );
   ```

### Run the Integration

```bash
camel forage run event-batching.camel.yaml forage-datasource-factory.properties MyAggregationStrategy.java --dep=org.kaoto:kaoto-camel-manager:1.0-SNAPSHOT
```

### Send Test Events

Send events to the `direct:events` endpoint with an `eventId` header. Events with the same `eventId` will be batched together.

To do so, kaoto-camel-manager UI can be used, open `/path/to/kaoto-camel-manager/test-vertx-sse-client.html` in the browser and update the `Message Production:` section with `direct:events`, and the header `eventId`.

## Example Output

The logs demonstrate the batching behavior:

```
2025-10-03 11:02:21.205  INFO event-batching.camel.yaml:9  : Received event with id :1 and body: Hello, Camel! 1
2025-10-03 11:02:23.200  INFO event-batching.camel.yaml:9  : Received event with id :1 and body: Hello, Camel! 2
2025-10-03 11:02:26.579  INFO event-batching.camel.yaml:9  : Received event with id :1 and body: Hello, Camel! 3
...
2025-10-03 11:03:16.819  INFO event-batching.camel.yaml:15 : Batch complete with 3 event id: 1 and events: [Hello, Camel! 1, Hello, Camel! 2, Hello, Camel! 3]
```

In this example:
- **Event ID 1**: Received 3 events, batch completed after 5-second timeout (11:02:21 → 11:03:16)
- **Event ID 2**: Received 5 events, batch completed immediately upon reaching completion size
- Events are grouped by `eventId` and processed independently

## Configuration

The `forage-datasource-factory.properties` configures the PostgreSQL connection and aggregation repository:

```properties
jdbc.db.kind=postgresql
jdbc.url=jdbc:postgresql://localhost:5432/postgres

jdbc.aggregation.repository.name=event_aggregation

jdbc.transaction.enabled=true
```

The aggregation repository reference `#event_aggregation` in the YAML route (event-batching.camel.yaml:19) maps to this configuration.
