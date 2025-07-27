# Event Pattern in the Bulb Manager Project

This document describes the Event pattern implementation in the Bulb Manager project.

## Overview

The Event pattern is a key component of Event Sourcing, where the state of an application is determined by a sequence of events rather than a snapshot of the current state. In the Bulb Manager project, events represent facts that have occurred as a result of command processing.

## Event Interface

The project defines a generic interface `Event` that includes methods for tracking and identifying events:

```java
@DomainEvent
public interface Event<I extends Identifier> {
    I aggregateId();
    UUID eventId();
    Long aggregateVersion();
    String eventType();
    Long eventVersion();
    ZonedDateTime eventDate();
}
```

This interface provides a robust structure for events with:
- `aggregateId`: The identifier of the aggregate that the event relates to
- `eventId`: A unique identifier for the event
- `aggregateVersion`: The version of the aggregate when the event occurred
- `eventType`: The type of the event as a string
- `eventVersion`: The version of the event schema
- `eventDate`: The date and time when the event occurred

The generic type parameter `<I extends Identifier>` allows events to be associated with specific aggregate identifier types.

## Event Implementations

The project defines a `BulbEvent` interface that extends the generic Event interface with the BulbId type:

```java
public sealed interface BulbEvent extends Event<BulbId> permits BulbCreated, BulbSwitchedOff, BulbSwitchedOn, BulbWentOut {
}
```

The project currently has four event implementations:

1. **BulbCreated**: Represents the fact that a new bulb has been created
   ```java
   public record BulbCreated(
       BulbId aggregateId,
       UUID eventId,
       Long aggregateVersion,
       ZonedDateTime eventDate
   ) implements BulbEvent {
       @Override
       public String eventType() {
           return "BulbCreated";
       }

       @Override
       public Long eventVersion() {
           return 1L;
       }
   }
   ```

2. **BulbSwitchedOn**: Represents the fact that a bulb has been turned on
   ```java
   public record BulbSwitchedOn(
       BulbId aggregateId,
       UUID eventId,
       Long aggregateVersion,
       ZonedDateTime eventDate
   ) implements BulbEvent {
       @Override
       public String eventType() {
           return "BulbSwitchedOn";
       }

       @Override
       public Long eventVersion() {
           return 1L;
       }
   }
   ```

3. **BulbSwitchedOff**: Represents the fact that a bulb has been turned off
   ```java
   public record BulbSwitchedOff(
       BulbId aggregateId,
       UUID eventId,
       Long aggregateVersion,
       ZonedDateTime eventDate
   ) implements BulbEvent {
       @Override
       public String eventType() {
           return "BulbSwitchedOff";
       }

       @Override
       public Long eventVersion() {
           return 1L;
       }
   }
   ```

4. **BulbWentOut**: Represents the fact that a bulb has burned out after reaching its lifespan limit
   ```java
   public record BulbWentOut(
       BulbId aggregateId,
       UUID eventId,
       Long aggregateVersion,
       ZonedDateTime eventDate
   ) implements BulbEvent {
       @Override
       public String eventType() {
           return "BulbWentOut";
       }

       @Override
       public Long eventVersion() {
           return 1L;
       }
   }
   ```

All event implementations are Java records, which means they are immutable and provide built-in equals, hashCode, and toString methods. Each implementation includes:
- The required fields from the Event interface (aggregateId, eventId, aggregateVersion, eventDate)
- Implementation of the eventType() method to return the name of the event
- Implementation of the eventVersion() method to return the version of the event schema (currently 1L for all events)

## Event Handling

Events are produced by the `BulbDecider` when processing commands. The decider uses the Decision framework to represent the result of a command:

```java
public Decision<BulbEvent, BulbValidationError, BulbId> apply(BulbCommand command, Optional<BulbAggregate> aggregate) {
    // Decision logic that produces events with all required metadata
    // Example for creating a new bulb:
    return new EventList<>(List.of(
        new BulbCreated(
            command.aggregateId(),
            UUID.randomUUID(),
            1L,
            ZonedDateTime.now()
        )
    ));
}
```

The Decision framework has been enhanced to work with the generic Event interface:
- `Decision<E extends Event<I>, VE extends ValidationError, I extends Identifier>`
- `EventList<E extends Event<I>, VE extends ValidationError, I extends Identifier>`
- `ErrorList<E extends Event<I>, VE extends ValidationError, I extends Identifier>`

This pattern provides a clear and structured way to determine which events should be produced based on the command and the current state of the aggregate. The events now include all required metadata (aggregateId, eventId, aggregateVersion, eventDate, eventType, eventVersion).

## State Evolution

The produced events are then used to evolve the state of the aggregate. The `BulbEvolver` applies each event to the current state:

```java
public BulbAggregate apply(Optional<BulbAggregate> aggregate, List<BulbEvent> events) {
    BulbAggregate result = aggregate.orElse(new InitialBulb(events.get(0).aggregateId()));
    
    for (BulbEvent event : events) {
        result = switch (event) {
            case BulbCreated ignored -> new InitialBulb(event.aggregateId());
            case BulbSwitchedOff ignored -> new OffBulb(event.aggregateId(), event.aggregateVersion());
            case BulbSwitchedOn ignored -> new OnBulb(event.aggregateId(), event.aggregateVersion(), new Count(1));
            case BulbWentOut ignored -> new WentOutBulb(event.aggregateId(), event.aggregateVersion());
        };
    }
    
    return result;
}
```

This approach separates the decision logic (what events to produce) from the evolution logic (how to change the state based on events), which is a key aspect of the Decider pattern. The evolution logic now uses the metadata from the events (like aggregateId and aggregateVersion) to create the new state.

## Future Enhancements

The current implementation already uses a list of events to represent the result of a command. Future enhancements planned in the roadmap include:

1. **Persist event lists**: Store events in a persistent store
2. **Event sourcing**: Rebuild the state of the application from the sequence of events

These enhancements will further develop the Event Sourcing capabilities of the project.

## Relationship to Other Patterns

The Event pattern works in conjunction with other patterns in the project:

- **Command Pattern**: Commands represent intentions to modify state, and they produce events
  - Commands include the aggregate ID, aggregate version, and command date
  - The aggregate version is used for optimistic concurrency control
  - The command date provides a timestamp for auditing and potentially time-based business rules
- **Decider Pattern**: The decision logic produces events, and the evolution logic consumes events to produce new state
  - The decider validates that the command's aggregate version matches the current aggregate version
  - The evolve function increments the aggregate version when producing a new state
- **CQRS**: Commands produce events, which are then used to update the write model

Together, these patterns provide a robust foundation for building a maintainable and scalable application.

## Framework Implementation

The project has implemented a framework to support the Event pattern:

- **Event Interface**: A marker interface that all events must implement
- **CommandHandler**: Orchestrates the process of handling a command, applying the decider to produce events, evolving the state, and saving the result
- **Decider**: Produces events based on the current state and the command
- **Evolve**: Applies events to the current state to produce a new state

For a detailed explanation of the framework implementation, see [Decider Framework](decider_framework.md).
