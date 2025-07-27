# Bulb Manager Project Guidelines

This document provides guidelines and information for developers working on the Bulb Manager project.

## Build/Configuration Instructions

### Prerequisites

- Java 24 (as specified in pom.xml)
- Maven 3.8+ (recommended)

### Building the Project

The project uses Maven for build management. Here are the common commands:

```bash
# Clean and build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Skip tests during build
mvn clean install -DskipTests
```

### Project Structure

The project follows a modular architecture using Spring Modulith:

- `fr.depix.bulb_manager` - Main application package
    - `bulb.domain` - Domain model and services
    - `bulb.domain.spi` - Service Provider Interfaces
    - `bulb.infra` - Infrastructure implementations

## Testing Information

### Running Tests

Tests can be run using Maven:

```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=BulbTest

# Run a specific test method
mvn test -Dtest=BulbTest#turnOn
```

### Test Structure

The project uses JUnit 5 (Jupiter) for testing with AssertJ for assertions. Tests follow this structure:

1. **Unit Tests**: Test individual components in isolation
    - Located in `src/test/java/fr/depix/bulb_manager`
    - Example: `BulbTest.java` tests the Bulb domain model

2. **Integration Tests**: Test the application as a whole
    - Located in `src/test/java/fr/depix/bulb_manager`
    - Example: `BulbManagerApplicationTests.java` tests the Spring context loading

### Writing New Tests

When writing new tests:

1. Follow the existing naming convention: `[Component]Test.java`
2. Use descriptive method names and `@DisplayName` annotations
3. Follow the Arrange-Act-Assert pattern
4. For domain tests, create repository and service instances manually
5. For integration tests, use `@SpringBootTest` to load the application context

### Example Test

Here's an example of a test for the Bulb domain model:

```java

@Test
@DisplayName("Given a new bulb, when I check its state, then it should be off")
void newBulbIsOff() {
    // Arrange
    bulbService.handleCommand(new CreateBulB(new BulbId(1L), ZonedDateTime.now()));

    // Act & Assert
    Assertions.assertThat(bulbService.isTurnOn()).isFalse();
}

@Test
@DisplayName("Given a bulb, when I turn it on, then it should be on")
void turnOnBulb() {
    // Arrange
    bulbService.handleCommand(new CreateBulB(new BulbId(1L), ZonedDateTime.now()));

    // Act
    Long aggregateVersion = bulbService.getAggregateVersion();
    bulbService.handleCommand(new BulbTurnOn(new BulbId(1L), aggregateVersion, ZonedDateTime.now()));

    // Assert
    Assertions.assertThat(bulbService.isTurnOn()).isTrue();
}
```

Note how commands include:

- The aggregate ID (BulbId)
- The aggregate version (retrieved from bulbService.getAggregateVersion() for existing aggregates)
- The command date (ZonedDateTime.now())

## Additional Development Information

### Code Style

The project follows standard Java code style conventions:

- Use 4 spaces for indentation
- Follow Java naming conventions (camelCase for methods and variables, PascalCase for classes)
- Keep methods small and focused on a single responsibility
- Use descriptive names for classes, methods, and variables

### Domain Model

The Bulb domain model has these key characteristics:

- A bulb can be turned on and off
- A bulb has a limited lifespan (3 switches)
- After reaching its lifespan limit, the bulb "burns out" and cannot be turned on again

### Command Pattern Implementation

The project uses the Command pattern:

- Commands are implemented as Java records that implement a sealed Command interface
- Commands include three key pieces of information:
    - The aggregate ID they target
    - The aggregate version they expect to modify (for optimistic concurrency control)
    - The date and time when the command was created
- The BulbService handles commands using pattern matching with a switch statement
- The system validates that the command's aggregate version matches the current aggregate version
- This provides a clear separation between the intent to perform an action and the execution of that action

### Event Pattern Implementation

The project uses the Event pattern:

- Events are implemented as Java records that implement a sealed Event interface
- The BulbService produces a list of events based on commands and the current state
- In some cases, an empty list is returned, indicating that the command did not result in any state change
- Events are used to evolve the state of the application
- This approach separates the decision logic (what events to produce) from the evolution logic (how to change the state based on the events)
- For more details, see [Event Pattern](event_pattern.md)

### Decider Pattern Implementation

The project uses the Decider pattern:

- The Decider is a function that takes a Command and an Aggregate and produces a list of Events
- The Evolve function takes an Aggregate and a list of Events and produces a new Aggregate
- The CommandHandler orchestrates the process of handling a command, applying the decider, evolving the state, and saving the result
- This separation of concerns makes the code more maintainable, testable, and easier to reason about
- For more details, see [Decider Framework](decider_framework.md)

### Architecture

The project follows a hexagonal architecture pattern:

- Domain model is isolated from infrastructure concerns
- Service Provider Interfaces (SPI) define the contracts for infrastructure components
- Infrastructure implementations provide concrete implementations of these contracts

### Spring Modulith

The project uses Spring Modulith for modular architecture:

- Modules are defined by a package structure
- Dependencies between modules are explicit
- Tests verify module boundaries

To verify module boundaries, run:

```
ApplicationModules.of(BulbManagerApplication.class).verify();
```

## Architecture Principles

For detailed information about the architectural principles this project aims to follow, please refer to:

- [Architecture Principles](architecture_principles.md) - Detailed explanation of CQRS/ES, modular architecture, and the Decider pattern
- [SOLID Analysis](SOLID_analysis.md) - Analysis of how the project adheres to SOLID principles
