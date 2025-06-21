# Event Pattern in the Bulb Manager Project

This document describes the Event pattern implementation in the Bulb Manager project.

## Overview

The Event pattern is a key component of Event Sourcing, where the state of an application is determined by a sequence of events rather than a snapshot of the current state. In the Bulb Manager project, events represent facts that have occurred as a result of command processing.

## Event Interface

The project defines a sealed interface `Event` that permits only specific event implementations:

```java
public sealed interface Event permits BulbSwitchedOff, BulbSwitchedOn, BulbWentOut, NothingHappen {
}
```

This sealed interface ensures type safety and restricts the possible event types to a known set.

## Event Implementations

The project currently has four event implementations:

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

4. **NothingHappen**: Represents the fact that a command did not result in any state change
   ```java
   public record NothingHappen() implements Event {
   }
   ```

All event implementations are Java records, which means they are immutable and provide built-in equals, hashCode, and toString methods.

## Event Handling

Events are produced by the `BulbService` when processing commands:

```java
Event event = switch (command) {
    case BulbTurnOff ignored when bulb.isTurnOn() -> new BulbSwitchedOff();
    case BulbTurnOff ignored -> new NothingHappen();
    case BulbTurnOn ignored when !bulb.isTurnOn() && bulb.count() >= LIMIT -> new BulbWentOut();
    case BulbTurnOn ignored when !bulb.isTurnOn() -> new BulbSwitchedOn();
    case BulbTurnOn ignored -> new NothingHappen();
};
```

This pattern matching approach provides a clear and concise way to determine which event should be produced based on the command and the current state of the bulb.

## State Evolution

The produced events are then used to evolve the state of the bulb:

```java
Bulb newBulb = switch (event) {
    case BulbSwitchedOff ignored -> new Bulb(false, bulb.count());
    case BulbSwitchedOn ignored -> new Bulb(true, bulb.count() + 1);
    case BulbWentOut ignored -> new Bulb(false, bulb.count());
    case NothingHappen ignored -> bulb;
};
```

This approach separates the decision logic (what event to produce) from the evolution logic (how to change the state based on the event), which is a key aspect of the Decider pattern.

## Future Enhancements

The current implementation uses a single event to represent the result of a command. Future enhancements planned in the roadmap include:

1. **Use a list of events**: Allow a command to produce multiple events
2. **Persist event lists**: Store events in a persistent store
3. **Event sourcing**: Rebuild the state of the application from the sequence of events

These enhancements will further develop the Event Sourcing capabilities of the project.

## Relationship to Other Patterns

The Event pattern works in conjunction with other patterns in the project:

- **Command Pattern**: Commands represent intentions to modify state, and they produce events
- **Decider Pattern**: The decision logic produces events, and the evolution logic consumes events to produce new state
- **CQRS**: Commands produce events, which are then used to update the write model

Together, these patterns provide a robust foundation for building a maintainable and scalable application.
