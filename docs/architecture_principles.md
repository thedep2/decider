# Architecture Principles of the Bulb Manager Project

This document describes the architecture principles that the Bulb Manager project aims to follow, particularly the SOLID principles, CQRS/ES, and the separation of business by module.

## SOLID Principles

The SOLID principles are detailed in the [SOLID_analysis.md](SOLID_analysis.md) file. In summary, the project currently respects:

- **S**ingle Responsibility Principle: Each class has a single responsibility
- **O**pen/Closed Principle: Partially respected, with planned improvements
- **L**iskov Substitution Principle: Implementations respect their contracts
- **I**nterface Segregation Principle: Interfaces are minimalist and targeted
- **D**ependency Inversion Principle: The domain depends on abstractions, not implementations

## Command Pattern

The project has implemented the Command pattern:

- **Command Interface**: A sealed interface that permits only specific command implementations
- **Command Implementations**: Immutable record classes (BulbTurnOn, BulbTurnOff) representing actions
- **Command Handler**: The BulbService handles commands using pattern matching with a switch statement

This implementation provides a clear separation between the intent to perform an action (the command) and the execution of that action (the handler).

## CQRS/ES (Command Query Responsibility Segregation / Event Sourcing)

### CQRS Principles

CQRS is an architectural pattern that separates read operations (Queries) from write operations (Commands).

#### Current State

The project has begun implementing CQRS with the Command pattern:

- [X] Use of commands
- [X] Immutable objects
- [X] Separation of decider/evolve

#### Planned Implementation

1. **Commands**: Immutable objects representing the intention to modify state (e.g., `SwitchOnCommand`)
2. **Queries**: Objects representing a request for information (e.g., `GetBulbStateQuery`)
3. **Command Handlers**: Process commands and produce events
4. **Query Handlers**: Process queries and return data

### Event Sourcing Principles

Event Sourcing is a pattern where the state of an application is determined by a sequence of events rather than a snapshot of the current state. For a detailed explanation of the Event pattern implementation in this project, see [Event Pattern](event_pattern.md).

#### Current State

The project has begun implementing Event Sourcing:

- [X] Use an event between the Event interface and its implementations (BulbSwitchedOn, BulbSwitchedOff, BulbWentOut, NothingHappen) have been created
- [ ] Use a list of events
- [ ] Persist event lists
- [ ] Event sourcing

#### Planned Implementation

1. **Events**: Immutable objects representing facts that have occurred (e.g., `BulbTurnedOnEvent`)
2. **Event Store**: Persistent storage of all events
3. **Event Handlers**: Update projections in response to events
4. **Projections**: Views optimized for specific queries

## Separation of Business by Module

### Principles of Modularity

Modularity aims to organize code into cohesive and loosely coupled modules, each representing a distinct business domain.

#### Current State

The project uses Spring Modulith to facilitate modularity:

- Package structure that reflects business modules
- Verification of dependencies between modules via tests

#### Current and Planned Implementation

1. **Current Modules**:
    - `bulb.domain`: Business logic for bulbs
    - `bulb.domain.spi`: Interfaces for external services
    - `bulb.infra`: Infrastructure implementations

2. **Planned Modules**:
    - [ ] Switch module (two switches for one bulb)

### Hexagonal Architecture

The project follows a hexagonal architecture (or ports and adapters):

- **Domain**: At the center, contains pure business logic
- **Ports**: Interfaces defining how the domain interacts with the outside
- **Adapters**: Concrete implementations of ports

## The Decider Pattern

### Principles of the Decider Pattern

The Decider pattern is a functional pattern that separates decision logic (what to do) from evolution logic (how to change state).

#### Current State

The project has implemented the Decider pattern:

- [X] Separation of decider/evolve

#### Planned Implementation

1. **Decider**: Pure function that takes a state and a command, and returns events
2. **Evolve**: Pure function that takes a state and an event, and returns a new state
3. **Decide+Evolve**: Combination of both to process a command and update the state

## Roadmap

Based on the roadmap in the README, here is the plan for implementing these principles:

1. **Phase 1 - CQRS Foundations**:
    - [X] Use commands
    - [X] Make objects immutable
    - [X] Separate decider/evolve

2. **Phase 2 - Event Sourcing**:
    - [X] Use an event between decider and evolver
    - [ ] Use a list of events
    - [ ] Initial state and terminal state
    - [ ] Persist event lists
    - [ ] Complete event sourcing

3. **Phase 3 - Framework and Modularity**:
    - [ ] Build a framework
    - [ ] Switch module (two switches for one bulb)
    - [ ] Improve the framework
