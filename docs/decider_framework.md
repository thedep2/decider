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

An Event represents a fact that has occurred as a result of processing a Command. Events are immutable and contain information about what has happened, including metadata for tracking and auditing.

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

The Event interface is generic with `<I extends Identifier>` parameter, allowing events to be associated with specific aggregate identifier types. It includes several methods:

- `aggregateId()`: Returns the identifier of the aggregate that the event relates to
- `eventId()`: Returns a unique identifier for the event
- `aggregateVersion()`: Returns the version of the aggregate when the event occurred
- `eventType()`: Returns the type of the event as a string
- `eventVersion()`: Returns the version of the event schema
- `eventDate()`: Returns the date and time when the event occurred

### 5. Decider

A Decider is a function that takes a Command and an Aggregate and produces a Decision, which can be either a list of Events or a list of ValidationErrors. It decides what events to produce based on the current state of the aggregate and the command being processed.

```java
public interface Decider<I extends Identifier, C extends Command<I>, A extends Aggregate<I>, E extends Event<I>, VE extends ValidationError> extends BiFunction<C, Optional<A>, Decision<E, VE, I>> {
}
```

The Decider interface:

- Take an Optional<A> instead of just A, to handle the case where the aggregate doesn't exist yet
- Return a Decision<E, VE, I> instead of a List<E>, to handle validation errors
- Include the ValidationError type parameter VE
- Work with the generic Event<I> interface

### 6. Decision

A Decision represents the result of processing a Command. It can be either a list of Events (success) or a list of ValidationErrors (failure).

```java
public sealed interface Decision<E extends Event<I>, VE extends ValidationError, I extends Identifier> permits ErrorList, EventList {
}
```

The Decision interface:

- Be a sealed interface that permits only EventList and ErrorList implementations
- Include three generic type parameters:
    - E extends Event<I>: The event type, which is now generic with identifier type I
    - VE extends ValidationError: The validation error type
    - I extends Identifier: The identifier type

#### EventList

EventList represents a successful decision that produced a list of events.

```java
public record EventList<E extends Event<I>, VE extends ValidationError, I extends Identifier>(
        List<E> events
) implements Decision<E, VE, I> {
}
```

#### ErrorList

ErrorList represents a failed decision that produced a list of validation errors.

```java
public record ErrorList<E extends Event<I>, VE extends ValidationError, I extends Identifier>(
        List<VE> errors
) implements Decision<E, VE, I> {
}
```

### 7. Evolve

An Evolve function takes an Aggregate and a list of Events and produces a new Aggregate. It applies the events to the current state of the aggregate to produce a new state.

```java
public interface Evolve<I extends Identifier, A extends Aggregate<I>, E extends Event<I>> extends BiFunction<Optional<A>, List<E>, A> {
}
```

The Evolve interface:

- Take an Optional<A> instead of just A, to handle the case where the aggregate doesn't exist yet
- Work with the generic Event<I> interface

### 8. InitialState

An InitialState is a special type of Aggregate that represents the initial state of an Aggregate. It is used to create a new Aggregate with a predefined initial state.

```java
public interface InitialState<I extends AggregateId> extends Aggregate<I> {
}
```

### 9. IsTerminal

An IsTerminal is a predicate that determines if an Aggregate is in a terminal state. Terminal states are states from which no further state transitions are allowed.

```java
public interface IsTerminal<A extends Aggregate<I>, I extends AggregateId> extends Predicate<A> {
}
```

### 10. Repository

A Repository is responsible for storing and retrieving Aggregates.

```java
public interface Repository<A extends Aggregate<I>, I extends AggregateId> {

    Optional<A> findById(I id);

    void save(A aggregate);
}
```

### 11. CommandHandler

A CommandHandler orchestrates the processing of a Command. It retrieves the Aggregate from the Repository, applies the Decider to produce a Decision (either Events or ValidationErrors), applies the Evolve function to produce a new state if the decision was successful, and then saves the new state to the Repository.

```java
public class CommandHandler<
        A extends Aggregate<I>,
        I extends Identifier,
        C extends Command<I>,
        R extends Repository<A, I>,
        E extends Event<I>,
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

    @org.jmolecules.architecture.cqrs.CommandHandler
    public A handle(C command) {
        Optional<A> aggregate = repository.findAggregateById(command.aggregateId());

        final Decision<E, VE, I> decision = decider.apply(command, aggregate);

        final A newState = switch (decision) {
            case EventList<E, VE, I> events -> evolve.apply(aggregate, events.events());
            case ErrorList<E, VE, I> errorList -> throw new ValidationRuntimeException(
                                                                                        errorList.errors()
                                                                                                 .stream()
                                                                                                 .map(ValidationError::message)
                                                                                                 .collect(Collectors.joining(" "))
            );
        };
        repository.save(newState);
        
        return newState;
    }
}
```

The CommandHandler:

- Works with the generic Event<I> interface
- Handles the Decision<E, VE, I> interface with three type parameters
- Uses pattern matching with EventList<E, VE, I> and ErrorList<E, VE, I> types
- Returns the new state after processing the command
- Throws a ValidationRuntimeException with detailed error messages when validation fails

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

## Validation Framework

The Decider Framework includes a validation system that allows for validating commands and aggregates before processing them. This helps ensure that only valid commands are processed and that the system maintains a consistent state.

### 1. ValidationError

The ValidationError interface represents an error that occurs during validation. It provides a message that describes the error.

```java
public interface ValidationError {
    String message();
}
```

### 2. ValidationRule

The ValidationRule interface defines a rule that can be used to validate an object. It takes an object of type T and returns a list of validation errors if the object does not satisfy the rule.

```java
@FunctionalInterface
public interface ValidationRule<T, VE extends ValidationError> {
    List<VE> isSatisfiedBy(T object);
}
```

### 3. ValidationRuntimeException

The ValidationRuntimeException is thrown when validation fails. It includes a message that describes the validation errors.

```java
public class ValidationRuntimeException extends RuntimeException {
    public ValidationRuntimeException(String message) {
        super(message);
    }
}
```

## Future Enhancements

As outlined in the project roadmap, future enhancements to the Decider Framework include:

1. **Initial State**: ✅ Support for creating an Aggregate with an initial state - Implemented with the InitialState interface and InitialBulb implementation
2. **Terminal State**: ✅ Support for marking an Aggregate as terminal, preventing further state changes - Implemented with the IsTerminal interface and WentOutBulb implementation
3. **Aggregate Versioning**: ✅ Support for tracking aggregate versions for optimistic concurrency control - Implemented with the aggregateVersion() method in the Aggregate interface
4. **Command Metadata**: ✅ Support for including command date and aggregate version in commands - Implemented with the aggregateVersion() and commandDate() methods in the Command interface
5. **Persist Lists of Events**: Store events in a persistent store for auditing and event sourcing
6. **Event Sourcing**: Rebuild the state of an Aggregate from the sequence of Events
7. **Improve the Framework**: Enhance the framework with additional features and optimizations
