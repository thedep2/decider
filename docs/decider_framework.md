# Decider Framework in the Bulb Manager Project

This document describes the Decider Framework implementation in the Bulb Manager project.

## Overview

The Decider Framework is a design pattern that separates decision logic (what events to produce) from evolution logic (how to change state based on events). This separation makes the code more maintainable, testable, and easier to reason about.

## Key Components

The framework consists of several key components:

### 1. Aggregate

An Aggregate is a domain object that represents a business entity. It encapsulates state and ensures that the state transitions are valid.

```java
public interface Aggregate<I extends AggregateId> {
    I aggregateId();
    Long aggregateVersion();
}
```

### 2. AggregateId

An AggregateId is a unique identifier for an Aggregate.

```java
public interface AggregateId {
}
```

### 3. Command

A Command represents an intention to change the state of an Aggregate. Commands are immutable and contain all the information needed to perform the action.

```java
public interface Command<I extends AggregateId> {
    I aggregateId();
    Long aggregateVersion();
    java.time.ZonedDateTime commandDate();
}
```

### 4. Event

An Event represents a fact that has occurred as a result of processing a Command. Events are immutable and contain information about what has happened.

```java
public interface Event {
}
```

### 5. Decider

A Decider is a function that takes a Command and an Aggregate and produces a list of Events. It decides what events to produce based on the current state of the aggregate and the command being processed.

```java
public interface Decider<I extends AggregateId, C extends Command<I>, A extends Aggregate<I>, E extends Event> extends BiFunction<C, A, List<E>> {
}
```

### 6. Evolve

An Evolve function takes an Aggregate and a list of Events and produces a new Aggregate. It applies the events to the current state of the aggregate to produce a new state.

```java
public interface Evolve<I extends AggregateId, A extends Aggregate<I>, E extends Event> extends BiFunction<A, List<E>, A> {
}
```

### 7. InitialState

An InitialState is a special type of Aggregate that represents the initial state of an Aggregate. It is used to create a new Aggregate with a predefined initial state.

```java
public interface InitialState<I extends AggregateId> extends Aggregate<I> {
}
```

### 8. IsTerminal

An IsTerminal is a predicate that determines if an Aggregate is in a terminal state. Terminal states are states from which no further state transitions are allowed.

```java
public interface IsTerminal<A extends Aggregate<I>, I extends AggregateId> extends Predicate<A> {
}
```

### 9. Repository

A Repository is responsible for storing and retrieving Aggregates.

```java
public interface Repository<A extends Aggregate<I>, I extends AggregateId> {
    Optional<A> findById(I id);
    void save(A aggregate);
}
```

### 10. CommandHandler

A CommandHandler orchestrates the processing of a Command. It retrieves the Aggregate from the Repository, applies the Decider to produce Events, applies the Evolve function to produce a new state, and then saves the new state to the Repository.

```java
public class CommandHandler<
        A extends Aggregate<I>,
        I extends AggregateId,
        C extends Command<I>,
        R extends Repository<A, I>,
        E extends Event,
        T extends IsTerminal<A, I>,
        D extends Decider<I, C, A, E, VE>,
        V extends Evolve<I, A, E>,
        VE extends ValidationError> {

    private final D decider;
    private final V evolve;
    private final R repository;

    public CommandHandler(Domain<A, I, C, R, E, T, D, V, VE> domain) {
        this.repository = domain.repository().get();
        this.decider = domain.decider();
        this.evolve = domain.evolve();
    }

    public void handle(C command) {
        Optional<A> aggregate = repository.findById(command.aggregateId());

        final Decision<E, VE> decision = decider.apply(command, aggregate);

        final A newState = switch (decision) {
            case EventList<E, VE> events -> evolve.apply(aggregate, events.events());
            case ErrorList<E, VE> ignored -> throw new RuntimeException();
        };

        repository.save(newState);
    }
}
```

## Flow of Command Processing

1. A Command is created and passed to the CommandHandler
2. The CommandHandler retrieves the Aggregate from the Repository
3. The Decider validates that the Command's aggregate version matches the current Aggregate version (optimistic concurrency control)
4. The Decider produces a list of Events based on the Command and the current state of the Aggregate
5. The Evolve function produces a new state based on the current state of the Aggregate and the list of Events
6. The new state includes an incremented aggregate version
7. The CommandHandler saves the new state to the Repository

## Benefits of the Decider Pattern

1. **Separation of Concerns**: The decision logic (what events to produce) is separated from the evolution logic (how to change state based on events)
2. **Testability**: The Decider and Evolve functions are pure functions that can be tested in isolation
3. **Auditability**: The list of Events produced by the Decider can be stored for auditing purposes
4. **Event Sourcing**: The list of Events can be used to rebuild the state of the Aggregate, enabling event sourcing
5. **Immutability**: The Aggregate, Command, and Event objects are immutable, making the system more predictable and easier to reason about

## Future Enhancements

As outlined in the project roadmap, future enhancements to the Decider Framework include:

1. **Initial State**: ✅ Support for creating an Aggregate with an initial state - Implemented with the InitialState interface and InitialBulb implementation
2. **Terminal State**: ✅ Support for marking an Aggregate as terminal, preventing further state changes - Implemented with the IsTerminal interface and WentOutBulb implementation
3. **Aggregate Versioning**: ✅ Support for tracking aggregate versions for optimistic concurrency control - Implemented with the aggregateVersion() method in the Aggregate interface
4. **Command Metadata**: ✅ Support for including command date and aggregate version in commands - Implemented with the aggregateVersion() and commandDate() methods in the Command interface
5. **Persist Lists of Events**: Store events in a persistent store for auditing and event sourcing
6. **Event Sourcing**: Rebuild the state of an Aggregate from the sequence of Events
7. **Improve the Framework**: Enhance the framework with additional features and optimizations
