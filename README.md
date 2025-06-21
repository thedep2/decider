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
- **Spring Modulith**: Modules are defined by package structure with explicit dependencies

## Build/Configuration Instructions

### Prerequisites

- Java 24
- Maven 3.8+ (recommended)

### Building the Project

```bash
# Clean and build the project
mvn clean package

# Run the application (no interest)
mvn spring-boot:run
```

## Testing Information

Tests can be run using Maven:

```bash
# Run all tests
mvn test

```

The project uses JUnit 5 (Jupiter) for testing with AssertJ for assertions.

## Roadmap

- [X] Naive implementation
- [X] apply SOLID principles
- [X] Use commands
- [X] Object immutable
- [ ] Split decider/evolve
- [ ] Use an event between
- [ ] Use a list of events
- [ ] Initial state
- [ ] Terminal state
- [ ] Persist lists of events
- [ ] Event sourcing
- [ ] Build framework
- [ ] Module switch (two switches for one bulb)
- [ ] Improve the framework

## Additional Documentation

For more detailed information, please refer to:

- [Architecture Principles](docs/architecture_principles.md) - Detailed explanation of CQRS/ES, modular architecture, and the Decider pattern
- [Development Guidelines](docs/guidelines.md) - Guidelines for developers working on the project
