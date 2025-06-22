# Bulb Manager Project

A test implementation of the decider pattern with Spring Modulith and pattern matching.

The goal of this project is to build a framework to implement the decider pattern in a Spring Modulith project, following SOLID principles and modern architectural patterns.

## Features

- A bulb can be switched on and switched off
- If it is already switched on, the bulb stays light on
- A bulb has a limited lifespan (3 switches), after which the bulb burns out

## Project Structure

The project follows a modular architecture using Spring Modulith:

- `fr.depix.bulb_manager` - Main application package
    - `bulb.domain` - Domain model and services
    - `bulb.domain.spi` - Service Provider Interfaces
    - `bulb.infra` - Infrastructure implementations

## Architecture Principles

The project follows these architectural principles:

- **Hexagonal Architecture**: A domain model is isolated from infrastructure concerns
- **SOLID Principles**: See [SOLID Analysis](docs/SOLID_analysis.md) for details
- **Command Pattern**: Commands represent intentions to modify the state
    - Commands are implemented as Java records that implement a sealed Command interface
    - The BulbService handles commands using pattern matching with a switch statement
    - This provides a clear separation between the intent to perform an action and the execution of that action
- **Event Pattern**: Events represent facts that have occurred as a result of command processing
    - Events are implemented as Java records that implement a sealed Event interface
    - The BulbService produces events based on commands and the current state
    - Events are used to evolve the state of the application
    - For more details, see [Event Pattern](docs/event_pattern.md)
- **Decider Pattern**: Separates decision logic from evolution logic
    - The Decider decides what events to produce based on the current state and the command
    - The Evolve function applies events to the current state to produce a new state
    - The CommandHandler orchestrates the process of handling a command
    - For more details, see [Decider Framework](docs/decider_framework.md)
- **Spring Modulith**: Modules are defined by package structure with explicit dependencies

## Build/Configuration Instructions

### Prerequisites

- Java 24
- Maven 3.8+ (recommended)

### Building the Project

```bash
# Clean and build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Skip tests during build
mvn clean install -DskipTests
```

## Testing Information

Tests can be run using Maven:

```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=BulbTest

# Run a specific test method
mvn test -Dtest=BulbTest#turnOn
```

The project uses JUnit 5 (Jupiter) for testing with AssertJ for assertions.

## Roadmap

- [X] Naive implementation
- [X] Apply SOLID principles
- [X] Use commands
- [X] Object immutable
- [X] Split decider/evolve
- [X] Use an event between
- [X] Use a list of events
- [X] Build a framework
- [X] Initial state
- [X] Terminal state
- [ ] Persist lists of events
- [ ] Event sourcing
- [ ] Module switch (two switches for one bulb)
- [ ] Improve the framework
- [ ] Add business validation rules
- [ ] Add validation by ArchUnit

## Additional Documentation

For more detailed information, please refer to:

- [Architecture Principles](docs/architecture_principles.md) - Detailed explanation of CQRS/ES, modular architecture, and the Decider pattern
- [Development Guidelines](docs/guidelines.md) - Guidelines for developers working on the project
- [SOLID Analysis](docs/SOLID_analysis.md) - Analysis of how the project adheres to SOLID principles
- [Event Pattern](docs/event_pattern.md) - Detailed explanation of the Event pattern implementation
- [Decider Framework](docs/decider_framework.md) - Detailed explanation of the Decider Framework implementation
