## Exercise 2.3

### Exercise f

mvn test: Compile and run tests (no integration tests)
mvn package: Compile, run tests and create a distributable jar file ( no integration test )
with -DskipTests=True, does the same but doesnt run any tests

mvn failsafe:integration-test: Way to run integration tests

mvn install: Compile, run tests and create a jar file to be used as dependecy by other local projects

#### package vs install

Install: compiles, creates and install jar file in local repository to be used by other projects
Package: just compiles and outputs a jar file
