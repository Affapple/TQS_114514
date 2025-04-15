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
