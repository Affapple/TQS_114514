# 8.1

## Issues

### Security

- No problems found

### Security Hotspot

- Problem Description: Securitywise, the app is using a random number generator that could be unsafe, as it could be predictable, which would prompt to be exploitable
- How to Solve: Using a criptographically secure random number generator

### Reliability

- No problems found

### Maintainability

- Problem: Commented code
- How to solve: remove Commented code as it serves no purpose

- Problem: Conventions problems - not following modifiers that cumply with conventions
- Solution: Reorder attributes

- Problem: Bad practices - Usage of system out instead of loggers - editing the for loop variable inside the loop instead of the for statement; usage of if else to return a boolean instead of returning itself
- Solution: switch to a logger, refactor code

Checkstyle, PMD, and SpotBugs are static code analysis tools used in Java projects to help maintain code quality and detect issues.
Checkstyle enforces coding styles
PMD detects potential coding flaws, like unused variables empty blocks, unnecesasry object creatings and other poor pratices

SpotBugs detects actual bugs in code throuh bytecode analysis, by searchhing for null poointers, bad comparisons, infinite rerusions, multithreading issues.

# 8.2

Technical debt refers to how the quality of the code was sacrificed to make up for quicker development, so a larger technical debt means that in order to refactor the existing code in a cleaner way, you will need to spend more time doing so.
This graph compares

In my project, CarManagementService has a relative low technical debt, however it has a coverage of only 82%, however my car controller has a greater technical debt, and a 100% code coverage

## d)

There were 41 lines to cover ()

After fixing issues reported such as public methods that should not be public, test operations that were not clear like changing from

```java

    @Test
    void whenSearchNonExistingCar_thenReturnNull() {
        assertThat(
            carService.getCarDetails(-1l).get
        ).
    }
```

```java
    @Test
    void whenSearchNonExistingCar_thenReturnNull() {
        assertThat(
            carService.getCarDetails(-1l)
        ).isNotPresent();
    }
```

# 8.3

The most severe probblem (Blocker) was that there were no tests made for the app
There wasn't any DTO either, detected because we created an endpoint that received an entity directly that was then saved in the database.
There was a not handled null pointer exception, as we used a method that could return a null and didnt account for that

We used deprecated methods, that decision was consciously made at the time as the deprecated method had a complex alternative that we decided not to dive for as this implementation was working at the time and we didn't want to account for long term Maintainability.

We also left out some comented code, and didnt use loggers, only prints to stdout
Finally an high severity was that we didnt follow convention

## c, d

Asked about TripInfoService class, if the code was maintainable, clean,easy to understand and open to future changes, and it suggested

- Standardize caching strategy: Choose a consistent approach - either cache-first or repository-first.
- Extract repeated logic: Move common operations like filtering completed trips into helper methods.
- Replace System.out with proper logging: Use a logging framework for better control and production readiness.
- Consistent exception handling: Standardize how you handle and throw exceptions.
- Add cache invalidation logic: Implement or at least provide hooks for cache expiration/invalidation.
- Optimize database queries: Consider adding repository methods to fetch exactly what you need rather than getting all trips and filtering.
- Use more efficient data structures: For example, when searching for a specific trip by ID.
- Add meaningful comments: Document non-obvious design decisions and complex logic.
- Consider adding unit tests: If you don't already have them, tests will help ensure reliability during refactoring.
- Add null checks: Defensive programming practices can prevent unexpected NPEs.

Of these, the first one was a big issue, there wasnt much consistency in the caching status.
So I do believe all these suggestions to be important to be done, current code could be cleaned to be more consistent and efficient, by making more queries in the database instead of filtering the results as how it currently is, will not scale well at all filtering should be optimally done in the database instead on the rest api

To my colleague I would suggest to try to make consistent cache logic, to slide repeated statements that appear in a function. Instead of using a function to fecth all trips and then filtering it, which may look cleaner and simpler in the beginning, to use repository methods to filter directly which would be more optimized and remove cachin all together as its data that is easily always changing, so caching wouldnt be a good strategy in this case, and leave caching to other less volatile entites and use cases.

In the overall code, I would suggest a new approach of dealing with errors, something not noticed by AI (which was to be expected as he doesnt have full context), I would suggest adding @ExceptionHandlers to handle different exception types in order for code logic to be overall simpler and optimistic. If anything goes wrong, throw an error that exception handler will catch, otherwise continue as everything was fine, instead of adding if statements, null checks etc.

## e

I felt like it's not at all a substitution to sonarqube and other tools, but could be a great way to get second opinions on code we have written instead of just waiting for colleague reviews. Mistakes could be detected right away, and other mistakes can be detected further in the line when a colleague finally reviews our code. With no AI, we would only could wait for our colleague to review our code when he could.
