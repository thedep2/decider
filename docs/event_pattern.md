# Event Pattern in the Bulb Manager Project

This document describes the Event pattern implementation in the Bulb Manager project.

## Overview

The Event pattern is a key component of Event Sourcing, where the state of an application is determined by a sequence of events rather than a snapshot of the current state. In the Bulb Manager project, events represent facts that have occurred as a result of command processing.

## Event Interface

The project defines a sealed interface `Event` that permits only specific event implementations:

```java
public sealed interface Event permits BulbSwitchedOff, BulbSwitchedOn, BulbWentOut {
}
```

This sealed interface ensures type safety and restricts the possible event types to a known set.

## Event Implementations

The project currently has three event implementations:

1. **BulbSwitchedOn**: Represents the fact that a bulb has been turned on
   ```java
   public record BulbSwitchedOn() implements Event {
   }
   ```

2. **BulbSwitchedOff**: Represents the fact that a bulb has been turned off
   ```java
   public record BulbSwitchedOff() implements Event {
   }
   ```

3. **BulbWentOut**: Represents the fact that a bulb has burned out after reaching its lifespan limit
   ```java
   public record BulbWentOut() implements Event {
   }
   ```

All event implementations are Java records, which means they are immutable and provide built-in equals, hashCode, and toString methods.

## Event Handling

Events are produced by the `BulbService` when processing commands. The service uses a list of events to represent the result of a command:

```java
List<Event> events = switch (command) {
    case BulbTurnOff ignored when bulb.isTurnOn() -> List.of(new BulbSwitchedOff());
    case BulbTurnOff ignored -> List.of();
    case BulbTurnOn ignored when !bulb.isTurnOn() && bulb.count() >= LIMIT -> List.of(new BulbWentOut());
    case BulbTurnOn ignored when !bulb.isTurnOn() -> List.of(new BulbSwitchedOn());
    case BulbTurnOn ignored -> List.of();
};
```

This pattern matching approach provides a clear and concise way to determine which events should be produced based on the command and the current state of the bulb. Note that in some cases, an empty list is returned, indicating that the command did not result in any state change.

## State Evolution

The produced events are then used to evolve the state of the bulb. The service iterates through the list of events and applies each one to the current state:

```java
private static Bulb evolve(List<Event> events, Bulb bulb) {
    for (Event event : events) {
        bulb = switch (event) {
            case BulbSwitchedOff ignored -> new Bulb(false, bulb.count());
            case BulbSwitchedOn ignored -> new Bulb(true, bulb.count() + 1);
            case BulbWentOut ignored -> new Bulb(false, bulb.count());
        };
    }
    return bulb;
}
```

This approach separates the decision logic (what event to produce) from the evolution logic (how to change the state based on the event), which is a key aspect of the Decider pattern.

## Future Enhancements

The current implementation already uses a list of events to represent the result of a command. Future enhancements planned in the roadmap include:

1. **Persist event lists**: Store events in a persistent store
2. **Event sourcing**: Rebuild the state of the application from the sequence of events

These enhancements will further develop the Event Sourcing capabilities of the project.

## Relationship to Other Patterns

The Event pattern works in conjunction with other patterns in the project:

- **Command Pattern**: Commands represent intentions to modify state, and they produce events
- **Decider Pattern**: The decision logic produces events, and the evolution logic consumes events to produce new state
- **CQRS**: Commands produce events, which are then used to update the write model

Together, these patterns provide a robust foundation for building a maintainable and scalable application.
